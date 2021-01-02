package mcpt.learning.event;

import mcpt.learning.event.challenges.Challenge;

import java.util.ArrayList;

public class LabyrinthNode
{
    private String challengeID;
    private LabyrinthNode parent; // unlocked_by
    private ArrayList<LabyrinthNode> children; // unlocks

    public LabyrinthNode()
    {
        children = new ArrayList<>();
    }

    public LabyrinthNode getParent()
    {
        return parent;
    }

    public void setParent(LabyrinthNode parent)
    {
        this.parent = parent;
    }

    public ArrayList<LabyrinthNode> getChildren()
    {
        return children;
    }

    public void setChildren(ArrayList<LabyrinthNode> children)
    {
        this.children = children;
    }

    public String getChallengeID()
    {
        return challengeID;
    }

    public void setChallengeID(String challengeID)
    {
        this.challengeID = challengeID;
    }
}
