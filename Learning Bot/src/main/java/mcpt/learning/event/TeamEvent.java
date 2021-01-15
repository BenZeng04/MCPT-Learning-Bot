package mcpt.learning.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface TeamEvent extends MCPTEvent
{
    void addTeam(String ID, String[] members);

    void removeTeam(String ID);

    Team getTeam(String ID);

    Team getTeamFromUser(String userID);

    ArrayList<Team> getTeamList();
}
