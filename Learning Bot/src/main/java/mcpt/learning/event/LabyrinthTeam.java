package mcpt.learning.event;

import mcpt.learning.core.Helper;
import mcpt.learning.event.challenges.Challenge;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * !createTeam [name] [members] (if members exist in other teams, that other team must be deleted)
 * Team names MUST BE ALPHANUMERIC.
 */
public class LabyrinthTeam extends Team
{
    public static final int FILE_PARSING_CONSTANT = 3; // Constant for the amount of lines an event will take up in a file (Excluding name and team members)

    private LabyrinthEvent event;
    private Set<String> challengeAttempts;
    private Set<String> correctChallengeAttempts;
    private long bonusTimeRecieved;
    private int points;

    public LabyrinthTeam(String teamName, String[] teamMembers)
    {
        super(teamName, teamMembers);
        challengeAttempts = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        correctChallengeAttempts = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    public String buildTeamDescription()
    {
        StringBuilder output = new StringBuilder();
        if(event.hasStarted())
        {
            output.append("**Team Points:** " + points + "\n");
            int minutesLeft = Math.max(0, Helper.millisToMinutes(
                event.getBeginTime() + bonusTimeRecieved + event.getEventDuration() - System.currentTimeMillis()));
            int secondsLeft = Math.max(0, Helper.millisToSeconds(
                event.getBeginTime() + bonusTimeRecieved + event.getEventDuration() - System.currentTimeMillis())) % 60;
            output.append(
                "**Time Left:** " + ((secondsLeft == 0 && minutesLeft == 0)? "Time is over!\n": (minutesLeft + " minutes and " + secondsLeft + " seconds to submit.\n")));
            output.append("**Uncompleted Unlocked Challenges:** ");
            Set<String> unlocks = event.getUnlocks(challengeAttempts);
            for(String completed: challengeAttempts)
                unlocks.remove(completed);
            if(unlocks.isEmpty())
                output.append("You've finished all the challenges!");

            int len = 0;
            for(String str: unlocks)
            {
                output.append(str);
                if(len != unlocks.size() - 1)
                    output.append(", ");
                len++;
            }

            output.append("\n**Completed Challenges** (Incorrect answers are bolded): ");
            if(challengeAttempts.isEmpty())
                output.append("You haven't attempted any challenges yet.");
            len = 0;
            for(String str: challengeAttempts)
            {
                if(!correctChallengeAttempts.contains(str))
                    output.append("**").append(str).append("**");
                else
                    output.append(str);
                if(len != challengeAttempts.size() - 1)
                    output.append(", ");
                len++;
            }
        }
        return output.toString();
    }

    public void setEvent(LabyrinthEvent event)
    {
        this.event = event;
    }

    public void resetTeamProgress()
    {
        bonusTimeRecieved = points = 0;
        challengeAttempts.clear();
        correctChallengeAttempts.clear();
    }

    public boolean submissionTimeOver()
    {
        long timeSinceEvent = System.currentTimeMillis() - event.getBeginTime();
        long dueDate = bonusTimeRecieved + event.getEventDuration();
        return timeSinceEvent > dueDate;
    }

    public boolean unlocked(Challenge challenge)
    {
        return challenge.getPrerequisite().getValue() == null || challengeAttempts.contains(
            challenge.getPrerequisite().getValue());
    }

    public boolean attempted(Challenge challenge)
    {
        return challengeAttempts.contains(challenge.ID);
    }

    public boolean correctlyAttempted(Challenge challenge)
    {
        return correctChallengeAttempts.contains(challenge.ID);
    }

    public Set<String> unlockedChallenges()
    {
        return event.getUnlocks(challengeAttempts);
    }

    public void onChallengeSuccess(Challenge challenge)
    {
        bonusTimeRecieved += Helper.minutesToMillis(challenge.getTimeReward().getValue());
        if(event.firstCompletion(challenge))
            points += event.getFirstCompletionBonus();
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
     *
     * @return a formatted string containing all important team information necessary for file IO.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(NAME).append("\n");
        for(int i = 0; i < teamMemberIDs.length; i++)
        {
            sb.append(teamMemberIDs[i]);
            if(i != teamMemberIDs.length - 1)
                sb.append(" ");
        }
        sb.append("\n");
        sb.append(bonusTimeRecieved).append(" ").append(points).append("\n");
        int i = 0;
        for(String challenge: challengeAttempts)
        {
            sb.append(challenge);
            if(i != challengeAttempts.size() - 1)
                sb.append(" ");
            i++;
        }
        sb.append("\n");
        i = 0;
        for(String challenge: correctChallengeAttempts)
        {
            sb.append(challenge);
            if(i != correctChallengeAttempts.size() - 1)
                sb.append(" ");
            i++;
        }
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
        String[] correctChallengeAttempts = args[2].split(" ");
        this.correctChallengeAttempts.addAll(Arrays.asList(correctChallengeAttempts));
    }
}
