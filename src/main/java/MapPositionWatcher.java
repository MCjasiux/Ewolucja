import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MapPositionWatcher implements IRemovalObserver, IChangeObserver {
    private ArrayList<Vector2d> freeSpots;
    private final Vector2d jungleOffset;
    private final JungleMap map;
    private final int width;
    private final int height;
    private final int jungleWidth;
    private final int jungleHeight;
    private final Predicate<Vector2d> insideJungle;

    public MapPositionWatcher(JungleMap map) {
        this.map = map;
        this.jungleOffset = map.properties.jungleOffset;
        this.jungleHeight = map.properties.jungleHeight;
        this.jungleWidth = map.properties.jungleWidth;
        this.height = map.properties.height;
        this.width = map.properties.width;
        this.freeSpots = new ArrayList<Vector2d>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                freeSpots.add(new Vector2d(i, j));
            }
        }
        insideJungle = vector2d -> vector2d.x >= jungleOffset.x && vector2d.y >= jungleOffset.y && vector2d.x < jungleOffset.x + jungleWidth && vector2d.y < jungleOffset.y + jungleHeight;
    }

    public List<Vector2d> getFreeNeighbours(Vector2d vect) {
        LinkedList<Vector2d> out = new LinkedList<>();
        Vector2d[] dirs = map.directions.getDirectionVectors();
        for (Vector2d dir : dirs
        ) {
            Vector2d newVect = vect.addWithLimit(dir, map.properties.size);
            if (this.freeSpots.contains(newVect))
                out.add(newVect);
        }
        Collections.shuffle(out);
        return out;
    }

    public List<Vector2d> getFreeSpots() {
        List<Vector2d> spots = freeSpots.stream().collect(Collectors.toList());
        Collections.shuffle(spots);
        return spots;
    }

    public List<Vector2d> getFreeJungleSpots() {

        List<Vector2d> jungleSpots = freeSpots.stream().filter(insideJungle).collect(Collectors.toList());
        Collections.shuffle(jungleSpots);
        return jungleSpots;
    }

    public List<Vector2d> getFreeSavannahSpots() {
        List<Vector2d> savannahSpots = freeSpots.stream().filter(insideJungle.negate()).collect(Collectors.toList());
        Collections.shuffle(savannahSpots);
        return savannahSpots;
    }

    public void entityAdded(Vector2d entityPosition) {
        freeSpots.remove(entityPosition);
    }


    @Override
    public void positionChanged(AbstractAnimal movedAnimal, Vector2d oldPosition, Vector2d newPostition) {
        if (map.getAnimals().get(oldPosition).isEmpty() && map.getGrasses().get(oldPosition) == null) {
            freeSpots.add(oldPosition);
        }
        freeSpots.remove(newPostition);
    }

    @Override
    public void entityRemoved(AbstractMapElement removedElement, Vector2d oldPosition) {
        if (map.getAnimals().get(oldPosition).isEmpty() && map.getGrasses().get(oldPosition) == null) {
            freeSpots.add(oldPosition);
        }
    }
}
