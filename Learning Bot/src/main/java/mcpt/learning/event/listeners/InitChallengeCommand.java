package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class InitChallengeCommand extends CommandListener
{
    public InitChallengeCommand()
    {
        super("InitChallenge", "!initchallenge [challengeName] [command] [args]");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        String[] tokens = args.split(" ");
        String challengeName = tokens[0]; // TODO use this later
        StringBuilder challengeArgs = new StringBuilder();
        for(int i = 1; i < tokens.length; i++)
        {
            challengeArgs.append(tokens[i]);
            if(i != tokens.length - 1) challengeArgs.append(" ");
        }
        ChallengeCommand.challenge.initChallenge(challengeArgs.toString(), event);
    }
}
