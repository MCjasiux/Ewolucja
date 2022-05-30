public class Grass extends AbstractMapElement {
    public Grass(Vector2d positon, JungleMap map) {
        this.position = positon;
        this.map = map;
    }

    @Override
    public String toString() {
        return "grass";
    }
}
