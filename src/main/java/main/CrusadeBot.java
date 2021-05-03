package main;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.Date;
import java.util.List;

public class CrusadeBot
{
    public static Config config;
    public static Date timeStarted = new Date(System.currentTimeMillis());
    public static JDA jda;
    public static EventWaiter waiter;

    public static void main(String[] args) throws LoginException
    {
        config = new Config();

        //Setting up EventWaiter
        waiter = new EventWaiter();

        //setting up CommandClientBuilder
        CommandClientBuilder client = new CommandClientBuilder();

            client.setOwnerId(CrusadeBot.config.get(Config.Field.TRODAIREID));
            client.setPrefix(CrusadeBot.config.get(Config.Field.PREFIX));
            client.setStatus(OnlineStatus.ONLINE);
            client.setActivity(Activity.playing("Competitive Toaster Bath"));

            //adding commands
            client.addCommands
                    (
                            new PurgeCommand(),
                            new SetupCommand(),
                            //new TestCommand(),
                            new HeadCount(),
                            new Poll(),
                            new PingCommand(),
                            new GhostPingCommand(),
                            new QueueCommand(),
                            new QueuePositionCommand(),
                            new ChonomoCommand(),
                            new TrodaireCommand(),
                            new MuteCommand(),
                            new UnmuteCommand()
                    );

        //setting up JDABuilder
        JDABuilder builder = JDABuilder.createDefault(config.get(Config.Field.ID),
                GatewayIntent.GUILD_BANS,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES);

            builder.addEventListeners(new Listeners(), waiter, client.build());
            builder.setMemberCachePolicy(MemberCachePolicy.ALL);

            jda = builder.build();
    }

}
