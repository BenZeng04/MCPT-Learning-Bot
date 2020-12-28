package mcpt.learning.event.challenges;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class MultiChoiceChallenge extends Challenge
{
    private static class Choice
    {
        private String description;
        private boolean correct;

        private Choice(String description, boolean correct)
        {
            this.description = description;
            this.correct = correct;
        }
    }
    protected ArrayList<Choice> options;
    public MultiChoiceChallenge(String description, String id)
    {
        super(description, id);
        options = new ArrayList<>();
    }
    // !createChallenge [challengeType] [nameID] [description]
    // !createChallenge MultiChoice 1A Which of the following is the answer to this code snippet?
    // !initChallenge [nameID] [params]
    // !initChallenge 1A IMG [url]
    // !initChallenge 1A ADDCHOICE [correct] [text]
    // !initChallenge 1A ADDCHOICE true 4
    // !initChallenge 1A DELCHOICE [ID]
    // !respond 1A [answer]
    // Challenge Types: MultiChoice (only one so far...)
    @Override
    public void initChallenge(String param, GuildMessageReceivedEvent event)
    {
        super.initChallenge(param, event);
        String[] args = param.split(" ");
        switch(args[0].toUpperCase())
        {
            case "IMG":
                break; // Already Handled by super.initChallenge() TODO fix this.
            case "ADDCHOICE":
                boolean correct = Boolean.parseBoolean(args[1].toLowerCase());
                StringBuilder description = new StringBuilder();
                for(int i = 2; i < args.length; i++)
                {
                    description.append(args[i]);
                    if(i != args.length - 1) description.append(" ");
                }
                options.add(new Choice(description.toString(), correct));
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | InitChallenge");
                embed.setDescription("Successfully added option " + options.size() + " to challenge " + ID + ".");
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.addField("", "MCPT Learning Bot", false);
                channel.sendMessage(embed.build()).queue();
                break;
            default:
                throw new IllegalArgumentException("Invalid Arguments!");
        }
    }

    @Override
    public boolean correctAnswer(String ans)
    {
        int answerID = Integer.parseInt(ans);
        return options.get(answerID - 1).correct;
    }

    @Override
    public void userPrompt(GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Challenge " + ID);
        embed.setDescription("**MULTIPLE CHOICE.**\nRespond to this challenge using !respond " + ID + " [option number].");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        StringBuilder optionPrompt = new StringBuilder();
        for(int i = 0; i < options.size(); i++)
        {
            Choice choice = options.get(i);
            optionPrompt.append("**" + (i + 1) + ". **");
            optionPrompt.append(choice.description);
            if(i != options.size() - 1) optionPrompt.append("\n");
        }
        embed.addField(DESCRIPTION, optionPrompt.toString(), false);
        embed.addField("", "MCPT Learning Bot", false);
        if(getImageURL() != null) embed.setImage(getImageURL());
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public int timeReward()
    {
        return 100000;
    }

    @Override
    public String[] URLBonusRewards()
    {
        return new String[0];
    }
}
