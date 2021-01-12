package mcpt.learning.event;

import mcpt.learning.core.Helper;
import mcpt.learning.event.challenges.Challenge;
import mcpt.learning.event.challenges.ChallengeFactory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author yeahbennou
 * LabyrinthEvent.java
 * The overhauling class responsible for all event-related commands and variables.
 * For event-related classes that can be created and deleted (such as teams and challenges) and stored in files, do NOT store instances of such classes outside of the LabyrinthEvent class.
 * Such classes should be stored via IDs and accessed with the Maps to prevent potential errors that may arise from deletion of these classes.
 *
 * SAVING / LOADING FORMAT:
 *
 * _challenges_:
 * [ChallengeCount]
 * [Challenge1Name] [Challenge1Type] [Challenge1ParamCount] -> use the factory to build the challenge
 * [Challenge1Param1] [Challenge1ValueLineCount1]
 * --- LineCount lines follow (For multiline parameters such as the description)
 *
 * [Challenge1Param2] [Challenge1Param2]... etc
 * [Challenge2Name]...
 *
 * _teams_:
 * [TeamCount]
 * [Team1Name]
 * [Team1UserID1] [Team1UserID2] [Team1UserID3] [Team1UserID4]… (Can have less than 4 users)
 * [Team1BonusTimeReceived] [Team1Points]
 * [Team1AttemptedChallenge1] [Team1AttemptedChallenge2]…
 * [Team1CorrectChallenge1] [Team1CorrectChallenge2]…
 *
 * _labyrinth_:
 * [AdminChannel] [BeginTime] [EventDuration] [ChallengePointBonus]
 * [MainChallengeDescriptionLineCount]
 * [LabyrinthMainChallenge]
 * [LabyrinthImageURL]
 */

public class LabyrinthEvent implements TeamEvent
{
    private final static long DEFAULT_EVENT_DURATION = Helper.minutesToMillis(20); // default 20 minutes
    private final static int DEFAULT_POINT_BONUS = 5; // default 5 points per question
    private static final String CHALLENGE_FILE = "challenges.txt";
    private static final String TEAM_FILE = "teams.txt";
    private static final String LABYRINTH_FILE = "labyrinth.txt";
    // Manual Grading
    private String adminChannel;
    private int manualGradeID;
    private Map<Integer, ChallengeSubmissionEvent> pendingManualGrades;
    // Labyrinth Descriptors
    public final String TITLE;
    private Map<String, Challenge> challenges;
    private String mainChallenge; // The main challenge
    private String imageURL;
    // Event-related variables
    private Map<String, LabyrinthTeam> teams;
    private Map<String, String> userToTeam;
    private long beginTime;
    private long eventDuration;
    private int challengePointBonus;

    /**
     * Default Constructor
     */
    public LabyrinthEvent()
    {
        TITLE = "PATHWAYS";
        // TODO Once multi-event system is set up, have the title be in the constructor
        challenges = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        teams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        userToTeam = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        pendingManualGrades = new TreeMap<>();
        beginTime = Long.MAX_VALUE; // Default begin time
        eventDuration = DEFAULT_EVENT_DURATION;
        challengePointBonus = DEFAULT_POINT_BONUS;
    }

    public void addTeam(String teamID, String[] teamMembers)
    {
        LabyrinthTeam oldTeam = teams.get(teamID);
        LabyrinthTeam newTeam = new LabyrinthTeam(teamID, teamMembers);
        for(String user: oldTeam.teamMemberIDs)
            userToTeam.put(user, teamID);
        teams.put(teamID, newTeam);
    }

    public void removeTeam(String teamID)
    {
        LabyrinthTeam oldTeam = teams.get(teamID);
        for(String user: oldTeam.teamMemberIDs)
            userToTeam.put(user, null);
        teams.put(teamID, null);
    }

    public Team getTeam(String teamID)
    {
        return teams.get(teamID);
    }

    public Team getTeamFromUser(String userID)
    {
        return teams.get(userToTeam.get(userID));
    }

    public boolean hasStarted()
    {
        return beginTime != Long.MAX_VALUE;
    }

    /**
     * Returns a challenge based on the unique challenge ID.
     *
     * @param ID the ID
     * @return the challenge
     */
    public Challenge getChallenge(String ID)
    {
        return challenges.get(ID);
    }

    /**
     * Sets or modifies an existing challenge based on its ID.
     *
     * @param ID        the ID
     * @param challenge the challenge
     */
    public void setChallenge(String ID, Challenge challenge)
    {
        challenges.put(ID, challenge);
    }

    /**
     * Getter for the entire list/map of challenges.
     *
     * @return the map of challenges
     */
    public Map<String, Challenge> getChallengeList()
    {
        return challenges;
    }

