import java.util.Comparator;

public class AnimalEnergyComparator implements Comparator<AbstractAnimal> {
    @Override
    public int compare(AbstractAnimal v1, AbstractAnimal v2) {
        int result = 0;
        if (v1.getEnergy() < v2.getEnergy()) {
            result = -1;
        } else if (v1.getEnergy() > v2.getEnergy()) {
            result = 1;
        }
        return result;

    }

}
