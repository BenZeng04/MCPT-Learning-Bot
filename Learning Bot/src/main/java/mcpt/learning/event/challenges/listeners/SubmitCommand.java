package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.ChallengeSubmissionEvent;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.LabyrinthTeam;
import mcpt.learning.event.challenges.Challenge;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class SubmitCommand extends CommandListener
{
    public SubmitCommand()
    {
        super("Submit", "submit [challenge] [answer]");
    }

    // The submit command is reliant on the grader* to properly judge the answer.
    // *automatic or manual
    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);
        LabyrinthTeam team = labyrinthEvent.getTeamFromUser(event.getMember().getId());

        String[] tokens = args.split(" ");
        String challengeName = tokens[0];
        Challenge challenge = labyrinthEvent.getChallenge(challengeName);

        if(!Helper.isExec(event))
        {
            // Default errors for non-admins
            if(team == null)
            {
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | Submit");
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.setDescription("ERROR: You're not in a team!");
                channel.sendMessage(embed.build()).queue();
                return;
            }
            if(!team.unlocked(challenge) || team.submissionTimeOver() || !labyrinthEvent.hasStarted() || team.attempted(
                challenge))
            {
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | Submit");
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.setDescription(
                    "ERROR: Your submission could not be delivered because your window to submit to the event hasn't started or is over, you have not unlocked this challenge yet, or you have already submitted to this challenge.");
                channel.sendMessage(embed.build()).queue();
                return;
            }
        }

        try
        {
            if(tokens.length == 1)
                throw new IllegalArgumentException(); // Caught immediately - this is to prevent people from doing "!submit [challengeName]" with no arguments and receiving a wrong answer on string-comparison based graders.
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < tokens.length; i++)
            {
                sb.append(tokens[i]);
                if(i != tokens.length - 1)
                    sb.append(' ');
            }
            String answer = sb.toString();
            String teamName = team == null? null: team.NAME;
            ChallengeSubmissionEvent submissionEvent = new ChallengeSubmissionEvent(challengeName, teamName, event);

            if(challenge.getGrader() != null)
            {
                boolean correct = challenge.getGrader().grade(answer.trim());
                if(correct)
                    challengeSuccessEvent(submissionEvent); // Correct
                else
                    challengeFailureEvent(submissionEvent); // Incorrect
            }
            else
            {
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | Challenge " + challengeName);
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.setDescription("Challenge submitted! Please wait for a human to grade this challenge.");

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

                int manualGradeID;
                try
                {
                    manualGradeID = labyrinthEvent.queue(submissionEvent); // Manual Grade Pending
                }
                catch(Exception e)
                {
                    embed.setDescription("ERROR: Your submission to this challenge is still being graded.");
                    channel.sendMessage(embed.build()).queue();
                    return;
                }

                EmbedBuilder adminEmbed = new EmbedBuilder();
                adminEmbed.setTitle("MCPT Learning Bot | Submission " + manualGradeID);
                adminEmbed.setDescription("**Submission Link:** " + event.getMessage()
                    .getJumpUrl() + "\n\nGrade this challenge using !grade " + manualGradeID);
                adminChannel.sendMessage(adminEmbed.build()).queue();
                channel.sendMessage(embed.build()).queue();
            }
        }
        catch(Exception e)
        {
            TextChannel channel = event.getChannel();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("MCPT Learning Bot | Challenge " + challengeName);
            embed.setColor(new Color(0x3B6EFF));
            embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
            embed.setDescription(
                "ERROR IN GRADING: Invalid Challenge Name or Syntax! Please try submitting again.\nIf this error persists, please contact an administrator for more information.");
            channel.sendMessage(embed.build()).queue();
            e.printStackTrace();
        }
    }

    // TODO: Improve / merge the fail and success methods
    protected static void challengeFailureEvent(ChallengeSubmissionEvent submissionEvent)
    {
        TextChannel channel = submissionEvent.getParentEvent().getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Incorrect Response | Challenge " + submissionEvent.getChallengeID());
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(submissionEvent.getParentEvent());
        Challenge challenge = labyrinthEvent.getChallenge(submissionEvent.getChallengeID());

        if(challenge.getTimeReward().getValue() == null)
        {
            embed.setDescription(
                "ERROR: This challenge's rewards may have not been fully initialized.\nPlease contact an administrator for more information.");
            channel.sendMessage(embed.build()).queue();
            return;
        }

        Set<String> unlockedChallenges = labyrinthEvent.getUnlocks(challenge.ID);

        StringBuilder completionPrompt = new StringBuilder("Incorrect Response D:\nGood luck next time...");

        if(unlockedChallenges.size() != 0)
        {
            completionPrompt.append("\n**NEW Unlocked Challenges:**");
            int i = 0;
            for(String unlock: unlockedChallenges)
            {
                completionPrompt.append(unlock);
                if(i != unlockedChallenges.size() - 1)
                    completionPrompt.append(",");
                i++;
            }
            completionPrompt.append(
                "\nTo view all of your challenges you can submit to, use the !unlockedChallenges command.");
        }

        LabyrinthTeam team = labyrinthEvent.getTeam(submissionEvent.getTeamID());
        if(team != null)
            team.onChallengeFailure(challenge);

        embed.setDescription(completionPrompt.toString());
        channel.sendMessage(embed.build()).queue();
    }

    protected static void challengeSuccessEvent(ChallengeSubmissionEvent submissionEvent)
    {
        TextChannel channel = submissionEvent.getParentEvent().getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Correct Response | Challenge " + submissionEvent.getChallengeID());
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(submissionEvent.getParentEvent());
        Challenge challenge = labyrinthEvent.getChallenge(submissionEvent.getChallengeID());

        if(challenge.getTimeReward().getValue() == null)
        {
            embed.setDescription(
                "ERROR: This challenge's rewards may have not been fully initialized.\nPlease contact an administrator for more information.");
            channel.sendMessage(embed.build()).queue();
            return;
        }

        int timeReward = challenge.getTimeReward().getValue();
        int pointReward = labyrinthEvent.getChallengePointBonus();
        int firstCompletionBonus = labyrinthEvent.getFirstCompletionBonus();
        ArrayList<String> URLBonusRewards = challenge.getBonusRewards().getValues();
        Set<String> unlockedChallenges = labyrinthEvent.getUnlocks(challenge.ID);

        StringBuilder completionPrompt = new StringBuilder(
            ":thumbsup: Successfully completed challenge " + challenge.ID + ".\nYou gain **" + pointReward + " points** and **" + timeReward + " bonus minutes of time** for completing this challenge.");

        LabyrinthTeam team = labyrinthEvent.getTeam(submissionEvent.getTeamID());

        boolean firstToComplete = labyrinthEvent.firstCompletion(challenge);

        if(team != null && firstToComplete)
            completionPrompt.append(":trophy: **You were the first team to complete challenge " + challenge.ID + "!\nYou gain " + firstCompletionBonus + " bonus points.");

        if(unlockedChallenges.size() != 0)
        {
            completionPrompt.append("\n**NEW Unlocked Challenges: **");
            int i = 0;
            for(String unlock: unlockedChallenges)
            {
                completionPrompt.append(unlock);
                if(i != unlockedChallenges.size() - 1)
                    completionPrompt.append(",");
                i++;
            }
            completionPrompt.append(
                "\nTo view all of your challenges you can submit to, use the !unlockedChallenges command.");
        }

        if(team != null)
        {
            team.onChallengeSuccess(challenge);

            if(firstToComplete)
            {
                for(TextChannel textChannel: labyrinthEvent
                        .getEventChannels(submissionEvent.getParentEvent()))
                {
                    if(textChannel.getId().equalsIgnoreCase(team.NAME)) // text channel name will always equal team name
                    {
                        EmbedBuilder message = new EmbedBuilder();
                        message.setTitle(team.NAME + " was the first team to complete challenge " + challenge.ID + "!");
                        message.setColor(new Color(0x3B6EFF));
                        textChannel.sendMessage(message.build()).queue();
                    }
                }
            }
        }

        embed.setDescription(completionPrompt.toString());
        channel.sendMessage(embed.build()).queue();

        if(URLBonusRewards != null)
        {
            for(int i = 0; i < URLBonusRewards.size(); i++)
            {
                String URL = URLBonusRewards.get(i);
                EmbedBuilder bonusEmbed = new EmbedBuilder();
                bonusEmbed.setTitle("**BONUS REWARD #" + (i + 1) + "**");
                bonusEmbed.setColor(new Color(0x3B6EFF));
                try
                {
                    bonusEmbed.setImage(URL);
                }
                catch(Exception e)
                {
                    bonusEmbed.setDescription("ERROR: Invalid reward URL!");
                }
                channel.sendMessage(bonusEmbed.build()).queue();
            }
        }
    }
}
