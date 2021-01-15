package mcpt.learning.event.listeners;

import mcpt.learning.core.CommandListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class SignUpCommand extends CommandListener
{
    public SignUpCommand()
    {
        super("SignUp", "signUp (no arguments)");
    }
    @Override
    public void onCommandRun(String args, GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | SignUp");
        embed.setColor(new Color(0x3B6EFF));
        embed.setThumbnail("https://avatars0.githubusercontent.com/u/18370622?s=200&v=4");
        // TODO: Link should be set via a command
        embed.setDescription("Sign up at https://forms.gle/Hycb1BqL7j4EAeFW7");
        channel.sendMessage(embed.build()).queue();
    }
}
