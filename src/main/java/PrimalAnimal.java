public class PrimalAnimal extends AbstractAnimal {

    public PrimalAnimal(Vector2d position, JungleMap map) {
        super(map.getAge(), map.nameAssigner.getName());
        this.position = position;
        this.energy = map.properties.startEnergy;
        this.map = map;
        this.dna = new int[32];
        for (int i = 0; i < 32; i++) {
            dna[i] = map.random.nextInt(8);
        }
    }

    @Override
    public String toString() {
        return this.name + ", primal " + "\n" + "Position:" + this.position.toString();
    }


    @Override
    public void newDescendant() {
        this.descendantsCount++;
    }
}
