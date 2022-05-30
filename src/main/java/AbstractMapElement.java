import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMapElement implements IRemovalPublisher, IMapElement {
    protected Vector2d position;
    protected List<IRemovalObserver> removalObserverList = new LinkedList<IRemovalObserver>();
    protected JungleMap map;

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public void addRemovalObserver(IRemovalObserver observer) {
        this.removalObserverList.add(observer);
    }

    @Override
    public void removeRemovalObserver(IRemovalObserver observer) {
        this.removalObserverList.remove(observer);
    }

    public void publishRemoval() {
        for (IRemovalObserver o : removalObserverList
        ) {
            o.entityRemoved(this, position);
        }
    }

    public void removeObservers() {
        removalObserverList.clear();
    }


}
