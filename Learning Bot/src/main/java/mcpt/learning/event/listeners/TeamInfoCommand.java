package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.Team;
import mcpt.learning.event.TeamEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class TeamInfoCommand extends CommandListener
{
    public TeamInfoCommand()
    {
        super("TeamInfo", "teamInfo [Team ID (leave blank to view your own team]");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        TeamEvent teamEvent = (TeamEvent) Helper.getMCPTEvent(event);
        Team userTeam = teamEvent.getTeamFromUser(event.getMember().getId());

        if(args.trim().length() != 0)
        {
            if(!Helper.isExec(event) && (userTeam == null || !userTeam.NAME.equalsIgnoreCase(args)))
            {
                TextChannel channel = event.getChannel();
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("MCPT Learning Bot | TeamInfo");
                embed.setColor(new Color(0x3B6EFF));
                embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
                embed.setDescription("ERROR: You do not have permission to view this team.");
                channel.sendMessage(embed.build()).queue();
                return;
            }
            userTeam = teamEvent.getTeam(args.trim());
        }
        else if(userTeam == null)
        {
            TextChannel channel = event.getChannel();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("MCPT Learning Bot | TeamInfo");
            embed.setColor(new Color(0x3B6EFF));
            embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
            embed.setDescription("ERROR: You don't have a team!");
            channel.sendMessage(embed.build()).queue();
            return;
        }
        StringBuilder output = new StringBuilder();

        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Team " + userTeam.NAME);
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        output.append("**Team Members:** ");
        for(int i = 0; i < userTeam.teamMemberIDs.length; i++)
        {
            String memberID = userTeam.teamMemberIDs[i];
            String username = Helper.jda.getUserById(memberID).getName();
            output.append(username);
            if(i != userTeam.teamMemberIDs.length - 1)
                output.append(", ");
        }

        output.append("\n");
        output.append(userTeam.buildTeamDescription());
        embed.setDescription(output.toString());

        channel.sendMessage(embed.build()).queue();
    }
}
