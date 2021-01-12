package mcpt.learning.core;

import mcpt.learning.event.EventTeam;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.challenges.Challenge;
import mcpt.learning.event.challenges.ChallengeFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.Map;
import java.util.Map;
import java.util.TreeMap;

/*
SAVING / LOADING FORMAT:

_challenges_:
[ChallengeCount]
[Challenge1Name] [Challenge1Type] [Challenge1ParamCount] -> use the factory to build the challenge
[Challenge1Param1] [Challenge1ValueLineCount1]
--- LineCount lines follow (For multiline parameters such as the description)

[Challenge1Param2] [Challenge1Param2]... etc
[Challenge2Name]...

_teams_:
[TeamCount]
[Team1Name]
[Team1UserID1] [Team1UserID2] [Team1UserID3] [Team1UserID4]… (Can have less than 4 users)
[Team1BonusTimeReceived] [Team1Points]
[Team1AttemptedChallenge1] [Team1AttemptedChallenge2]…
[Team1CorrectChallenge1] [Team1CorrectChallenge2]…

_labyrinth_:
[AdminChannel]
[BeginTime]
[EventDuration]
[ChallengePointBonus]
[LabyrinthMainChallenge]
[LabyrinthImageURL]
 */

public final class Helper
{
    private Helper() {}
    private static final String CHALLENGE_FILE = "challenges.txt";
    private static final String TEAM_FILE = "teams.txt";
    private static Map<String, LabyrinthEvent> guildEvents = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
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
    public static LabyrinthEvent getLabyrinth(GuildMessageReceivedEvent event)
    {
        return guildEvents.get(event.getGuild().getId());
    }

    /**
     * TODO: The saving process can definitely be improvable with classes in the future.
     * Reloads the LabyrinthEvent associated with the current guild.
     * @param event the guild message event
     */
    public static LabyrinthEvent reloadLabyrinth(GuildMessageReceivedEvent event)
    {
        String eventGuildID = event.getGuild().getId();
        LabyrinthEvent labyrinthEvent = guildEvents.get(eventGuildID);
        if(labyrinthEvent == null) labyrinthEvent = new LabyrinthEvent();
        // Challenges
        try
        {
            File file = new File(eventGuildID + CHALLENGE_FILE);
            if(!file.isFile())
            {
                PrintWriter pw = new PrintWriter(new FileWriter(eventGuildID + CHALLENGE_FILE));
                pw.println(0);
                pw.close();
            }
            else
            {
                BufferedReader br = new BufferedReader(new FileReader(eventGuildID + CHALLENGE_FILE));
                int challengeCount = Integer.parseInt(br.readLine());
                for(int i = 0; i < challengeCount; i++)
                {
                    String[] tokens = br.readLine().split(" ");
                    Challenge challenge = ChallengeFactory.createChallenge(tokens[0], tokens[1]);
                    int paramCount = Integer.parseInt(tokens[2]);
                    String[] params = new String[paramCount];
                    for(int j = 0; j < paramCount; j++)
                    {
                        String buf = br.readLine();
                        String[] args = buf.split(" ");
                        int lineCount = Integer.parseInt(args[1]);
                        StringBuilder sb = new StringBuilder(args[0]).append(' ');
                        for(int k = 0; k < lineCount; k++)
                        {
                            sb.append(br.readLine());
                            if(k != lineCount - 1)
                                sb.append('\n');
                        }
                        params[j] = sb.toString();
                    }

                    assert challenge != null;
                    challenge.parseFromFile(params);
                    labyrinthEvent.setChallenge(tokens[0], challenge);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        // Teams
        try
        {
            File file = new File(eventGuildID + TEAM_FILE);
            if(!file.isFile())
            {
                PrintWriter pw = new PrintWriter(new FileWriter(eventGuildID + TEAM_FILE));
                pw.println(0);
                pw.close();
            }
            else
            {
                BufferedReader br = new BufferedReader(new FileReader(eventGuildID + TEAM_FILE));
                int teamCount = Integer.parseInt(br.readLine());
                for(int i = 0; i < teamCount; i++)
                {
                    String teamName = br.readLine();
                    String[] teamMembers = br.readLine().split(" ");
                    EventTeam team = new EventTeam(teamName, teamMembers);
                    team.setEvent(labyrinthEvent);
                    String[] args = new String[EventTeam.FILE_PARSING_CONSTANT];
                    for(int j = 0; j < args.length; j++) args[j] = br.readLine();
                    team.parseFromFile(args);
                    labyrinthEvent.setTeam(teamName, team);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        guildEvents.put(eventGuildID, labyrinthEvent);
        return labyrinthEvent;
    }
    public static void save(LabyrinthEvent labyrinthEvent, GuildMessageReceivedEvent event)
    {
        String eventGuildID = event.getGuild().getId();
        try
        {
            PrintWriter pw = new PrintWriter(new FileWriter(eventGuildID + CHALLENGE_FILE));
            Map<String, Challenge> challengeList = labyrinthEvent.getChallengeList();
            int challengeCount = challengeList.size();
            pw.println(challengeCount);
            for(Map.Entry<String, Challenge> entry: challengeList.entrySet())
                pw.println(entry.getValue());
            pw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        // teams
        try
        {
            PrintWriter pw = new PrintWriter(new FileWriter(eventGuildID + TEAM_FILE));
            Map<String, EventTeam> teamList = labyrinthEvent.getTeams();
            int teamCount = teamList.size();
            pw.println(teamCount);
            for(Map.Entry<String, EventTeam> entry: teamList.entrySet())
                pw.println(entry.getValue());
            pw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static char getPrefix(GuildMessageReceivedEvent event)
    {
        // TODO add method to change prefix
        return DEFAULT_PREFIX;
    }
}
