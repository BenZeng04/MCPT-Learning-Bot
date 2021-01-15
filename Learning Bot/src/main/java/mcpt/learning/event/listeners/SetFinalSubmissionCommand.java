package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.LabyrinthTeam;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class SetFinalSubmissionCommand extends CommandListener
{
    public SetFinalSubmissionCommand()
    {
        super("SetFinalSubmission", "setFinalSubmission [Upload file or link!]");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);
        LabyrinthTeam team = labyrinthEvent.getTeamFromUser(event.getMember().getId());

        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | SetFinalSubmission");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        if(team == null)
        {
            embed.setDescription("ERROR: You're not in a team!");
            channel.sendMessage(embed.build()).queue();
            return;
        }
        if(team.submissionTimeOver() || !labyrinthEvent.hasStarted())
        {
            embed.setDescription(
                "ERROR: Your submission could not be delivered because the event hasn't started, or your time to submit is up.");
            channel.sendMessage(embed.build()).queue();
            return;
        }

        embed.setDescription("Final submission set!\n\nYou can always adjust your submission by calling this command again before your time is up.");

        TextChannel adminChannel;
        try
        {
            adminChannel = labyrinthEvent.getAdminChannel(event);
        }
        catch(Exception e)
        {
            embed.setDescription(
                "Admin channel not set up or incorrectly set up! Please contact an administrator.");
            channel.sendMessage(embed.build()).queue();
            return;
        }

        EmbedBuilder adminEmbed = new EmbedBuilder();
        adminEmbed.setTitle("MCPT Learning Bot | Final Submission from Team " + team.NAME);
        adminEmbed.setDescription("**Submission Link:** " + event.getMessage()
            .getJumpUrl());
        adminChannel.sendMessage(adminEmbed.build()).queue();
        channel.sendMessage(embed.build()).queue();
    }
}
