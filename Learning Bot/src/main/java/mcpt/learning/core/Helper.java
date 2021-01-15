package mcpt.learning.core;

import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.MCPTEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public final class Helper
{
    private Helper()
    {
    }

    private static Map<String, MCPTEvent> guildEvents = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    public static JDA jda;
    public static final char DEFAULT_PREFIX = '!';
    public static final String ALPHANUMERIC = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXXCVBNM";

    public static boolean isAlphanumeric(String str)
    {
        boolean[] charMap = new boolean[256];
        for(char c: ALPHANUMERIC.toCharArray())
            charMap[c] = true;
        for(char c: str.toCharArray())
            if(!charMap[c])
                return false;
        return true;
    }

    public static long minutesToMillis(int mins)
    {
        return mins * 60000L;
    }

    public static int millisToMinutes(long millis)
    {
        return (int) (millis / 60000);
    }

    public static int millisToSeconds(long millis)
    {
        return (int) (millis / 1000);
    }

    public static int lineCount(String str)
    {
        if(str == null)
            return 1;
        int ret = 0;
        for(char c: str.toCharArray())
            if(c == '\n')
                ret++;
        return ret + 1;
    }

    /**
     * Returns the LabyrinthEvent associated with the current guild.
     *
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
     *
     * @param event the guild message event
     */
    public static void reload(GuildMessageReceivedEvent event)
    {
        // this needs to be changed to support a system where multiple events exist in a given guild. This will be the case for every event after this one.
        String eventGuildID = event.getGuild().getId();
        MCPTEvent mcptEvent = guildEvents.get(eventGuildID);
        if(mcptEvent == null)
            mcptEvent = new LabyrinthEvent();
        mcptEvent.load(eventGuildID);
        guildEvents.put(eventGuildID, mcptEvent);
    }

    public static boolean isExec(GuildMessageReceivedEvent event)
    {
        Set<String> validRoles = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        validRoles.add("Exec");

        boolean hasExecRole = false;
        for(Role role: event.getMember().getRoles())
            if(validRoles.contains(role.getName()))
                hasExecRole = true;
        // TODO fix the administration system within the bot -> Make admin channels guild-based instead of event based
        return hasExecRole && event.getChannel().getId().equalsIgnoreCase(
            ((LabyrinthEvent) getMCPTEvent(event)).getAdminChannel(event).getId());
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
