package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GhostPingCommand extends Command {

    public GhostPingCommand()
    {
        this.name = "GhostPing";
        this.aliases = new String[] {"hello", "gp", "ghostping"};
        this.hidden = true;
        this.guildOnly = true;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent command) {

        if (command.getMessage().getMentionedMembers().isEmpty()) {
            command.getMessage().delete().queue();

            return;
        }

        Member member = command.getMessage().getMentionedMembers().get(0);
        List<TextChannel> textChannelList;
        long delay = 0L;
        String[] args = command.getArgs().split(" ");
        int numPings;

        if (member != null) {
            textChannelList = command.getGuild().getTextChannels().stream().filter(textChannel -> member.hasPermission(textChannel, Permission.VIEW_CHANNEL)).collect(Collectors.toList());

            command.getMessage().delete().queue();

            if (args.length > 1) {
                try {
                    numPings = Integer.parseInt(args[1]);

                    for (int i = 0; i < numPings; i++) {
                        textChannelList.get(i % textChannelList.size()).sendMessage(member.getAsMention()).queueAfter(delay, TimeUnit.MILLISECONDS, message -> message.delete().queue());

                        delay += 100;
                    }

                    return;
                } catch (NumberFormatException e) {
                    command.getMessage().delete().queue();
                }
            }

            for (TextChannel textChannel : textChannelList) {
                textChannel.sendMessage(member.getAsMention()).queueAfter(delay, TimeUnit.MILLISECONDS, message -> message.delete().queue());

                delay += 100;
            }
        }
        else {
            command.getMessage().delete().queue();
        }
    }
}
