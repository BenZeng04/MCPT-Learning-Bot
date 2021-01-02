package mcpt.learning.core;

import mcpt.learning.event.challenges.listeners.*;
import mcpt.learning.event.listeners.LabyrinthStructureCommand;
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
    /*
    TODO:
    Implement a saving system such that the bot can go offline and online again, while having information saved.
    Implement the other types of challenges (especially the manual grading system)
    Implement the actual event (time, unlocks, teams, points, etc)
    Other stuff?
     */
    public static void main(String[] args) throws LoginException
    {
        JDA jda = JDABuilder.createDefault("NzkwNzkxMjQ4NzEzNjEzMzIy.X-FvvQ.w5DOwIQW7sep71eEc2IUqi_1iEc")
            .setChunkingFilter(ChunkingFilter.ALL)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .setActivity(Activity.playing("Prefix: " + Helper.DEFAULT_PREFIX))
            .build();
        jda.addEventListener(new HelpCommand(),
            new MessageCommand(), new ChallengeCommand(),
            new InitChallengeCommand(), new CreateChallengeCommand(),
            new RemoveChallengeCommand(), new SubmitCommand(), new LabyrinthStructureCommand());
    }
}
