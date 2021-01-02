package mcpt.learning.event.challenges.interfaces.attributes;

import mcpt.learning.event.challenges.interfaces.Parameter;

import java.util.ArrayList;

public abstract class ParameterList<T> implements Parameter
{
    private ArrayList<T> values;

    @Override
    public void init(String args)
    {
        String[] values = args.split(",");
        this.values = new ArrayList<>();
        for(String value: values)
            this.values.add(parseValue(value.trim()));
    }

    protected abstract T parseValue(String args);

    public ArrayList<T> getValues()
    {
        return values;
    }

    public void setValues(ArrayList<T> values)
    {
        this.values = values;
    }
}