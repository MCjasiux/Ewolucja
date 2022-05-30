import java.util.*;

public class JungleMap {

    public int getAge() {
        return age;
    }

    public Random random;
    private int age;
    public final MapDirections directions;
    private final Map<Vector2d, LinkedList<AbstractAnimal>> animals;
    private final Map<Vector2d, Grass> grasses;
    public final MapPositionWatcher watcher;
    public final MapVisualizer visualizer;
    public final MapInfo info;
    public final NameAssigner nameAssigner;
    public final Controls controls;
    private final AnimalEnergyComparator comparator;
    public final MapProperties properties;
    public final FileSaver saver;

    public JungleMap(MapProperties properties, ArrayList<String> nameList, String saveName) {
        this.properties = properties;
        this.directions = new MapDirections();
        this.random = new Random();
        this.age = 0;
        this.comparator = new AnimalEnergyComparator(); //porównywacz energii zwierząt
        this.animals = new LinkedHashMap<>();
        this.grasses = new LinkedHashMap<>();
        this.nameAssigner = new NameAssigner(nameList); //klasa służąca do generowania zwierzętom imion
        this.info = new MapInfo(this);  //obiekt przechowujący i liczący statystyki mapy
        this.visualizer = new MapVisualizer(this);  //obiekt odpowiedzialny za wyświetlanie mapy i kontrolę nad nią
        this.watcher = new MapPositionWatcher(this);    //obiekt odpowiedzialny za dostarczanie na życzenie wolnych miejsc na mapie
        this.saver = new FileSaver(saveName);   //zapisywanie danych do pliku
        this.controls = new Controls(this);
        for (int i = 0; i < properties.width; i++) {
            for (int j = 0; j < properties.height; j++) {
                animals.put(new Vector2d(i, j), new LinkedList<AbstractAnimal>());  //Puste listy na zwierzęta na każdym polu
            }
        }
    }

    public Map<Vector2d, LinkedList<AbstractAnimal>> getAnimals() {
        return animals;
    }

    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    public void placeAnimal(AbstractAnimal animalToPlace) {
        animals.get(animalToPlace.getPosition()).add(animalToPlace);
        watcher.entityAdded(animalToPlace.getPosition());
        animalToPlace.addChangeObserver(watcher);
        animalToPlace.addRemovalObserver(info);
        animalToPlace.addRemovalObserver(visualizer);
        animalToPlace.addRemovalObserver(watcher);
        visualizer.addAnimal(animalToPlace);
        info.animalPlaced();
    }

    public void plantGrass(Grass grassToPlant) {
        grasses.put(grassToPlant.getPosition(), grassToPlant);
        watcher.entityAdded(grassToPlant.getPosition());
        grassToPlant.addRemovalObserver(watcher);
    }

