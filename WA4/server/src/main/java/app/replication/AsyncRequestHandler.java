package app.replication;

@FunctionalInterface
public interface AsyncRequestHandler<T> {
    void run(T t);
}
