public interface IChangeObserver {
    void positionChanged(AbstractAnimal movedAnimal, Vector2d oldPosition, Vector2d newPosition);
}
