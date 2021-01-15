package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.LabyrinthTeam;
import mcpt.learning.event.Team;
import mcpt.learning.event.TeamEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardCommand extends CommandListener
{
    public LeaderboardCommand()
    {
        super("Leaderboard", "leaderboard (no arguments)");
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Leaderboard");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        StringBuilder sb = new StringBuilder();

        TeamEvent teamEvent = (TeamEvent) Helper.getMCPTEvent(event);

        ArrayList<Team> teams = teamEvent.getTeamList();
        teams.sort(Comparator.comparingInt(Team::getPoints));
        Collections.reverse(teams);
        int placement = 0;
        for(Team team: teamEvent.getTeamList())
            sb.append(++placement)
                .append(". **")
                .append(team.NAME)
                .append(":** ")
                .append(team.getPoints())
                .append(" points\n");
        embed.setDescription(sb.toString());
        channel.sendMessage(embed.build()).queue();
    }
}
