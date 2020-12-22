package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class CommandListener extends ListenerAdapter
{
    public abstract String commandName();
    public int minArguments() { return 0; /* Minimum arguments excluding the command name */ }
    public void onIncorrectArguments(GuildMessageReceivedEvent event) {} // Default nothing happens
    public abstract void onCommandRun(String[] args, GuildMessageReceivedEvent event);
    @Override
    public final void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if(event.getAuthor().isBot()) return;
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase(Helper.getPrefix(event.getGuild()) + commandName()))
        {
            String[] updatedArgs = new String[args.length - 1];
            if(updatedArgs.length >= minArguments())
            {
                for(int i = 0; i < args.length - 1; i++)
                    updatedArgs[i] = args[i + 1];
                onCommandRun(updatedArgs, event);
            }
            else onIncorrectArguments(event);
        }
    }
}
