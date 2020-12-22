import events.CommandListener;
import events.HelpCommand;
import events.MessageCommand;
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
            .setActivity(Activity.playing("orz"))
            .build();
        jda.addEventListener(new HelpCommand());
        jda.addEventListener(new MessageCommand());
    }
}
