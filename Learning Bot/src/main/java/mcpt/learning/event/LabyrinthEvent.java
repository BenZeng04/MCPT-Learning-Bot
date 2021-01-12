package mcpt.learning.event;

import mcpt.learning.core.Helper;
import mcpt.learning.event.challenges.Challenge;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.Map;

/**
 * @author yeahbennou
 * LabyrinthEvent.java
 * The overhauling class responsible for all event-related commands and variables.
 * For event-related classes that can be created and deleted (such as teams and challenges) and stored in files, do NOT store instances of such classes outside of the LabyrinthEvent class.
 * Such classes should be stored via IDs and accessed with the Maps to prevent potential errors that may arise from deletion of these classes.
 */
public class LabyrinthEvent
{
    private final static long DEFAULT_EVENT_DURATION = Helper.minutesToMillis(20); // default 20 minutes
    private final static int DEFAULT_POINT_BONUS = 5; // default 5 points per question

    // Manual Grading
    private String adminChannel;
    private int manualGradeID;
    private Map<Integer, ChallengeSubmissionEvent> pendingManualGrades;
    // Labyrinth Structure
    private String root;
    private Map<String, Challenge> challenges;
    // Labyrinth Descriptors
    private String mainChallenge; // The main challenge
    private String imageURL;
    // Event-related variables
    private Map<String, EventTeam> teams;
    private Map<String, String> userToTeam;
    private long beginTime;
    private long eventDuration;
    private int challengePointBonus;

    /*
    TMP:
    Variables that still need to be saved:
    adminChannel (!setAdminChannel)
    beginTime (!start)
    eventDuration (!setEventDuration)
    challengePointBonus (!setChallengePointBonus)
    labyrinthMainChallenge (!setMainChallenge)
    labyrinthImageURL (!setImageURL)

    teams (duh, do that later)
     */

    /**
     * Default Constructor
     */
    public LabyrinthEvent()
    {
        challenges = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        teams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        pendingManualGrades = new TreeMap<>();
        beginTime = Long.MAX_VALUE; // Default begin time
        eventDuration = DEFAULT_EVENT_DURATION;
        challengePointBonus = DEFAULT_POINT_BONUS;
    }

    public void setTeam(String teamID, EventTeam team)
    {
        EventTeam oldTeam = teams.get(teamID);
        for(String user: oldTeam.teamMemberIDs)
            userToTeam.put(user, team == null? null: team.NAME);
        teams.put(teamID, team);
    }

    public EventTeam getTeam(String teamID)
    {
        return teams.get(teamID);
    }

    public EventTeam getTeamFromUser(String userID)
    {
        return teams.get(userToTeam.get(userID));
    }

    public boolean hasStarted()
    {
        return beginTime != Long.MAX_VALUE;
    }

    /**
     * Returns a challenge based on the unique challenge ID.
     * @param ID the ID
     * @return the challenge
     */
    public Challenge getChallenge(String ID)
    {
        return challenges.get(ID);
    }

    /**
     * Sets or modifies an existing challenge based on its ID.
     * @param ID the ID
     * @param challenge the challenge
     */
    public void setChallenge(String ID, Challenge challenge)
    {
        challenges.put(ID, challenge);
    }

    /**
     * Getter for the entire list/map of challenges.
     * @return the map of challenges
     */
    public Map<String, Challenge> getChallengeList()
    {
        return challenges;
    }

    /**
     * Getter for the root challenge (first challenge you must complete)
     * @return the root
     */
    public String getRoot()
    {
        return root;
    }

    /**
     * Setter for the root challenge (first challenge you must complete)
     * @param root the root
     */
    public void setRoot(String root)
    {
        this.root = root;
    }

    public void setAdminChannel(TextChannel adminChannel)
    {
        this.adminChannel = adminChannel.getId();
    }

    /**
     * Getter for the admin channel, where administrative announcements take place.
     * @param event the event containing the guild
     * @return the admin channel
     */
    public TextChannel getAdminChannel(GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getGuild().getTextChannelById(adminChannel);
        if(channel == null) throw new NullPointerException("Admin channel not set up");
        return channel;
    }

    /**
     * Adds a pending manual grade event into the queue.
     * @param submissionEvent the event
     * @return the manual grading ID assigned to the event
     */
    public int queue(ChallengeSubmissionEvent submissionEvent)
    {
        for(Map.Entry<Integer, ChallengeSubmissionEvent> submissionEventEntry: pendingManualGrades.entrySet())
        {
            ChallengeSubmissionEvent entry = submissionEventEntry.getValue();
            if(entry.getTeamID().equals(submissionEvent.getTeamID()) && entry.getChallengeID().equals(submissionEvent.getChallengeID()))
                throw new IllegalArgumentException("Already being graded.");
        }
        int ID = manualGradeID++;
        pendingManualGrades.put(ID, submissionEvent);
        return ID;
    }

    /**
     * Returns a challenge that needs to be graded.
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

    public Map<String, EventTeam> getTeams()
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
