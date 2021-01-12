package mcpt.learning.event.challenges.interfaces.attributes;

import mcpt.learning.core.Helper;
import mcpt.learning.event.challenges.interfaces.Parameter;

import java.util.ArrayList;

public abstract class ParameterList<T> implements Parameter
{
    private ArrayList<T> values;

    @Override
    public void init(String args)
    {
        if(args.equalsIgnoreCase("null")) values = null;
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

    @Override
    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        if(values == null) ret.append("null");
        else for(int i = 0; i < values.size(); i++)
        {
            ret.append(values.get(i));
            if(i != values.size() - 1) ret.append(',');
        }
        return name() + " " + Helper.lineCount(ret.toString()) + "\n" + ret.toString();
    }
}