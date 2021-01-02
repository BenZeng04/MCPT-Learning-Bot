package mcpt.learning.core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.HashMap;

public abstract class CommandListener extends ListenerAdapter
{
    public static HashMap<String, CommandListener> map = new HashMap<>();
    public final String COMMAND_NAME;
    public final String DEFAULT_ARGUMENTS;

    public CommandListener(String command_name, String default_arguments)
    {
        COMMAND_NAME = command_name;
        DEFAULT_ARGUMENTS = default_arguments;
        map.put(COMMAND_NAME.toLowerCase(), this);
    }

    public boolean hasPermissions(GuildMessageReceivedEvent event) { return true; }
    public void helpMessage(GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | " + COMMAND_NAME.substring(0, 1).toUpperCase() + COMMAND_NAME.substring(1));
        embed.setColor(new Color(0x3B6EFF));
        embed.setDescription("**Arguments:** " + Helper.getPrefix(event.getGuild()) + DEFAULT_ARGUMENTS);
        channel.sendMessage(embed.build()).queue();
    }
    public abstract void onCommandRun(String args, GuildMessageReceivedEvent event);
    @Override
    public final void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if(!hasPermissions(event)) return;
        if(event.getAuthor().isBot()) return;
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase(Helper.getPrefix(event.getGuild()) + COMMAND_NAME))
        {
            StringBuilder updatedArgs = new StringBuilder();
            for(int i = 0; i < args.length - 1; i++)
            {
                updatedArgs.append(args[i + 1]);
                if(i != args.length - 2) updatedArgs.append(" ");
            }
            try
            {
                onCommandRun(updatedArgs.toString(), event);
                Helper.save(); // Updates the locally stored files
            }
            catch(Exception e)
            {
                helpMessage(event);
                e.printStackTrace();
            }
        }
    }
}
