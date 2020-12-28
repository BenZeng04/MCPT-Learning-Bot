package mcpt.learning.event;

import java.util.*;

public final class Labyrinth
{
    public final String IMAGE_URL;
    public final String LABYRINTH_DESCRIPTION;
    public final HashMap<Integer, LabyrinthNode> NODES;
    public final LabyrinthNode START;

    public Labyrinth(String image_url, String labyrinth_description, HashMap<Integer, LabyrinthNode> nodes, LabyrinthNode start)
    {
        IMAGE_URL = image_url;
        LABYRINTH_DESCRIPTION = labyrinth_description;
        NODES = nodes;
        START = start;
    }
}
