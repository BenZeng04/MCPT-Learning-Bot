package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.event.LabyrinthEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class RemoveChallengeCommand extends CommandListener
{
    public RemoveChallengeCommand()
    {
        super("RemoveChallenge", "removechallenge [challengeName]");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        String challengeName = args.toUpperCase();
        TextChannel channel = event.getChannel();
        LabyrinthEvent.labyrinth.CHALLENGES.put(challengeName, null);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Challenge " + challengeName);
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        embed.setDescription("Successfully removed challenge " + challengeName + ".");
        channel.sendMessage(embed.build()).queue();
    }
}
