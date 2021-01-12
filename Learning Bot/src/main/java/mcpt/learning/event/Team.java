package mcpt.learning.event;

public class Team
{
    public final String NAME;
    public final String[] teamMemberIDs;
    // TODO: Add generic wrapper information for class Team.
    public Team(String name, String[] teamMemberIDs)
    {
        NAME = name;
        this.teamMemberIDs = teamMemberIDs;
    }
}
