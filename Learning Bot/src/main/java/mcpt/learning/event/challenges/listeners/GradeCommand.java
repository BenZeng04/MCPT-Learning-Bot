package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.ChallengeSubmissionEvent;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.challenges.Challenge;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

// Manual Grading
public class GradeCommand extends CommandListener
{
    public GradeCommand()
    {
        super("Grade", "grade [id] [true / false / error (resubmit)]");
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
        int ID = Integer.parseInt(tokens[0]);

        ChallengeSubmissionEvent submissionEvent = labyrinthEvent.getPendingSubmission(ID);
        Challenge challenge = labyrinthEvent.getChallenge(submissionEvent.getChallengeID());

        if(challenge == null)
        {
            TextChannel channel = event.getChannel();
            EmbedBuilder embed = new EmbedBuilder();

            embed.setTitle("MCPT Learning Bot | Grade");
            embed.setColor(new Color(0x3B6EFF));
            embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
            embed.setDescription(
                "ERROR: The ID you have specified is either invalid or corresponds to a deleted challenge.");
            labyrinthEvent.finishManualGrade(
                ID); // Finishes the pending manual grade by default to resolve unresolvable errors
            channel.sendMessage(embed.build()).queue();
            return;
        }

        if(tokens[1].equalsIgnoreCase("error"))
        {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Invalid Response | Challenge " + challenge.ID);
            embed.setColor(new Color(0x3B6EFF));
            embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
            embed.setDescription("Invalid Response! Please resubmit to this challenge using !submit.");
            event.getChannel().sendMessage(embed.build()).queue();
        }
        else
        {
            boolean correct = Boolean.parseBoolean(tokens[1].toLowerCase());

            if(correct)
                SubmitCommand.challengeSuccessEvent(submissionEvent);
            else
                SubmitCommand.challengeFailureEvent(submissionEvent);
        }
        labyrinthEvent.finishManualGrade(ID); // Finishes the pending manual grade if there are no errors.
    }
}
