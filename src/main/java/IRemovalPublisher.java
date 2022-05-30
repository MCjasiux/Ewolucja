public interface IRemovalPublisher {
    void addRemovalObserver(IRemovalObserver observer);
    void removeRemovalObserver(IRemovalObserver observer);
    void publishRemoval();
}
