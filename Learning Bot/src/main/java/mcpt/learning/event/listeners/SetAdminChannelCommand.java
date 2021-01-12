package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class SetAdminChannelCommand extends CommandListener
{
    public SetAdminChannelCommand()
    {
        super("SetAdminChannel", "setAdminChannel (use this command in the channel you want to be the admin channel.)");
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
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | SetAdminChannel");
        embed.setColor(new Color(0x3B6EFF));
        labyrinthEvent.setAdminChannel(event.getChannel());
        embed.setDescription("Success.");
        channel.sendMessage(embed.build()).queue();
    }
}
