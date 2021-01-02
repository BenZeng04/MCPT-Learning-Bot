package mcpt.learning.event.challenges.interfaces.attributes;

public class SingleStringParameter extends SingleParameter<String>
{
    private String name, helpArgs;

    public SingleStringParameter(String name, String helpArgs)
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
    protected String parseValue(String args)
    {
        return args.trim();
    }
}