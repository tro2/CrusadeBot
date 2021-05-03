package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class UnmuteCommand extends Command {

    public UnmuteCommand () {
        this.name = "unmute";
        this.aliases = new String[] {"unmute"};
        this.requiredRole = CrusadeBot.config.get(Config.Field.OFFICERROLE);
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent command) {

        String[] args = command.getArgs().split(" ");
        Role mutedRole = command.getGuild().getRoleById(CrusadeBot.config.get(Config.Field.MUTEDROLEID));

        int isValid = Utils.isValidMember(args[0], command.getGuild());

        switch (isValid) {
            case -1 -> {
                command.reply("Unable to find user");
            }
            case 0 -> {
                Member member = command.getGuild().getMemberById(args[0]);

                if (MuteDatabase.removeMute(args[0]) || member.getRoles().contains(mutedRole)) {
                    command.getGuild().removeRoleFromMember(args[0], mutedRole).complete();

                    sendUnmuteLog(member.getId());

                    command.reply("Successfully unmuted user");

                    return;
                }
                command.reply("User was not muted");
            }
            case 1 -> {
                Member member = command.getGuild().getMembersByEffectiveName(args[0], true).get(0);

                if (MuteDatabase.removeMute(member.getId()) || member.getRoles().contains(mutedRole)) {
                    sendUnmuteLog(member.getId());

                    command.getGuild().removeRoleFromMember(member, mutedRole).complete();

                    command.reply("Successfully unmuted user");

                    return;
                }
                command.reply("User was not muted");
            }
            case 2 -> {
                command.reply("Multiple users found by that name, please unmute by id");
            }
        }

    }

    public void sendUnmuteLog(String id) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Unmute Log");
        embedBuilder.setDescription("User <@" + id + "> has been unmuted");
        CrusadeBot.jda.getGuildById(CrusadeBot.config.get(Config.Field.GUILDID)).getTextChannelById(CrusadeBot.config.get(Config.Field.LOGSCHANNELID)).sendMessage(embedBuilder.build()).queue();
    }
}
