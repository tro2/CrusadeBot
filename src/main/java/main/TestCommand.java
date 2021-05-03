package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.awt.*;

public class TestCommand extends Command {

    public TestCommand()
    {
        this.name = "test";
        this.aliases = new String[]{"test"};
        this.help = "Test command";
        this.hidden = true;
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent command)
    {
        String mapLink = "https://media.discordapp.net/attachments/656752015799484435/837020344707186688/unknown.png?width=324&height=331";

        Feedback testFeedback = new Feedback(
                CrusadeBot.config.get(Config.Field.GUILDID),
                Color.BLUE,
                CrusadeBot.config.get(Config.Field.TRODAIREID),
                CrusadeBot.config.get(Config.Field.TRODAIREID),
                CrusadeBot.config.get(Config.Field.TRODAIREID),
                "Void",
                mapLink,
                "",
                "Trodaire gave good feedback");

        command.reply(testFeedback.getFeedbackEmbed());
    }
}
