package mcpt.learning.core;

import mcpt.learning.event.challenges.listeners.*;
import mcpt.learning.event.listeners.CreateTeamCommand;
import mcpt.learning.event.listeners.SetAdminChannelCommand;
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
    Add the !startEvent command! (This is responsible for a decent amount of stuff)

    Other stuff? (help command lol)
    Optional: Disallow modification of challenges during an event running (not like that would happen anyways but just to be safe...)
    Another note: NOTHING (but the JDA) related to the discord bot should be stored in global variables. EVER... Store IDS and IDS only. This is to ensure compatibility when saving to files.

    TODO: if there is time, improve the error handling code organization (better errortrapping and actually having throw clauses on methods!)
    Also have more generic classes...
    -> MCPTEvent (A superclass for LabyrinthEvent)
    -> LabyrinthCommandListener (all labyrinth related commands, checks if the event the user is in is an instance of LabyrinthEvent)
     */
    public static void main(String[] args) throws LoginException
    {
        Helper.jda = JDABuilder.createDefault("NzkwNzkxMjQ4NzEzNjEzMzIy.X-FvvQ.w5DOwIQW7sep71eEc2IUqi_1iEc")
            .setChunkingFilter(ChunkingFilter.ALL)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .setActivity(Activity.playing("Prefix: " + Helper.DEFAULT_PREFIX))
            .build();
        Helper.jda.addEventListener(new HelpCommand(),
            new ChallengeCommand(), new InitChallengeCommand(), new CreateChallengeCommand(),
            new RemoveChallengeCommand(), new SubmitCommand(),
            new SetAdminChannelCommand(), new GradeCommand(),
            new CreateTeamCommand());
    }
}
