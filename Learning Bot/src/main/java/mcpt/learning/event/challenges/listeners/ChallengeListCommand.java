package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.challenges.Challenge;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class ChallengeListCommand extends CommandListener
{
    public ChallengeListCommand()
    {
        super("ChallengeList", "challengeList");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);
        Map<String, Challenge> challengeList = labyrinthEvent.getChallengeList();

        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | ChallengeList");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        StringBuilder list = new StringBuilder();
        for(Map.Entry<String, Challenge> entry: challengeList.entrySet())
        {
            Challenge challenge = entry.getValue();
            list.append(
                "Challenge " + challenge.ID + " with prerequisite: " + challenge.getPrerequisite().getValue() + "\n");
        }
        if(challengeList.size() == 0)
            list.append("There are no challenges that you can currently submit to. Maybe try creating some?");
        embed.setDescription(list.toString());
        channel.sendMessage(embed.build()).queue();
    }
}
