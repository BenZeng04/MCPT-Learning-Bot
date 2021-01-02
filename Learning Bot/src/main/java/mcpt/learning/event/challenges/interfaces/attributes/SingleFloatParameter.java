package mcpt.learning.event.challenges.interfaces.attributes;

public class SingleFloatParameter extends SingleParameter<Double>
{
    private String name, helpArgs;

    public SingleFloatParameter(String name, String helpArgs)
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
    protected Double parseValue(String args)
    {
        return Double.parseDouble(args.trim());
    }
}
