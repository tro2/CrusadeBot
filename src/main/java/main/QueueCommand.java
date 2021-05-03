package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class QueueCommand extends Command
{
    public static Message persistentQueue;

    public QueueCommand ()
    {
        this.name = "Queue";
        this.aliases = new String[] {"q", "queue"};
        this.requiredRole = CrusadeBot.config.get(Config.Field.OFFICERROLE);
        this.guildOnly = true;
        this.arguments = "";
    }

    @Override
    protected void execute(CommandEvent command)
    {
        String[] args = command.getArgs().split(" ");

        if (args.length == 0) {
            command.reply("Invalid arguments");

            return;
        }

        String commandArgs = args[0].toLowerCase();

        switch (commandArgs) {
            case "add":
                if (args.length < 3) {
                    command.reply("Invalid arguments");

                    break;
                }

                int isValid = Utils.isValidMember(args[2], command.getGuild());

                switch (isValid) {
                    case -1 -> {
                        command.reply("Unable to find user, make sure you input a valid id as the third argument");
                        return;
                    }
                    case 1 , 2 ->  {
                        command.reply("You moron, you didn't even input a number as the third argument");
                        return;
                    }
                }

                if (addMember(args[1], args[2])) {
                    addMemberLog(args[1], args[2], command.getMember());

                    command.reply("Successfully added member " + args[1]);
                }
                else {
                    command.reply("Member already in queue");
                }

                updateQueue(command.getGuild());

                break;
            case "remove":
                if (args.length < 2) {
                    command.reply("Invalid argument");

                    break;
                }

                QueueMember rmember = QueueDatabase.getMember(args[1]);

                if (rmember != null) {
                    removeMember(args[1]);

                    removeMemberLog(rmember.ign, rmember.id, command.getMember());

                    command.reply("Successfully removed member " + args[1]);

                    updateQueue(command.getGuild());
                }
                else {
                    command.reply("Unable to find member");
                }
                break;
            case "edit":
                if (args.length < 3) {
                    command.reply("Invalid argument");

                    break;
                }

                QueueMember member = QueueDatabase.getMember(args[1]);

                if (member != null) {
                    editMember(args[1], args[2]);

                    editMemberLog(args[1], args[2], member, command.getMember());

                    updateQueue(command.getGuild());

                    command.reply("Successfully updated member");

                    break;
                }
                else {
                    command.reply("Unable to find member");
                }


                break;
            case "list":
                if (args.length == 2) {
                    QueueMember lmember = QueueDatabase.getMember(args[1]);

                    if (lmember == null) {
                        command.reply("Unable to find member");
                    }
                    else {
                        command.reply(presentMember(lmember));
                    }

                    return;
                }

                command.reply(list());

                break;
            case "update":
                updateQueue(command.getGuild());

                command.reply("Queue updated");

                break;
            default:
                command.reply("Invalid arguments");
                break;
        }
    }

    private static boolean addMember (String ign, String id)
    {
        return QueueDatabase.addMember(id, ign);
    }

    private static boolean removeMember (String string)
    {
        return QueueDatabase.removeMember(string);
    }

    private static boolean editMember (String memberIdentifier, String string) {
        return QueueDatabase.editMember(memberIdentifier, string);
    }

    private static MessageEmbed list ()
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        List<QueueMember> memberList = QueueDatabase.getQueue();

        embedBuilder.setColor(Color.BLUE);
        embedBuilder.setTitle("Current Queue");
        embedBuilder.setDescription("**Current number of people in queue: **`" + memberList.size() + "`");

        String currentField = "";
        String temp = "";

        boolean desc = false;

        for (int i = 0; i < memberList.size(); i++) {
            temp = "**#" + (i+1) + ": [" + memberList.get(i).ign + "](https://www.realmeye.com/player/" + memberList.get(i).ign + ")** <@" + memberList.get(i).id + ">\n";

            if (i % 10 != 0 || i == 0) {
                currentField += temp;

            }
            else {
                if (embedBuilder.getFields().isEmpty()) {
                    embedBuilder.setDescription("**Current number of people in queue: **`" + memberList.size() + "`\n" + currentField);

                    desc = true;
                }
                else {
                    embedBuilder.addField(" ", currentField, false);
                }

                currentField = temp;
            }
        }

        if (!currentField.isEmpty() && embedBuilder.getFields().isEmpty() && !desc) {
            embedBuilder.setDescription("**Current number of people in queue: **`" + memberList.size() + "`\n" + currentField);

        }
        else {
            embedBuilder.addField(" ", currentField, false);

        }

        return embedBuilder.build();
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

    public static void updateQueue(Guild guild)
    {
        if (persistentQueue == null) {
            guild.getTextChannelById(CrusadeBot.config.get(Config.Field.QUEUECHANNELID)).purgeMessages(guild.getTextChannelById(CrusadeBot.config.get(Config.Field.QUEUECHANNELID)).getHistory().retrievePast(99).complete());

            persistentQueue = guild.getTextChannelById(CrusadeBot.config.get(Config.Field.QUEUECHANNELID)).sendMessage(list()).complete();

        }
        else {
            persistentQueue.editMessage(list()).queue();
        }
    }

    private static void addMemberLog(String ign, String id, Member staffMember) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Queue Add Log");
        embedBuilder.setDescription(" <@" + id + "> (" + ign + ") has been added to the queue by <@" + staffMember.getId() + "> (" + staffMember.getEffectiveName() + ")");

        embedBuilder.setFooter("Added at").setTimestamp(new Timestamp(System.currentTimeMillis()).toInstant());

        CrusadeBot.jda.getTextChannelById(CrusadeBot.config.get(Config.Field.LOGSCHANNELID)).sendMessage(embedBuilder.build()).queue();
    }

    private static void removeMemberLog(String ign, String id, Member staffMember) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Queue Remove Log");
        embedBuilder.setDescription(" <@" + id + "> (" + ign + ") has been removed from the queue by <@" + staffMember.getId() + "> (" + staffMember.getEffectiveName() + ")");

        embedBuilder.setFooter("Removed at").setTimestamp(new Timestamp(System.currentTimeMillis()).toInstant());

        CrusadeBot.jda.getTextChannelById(CrusadeBot.config.get(Config.Field.LOGSCHANNELID)).sendMessage(embedBuilder.build()).queue();
    }

    private static void editMemberLog(String string1, String string2, QueueMember queueMember, Member staffMember) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Queue Edit Log");
        embedBuilder.setDescription(" <@" + queueMember.id + "> (" + queueMember.ign + ") has been edited in the queue by <@" + staffMember.getId() + "> (" + staffMember.getEffectiveName() + ")\n" +
                "Changed `" + string1 + "` to `" + string2 + "`");

        embedBuilder.setFooter("Edited at").setTimestamp(new Timestamp(System.currentTimeMillis()).toInstant());

        CrusadeBot.jda.getTextChannelById(CrusadeBot.config.get(Config.Field.LOGSCHANNELID)).sendMessage(embedBuilder.build()).queue();
    }
}
