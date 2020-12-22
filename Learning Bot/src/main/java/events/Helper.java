package events;

import net.dv8tion.jda.api.entities.Guild;

public final class Helper
{
    private Helper() {}
    public static char DEFAULT_PREFIX = '!';
    public static char getPrefix(Guild guild)
    {
        // TODO add method to change prefix
        return DEFAULT_PREFIX;
    }
}
