package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class RemoveChallengeCommand extends CommandListener
{
    public RemoveChallengeCommand()
    {
        super("RemoveChallenge", "removechallenge [challengeName]");
    }

    @Override
    public boolean hasPermissions(GuildMessageReceivedEvent event)
    {
        return event.getMember().getPermissions().contains(Permission.ADMINISTRATOR);
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);

        TextChannel channel = event.getChannel();
        labyrinthEvent.setChallenge(args, null);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Challenge " + args);
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        embed.setDescription("Successfully removed challenge " + args + ".");
        channel.sendMessage(embed.build()).queue();
    }
}
