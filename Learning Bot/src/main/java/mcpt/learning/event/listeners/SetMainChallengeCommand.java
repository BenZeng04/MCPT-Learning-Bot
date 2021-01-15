package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class SetMainChallengeCommand extends CommandListener
{
    public SetMainChallengeCommand()
    {
        super("SetMainChallenge", "setMainChallenge [description]");
    }

    @Override
    public boolean hasPermissions(GuildMessageReceivedEvent event)
    {
        return Helper.isExec(event);
    }

    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | SetMainChallenge");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);
        labyrinthEvent.setMainChallenge(args.trim());
        embed.setDescription("Successfully set the main challenge.");
        channel.sendMessage(embed.build()).queue();
    }
}
