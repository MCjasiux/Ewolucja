public class MapDirections {
    private static String[] directionNames;
    private final Vector2d[] directionVectors;

    public MapDirections() {
        this.directionNames = new String[]{"North", "Northeast", "East", "Southeast", "South", "Southwest", "West", "Northwest"};
        this.directionVectors = new Vector2d[]{new Vector2d(0, 1), new Vector2d(1, 1), new Vector2d(1, 0), new Vector2d(1, -1), new Vector2d(0, -1), new Vector2d(-1, -1), new Vector2d(-1, 0), new Vector2d(-1, 1)};
    }

    public Vector2d[] getDirectionVectors() {
        return directionVectors;
    }

    public static String toString(int intDirection) {
        return directionNames[intDirection];
    }

    public Vector2d toUnitVector(int intDirection) {
        return directionVectors[intDirection];
    }
}
