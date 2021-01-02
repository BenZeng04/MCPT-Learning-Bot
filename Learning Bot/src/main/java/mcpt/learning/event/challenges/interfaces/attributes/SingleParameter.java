package mcpt.learning.event.challenges.interfaces.attributes;

import mcpt.learning.event.challenges.interfaces.Parameter;

public abstract class SingleParameter<T> implements Parameter
{
    private T value;
    private String name;
    public T getValue()
    {
        return value;
    }
    public void init(String args)
    {
        value = parseValue(args);
    }

    protected abstract T parseValue(String args);
}
