package main;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MuteCleaner {

    ScheduledExecutorService TIMER = new ScheduledThreadPoolExecutor(1);

    public MuteCleaner () {
        TIMER.scheduleAtFixedRate(() -> {
            cleanMutes();
        }, 30L, 30L, TimeUnit.SECONDS);
    }

    private void cleanMutes() {
        List<Mute> muteList = MuteDatabase.getMutesToBeRemoved();

        if (!muteList.isEmpty()) {
            Guild guild = CrusadeBot.jda.getGuildById(CrusadeBot.config.get(Config.Field.GUILDID));
            Role mutedRole = guild.getRoleById(CrusadeBot.config.get(Config.Field.MUTEDROLEID));
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Unmute Log");

            for (Mute mute : muteList) {
                CrusadeBot.jda.getGuildById(CrusadeBot.config.get(Config.Field.GUILDID)).removeRoleFromMember(mute.id, mutedRole).queue();

                MuteDatabase.removeMute(mute.id);

                embedBuilder.setDescription("User <@" + mute.id + "> has been unmuted");

                guild.getTextChannelById(CrusadeBot.config.get(Config.Field.LOGSCHANNELID)).sendMessage(embedBuilder.build()).queue();
            }
        }
    }

}
