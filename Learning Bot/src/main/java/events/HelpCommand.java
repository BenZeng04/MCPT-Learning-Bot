package events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class HelpCommand extends CommandListener
{

    @Override
    public String commandName()
    {
        return "help";
    }

    @Override
    public void onCommandRun(String[] args, GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Help");
        embed.setColor(new Color(0x3B6EFF));
        embed.addField("What did you expect this to do?", "MCPT Learning Bot", true);
        channel.sendMessage(embed.build()).queue();
    }
}
