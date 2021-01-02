package mcpt.learning.event.challenges;

import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthNode;
import mcpt.learning.event.challenges.interfaces.attributes.SingleIntegerParameter;
import mcpt.learning.event.challenges.interfaces.attributes.StringParameterList;

import java.util.ArrayList;

public class ChallengeFactory
{
    public static Challenge createChallenge(String ID, ChallengeType type)
    {
        switch(type)
        {
            case MULTIPLE_CHOICE:
                Challenge ret = new Challenge(ID);

                StringParameterList choices = new StringParameterList("CHOICES", "CHOICES [choice1,choice2,choice3...]");
                SingleIntegerParameter answerID = new SingleIntegerParameter("ANSWER", "ANSWER [index (one-indexed)]");

                ret.setSubmissionFormat("submit " + ID + " [Option Number]");
                ret.addParameters(choices, answerID);
                ret.setGrader(ans -> Integer.parseInt(ans.trim()) == answerID.getValue());
                ret.setPrompt(() -> {
                    StringBuilder optionPrompt = new StringBuilder();
                    ArrayList<String> options = choices.getValues();
                    for(int i = 0; i < options.size(); i++)
                    {
                        optionPrompt.append("**" + (i + 1) + ". **");
                        optionPrompt.append(options.get(i));
                        if(i != options.size() - 1)
                            optionPrompt.append("\n");
                    }
                    return optionPrompt.toString();
                });
                return ret;
            default:
                return null; // haven't created yet : (
        }
    }
}