    void tick() {
        //######    FAZA 1: RUCH    ######//
        LinkedList<AbstractAnimal> animalsToUpdate = new LinkedList<>();    //lista zwierząt, które zmieniają pozycję
        for (LinkedList<AbstractAnimal> animalList : animals.values()
        ) {
            for (AbstractAnimal animal : animalList
            ) {
                animalsToUpdate.add(animal);
            }
        }
        for (AbstractAnimal animal : animalsToUpdate
        ) {
            Vector2d oldPosition = animal.getPosition();
            animals.get(oldPosition).remove(animal);    //usuwanie ze starej pozycji
            animal.move();
            Vector2d newPosition = animal.getPosition();
            animals.get(newPosition).add(animal);  //dodawanie do nowej
            animal.positionChanged(oldPosition);    //informowanie o zmianie pozycji
        }
        //######    FAZA 2: JEDZENIE    ######//
        LinkedList<Grass> toBeEaten = new LinkedList<>();
        for (Vector2d positionWithGrass : grasses.keySet()
        ) {
            LinkedList<AbstractAnimal> animalsOnGrass = animals.get(positionWithGrass);
            //sprawdzanie każdej pozycji z trawą - czy znalazło się tam zwierze?
            if (animalsOnGrass.isEmpty()) {

            } else {
                animalsOnGrass.sort(this.comparator);   //najsilniejsze zwierzęta przodem
                toBeEaten.add(grasses.get(positionWithGrass));  //trawa zostanie zjedzona
                int animalsFed = 0;
                for (AbstractAnimal animal : animalsOnGrass
                ) {
                    if (animal.getEnergy() == animalsOnGrass.getFirst().getEnergy()) {
                        animalsFed++;   //liczenie zwierząt z rówą ilością energii
                    } else break;
                }
                for (int i = 0; i < animalsFed; i++) {
                    animalsOnGrass.get(i).feed(properties.plantEnergy / animalsFed);  //karmienie zwierząt po równo
                }
            }


        }
        for (Grass eatenGrass : toBeEaten
        ) {
            Vector2d removedPosition = eatenGrass.getPosition();
            grasses.remove(removedPosition); //usuwanie z mapy zjedzonej trawy
            eatenGrass.publishRemoval();    //informowanie o usunięciu trawy
            eatenGrass.removeObservers();   //usuwanie obserwatorów
        }
        //######    FAZA 3 i 4: UMIERANIE I ROZMNAŻANIE  ######//
        LinkedList<AbstractAnimal> animalsToDie = new LinkedList<>();
        for (LinkedList<AbstractAnimal> animalList : animals.values()
        ) {
            if (animalList.size() > 1) {
                animalList.sort(this.comparator);

                AbstractAnimal animal1 = animalList.getFirst();
                AbstractAnimal animal2 = animalList.get(1);
                if (animal1.getEnergy() >= properties.startEnergy * properties.reproductionRatio && animal2.getEnergy() >= properties.startEnergy * properties.reproductionRatio) {
                    //WK na rozmnażanie
                    Vector2d reproductionPostition = animal1.getPosition();
                    Vector2d newAnimalPosition;
                    List<Vector2d> neightbors = watcher.getFreeNeighbours(reproductionPostition);
                    if (neightbors.size() > 0) {
                        newAnimalPosition = neightbors.get(0);
                    } else newAnimalPosition = animal1.getPosition();
                    Animal a = new Animal(newAnimalPosition, nameAssigner.getName(), animal1, animal2, (int) Math.floor(animal1.reproduce() + animal2.reproduce()), this);
                    placeAnimal(a);
                }
            }
            for (AbstractAnimal animalToDie : animalList
            ) {
                if (animalToDie.getEnergy() < properties.moveEnergy) { //nie może wykonać ruchu - praktycznie nie żyje
                    animalsToDie.add(animalToDie);      //zwierzak skazany na śmierć
                }
            }
        }
        for (AbstractAnimal animalToDie : animalsToDie
        ) {
            Vector2d diePosition = animalToDie.getPosition();
            animals.get(diePosition).remove(animalToDie);  //czyszczenie listy z martwych zwierzaków
            animalToDie.publishRemoval();   //poinformowanie, że zwierze już nie żyje
            animalToDie.removeObservers();  //usunięcie obserwatorów
        }
        //######    FAZA 5: NOWA TRAWA  ######//\
        List<Vector2d> freeJungleSpots = watcher.getFreeJungleSpots();
        List<Vector2d> freeSavannahSpots = watcher.getFreeSavannahSpots();
        //wyciągnięcie wolnych pozycji na sawannie i w dżungli
        ArrayList<Vector2d> grassesToAdd = new ArrayList<>();
        for (int i = 0; i < properties.growthRate; i++) {
            try {
                Vector2d g1 = freeJungleSpots.get(i);
                grassesToAdd.add(g1);
            } catch (IndexOutOfBoundsException e) {
                //   Brak miejsca w dżungli
                break;
            }
        }
        for (int i = 0; i < properties.growthRate; i++) {
            try {
                Vector2d g2 = freeSavannahSpots.get(i);
                grassesToAdd.add(g2);
            } catch (IndexOutOfBoundsException e) {
                //Brak miejsca na sawannie
                break;
            }

        }
        for (Vector2d vect : grassesToAdd
        ) {
            plantGrass(new Grass(vect, this));
        }

        age++;
    }
}
