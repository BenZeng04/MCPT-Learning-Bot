package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.ChallengeSubmissionEvent;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.listeners.LabyrinthCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class PendingSubmissionListCommand extends CommandListener
{
    public PendingSubmissionListCommand()
    {
        super("PendingSubmissionList", "pendingSubmissionList (no arguments)");
    }

    @Override
    public boolean hasPermissions(GuildMessageReceivedEvent event)
    {
        return Helper.isExec(event);
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | PendingSubmissionList");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        StringBuilder desc = new StringBuilder();

        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);
        desc.append("Pending submissions:\n");
        for(Map.Entry<Integer, ChallengeSubmissionEvent> entry: labyrinthEvent.getPendingManualGrades().entrySet())
            desc.append("ID " + entry.getKey() + ": " + entry.getValue().getParentEvent().getMessage().getJumpUrl() + "\n");

        if(labyrinthEvent.getPendingManualGrades().isEmpty())
            desc.append("No pending submissions.\n");

        channel.sendMessage(embed.build()).queue();
    }
}