    @Override
    public void load(String eventGuildID)
    {
        try
        {
            // challenges
            BufferedReader br = new BufferedReader(new FileReader(eventGuildID + TITLE + CHALLENGE_FILE));
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
                setChallenge(tokens[0], challenge);
            }
            // teams
            br = new BufferedReader(new FileReader(eventGuildID + TITLE + TEAM_FILE));
            int teamCount = Integer.parseInt(br.readLine());
            for(int i = 0; i < teamCount; i++)
            {
                String teamName = br.readLine();
                String[] teamMembers = br.readLine().split(" ");
                LabyrinthTeam team = new LabyrinthTeam(teamName, teamMembers);
                team.setEvent(this);
                String[] args = new String[LabyrinthTeam.FILE_PARSING_CONSTANT];
                for(int j = 0; j < args.length; j++)
                    args[j] = br.readLine();
                team.parseFromFile(args);
                addTeam(teamName, teamMembers);
            }
            // General labyrinth info
            br = new BufferedReader(new FileReader(eventGuildID + TITLE + LABYRINTH_FILE));
            String[] tokens = new String[4];
            adminChannel = tokens[0];
            beginTime = Long.parseLong(tokens[1]);
            eventDuration = Long.parseLong(tokens[2]);
            challengePointBonus = Integer.parseInt(tokens[3]);
            int lineCount = Integer.parseInt(br.readLine());
            StringBuilder desc = new StringBuilder();
            for(int i = 0; i < lineCount; i++)
            {
                desc.append(br.readLine());
                if(i != lineCount - 1) desc.append("\n");
            }
            imageURL = br.readLine();
        }
        catch(Exception e)
        {
            save(eventGuildID); // Saves default information in a newly created file if there is an exception
            e.printStackTrace();
        }
    }

    @Override
    public void save(String eventGuildID)
    {
        try
        {
            // challenges
            PrintWriter pw = new PrintWriter(new FileWriter(eventGuildID + TITLE + CHALLENGE_FILE));
            Map<String, Challenge> challengeList = getChallengeList();
            int challengeCount = challengeList.size();
            pw.println(challengeCount);
            for(Map.Entry<String, Challenge> entry: challengeList.entrySet())
                pw.println(entry.getValue());
            pw.close();
            // teams
            pw = new PrintWriter(new FileWriter(eventGuildID + TITLE + TEAM_FILE));
            Map<String, LabyrinthTeam> teamList = getTeams();
            int teamCount = teamList.size();
            pw.println(teamCount);
            for(Map.Entry<String, LabyrinthTeam> entry: teamList.entrySet())
                pw.println(entry.getValue());
            pw.close();
            // labyrinth
            pw = new PrintWriter(new FileWriter(eventGuildID + TITLE + LABYRINTH_FILE));
            pw.println(adminChannel + " " + beginTime + " " + eventDuration + " " + challengePointBonus);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setAdminChannel(TextChannel adminChannel)
    {
        this.adminChannel = adminChannel.getId();
    }

    /**
     * Getter for the admin channel, where administrative announcements take place.
     *
     * @param event the event containing the guild
     * @return the admin channel
     */
    public TextChannel getAdminChannel(GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getGuild().getTextChannelById(adminChannel);
        if(channel == null)
            throw new NullPointerException("Admin channel not set up");
        return channel;
    }

    /**
     * Adds a pending manual grade event into the queue.
     *
     * @param submissionEvent the event
     * @return the manual grading ID assigned to the event
     */
    public int queue(ChallengeSubmissionEvent submissionEvent)
    {
        for(Map.Entry<Integer, ChallengeSubmissionEvent> submissionEventEntry: pendingManualGrades.entrySet())
        {
            ChallengeSubmissionEvent entry = submissionEventEntry.getValue();
            if(entry.getTeamID().equals(submissionEvent.getTeamID()) && entry.getChallengeID()
                .equals(submissionEvent.getChallengeID()))
                throw new IllegalArgumentException("Already being graded.");
        }
        int ID = manualGradeID++;
        pendingManualGrades.put(ID, submissionEvent);
        return ID;
    }

    /**
     * Returns a challenge that needs to be graded.
     *
     * @param manualGradeID the manual grading ID
     * @return the challenge
     */
    public ChallengeSubmissionEvent getPendingSubmission(int manualGradeID)
    {
        return pendingManualGrades.get(manualGradeID);
    }

    public void finishManualGrade(int manualGradeID)
    {
        pendingManualGrades.put(manualGradeID, null);
    }

    public long getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(long beginTime)
    {
        this.beginTime = beginTime;
    }

    public long getEventDuration()
    {
        return eventDuration;
    }

    public void setEventDuration(long eventDuration)
    {
        this.eventDuration = eventDuration;
    }

    public int getChallengePointBonus()
    {
        return challengePointBonus;
    }

    public void setChallengePointBonus(int challengePointBonus)
    {
        this.challengePointBonus = challengePointBonus;
    }

    public Map<String, LabyrinthTeam> getTeams()
    {
        return teams;
    }

    public ArrayList<String> getUnlocks(String id)
    {
        ArrayList<String> ret = new ArrayList<>();
        for(Map.Entry<String, Challenge> entry: challenges.entrySet())
        {
            Challenge challenge = entry.getValue();
            String ID = entry.getKey();
            if(id.equalsIgnoreCase(challenge.getPrerequisite().getValue()))
                ret.add(ID);
        }
        return ret;
    }

    public ArrayList<String> getUnlocks(Set<String> completed)
    {
        ArrayList<String> ret = new ArrayList<>();
        for(Map.Entry<String, Challenge> entry: challenges.entrySet())
        {
            Challenge challenge = entry.getValue();
            String ID = entry.getKey();
            if(completed.contains(challenge.getPrerequisite().getValue()))
                ret.add(ID);
        }
        return ret;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public void setImageURL(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public String getMainChallenge()
    {
        return mainChallenge;
    }

    public void setMainChallenge(String mainChallenge)
    {
        this.mainChallenge = mainChallenge;
    }
}
