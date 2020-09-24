package app.utils;

@FunctionalInterface
public interface Function1<T,V> {

    V apply(T t) throws Exception;
}
