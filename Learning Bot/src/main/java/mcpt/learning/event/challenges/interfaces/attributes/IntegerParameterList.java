package mcpt.learning.event.challenges.interfaces.attributes;

public class IntegerParameterList extends ParameterList<Integer>
{
    private String name, helpArgs;

    public IntegerParameterList(String name, String helpArgs)
    {
        this.name = name;
        this.helpArgs = helpArgs;
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String helpArgs()
    {
        return helpArgs;
    }

    @Override
    protected Integer parseValue(String args)
    {
        return Integer.parseInt(args.trim());
    }
}