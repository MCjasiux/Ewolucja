import java.util.LinkedList;

public class Animal extends AbstractAnimal {

    public AbstractAnimal[] parents;

    @Override
    public String toString() {
        return this.name + ", child of " + parents[0].name + " and " + parents[1].name + "\n" + "Position:" + this.position.toString();
    }


    private int[] crossOver(int[] dna1, int[] dna2) {
        //mieszanie dna rodziców
        int cut1 = map.random.nextInt(31);
        int cut2 = map.random.nextInt(31);
        if (cut1 == cut2) {
            cut2 = (cut2 + 16) % 32;
        }
        int[] out = new int[32];
        for (int i = 0; i < 32; i++) {
            if (i < Math.min(cut1, cut2) || i > Math.max(cut1, cut2)) { //punkty cięcia
                out[i] = dna1[i];
            } else {
                out[i] = dna2[i];
            }
        }
        adjustDna(out);
        return out;
    }

    public Animal(Vector2d position, String name, AbstractAnimal parent1, AbstractAnimal parent2, int energy, JungleMap map) {
        super(map.getAge(), map.nameAssigner.getName());
        this.map = map;
        this.position = position;
        this.dna = crossOver(parent1.getDna(), parent2.getDna());
        this.parents = new AbstractAnimal[]{parent1, parent2};
        this.energy = energy;
        this.direction = map.random.nextInt(63) % 8;
        parent1.childrenCount++;
        parent2.childrenCount++;
        LinkedList<AbstractAnimal> ancestorLine = new LinkedList<>();
        findAncestors(ancestorLine, parent1);
        findAncestors(ancestorLine, parent2);
    }

    private void findAncestors(LinkedList<AbstractAnimal> list, AbstractAnimal ancestor) {
        //służy o informowaniu przodków o nowych dzieciach/potomkach
        //oraz zapobiega wielokrotnemu liczeniu potomków w przypadku związków kazirodczych
        if (list.contains(ancestor)) {

        } else {
            list.add(ancestor);
            ancestor.newDescendant();
            if (ancestor instanceof PrimalAnimal) {
                return;
            } else {
                findAncestors(list, ((Animal) ancestor).parents[0]);
                findAncestors(list, ((Animal) ancestor).parents[1]);

            }
        }

    }

    @Override
    public void newDescendant() {
        this.descendantsCount++;
    }
}
