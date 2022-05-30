import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public abstract class AbstractAnimal extends AbstractMapElement implements IAnimal, IChangePublisher {
    protected List<IChangeObserver> changeObserverList = new LinkedList<IChangeObserver>();
    protected float energy;
    protected int[] dna;
    protected int direction;
    protected final int birthDate;
    protected final String name;

    protected AbstractAnimal(int birthDate, String name) {
        this.birthDate = birthDate;
        this.name = name;
    }


    public int getChildrenCount() {
        return childrenCount;
    }

    public int getDescendantsCount() {
        return descendantsCount;
    }

    protected int childrenCount = 0;
    protected int descendantsCount = 0;

    public float getEnergy() {

        return energy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractAnimal that = (AbstractAnimal) o;
        return Float.compare(that.energy, energy) == 0 &&
                direction == that.direction &&
                birthDate == that.birthDate &&
                childrenCount == that.childrenCount &&
                descendantsCount == that.descendantsCount &&
                Arrays.equals(dna, that.dna) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(energy, direction, birthDate, name, childrenCount, descendantsCount);
        result = 31 * result + Arrays.hashCode(dna);
        return result;
    }

    public String getName() {
        return name;
    }

    public int[] getDna() {
        return dna;
    }

    protected void adjustDna(int[] dna) {
        //w razie konieczności zmienia dna tak, żeby występował każdy kierunek
        int[] safeGuard = {0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < dna.length; i++) {
            safeGuard[dna[i]]++;
        }
        for (int i = 0; i < safeGuard.length; i++) {
            int check = safeGuard[i];
            if (check > 0) {

            } else {
                for (int j = 0; j < dna.length; j++) {
                    if (safeGuard[j] > 1) { //jeśli inna liczba występuje 2 razy lub więcej, to można ją zastąpić
                        dna[j] = i;
                        break;
                    }
                }
                dna[i] = i;
            }
        }
    }

    public float reproduce() {
        //zabiera 1/4 energii aby przekazać ją dziecku
        float quarter = (float) (energy * 0.25);
        this.energy = (float) (this.energy * 0.75);
        return quarter;
    }

    public void move() {
        int newRotation = this.map.random.nextInt(31);  //losowy numer do wylosowania kierunku z dna
        this.direction = (this.direction + newRotation) % 8;         //obrót zwierzęcia
        Vector2d directionVect = this.map.directions.toUnitVector(this.direction);  //wektor przemieszczenia
        this.position = this.position.addWithLimit(directionVect, map.properties.size);
        this.energy -= this.map.properties.moveEnergy;  //strata energii
    }

    public void feed(float howMuch) {
        this.energy += howMuch;
    }

    public void positionChanged(Vector2d oldPosition) {
        for (IChangeObserver o : changeObserverList
        ) {
            o.positionChanged(this, oldPosition, position);
        }
    }

    @Override
    public void addChangeObserver(IChangeObserver observer) {
        changeObserverList.add(observer);
    }

    @Override
    public void removeChangeObserver(IChangeObserver observer) {
        changeObserverList.remove(observer);
    }

    @Override
    public void removeObservers() {
        changeObserverList.clear();
        removalObserverList.clear();
    }
}
