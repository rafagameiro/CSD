package app.utils;

@FunctionalInterface
public interface Function2<A,B,C> {
    C apply(A a, B b);
}
