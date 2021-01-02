package mcpt.learning.event.challenges;

import mcpt.learning.event.LabyrinthNode;
import mcpt.learning.event.challenges.interfaces.attributes.*;
import mcpt.learning.event.challenges.interfaces.Grader;
import mcpt.learning.event.challenges.interfaces.Parameter;
import mcpt.learning.event.challenges.interfaces.Prompt;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Challenge
{
    public final String ID;

    private Grader grader;
    private Prompt prompt;
    private HashMap<String, Parameter> challengeParameters;

    protected SingleStringParameter imageURL, description;
    protected StringParameterList bonusRewards;
    protected SingleIntegerParameter timeReward;
    protected String submissionFormat;

    private LabyrinthNode parentNode;

    protected Challenge(String id)
    {
        ID = id;
        challengeParameters = new HashMap<>();
        imageURL = new SingleStringParameter("IMG", "IMG [image URL (blank to remove)]");
        timeReward = new SingleIntegerParameter("TIME", "TIME [time reward (minutes)]");
        description = new SingleStringParameter("DESC", "DESC [description]");
        bonusRewards = new StringParameterList("BONUS", "BONUS [bonusURL1, bonusURL2... (Image URLs) (blank to remove)]");
        submissionFormat = "submit [answer]";
        addParameters(imageURL, description, bonusRewards, timeReward); // Default parameters that are shared across all challenges
    }

    public String getSubmissionFormat()
    {
        return submissionFormat;
    }

    public void setSubmissionFormat(String submissionFormat)
    {
        this.submissionFormat = submissionFormat;
    }

    public SingleStringParameter getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(SingleStringParameter imageURL)
    {
        this.imageURL = imageURL;
    }

    public SingleStringParameter getDescription()
    {
        return description;
    }

    public void setDescription(SingleStringParameter description)
    {
        this.description = description;
    }

    public Grader getGrader()
    {
        return grader;
    }

    public void setGrader(Grader grader)
    {
        this.grader = grader;
    }

    public SingleIntegerParameter getTimeReward()
    {
        return timeReward;
    }

    public void setTimeReward(SingleIntegerParameter timeReward)
    {
        this.timeReward = timeReward;
    }

    public StringParameterList getBonusRewards()
    {
        return bonusRewards;
    }

    public void setBonusRewards(StringParameterList bonusRewards)
    {
        this.bonusRewards = bonusRewards;
    }

    public HashMap<String, Parameter> getParameters()
    {
        return challengeParameters;
    }

    public Parameter getParameter(String name)
    {
        return challengeParameters.get(name.toUpperCase());
    }

    public void addParameter(Parameter parameter)
    {
        challengeParameters.put(parameter.name().toUpperCase(), parameter);
    }

    public void addParameters(Parameter... parameters)
    {
        for(Parameter parameter: parameters) addParameter(parameter);
    }

    public Prompt getPrompt()
    {
        return prompt;
    }

    public void setPrompt(Prompt prompt)
    {
        this.prompt = prompt;
    }

    public LabyrinthNode getParentNode()
    {
        return parentNode;
    }

    public void setParentNode(LabyrinthNode parentNode)
    {
        this.parentNode = parentNode;
    }
}
