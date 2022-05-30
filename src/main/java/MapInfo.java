import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MapInfo implements IRemovalObserver {
    private JungleMap map;

    public int getAnimalCount() {
        return animalCount;
    }

    public int getGrassCount() {
        return map.getGrasses().values().size();
    }

    public int getDominatingGenotype() {
        return dominatingGenotype;
    }

    public int getDominatingGenotypeCount() {
        return dominatingGenotypeCount;
    }

    public float getAverageEnergy() {
        return averageEnergy;
    }

    public float getAverageDeathAge() {
        return averageDeathAge;
    }

    public float getAverageChildrenCount() {
        return averageChildrenCount;
    }

    public int getMapAge() {
        return map.getAge();
    }

    public void animalPlaced() {
        animalCount++;
        findTopGenotype();
    }

    public int[] getGenotypes() {
        return genotypes;
    }

    private int animalCount;
    private int dominatingGenotype;
    private int dominatingGenotypeCount;
    private float averageEnergy;
    private float averageDeathAge;
    private float averageChildrenCount;
    private int[] genotypes;
    private ArrayList<Integer> graveyard;   //lista z wiekami zmar³ych zwierz¹t

    public MapInfo(JungleMap map) {
        this.map = map;
        this.graveyard = new ArrayList<>();
        getData();
        findTopGenotype();
    }

    private void findTopGenotype() {
        this.genotypes = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        int topGene = -1;
        int topGeneCount = -1;
        for (LinkedList<AbstractAnimal> list : map.getAnimals().values()
        ) {
            for (AbstractAnimal animal : list
            ) {
                int[] dna = animal.dna;
                for (int i = 0; i < dna.length; i++) {
                    genotypes[dna[i]]++;
                }
            }
        }
        for (int i = 0; i < genotypes.length; i++) {
            if (genotypes[i] > topGeneCount) {
                topGene = i;
                topGeneCount = genotypes[i];
            }
        }
        dominatingGenotype = topGene;
        dominatingGenotypeCount = topGeneCount;
    }

    public void getData() {
        this.animalCount = getAnimalCount();
        float totalEnergy = 0;
        float totalChildrenCount = 0;
        for (LinkedList<AbstractAnimal> list : map.getAnimals().values()
        ) {
            for (AbstractAnimal animal : list
            ) {
                totalEnergy += animal.getEnergy();
                totalChildrenCount += animal.getChildrenCount();
            }
        }
        averageChildrenCount =  (totalChildrenCount / animalCount);
        averageEnergy = (totalEnergy / animalCount);

    }

    @Override
    public void entityRemoved(AbstractMapElement removedElement, Vector2d oldPosition) {
        if (removedElement instanceof AbstractAnimal) {
            animalCount--;
            graveyard.add(map.getAge() - ((AbstractAnimal) removedElement).birthDate);
            for (Integer i : graveyard
            ) {
                averageDeathAge += i;
            }
            averageDeathAge = averageDeathAge / graveyard.size();
        }
        findTopGenotype();
    }
}
