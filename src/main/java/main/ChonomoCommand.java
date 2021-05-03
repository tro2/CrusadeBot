package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

import java.io.File;
import java.util.Random;

public class ChonomoCommand extends Command {

    public ChonomoCommand() {
        this.name = "Chonomo";
        this.aliases = new String[] {"chonomo", "cho"};
        this.arguments = "(opt) messageid";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent command) {
        String[] choGifs = new String[] {"https://tenor.com/view/skill-issue-tuff-salty-gif-19967076",
                "https://tenor.com/view/damn-thats-crazy-bro-but-did-i-ask-gif-18687637",
                "https://tenor.com/view/dont-care-idgaf-gif-5730259"};

        String coconutMalled = "https://tenor.com/view/coconut-malled-gif-20280826";

        Random random = new Random();

        if (random.nextInt(20) == 19) {
            command.getMessage().reply(coconutMalled).queue();

            return;
        }

        if (command.getArgs() != "") {
            String[] args = command.getArgs().split(" ");

            try {
                Message message = command.getChannel().retrieveMessageById(args[0]).complete();

                if (message != null && args.length > 1) { //Gets if args[0] is a valid message id
                    switch (args[1]) {
                        case "1":
                            message.reply(choGifs[0]).mentionRepliedUser(false).queue();
                            return;
                        case "2":
                            message.reply(choGifs[1]).mentionRepliedUser(false).queue();
                            return;
                        case "3":
                            message.reply(choGifs[2]).mentionRepliedUser(false).queue();
                            return;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                command.reply("Invalid Message ID");

                return;
            }
        }

        File[] files = new File("Resources/Chonomo").listFiles();

        File file = files[random.nextInt(files.length)];

        command.reply(file, file.getName());
    }
}
