package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.challenges.Challenge;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Map;

public class ChallengeListCommand extends CommandListener
{
    public ChallengeListCommand()
    {
        super("ChallengeList", "challengeList");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        // TODO this whole class, along with the rest of the small commands
    }
}
