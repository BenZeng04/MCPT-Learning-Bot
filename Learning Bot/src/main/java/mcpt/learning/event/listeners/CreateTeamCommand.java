package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthTeam;
import mcpt.learning.event.LabyrinthEvent;
import mcpt.learning.event.TeamEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class CreateTeamCommand extends CommandListener
{
    public CreateTeamCommand()
    {
        super("CreateTeam", "createTeam [teamName] [teamMember1] [teamMember2]...");
    }
    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        TeamEvent teamEvent = (TeamEvent) Helper.getMCPTEvent(event);
        String[] tokens = args.split(" ");
        String teamName = tokens[0];
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | SubmitCommand");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        if(!Helper.isAlphanumeric(teamName))
        {
            embed.setDescription("ERROR: Team name must be alphanumeric!");
            channel.sendMessage(embed.build()).queue();
            return;
        }
        if(tokens.length == 1)
        {
            embed.setDescription("ERROR: Team must have more than 0 members!");
            channel.sendMessage(embed.build()).queue();
            return;
        }
        String[] teamMembers = new String[tokens.length - 1];
        for(int i = 1; i < tokens.length; i++)
        {
            String memberID = tokens[i];
            if(memberID.charAt(0) == '<')
                memberID = memberID.substring(2, memberID.length() - 1);
            Member member = event.getGuild().getMemberById(memberID);
            if(member == null)
            {
                embed.setDescription("ERROR: Team members must correspond to a valid user mention or user ID!");
                channel.sendMessage(embed.build()).queue();
                return;
            }
            if(teamEvent.getTeamFromUser(memberID) != null)
            {
                embed.setDescription("ERROR: One of these users are already in a team!\nYou can view all existing teams and users in these teams using the !teamList command.");
                channel.sendMessage(embed.build()).queue();
                return;
            }
            teamMembers[i - 1] = memberID;
        }
        teamEvent.addTeam(teamName, teamMembers);
    }
}
