package mcpt.learning.event;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface TeamEvent extends MCPTEvent
{
    void addTeam(String ID, String[] members);
    Team getTeam(String ID);
    Team getTeamFromUser(String userID);
}
