package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.event.challenges.Challenge;
import mcpt.learning.event.challenges.MultiChoiceChallenge;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ChallengeCommand extends CommandListener
{
    public static Challenge challenge = new MultiChoiceChallenge("Which of the following is the correct answer?", "1A");
    public ChallengeCommand()
    {
        super("challenge", "!challenge [challengeName]");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        challenge.userPrompt(event);
    }
}
