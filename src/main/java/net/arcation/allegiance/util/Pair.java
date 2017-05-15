package net.arcation.allegiance.util;

/**
 * Created by Mr_Little_Kitty on 5/15/2017.
 */
public class Pair<T,V>
{
    private final T one;
    private final V two;

    public Pair(T t, V v)
    {
        this.one = t;
        this.two = v;
    }

    public T getOne()
    {
        return one;
    }

    public V getTwo()
    {
        return two;
    }
}
