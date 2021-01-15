package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.LabyrinthTeam;
import mcpt.learning.event.challenges.Challenge;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PracticeCommand extends CommandListener
{
    public PracticeCommand()
    {
        super("Practice", "practice (no arguments)");
    }
    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Practice");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);

        if(labyrinthEvent.hasStarted())
        {
            embed.setDescription("ERROR: The main event has started already!");
            channel.sendMessage(embed.build()).queue();
            return;
        }

        if(labyrinthEvent.getCurrent() != null)
        {
            embed.setDescription("ERROR: A practice session is already in place.");
            channel.sendMessage(embed.build()).queue();
            return;
        }
        ArrayList<Challenge> challenges = labyrinthEvent.getPractice();
        Challenge randomChallenge = challenges.get((int) (Math.random() * challenges.size()));
        labyrinthEvent.setCurrent(randomChallenge);
        labyrinthEvent.setPracticeStartTime(System.currentTimeMillis());

        embed.setDescription("**QUESTION: **" + randomChallenge.getDescription()
            .getValue() + "\n\nSubmit to this challenge using: " + Helper.getPrefix(
            event) + "submit [answer]" + "\n\n" + randomChallenge.getPrompt().createPrompt());

        EmbedBuilder timeOver = new EmbedBuilder();
        timeOver.setTitle("Time is over!");
        timeOver.setDescription("You can check the current practice standings with !leaderboard.");

        channel.sendMessage(embed.build()).queue();
        channel.sendMessage(timeOver.build()).queueAfter(2, TimeUnit.MINUTES, message -> {
            labyrinthEvent.setCurrent(null);
            labyrinthEvent.setSolveCount(0);
            labyrinthEvent.setUserAttempted(new HashMap<>());
        });
    }
}
