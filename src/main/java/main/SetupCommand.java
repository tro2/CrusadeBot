package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SetupCommand extends Command {

    EventWaiter waiter;

    public SetupCommand()
    {
        this.name = "setup";
        this.aliases = new String[]{"setup"};
        this.hidden = true;
        this.waiter = CrusadeBot.waiter;
        this.guildOnly = true;
        this.arguments = "";
        this.requiredRole = CrusadeBot.config.get(Config.Field.OFFICERROLE);
    }

    @Override
    protected void execute(CommandEvent command)
    {
        command.reply("Setup initiated");

        List<Command> CommandList = command.getClient().getCommands();

        EmbedBuilder SetupMenuEmbed = new EmbedBuilder();

            SetupMenuEmbed.setTitle("Menu");
            SetupMenuEmbed.setDescription("Please enter a section");
            SetupMenuEmbed.setFooter("Type `cancel` to cancel setup");
            SetupMenuEmbed.addField("Prefix", "Enter: 0", false);
            SetupMenuEmbed.addField("Commands", "Enter: 1", false);
            SetupMenuEmbed.addField("Roles", "Enter: 2", false);
            SetupMenuEmbed.addField("Channels", "Enter: 3", false);

        command.getMessage().getChannel().sendMessage(SetupMenuEmbed.build()).queue();

        waiter.waitForEvent(MessageReceivedEvent.class, messageReceivedEvent -> messageReceivedEvent.getAuthor().equals(command.getAuthor()) && messageReceivedEvent.getChannel().equals(command.getChannel()), messageReceivedEvent -> {
            String Answer = messageReceivedEvent.getMessage().getContentRaw();

            if (Answer.equals("0")) {
                command.reply("Selected: 0");
            }
            else if (Answer.equals("1")) {
                command.reply("Selected: 1");
            }
            else if (Answer.equals("2")) {
                command.reply("Selected: 2");
            }
            else if (Answer.equals("3")) {
                command.reply("Selected: 3");
            }
            else if (Answer.equals("cancel")) {
                command.reply("Cancelled");

                closeSetup(command);
            }
            else {
                command.reply("Invalid response, cancelling...");
            }
        }, 10000, TimeUnit.MILLISECONDS, () -> {
            command.reply("Timeout, cancelling...");

            closeSetup(command);
        });
    }

    public void closeSetup(CommandEvent command)
    {
        command.reply("Setup finished");

        command.getChannel().purgeMessages(command.getChannel().getHistory().retrievePast(3).completeAfter(1000, TimeUnit.MILLISECONDS));
    }
}
