package mcpt.learning.core;

import mcpt.learning.event.LabyrinthTeam;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.MCPTEvent;
import mcpt.learning.event.challenges.Challenge;
import mcpt.learning.event.challenges.ChallengeFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public final class Helper
{
    private Helper() {}

    private static Map<String, MCPTEvent> guildEvents = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    protected static JDA jda;
    public static final char DEFAULT_PREFIX = '!';
    public static final String ALPHANUMERIC = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXXCVBNM";
    public static boolean isAlphanumeric(String str)
    {
        boolean[] charMap = new boolean[256];
        for(char c: ALPHANUMERIC.toCharArray()) charMap[c] = true;
        for(char c: str.toCharArray()) if(!charMap[c]) return false;
        return true;
    }
    public static long minutesToMillis(int mins)
    {
        return mins * 60000L;
    }
    public static int lineCount(String str)
    {
        int ret = 0;
        for(char c: str.toCharArray()) if(c == '\n') ret++;
        return ret + 1;
    }
    /**
     * Returns the LabyrinthEvent associated with the current guild.
     * @param event the guild message event
     */
    public static MCPTEvent getMCPTEvent(GuildMessageReceivedEvent event)
    {
        // TODO this needs to be changed to support a system where multiple events exist in a given guild. This will be the case for every event after this one.
        return guildEvents.get(event.getGuild().getId());
    }

    /**
     * TODO: The saving process can definitely be improvable with classes in the future.
     * Reloads the LabyrinthEvent associated with the current guild.
     * @param event the guild message event
     */
    public static void reload(GuildMessageReceivedEvent event)
    {
        String eventGuildID = event.getGuild().getId();
        MCPTEvent mcptEvent = guildEvents.get(eventGuildID);
        if(mcptEvent == null) mcptEvent = new LabyrinthEvent();
        mcptEvent.load(eventGuildID);
        guildEvents.put(eventGuildID, mcptEvent);
    }
    public static void save(GuildMessageReceivedEvent event)
    {
        String eventGuildID = event.getGuild().getId();
        MCPTEvent mcptEvent = guildEvents.get(eventGuildID);
        mcptEvent.save(eventGuildID);
    }
    public static char getPrefix(GuildMessageReceivedEvent event)
    {
        // TODO add method to change prefix
        return DEFAULT_PREFIX;
    }
}
