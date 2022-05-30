import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.LinkedList;

public class MapVisualizer implements IChangeObserver, IRemovalObserver {
    private final JungleMap map;
    private final int resolution; //rozdzielczość w pikselach jednego pola mapy
    private final float ratio;
    private final int jOffsetX;
    private final int jOffsetY;
    private final int height;
    private final int width;
    private final int jWidth;
    private final int jHeight;
    private final int startEnergy;
    private final Group environment;
    private final Group animalGroup;
    private final Group grassGroup;
    private final Group mapGroup;
    private final HBox mapAndInfo;
    private final VBox fullMap;
    private final ComboBox<AbstractAnimal> comboBox;
    private AbstractAnimal selectedAnimal;
    private final MapInfo info;
    private final Label mapInfoLabel;
    private final Label animalInfoLabel;
    private final LegacyLineChart charter;
    private final LineChart lineChart;
    private final ObservableList<AbstractAnimal> comboList;

    public MapVisualizer(JungleMap map) {
        this.map = map;
        this.resolution = map.properties.resolution;
        ratio = map.properties.jungleRatio;
        jOffsetX = map.properties.jungleOffset.x;
        jOffsetY = map.properties.jungleOffset.y;
        height = map.properties.height;
        width = map.properties.width;
        jWidth = (int) (width * ratio);
        jHeight = (int) (width * ratio);
        startEnergy = map.properties.startEnergy;
        this.environment = new Group();
        this.animalGroup = new Group();
        this.grassGroup = new Group();

        this.comboBox = new ComboBox();
        this.mapInfoLabel = new Label();
        this.animalInfoLabel = new Label();
        this.info = map.info;
        this.charter = new LegacyLineChart();
        this.lineChart = charter.getLineChart();
        this.mapGroup = new Group(environment, grassGroup, animalGroup);
        this.mapAndInfo = new HBox(10);
        this.mapAndInfo.getChildren().addAll(mapGroup, mapInfoLabel);
        this.fullMap = new VBox(10);
        this.fullMap.getChildren().addAll(mapAndInfo, comboBox, animalInfoLabel, lineChart);
        charter.hide();
        comboList = FXCollections.observableArrayList();
        comboBox.setItems(comboList);
        comboBox.valueProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem == null) {
            } else {
                charter.clear();    //przestajemy śledzić zwierze - chcemy żeby dane zostały na wykresie
            }
            selectedAnimal = newItem;
            renderAnimalInfo();
            renderAnimals();
        });
        refresh();

    }


    public VBox getFullMap() {
        return fullMap;
    }

    public void refresh() {
        renderEnvironment();
        renderGrass();
        renderAnimals();
        renderMapInfo();
        renderAnimalInfo();
    }

    public void renderEnvironment() {
        Rectangle savannah = new Rectangle(0, 0, width * resolution, height * resolution);
        savannah.setFill(Color.KHAKI);
        Rectangle jungle = new Rectangle(jOffsetX * resolution, jOffsetY * resolution, width * ratio * resolution, height * ratio * resolution);
        jungle.setFill(Color.OLIVEDRAB);
        environment.getChildren().clear();
        environment.getChildren().add(savannah);
        environment.getChildren().add(jungle);
    }

    public void renderGrass() {
        grassGroup.getChildren().clear();
        for (Grass grass : map.getGrasses().values()
        ) {
            Vector2d grassVect = grass.getPosition();
            Rectangle rectangle = new Rectangle(grassVect.x * resolution, grassVect.y * resolution, resolution, resolution);
            rectangle.setFill(Color.GREEN);
            grassGroup.getChildren().add(rectangle);
        }
    }

    public void renderAnimals() {
        animalGroup.getChildren().clear();
        for (Vector2d vect : map.getAnimals().keySet()
        ) {
            if (map.getAnimals().get(vect).isEmpty()) {

            } else {
                if (selectedAnimal != null && selectedAnimal.getPosition().equals(vect)) {
                    //obserwowane zwierze jest zaznaczone na czerwono
                    Circle selectedCircle = new Circle(selectedAnimal.getPosition().x * resolution + resolution / 2, selectedAnimal.getPosition().y * resolution + resolution / 2, resolution / 2, Color.rgb(255, 0, 0, Math.min(1.0, .25 + selectedAnimal.getEnergy() / startEnergy)));
                    animalGroup.getChildren().add(selectedCircle);
                } else {
                    //a zwykłe na czarno
                    Circle circle = new Circle(vect.x * resolution + resolution / 2, vect.y * resolution + resolution / 2, resolution / 2, Color.rgb(0, 0, 0, Math.min(1.0, .25 + map.getAnimals().get(vect).getFirst().getEnergy() / startEnergy)));
                    //Przezroczystość odpowiada energii pierwszego zwierzęcia na tym polu
                    animalGroup.getChildren().add(circle);
                }

            }

        }
    }

    public void renderControls() {
        comboBox.getItems().clear();
        for (LinkedList<AbstractAnimal> animals : map.getAnimals().values()
        ) {
            for (AbstractAnimal animal : animals
            ) {
                comboBox.getItems().add(animal);
            }
        }
    }

    public void renderAnimalInfo() {
        if (selectedAnimal == null) {

        } else {
            charter.show();
            charter.addData(map.getAge(), selectedAnimal.getChildrenCount(), selectedAnimal.getDescendantsCount());
            String textToSet = selectedAnimal.name + ", " + (map.getAge() - selectedAnimal.birthDate);
            textToSet += "\n" + selectedAnimal.energy + " energy";
            if (selectedAnimal instanceof PrimalAnimal) {
                textToSet += "\nPrimal";
            } else {
                textToSet += "\nChild of " + ((Animal) selectedAnimal).parents[0].name + " and " + ((Animal) selectedAnimal).parents[1].name;
            }
            textToSet += "\n" + selectedAnimal.position.toString() + ", " + MapDirections.toString(selectedAnimal.direction);
            textToSet += "\n" + Arrays.toString(selectedAnimal.getDna());
            textToSet += "\n" + selectedAnimal.getChildrenCount() + " children, " + selectedAnimal.getDescendantsCount() + " descendants.";
            animalInfoLabel.setText(textToSet);
        }
    }

    public void renderMapInfo() {
        info.getData();
        String textToSet = "Day " + map.info.getMapAge();
        textToSet += "\nNumber of animals:" + map.info.getAnimalCount();
        textToSet += "\nNumber of plants:" + map.info.getGrassCount();
        textToSet += "\nDominating genotype:" + map.info.getDominatingGenotype() + " (" + map.info.getDominatingGenotypeCount() + " appearances)";
        textToSet += "\nAverage energy level (alive):" + map.info.getAverageEnergy();
        textToSet += "\nAverage lifespan (dead):" + map.info.getAverageDeathAge();
        textToSet += "\nAverage number of children(alive):" + map.info.getAverageChildrenCount();
        mapInfoLabel.setText(textToSet);
    }

    public void addAnimal(AbstractAnimal animal) {
        comboList.add(animal);
    }

    @Override
    public void positionChanged(AbstractAnimal movedAnimal, Vector2d oldPosition, Vector2d newPostition) {

    }

    @Override
    public void entityRemoved(AbstractMapElement removedElement, Vector2d oldPosition) {
        if (removedElement == selectedAnimal) {
            animalInfoLabel.setText(animalInfoLabel.getText() + "\nDied on day " + map.getAge());
            comboBox.setValue(null);
        }
        comboList.remove(removedElement);
    }
}