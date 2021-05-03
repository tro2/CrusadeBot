package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class QueuePositionCommand extends Command
{
    public QueuePositionCommand ()
    {
        this.name = "General Queue";
        this.aliases = new String[] {"qp", "queuep", "queueposition", "position", "pos"};
        this.guildOnly = true;
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent command) {
        String[] args = command.getArgs().split(" ");

        if (args.length == 0) {
            command.reply("Invalid arguments");

            return;
        }

        QueueMember member = QueueDatabase.getMember(args[0]);

        if (member == null) {
            command.reply("Unable to find member");
        }
        else {
            command.reply(presentMember(member));
        }
    }

    private static MessageEmbed presentMember (QueueMember member)
    {
        EmbedBuilder builder =  new EmbedBuilder();

        builder.setColor(Color.blue);
        builder.setDescription("<@" + member.id + "> **| Position: **`" + member.position + "`");
        builder.setAuthor(member.ign, "https://www.realmeye.com/player/" + member.ign , CrusadeBot.jda.retrieveUserById(member.id).complete().getAvatarUrl());
        builder.setFooter("Entered queue on").setTimestamp(member.timeAdded.toInstant());

        return builder.build();
    }
}
