package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class CreateChallengeCommand extends CommandListener
{
    public CreateChallengeCommand()
    {
        super("CreateChallenge",
              "createchallenge [challengeName] [challengeType | MULTIPLE_CHOICE, MANUAL_GRADE]");
    }

    @Override
    public boolean hasPermissions(GuildMessageReceivedEvent event)
    {
        return Helper.isExec(event);
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);

        String[] tokens = args.split(" ");
        String challengeName = tokens[0];
        String challengeType = tokens[1];

        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Challenge " + challengeName);
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        if(labyrinthEvent.getChallenge(challengeName) != null)
        {
            embed.setDescription(
                "ERROR: This challenge already exists under the current event." + "\nTo remove this challenge, use the !removeChallenge command." + "\nTO modify this challenge, use the !initChallenge command.");
        }
        else
        {
            try
            {
                labyrinthEvent.addChallenge(challengeName, challengeType);
                embed.setDescription("Successfully added challenge " + challengeName + " of type " + tokens[1] + "!");
            }
            catch(Exception e)
            {
                embed.setDescription("Unsupported challenge type: " + challengeType);
            }
        }
        channel.sendMessage(embed.build()).queue();
    }
}
