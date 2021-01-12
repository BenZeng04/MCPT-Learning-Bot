package mcpt.learning.event;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

public interface MCPTEvent
{
    void load(String eventGuildID);
    void save(String eventGuildID);
}
