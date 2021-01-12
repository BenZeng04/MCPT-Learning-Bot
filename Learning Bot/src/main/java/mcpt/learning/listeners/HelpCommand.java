package mcpt.learning.listeners;
import mcpt.learning.core.CommandListener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class HelpCommand extends CommandListener
{
    public HelpCommand()
    {
        super("help", "help OR help [command name]");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        if(args.length() == 0)
        {
            TextChannel channel = event.getChannel();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("MCPT Learning Bot | Help");
            embed.setColor(new Color(0x3B6EFF));
            embed.setDescription("Use help [page] to scroll to a specific page.\ntest");
            embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
            embed.addField("help", "Brings up this page.", false);
            embed.addField("", "Page " + "1", true);
            channel.sendMessage(embed.build()).queue();
        }
        else
        {
            CommandListener infoCommand = CommandListener.map.get(args.toLowerCase());
            if(infoCommand != null) infoCommand.helpMessage(event);
            else throw new IllegalArgumentException();
        }
    }
}
