package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PurgeCommand extends Command
{
    public PurgeCommand() {
        this.name = "purge";
        this.aliases = new String[]{"purge"};
        this.help = "Purges x number of messages from channel command is entered in";
        this.arguments = "";
        this.guildOnly = true;
        this.requiredRole = CrusadeBot.config.get(Config.Field.OFFICERROLE);
    }

    @Override
    protected void execute(CommandEvent command) {
        int MessagesToDelete;

        try {
            MessagesToDelete = Integer.parseInt(command.getArgs());

            if (MessagesToDelete < 1 || MessagesToDelete > 100) {
                command.reply("Invalid number of messages to delete (Must input a number between 1 and 100)");

                return;
            }

        } catch (NumberFormatException e) {
            command.reply("Argument not recognized, use format `" + CrusadeBot.config.get(Config.Field.PREFIX) + "purge x`");

            return;
        }

        command.getMessage().delete().complete();

        List<Message> Messages = command.getChannel().getHistory().retrievePast(MessagesToDelete).complete();

        command.getChannel().purgeMessages(Messages);

        Message botReply = command.getChannel().sendMessage("Purge finished").complete();

        botReply.delete().completeAfter(1000, TimeUnit.MILLISECONDS);
    }
}
