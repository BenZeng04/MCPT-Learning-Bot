package mcpt.learning.core;

import mcpt.learning.event.listeners.ChallengeCommand;
import mcpt.learning.event.listeners.InitChallengeCommand;
import mcpt.learning.listeners.HelpCommand;
import mcpt.learning.listeners.MessageCommand;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class Main
{
    public static void main(String[] args) throws LoginException
    {
        JDA jda = JDABuilder.createDefault("NzkwNzkxMjQ4NzEzNjEzMzIy.X-FvvQ.w5DOwIQW7sep71eEc2IUqi_1iEc")
            .setChunkingFilter(ChunkingFilter.ALL)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .setActivity(Activity.playing("Prefix: " + Helper.DEFAULT_PREFIX))
            .build();
        jda.addEventListener(new HelpCommand());
        jda.addEventListener(new MessageCommand());
        jda.addEventListener(new ChallengeCommand());
        jda.addEventListener(new InitChallengeCommand());
    }
}
