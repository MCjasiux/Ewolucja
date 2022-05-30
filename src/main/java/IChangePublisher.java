public interface IChangePublisher {
    void addChangeObserver(IChangeObserver observer);
    void removeChangeObserver(IChangeObserver observer);

    void positionChanged(Vector2d oldPosition);
}
