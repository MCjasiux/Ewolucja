public interface IAnimal extends IMapElement {
    public void newDescendant();

    public float getEnergy();

    public float reproduce();

    public int[] getDna();

    public void move();

    public void feed(float howMuch);
}
