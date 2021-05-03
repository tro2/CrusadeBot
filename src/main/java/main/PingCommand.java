package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class PingCommand extends Command {

    public PingCommand ()
    {
        this.name = "Ping";
        this.aliases =  new String[] {"ping"};
        this.help = "Pings the bot";
        this.arguments = "";
        this.requiredRole = CrusadeBot.config.get(Config.Field.VERIFIEDROLE);
    }

    @Override
    protected void execute(CommandEvent command)
    {
        Duration duration = Duration.between(main.CrusadeBot.timeStarted.toInstant(), new Date(System.currentTimeMillis()).toInstant());

        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();

        String uptime = (days == 0 ? "" : days + " day" + (days == 1 ? " " : "s "))
                + (hours == 0 ? "" : hours + " hour" + (hours == 1 ? " " : "s "))
                + (minutes == 0 ? "" : minutes + " minute" + (minutes == 1 ? " " : "s "))
                + seconds + " second" + (seconds == 1 ? "" : "s");

        command.getChannel().sendMessage("Ping: `" + command.getJDA().getGatewayPing() + " ms`\n" + "Uptime: `" + uptime + "`").queue();
    }

}
