package mcpt.learning.event;

import mcpt.learning.event.challenges.Challenge;

import java.util.ArrayList;

public class LabyrinthNode
{
    public final int ID;
    public final String NODE_NAME;
    public final Challenge CHALLENGE;
    public final ArrayList<LabyrinthNode> UNLOCKS;
    public LabyrinthNode(int ID, String NODE_NAME, Challenge challenge)
    {
        this.ID = ID;
        this.NODE_NAME = NODE_NAME;
        CHALLENGE = challenge;
        UNLOCKS = new ArrayList<>();
    }
}
