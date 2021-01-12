package mcpt.learning.event.challenges.interfaces.attributes;

import mcpt.learning.core.Helper;
import mcpt.learning.event.challenges.interfaces.Parameter;

public abstract class SingleParameter<T> implements Parameter
{
    private T value;
    public T getValue()
    {
        return value;
    }
    public void init(String args)
    {
        if(args.equalsIgnoreCase("null")) value = null;
        value = parseValue(args);
    }

    protected abstract T parseValue(String args);

    @Override
    public String toString()
    {
        return name() + " " + Helper.lineCount("" + value) // String conversion without using .toString(), as that may invoke null
            + "\n" + value;
    }
}
