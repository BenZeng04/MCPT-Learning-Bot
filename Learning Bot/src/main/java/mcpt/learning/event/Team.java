package mcpt.learning.event;

public abstract class Team
{
    public final String NAME;
    public final String[] teamMemberIDs;
    // TODO: Add generic wrapper information for class Team.
    public Team(String name, String[] teamMemberIDs)
    {
        NAME = name;
        this.teamMemberIDs = teamMemberIDs;
    }

    /**
     * Creates a string describing information about the given team outside of listing names and members.
     *
     * @return a description
     */
    public abstract String buildTeamDescription();

    public abstract int getPoints();
}
