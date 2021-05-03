package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Duration;
import java.util.Arrays;

public class MuteCommand extends Command {

    public MuteCommand() {
        this.name = "mute";
        this.aliases = new String[] {"mute"};
        this.requiredRole = CrusadeBot.config.get(Config.Field.OFFICERROLE);
        this.arguments = "user, duration";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent command) {

        String[] args = command.getArgs().split(" ");
        Role mutedRole = command.getGuild().getRoleById(CrusadeBot.config.get(Config.Field.MUTEDROLEID));
        Member member = null;
        long duration;
        String lengthType = "";
        String reason = "";
        boolean hasDuration = false;
        boolean hasReason = false;
        boolean success;
        String staffMemberId = command.getMember().getId();

        if (args.length == 0) {
            command.reply("Invalid Arguments (`command` `user` `duration(opt.)` `reason(opt.)`)");

            return;
        }

        int isValid = Utils.isValidMember(args[0], command.getGuild());

        switch (isValid) {
            case -1 -> {
                command.reply("Unable to find user, make sure you input a valid nickname or id as the first argument");
                return;
            }
            case 0 -> member = command.getGuild().getMemberById(args[0]);
            case 1 -> member = command.getGuild().getMembersByEffectiveName(args[0], true).get(0);
            case 2 -> {
                command.reply("Multiple users found with that nickname, please use the id to mute");
                return;
            }
        }

        //member is guaranteed to be not null at this point

        if (args.length >= 3) {
            try {
                Integer.parseInt(args[1]);

                lengthType = args[1] + " " + args[2];

                hasDuration = true;

                if (args.length > 3) {
                    hasReason = true;

                    reason = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                }
            } catch (NumberFormatException e) {
                hasReason = true;

                reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            }
        }

        if (args.length == 2) {
            hasReason = true;

            reason = args[1];
        }

        assert member != null;
        if (member.getRoles().contains(mutedRole) || !(MuteDatabase.getMute(member.getId()) == null)) {
            command.reply("User already has the muted role or is muted in the database, would you like to override? Y/N");

            Member finalMember = member;
            boolean finalHasDuration = hasDuration;
            boolean finalHasReason = hasReason;
            Member finalMember1 = member;
            String finalReason = reason;
            String finalLengthType = lengthType;

            CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                    m.getChannel().equals(command.getChannel()) &&
                    m.getMember().equals(command.getMember()) &&
                    m.getMessage().getContentRaw().trim().equalsIgnoreCase("y") ||
                    m.getMessage().getContentRaw().trim().equalsIgnoreCase("n"),
                    m -> {

                if (m.getMessage().getContentRaw().trim().equalsIgnoreCase("y") ) {
                    finalMember.getGuild().removeRoleFromMember(finalMember, mutedRole);

                    MuteDatabase.removeMute(finalMember.getId());

                    command.reply("Overriding mute...");

                    muteUser(args, finalHasDuration, finalHasReason, finalMember1, staffMemberId, finalReason, finalLengthType, mutedRole, command);
                }
                else {
                    command.reply("Confirmed, mute has not been overriden");

                    return;
                }
            });
        }
        else {
            muteUser(args, hasDuration, hasReason, member, staffMemberId, reason, lengthType, mutedRole, command);
        }
    }

    /*
    Converts to time in millis
    Only use if the args of the command are 3 or more
    string length has to be an int, checked earlier
    Handles error checking and responds
    */
    private long convertToTimeInMillis(String length, String type, CommandEvent commandEvent) {
        long duration;

        duration = Integer.parseInt(length);

        if (duration < 1 || duration > 999) {
            commandEvent.reply("Invalid duration (Make sure it's a number between 1 and 999)");

            return 0;
        }

        switch (type) {
            case "seconds", "s", "second" -> duration = duration * 1000;
            case "minutes", "m", "minute" -> duration = duration * 60000;
            case "hours", "h", "hour" -> duration = duration * 3600000;
            case "days", "d", "day" -> duration = duration * 86400000;
            case "weeks", "w", "week" -> duration = duration * 604800000;
            case "years", "y", "year" -> duration = duration * 31536000000L;
            default -> {
                commandEvent.reply("Invalid duration type, use seconds(s)/minutes(m)/hours(h)/days(d)/weeks(w)/years(y)");
                duration = -1;
            }
        }

        return duration;
    }

    private MessageEmbed MuteLog(Mute mute, CommandEvent commandEvent) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Member member = commandEvent.getGuild().getMemberById(mute.id);
        Member staffMember = commandEvent.getGuild().getMemberById(mute.staffMemberId);

        String length;

        if (mute.durationInMillis > 0) {
            Duration duration = Duration.between(mute.timeAdded.toInstant(), mute.timeToBeRemoved.toInstant());

            long years  = duration.toDays() / 365;
            duration = duration.minusDays(years * 365);
            long weeks = duration.toDays() / 7;
            duration = duration.minusDays(weeks * 7);
            long days = duration.toDays();
            duration = duration.minusDays(days);
            long hours = duration.toHours();
            duration = duration.minusHours(hours);
            long minutes = duration.toMinutes();
            duration = duration.minusMinutes(minutes);
            long seconds = duration.getSeconds();

            length = (years == 0 ? "" : years + " year" + (years == 1 ? " " : "s "))
                    + (weeks == 0 ? "" : weeks + " week" + (weeks == 1 ? " " : "s "))
                    + (days == 0 ? "" : days + " day" + (days == 1 ? " " : "s "))
                    + (hours == 0 ? "" : hours + " hour" + (hours == 1 ? " " : "s "))
                    + (minutes == 0 ? "" : minutes + " minute" + (minutes == 1 ? " " : "s "))
                    + (seconds == 0 ? "" :seconds + " second" + (seconds == 1 ? "" : "s"));
        }
        else {
            length = "time and all eternity";
        }



        embedBuilder.setColor(Color.red);
        embedBuilder.setTitle("Mute Information");
        embedBuilder.setDescription("The mute is for " + length);
        embedBuilder.addField("Member info `" + member.getEffectiveName() + "`", "<@" + mute.id + "> (Tag: " + member.getUser().getAsTag() + ")", false);
        embedBuilder.addField("Staff Member info `" + staffMember.getEffectiveName() + "`","<@" + mute.staffMemberId + "> (Tag: " + member.getUser().getAsTag() + ")", true);
        embedBuilder.addField("Reason:", mute.reason, false);

        return embedBuilder.build();
    }

    private void muteUser(String[] args, boolean hasDuration, boolean hasReason, Member member, String staffMemberId, String reason, String lengthType, Role mutedRole, CommandEvent command) {
        long duration;

        if (args.length == 1) { //hasDuration and hasReason are false
            MuteDatabase.addMute(member.getId(), staffMemberId);
        }
        else if (args.length == 3 && hasDuration) { //has a duration but no reason, hasReason is false if conditions are true
            duration = convertToTimeInMillis(lengthType.split(" ")[0], lengthType.split(" ")[1], command); //lengthType has the format (int, durationtype (seconds, minutes, days, etc)

            if (duration == -1) {
                return;
            }

            MuteDatabase.addMute(member.getId(), duration, staffMemberId);
        }
        else if (!hasDuration && hasReason) { //hasReason can only be true if command has more than 1 arg, so no need to check
            MuteDatabase.addMute(member.getId(), reason, staffMemberId);
        }
        else { //both booleans are true, and because of that, length has to be long enough
            duration = convertToTimeInMillis(lengthType.split(" ")[0], lengthType.split(" ")[1], command); //lengthType has the format (int, durationtype (seconds, minutes, days, etc)

            if (duration == -1) {
                return;
            }

            MuteDatabase.addMute(member.getId(), duration, reason, staffMemberId);
        }

        member.getGuild().addRoleToMember(member, mutedRole).complete();

        Mute mute = MuteDatabase.getMute(member.getId());

        CrusadeBot.jda.openPrivateChannelById(mute.id).complete().sendMessage(MuteLog(mute, command)).queue();

        command.getGuild().getTextChannelById(CrusadeBot.config.get(Config.Field.LOGSCHANNELID)).sendMessage(MuteLog(mute, command)).queue();

        command.reply("Successfully muted user");
    }
}
