package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.Team;
import mcpt.learning.event.TeamEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class RemoveTeamCommand extends CommandListener
{
    public RemoveTeamCommand()
    {
        super("RemoveTeam", "removeTeam [teamID]");
    }

    @Override
    public boolean hasPermissions(GuildMessageReceivedEvent event)
    {
        return Helper.isExec(event);
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        TeamEvent teamEvent = (TeamEvent) Helper.getMCPTEvent(event);
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | RemoveTeam");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        Team team = teamEvent.getTeam(args.trim());
        if(team == null) embed.setDescription("ERROR: No such team exists.");
        else
        {
            embed.setDescription("Successfully removed team " + team.NAME + ".");
            teamEvent.removeTeam(args.trim());
        }
        channel.sendMessage(embed.build()).queue();
    }
}
