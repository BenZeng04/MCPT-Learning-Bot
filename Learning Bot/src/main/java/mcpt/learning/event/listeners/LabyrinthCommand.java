package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import mcpt.learning.core.Helper;
import mcpt.learning.event.LabyrinthEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class LabyrinthCommand extends CommandListener
{
    public LabyrinthCommand()
    {
        super("Labyrinth", "labyrinth [no arguments]");
    }


    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        LabyrinthEvent labyrinthEvent = (LabyrinthEvent) Helper.getMCPTEvent(event);
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Labyrinth");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");

        try
        {
            if(labyrinthEvent.hasStarted() || Helper.isExec(event))
            {
                embed.setImage(labyrinthEvent.getImageURL());
                embed.setDescription(labyrinthEvent.getMainChallenge());
            }
            else embed.setDescription("The event hasn't started yet!");
        }
        catch(Exception e)
        {
            embed.setDescription("ERROR: Labyrinth may not have been initialized correctly. Please contact an administrator.");
        }
        channel.sendMessage(embed.build()).queue();
    }
}
