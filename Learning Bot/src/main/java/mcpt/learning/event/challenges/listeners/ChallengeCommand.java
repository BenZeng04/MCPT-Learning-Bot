package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthTeam;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.challenges.Challenge;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
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
        Member user = event.getMember();

        LabyrinthTeam team = (LabyrinthTeam) labyrinthEvent.getTeamFromUser(event.getMember().getId());

        if(!user.getPermissions().contains(Permission.ADMINISTRATOR))
        {
            // Default errors for non-admins
            if(team == null)
            {
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | SubmitCommand");
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
                embed.setTitle("MCPT Learning Bot | SubmitCommand");
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.setDescription("ERROR: The event hasn't started yet!");
                channel.sendMessage(embed.build()).queue();
                return;
            }
        }

        String challengeName = args;
        Challenge challenge = labyrinthEvent.getChallenge(challengeName);
        if(challenge == null) throw new IllegalArgumentException();

        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Challenge " + challenge.ID);

        try
        {
            if(challenge.getDescription().getValue() == null) throw new IllegalArgumentException();
            embed.setDescription("**QUESTION: **" + challenge.getDescription().getValue() + "\n\nSubmit to this challenge using: " + Helper.getPrefix(event) + challenge.getSubmissionFormat() +
                "\n\n" + challenge.getPrompt().createPrompt());
        }
        catch(Exception e)
        {
            embed.setDescription("ERROR: This challenge may have not been fully initialized.\nPlease contact an administrator for more information.");
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
