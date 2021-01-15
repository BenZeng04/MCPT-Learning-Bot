package mcpt.learning.event;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface MCPTEvent
{
    // TODO: Add more stuff here after if there is time...
    void load(String eventGuildID);

    void save(String eventGuildID);

    void startEvent(GuildMessageReceivedEvent event);

    boolean hasStarted();
}
