package mcpt.learning.event;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ChallengeSubmissionEvent
{
    private final String challengeID;
    private final String teamID;
    private final GuildMessageReceivedEvent parentEvent;

    public ChallengeSubmissionEvent(String challengeID, String teamID, GuildMessageReceivedEvent parentEvent)
    {
        this.challengeID = challengeID;
        this.teamID = teamID;
        this.parentEvent = parentEvent;
    }


    public String getChallengeID()
    {
        return challengeID;
    }

    public String getTeamID()
    {
        return teamID;
    }

    public GuildMessageReceivedEvent getParentEvent()
    {
        return parentEvent;
    }
}
