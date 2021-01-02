package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.challenges.Challenge;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

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
        String[] tokens = args.split(" ");
        String challengeName = tokens[0].toUpperCase();
        Challenge challenge = LabyrinthEvent.labyrinth.CHALLENGES.get(challengeName);
        try
        {
            if(tokens.length == 1) throw new IllegalArgumentException(); // Caught immediately - this is to prevent people from doing "!submit [challengeName]" with no arguments and receiving a wrong answer on string-comparison based graders.
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < tokens.length; i++)
            {
                sb.append(tokens[i]);
                if(i != tokens.length - 1) sb.append(' ');
            }
            String answer = sb.toString();
            if(challenge.getGrader() != null)
            {
                boolean correct = challenge.getGrader().grade(answer.trim());
                if(correct) challengeSuccessEvent(challenge, event);
                else challengeFailureEvent(challenge, event);
            }
            else
            {
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | Challenge " + challengeName);
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.setDescription("Challenge submitted! Please wait for a human to grade this challenge.");
                // TODO Add Manual Grading.
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
            embed.setDescription("ERROR IN GRADING: Invalid Challenge Name or Syntax! Please try submitting again.\nIf this error persists, please contact an administrator for more information.");
            channel.sendMessage(embed.build()).queue();
        }
    }

    protected static void challengeFailureEvent(Challenge challenge, GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Incorrect Response | Challenge " + challenge.ID);
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        if(challenge.getTimeReward().getValue() == null)
        {
            embed.setDescription("ERROR: This challenge's rewards may have not been fully initialized.\nPlease contact an administrator for more information.");
            channel.sendMessage(embed.build()).queue();
            return;
        }

        embed.setDescription("Incorrect Response D:\nGood luck next time...");
        channel.sendMessage(embed.build()).queue();
    }

    protected static void challengeSuccessEvent(Challenge challenge, GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Correct Response | Challenge " + challenge.ID);
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        if(challenge.getTimeReward().getValue() == null)
        {
            embed.setDescription("ERROR: This challenge's rewards may have not been fully initialized.\nPlease contact an administrator for more information.");
            channel.sendMessage(embed.build()).queue();
            return;
        }

        // TODO actually make the rewards DO SOMETHING instead of simply being a message.
        int timeReward = challenge.getTimeReward().getValue();
        ArrayList<String> URLBonusRewards = challenge.getBonusRewards().getValues();
        embed.setDescription(":thumbsup: Successfully completed challenge " + challenge.ID + ".\nYou gain **5 points** and **" + timeReward + " bonus minutes of time** for completing this challenge.");
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
