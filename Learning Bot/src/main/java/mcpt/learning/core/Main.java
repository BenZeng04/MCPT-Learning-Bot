package mcpt.learning.core;

import mcpt.learning.event.challenges.listeners.*;
import mcpt.learning.event.listeners.*;
import mcpt.learning.listeners.HelpCommand;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main
{
    /**
     * The main method.
     * TODO -> Help command, Practice Challenges
     * TODO (Post-event) -> Structure rework, Multiple event types, Better error-handling and file IO
     * @param args command line arguments
     * @throws LoginException thrown by JDABuilder
     * @throws IOException exception when reading bot token
     */
    public static void main(String[] args) throws LoginException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("token.txt"));
        Helper.jda = JDABuilder.createDefault(br.readLine()) // Modified the bot token, don't even try to look through commit logs.
            .setChunkingFilter(ChunkingFilter.ALL)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .setActivity(Activity.playing("Prefix: " + Helper.DEFAULT_PREFIX))
            .build();
        Helper.jda.addEventListener(new HelpCommand(), new ChallengeCommand(), new InitChallengeCommand(),
                                    new CreateChallengeCommand(), new RemoveChallengeCommand(), new SubmitCommand(),
                                    new SetAdminChannelCommand(), new GradeCommand(), new CreateTeamCommand(),
                                    new ChallengeListCommand(), new TeamInfoCommand(), new RemoveTeamCommand(),
                                    new SetMainChallengeCommand(), new SetLabyrinthImageCommand(), new LabyrinthCommand(),
                                    new SetFinalSubmissionCommand(), new StartEventCommand(), new PendingSubmissionListCommand(),
                                    new SignUpCommand(), new LeaderboardCommand(), new PracticeCommand());
    }
}
