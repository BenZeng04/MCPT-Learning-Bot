package mcpt.learning.event;

import mcpt.learning.core.Helper;
import mcpt.learning.event.challenges.Challenge;

import java.util.*;

/**
 * !createTeam [name] [members] (if members exist in other teams, that other team must be deleted)
 * Team names MUST BE ALPHANUMERIC.
 */
public class EventTeam
{
    public static final int FILE_PARSING_CONSTANT = 3; // Constant for the amount of lines an event will take up in a file (Excluding name and team members)
    public final String NAME;
    public final String[] teamMemberIDs;
    private LabyrinthEvent event;
    private Set<String> challengeAttempts;
    private Set<String> correctChallengeAttempts;
    private long bonusTimeRecieved;
    private int points;

    public EventTeam(String name, String[] teamMemberIDs)
    {
        NAME = name;
        this.teamMemberIDs = teamMemberIDs;
        challengeAttempts = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        correctChallengeAttempts = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    }

    public void setEvent(LabyrinthEvent event)
    {
        this.event = event;
    }

    public boolean canSubmit(Challenge challenge)
    {
        if(event.getBeginTime() == Long.MAX_VALUE) return false; // event hasn't started yet
        long timeSinceEvent = System.currentTimeMillis() - event.getBeginTime();
        long dueDate = bonusTimeRecieved + event.getEventDuration();
        return timeSinceEvent <= dueDate && !challengeAttempts.contains(challenge.ID); // already submitted
    }

    public ArrayList<String> unlockedChallenges()
    {
        return event.getUnlocks(challengeAttempts);
    }

    public void onChallengeSuccess(Challenge challenge)
    {
        bonusTimeRecieved += Helper.minutesToMillis(challenge.getTimeReward().getValue());
        points += event.getChallengePointBonus();
        challengeAttempts.add(challenge.ID);
        correctChallengeAttempts.add(challenge.ID);
    }

    public void onChallengeFailure(Challenge challenge)
    {
        challengeAttempts.add(challenge.ID);
    }

    public LabyrinthEvent getEvent()
    {
        return event;
    }

    public long getBonusTimeRecieved()
    {
        return bonusTimeRecieved;
    }

    public int getPoints()
    {
        return points;
    }

    /**
     * FORMAT:
     * [Team1Name]
     * [Team1UserID1] [Team1UserID2] [Team1UserID3] [Team1UserID4]… (Can have less than 4 users)
     * [Team1BonusTimeReceived] [Team1Points]
     * [Team1AttemptedChallenge1] [Team1AttemptedChallenge2]…
     * [Team1CorrectChallenge1] [Team1CorrectChallenge2]…
     * @return a formatted string containing all important team information necessary for file IO.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(NAME + "\n");
        for(int i = 0; i < teamMemberIDs.length; i++)
        {
            sb.append(teamMemberIDs[i]);
            if(i != teamMemberIDs.length - 1) sb.append(" ");
        }
        sb.append("\n");
        sb.append(bonusTimeRecieved + " " + points + "\n");
        int i = 0;
        for(String challenge: challengeAttempts)
        {
            sb.append(challenge);
            if(i != challengeAttempts.size() - 1) sb.append(" ");
            i++;
        }
        sb.append("\n");
        i = 0;
        for(String challenge: correctChallengeAttempts)
        {
            sb.append(challenge);
            if(i != correctChallengeAttempts.size() - 1) sb.append(" ");
            i++;
        }
        sb.append("\n");
        return sb.toString();
    }

    public void parseFromFile(String[] args)
    {
        // Make sure to update the constant variable if the file syntax is ever changed
        String[] data = args[0].split(" ");
        bonusTimeRecieved = Long.parseLong(data[0]);
        points = Integer.parseInt(data[1]);
        String[] challengeAttempts = args[1].split(" ");
        this.challengeAttempts.addAll(Arrays.asList(challengeAttempts));
        String[] correctChallengeAttempts = args[1].split(" ");
        this.correctChallengeAttempts.addAll(Arrays.asList(correctChallengeAttempts));
    }
}
