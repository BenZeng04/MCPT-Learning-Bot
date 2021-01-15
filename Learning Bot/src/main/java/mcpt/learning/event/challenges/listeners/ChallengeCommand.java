package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.LabyrinthTeam;
import mcpt.learning.event.challenges.Challenge;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class ChallengeCommand extends CommandListener
{

    public ChallengeCommand()
    {
        super("challenge", "challenge [challengeName]");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);
        LabyrinthTeam team = labyrinthEvent.getTeamFromUser(event.getMember().getId());
        Challenge challenge = labyrinthEvent.getChallenge(args);
        if(challenge == null)
            throw new IllegalArgumentException();

        if(!Helper.isExec(event))
        {
            // Default errors for non-admins
            if(team == null)
            {
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | Challenge");
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.setDescription("ERROR: You're not in a team!");
                channel.sendMessage(embed.build()).queue();
                return;
            }
            if(!labyrinthEvent.hasStarted())
            {
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | Challenge");
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.setDescription("ERROR: The event hasn't started yet!");
                channel.sendMessage(embed.build()).queue();
                return;
            }
            if(!team.unlocked(challenge) && !team.submissionTimeOver())
            {
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | Challenge");
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.setDescription("ERROR: You have not unlocked this challenge yet!");
                channel.sendMessage(embed.build()).queue();
                return;
            }
        }

        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Challenge " + challenge.ID);

        try
        {
            if(challenge.getDescription().getValue() == null)
                throw new IllegalArgumentException();
            embed.setDescription("**QUESTION: **" + challenge.getDescription()
                .getValue() + "\n\nSubmit to this challenge using: " + Helper.getPrefix(
                event) + challenge.getSubmissionFormat() + "\n\n" + challenge.getPrompt().createPrompt());
        }
        catch(Exception e)
        {
            embed.setDescription(
                "ERROR: This challenge may have not been fully initialized.\nPlease contact an administrator for more information.");
        }

        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        if(challenge.getImageURL().getValue() != null)
        {
            try
            {
                embed.setImage(challenge.getImageURL().getValue());
            }
            catch(Exception e)
            {
                embed.setDescription("ERROR: Invalid Image URL!");
            }
        }
        channel.sendMessage(embed.build()).queue();
    }
}
