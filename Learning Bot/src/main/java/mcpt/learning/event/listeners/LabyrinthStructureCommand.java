package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.event.LabyrinthNode;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/*
Literally just reading in a tree.

First Line: NodeCount
Second Line: Nodes (space-separated), root is assumed to be the first one.
Next NodeCount - 1 Lines: Edges

Example:
7
1 1A 2 2A 3 4 4A
0 1 1 2 3 3 6
 */
public class LabyrinthStructureCommand extends CommandListener
{
    public LabyrinthStructureCommand()
    {
        super("LabyrinthStructure", "labyrinthStructure [NodeCount]\n[Challenges associated with each node (space-separated)]\n[\"Parent\" node (one-indexed). Special index 0 for the root.].\n\n**Example:**\n!labyrinthStructure 7\n" + "1 1A 2 2A 3 4 4A\n" + "0 1 1 2 3 3 6\n\nIt is assumed that the first challenge listed is the \"root node\".");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        String[] lines = args.split("\n");
        int nodeCount = Integer.parseInt(lines[0].trim());
        String[] challengeNames = lines[1].trim().split(" ");
        String[] parentNodes = lines[2].trim().split(" ");
        LabyrinthNode[] nodes = new LabyrinthNode[nodeCount];
        for(int i = 0; i < nodes.length; i++)
        {
            nodes[i] = new LabyrinthNode();
            nodes[i].setChallengeID(challengeNames[i].trim().toUpperCase());
        }
        for(int i = 0; i < nodes.length; i++)
        {
            int idx = Integer.parseInt(parentNodes[i]) - 1;
            if(idx != -1)
            {
                nodes[i].setParent(nodes[idx]);
                nodes[idx].getChildren().add(nodes[i]);
            }
        }
    }
}
