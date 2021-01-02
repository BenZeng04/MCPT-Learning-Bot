package mcpt.learning.core;

import net.dv8tion.jda.api.entities.Guild;

import java.io.*;

public final class Helper
{
    private Helper() {}
    private static final String FILE_PATH = "data.txt";
    private static PrintWriter pw;
    private static BufferedReader br;
    static
    {
        try
        {
            pw = new PrintWriter(new FileWriter(FILE_PATH));
            br = new BufferedReader(new FileReader(FILE_PATH));
        }
        catch(IOException e) {}
    }
    public static final char DEFAULT_PREFIX = '!';

    /**
     * Format for labyrinth:
     * NodeCount ChallengeCount
     * Root Node
     *
     */
    public static void load()
    {

    }
    public static void save()
    {

    }
    public static char getPrefix(Guild guild)
    {
        // TODO add method to change prefix
        return DEFAULT_PREFIX;
    }
}
