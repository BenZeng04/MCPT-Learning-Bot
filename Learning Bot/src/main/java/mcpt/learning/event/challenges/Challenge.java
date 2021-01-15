package mcpt.learning.event.challenges;

import mcpt.learning.core.Helper;
import mcpt.learning.event.challenges.interfaces.Grader;
import mcpt.learning.event.challenges.interfaces.Parameter;
import mcpt.learning.event.challenges.interfaces.Prompt;
import mcpt.learning.event.challenges.interfaces.attributes.SingleIntegerParameter;
import mcpt.learning.event.challenges.interfaces.attributes.SingleStringParameter;
import mcpt.learning.event.challenges.interfaces.attributes.StringParameterList;

import java.util.Map;
import java.util.TreeMap;

public class Challenge
{
    public final String ID;
    private final String type;

    private Grader grader;
    private Prompt prompt;
    private Map<String, Parameter> challengeParameters;

    protected SingleStringParameter imageURL, description, prerequisite;
    protected StringParameterList bonusRewards;
    protected SingleIntegerParameter timeReward;
    protected String submissionFormat;

    protected Challenge(String id, String type)
    {
        if(!Helper.isAlphanumeric(id))
            throw new IllegalArgumentException("Challenge IDs must be alphanumeric.");
        ID = id;
        this.type = type;
        challengeParameters = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        imageURL = new SingleStringParameter("IMG", "IMG [image URL (\"null\" to remove)]");
        timeReward = new SingleIntegerParameter("TIME", "TIME [time reward (minutes)]");
        description = new SingleStringParameter("DESC", "DESC [description]");
        prerequisite = new SingleStringParameter("PREREQ",
                                                 "PREREQ [challenge prerequisite ID (\"null\" for no prerequisite)]");
        bonusRewards = new StringParameterList("BONUS",
                                               "BONUS [bonusURL1, bonusURL2... (Image URLs) (\"null\" to remove)]");
        submissionFormat = "submit [answer]";
        addParameters(imageURL, description, bonusRewards, timeReward,
                      prerequisite); // Default parameters that are shared across all challenges
    }

    public SingleStringParameter getImageURL()
    {
        return imageURL;
    }

    public SingleStringParameter getDescription()
    {
        return description;
    }

    public SingleIntegerParameter getTimeReward()
    {
        return timeReward;
    }

    public StringParameterList getBonusRewards()
    {
        return bonusRewards;
    }

    public Grader getGrader()
    {
        return grader;
    }

    public void setGrader(Grader grader)
    {
        this.grader = grader;
    }

    public String getSubmissionFormat()
    {
        return submissionFormat;
    }

    public void setSubmissionFormat(String submissionFormat)
    {
        this.submissionFormat = submissionFormat;
    }

    public Map<String, Parameter> getParameters()
    {
        return challengeParameters;
    }

    public Parameter getParameter(String name)
    {
        return challengeParameters.get(name);
    }

    public void addParameter(Parameter parameter)
    {
        challengeParameters.put(parameter.name(), parameter);
    }

    public void addParameters(Parameter... parameters)
    {
        for(Parameter parameter: parameters)
            addParameter(parameter);
    }

    public Prompt getPrompt()
    {
        return prompt;
    }

    public void setPrompt(Prompt prompt)
    {
        this.prompt = prompt;
    }

    public SingleStringParameter getPrerequisite()
    {
        return prerequisite;
    }

    // File IO stuff

    /**
     * Creates a multi-line string that will contain all the information this challenge contains for storing in a file.
     *
     * @return This string.
     */
    @Override
    public String toString()
    {
        // Note that the challenge made up of three interfaces, which are initialized by the ChallengeFactory and determined by the type.
        // The challenge stores a collection of parameters which have a name, and a string value that can be parsed by the parameter.
        StringBuilder ret = new StringBuilder();
        // First Line: [ID] [type] [parameterCount]
        ret.append(ID).append(" ").append(type).append(" ").append(challengeParameters.entrySet().size()).append("\n");
        // Next N Parameters (where N is the parameter count): [Parameter name] [Parameter value line count] -> Next LineCount lines: [Parameter value]
        // Example:
        // DESC 5
        // This is
        // an example
        // of a
        // description that
        // takes up 5 lines
        int count = 0;
        for(Map.Entry<String, Parameter> parameterEntry: challengeParameters.entrySet())
        {
            count++;
            ret.append(parameterEntry.getValue());
            if(count != challengeParameters.entrySet().size())
                ret.append("\n");
        }
        return ret.toString();
    }

    /**
     * We assume that the challenge that is calling parseFromFile() has already been created using an ID and a type from the ChallengeFactory.
     * args contains a list of all the parameters in the format [Parameter name] [parameter value] (no [])
     *
     * @param args the parameters in the format specified above
     */
    public void parseFromFile(String[] args)
    {
        for(String param: args)
        {
            String[] tokens = param.split(" ");
            String paramName = tokens[0];
            StringBuilder paramArgs = new StringBuilder();
            for(int i = 1; i < tokens.length; i++)
            {
                paramArgs.append(tokens[i]);
                if(i != tokens.length - 1)
                    paramArgs.append(' ');
            }
            if(!paramArgs.toString().equals("null"))
                challengeParameters.get(paramName).init(paramArgs.toString());
        }
    }
}
