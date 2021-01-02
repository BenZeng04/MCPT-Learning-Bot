package mcpt.learning.event;

import mcpt.learning.event.challenges.Challenge;
import mcpt.learning.event.challenges.ChallengeFactory;
import mcpt.learning.event.challenges.ChallengeType;

import java.util.*;

public final class Labyrinth
{
    public final HashMap<String, Challenge> CHALLENGES;
    private LabyrinthNode root;

    // The ID is a property of the challenges, the labyrinth nodes only dictate the PATH of challenges.

    public Labyrinth()
    {
        CHALLENGES = new HashMap<>();
    }

    public LabyrinthNode getRoot()
    {
        return root;
    }

    public void setRoot(LabyrinthNode root)
    {
        this.root = root;
    }
}
