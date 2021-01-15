package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.MCPTEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class StartEventCommand extends CommandListener
{
    public StartEventCommand()
    {
        super("StartEvent", "startEvent (no arguments)");
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
        embed.setTitle("MCPT Learning Bot | StartEvent");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        try
        {
            MCPTEvent mcptEvent = Helper.getMCPTEvent(event);
            if(!mcptEvent.hasStarted())
            {
                mcptEvent.startEvent(event);
                embed.setDescription("Starting event!");
            }
            else
                embed.setDescription("ERROR: The event has already started!");
        }
        catch(Exception e)
        {
            embed.setDescription(e.toString());
        }
        channel.sendMessage(embed.build()).queue();
    }
}
