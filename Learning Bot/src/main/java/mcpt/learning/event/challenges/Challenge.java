package mcpt.learning.event.challenges;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public abstract class Challenge
{
    public final String DESCRIPTION;
    public final String ID;
    protected String imageURL;
    protected Challenge(String description, String id)
    {
        DESCRIPTION = description;
        ID = id;
    }

    /**
     * Run when the !initChallenge command is called, targeting this specific challenge. Responsible for initialization of the challenge variables and responding with an appropriate message.
     * @param param
     * @param event
     */
    public void initChallenge(String param, GuildMessageReceivedEvent event)
    {
        String[] args = param.split(" ");
        if(!args[0].equalsIgnoreCase("IMG")) return;
        imageURL = args[1];
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | InitChallenge");
        embed.setDescription("Successfully set the thumbnail image for this challenge.");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        embed.addField("", "MCPT Learning Bot", false);
        embed.setImage(imageURL);
        channel.sendMessage(embed.build()).queue();
    }
    public abstract boolean correctAnswer(String ans);

    /**
     * Prompts the user to respond to the multiple choice challenge. Called when the !challenge command is called, targetting this specific challege.
     * Example: !challenge 1A
     * @param event
     */
    public abstract void userPrompt(GuildMessageReceivedEvent event);
    public abstract int timeReward();
    public abstract String[] URLBonusRewards();
    public String getImageURL() { return imageURL; }
    public void setImageURL(String URL) { imageURL = URL; }
}
