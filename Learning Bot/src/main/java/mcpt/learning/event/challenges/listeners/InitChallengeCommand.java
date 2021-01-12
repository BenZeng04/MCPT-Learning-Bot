package mcpt.learning.event.challenges.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.challenges.Challenge;
import mcpt.learning.event.challenges.interfaces.Parameter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class InitChallengeCommand extends CommandListener
{
    public InitChallengeCommand()
    {
        super("InitChallenge", "initchallenge [challengeName] [parameter] [args] OR initchallenge [challengeName] to view parameters.");
    }

    @Override
    public boolean hasPermissions(GuildMessageReceivedEvent event)
    {
        return event.getMember().getPermissions().contains(Permission.ADMINISTRATOR);
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);

        String[] tokens = args.split(" ");
        String challengeName = tokens[0];
        Challenge challenge = labyrinthEvent.getChallenge(challengeName);

        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Challenge " + challengeName);
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        if(tokens.length == 1) // No argument other than !initChallenge [challengeName] -> List all parameters
        {
            StringBuilder parameterList = new StringBuilder();
            Map<String, Parameter> challengeParameters = challenge.getParameters();
            int cnt = 0;
            for(Map.Entry<String, Parameter> entry: challengeParameters.entrySet())
            {
                cnt++;
                parameterList.append(entry.getValue().helpArgs());
                if(cnt != challengeParameters.entrySet().size()) parameterList.append("\n\n");
            }
            embed.setDescription("**SYNTAX:** !initChallenge " + challengeName + " [Parameters]\n\n__Parameters:__\n" + parameterList);
            channel.sendMessage(embed.build()).queue();
        }
        else // Inputting parameters
        {
            String parameterName = tokens[1];
            StringBuilder challengeArgs = new StringBuilder();
            for(int i = 2; i < tokens.length; i++)
            {
                challengeArgs.append(tokens[i]);
                if(i != tokens.length - 1)
                    challengeArgs.append(" ");
            }
            if(challenge.getParameter(parameterName) == null) throw new IllegalArgumentException("Invalid Parameter Name.");
            Parameter challengeParameter = challenge.getParameter(parameterName);

            try
            {
                challengeParameter.init(challengeArgs.toString());
                embed.setDescription("Successfully initialized parameter " + parameterName + " of challenge " + challengeName + " to " + challengeArgs
                    .toString() + ".");
            }
            catch(Exception e)
            {
                embed.setDescription("**Arguments: **" + "!initChallenge " + challengeName + " " + challengeParameter
                    .helpArgs());
            }
            channel.sendMessage(embed.build()).queue();
        }
    }
}
