package events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class MessageCommand extends CommandListener
{
    @Override
    public int minArguments()
    {
        return 2;
    }

    @Override
    public void onIncorrectArguments(GuildMessageReceivedEvent event)
    {
        TextChannel channel = event.getChannel();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("MCPT Learning Bot | Message");
        embed.setColor(new Color(0x3B6EFF));
        embed.addField("Arguments: !message [userID] [message]", "MCPT Learning Bot", true);
        channel.sendMessage(embed.build()).queue();
    }

    @Override
    public String commandName()
    {
        return "message";
    }

    @Override
    public void onCommandRun(String[] args, GuildMessageReceivedEvent event)
    {
        System.out.println(event.getGuild().getMembers());
        try
        {
            long userID = -1;
            try
            {
                userID = Long.parseLong(args[0]); // user ID
            }
            catch(NumberFormatException e)
            {
                boolean found = false;
                for(Member member: event.getGuild().getMembers())
                {
                    if(member.getUser().getAsTag().equals(args[0])) // user Tag
                    {
                        userID = member.getUser().getIdLong();
                        found = true;
                        break;
                    }
                }
                if(!found) throw new IllegalArgumentException();
            }
            TextChannel channel = event.getChannel();
            String message = "";
            for(int i = 1; i < args.length; i++)
            {
                message += args[i];
                if(i != args.length - 1) message += " ";
            }
            String finalMessage = message;

            event.getGuild().getMemberById(userID).getUser().openPrivateChannel().flatMap(c -> c.sendMessage(finalMessage)).queue();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("MCPT Learning Bot | Help");
            embed.setColor(new Color(0x3B6EFF));
            embed.addField("Successfully messaged " + event.getGuild().getMemberById(userID).getUser().getAsTag() +  ".", "MCPT Learning Bot", true);
            channel.sendMessage(embed.build()).queue();
        }
        catch(Exception e)
        {
            onIncorrectArguments(event);
        }
    }
}
