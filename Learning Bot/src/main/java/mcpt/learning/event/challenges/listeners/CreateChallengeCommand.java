package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.challenges.Challenge;
import mcpt.learning.event.challenges.ChallengeFactory;
import mcpt.learning.event.challenges.ChallengeType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;

public class CreateChallengeCommand extends CommandListener
{
    private static HashMap<String, ChallengeType> enumConversion = new HashMap<String, ChallengeType>()
    {{
       put("MULTIPLE_CHOICE", ChallengeType.MULTIPLE_CHOICE);
       put("SHORT_ANSWER", ChallengeType.SHORT_ANSWER);
       put("TRUE_FALSE", ChallengeType.TRUE_FALSE);
       put("MANUAL_GRADE", ChallengeType.MANUAL_GRADE);
    }};
    public CreateChallengeCommand()
    {
        super("CreateChallenge", "createchallenge [challengeName] [challengeType | MULTIPLE_CHOICE, SHORT_ANSWER, TRUE_FALSE, MANUAL_GRADE]");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        String[] tokens = args.split(" ");
        String challengeName = tokens[0].toUpperCase();
        ChallengeType challengeType = enumConversion.get(tokens[1].toUpperCase());
        if(challengeType == null) throw new NullPointerException();

        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Challenge " + challengeName);
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        if(LabyrinthEvent.labyrinth.CHALLENGES.get(challengeName) != null)
        {
            embed.setDescription("ERROR: This challenge already exists under the current event." +
                "\nTo remove this challenge, use the !removeChallenge command." +
                "\nTO modify this challenge, use the !initChallenge command.");
        }
        else
        {
            Challenge challenge = ChallengeFactory.createChallenge(challengeName, challengeType);
            LabyrinthEvent.labyrinth.CHALLENGES.put(challengeName, challenge);
            embed.setDescription("Successfully added challenge " + challengeName + " of type " + tokens[1].toUpperCase() + "!");
        }
        channel.sendMessage(embed.build()).queue();
    }
}
