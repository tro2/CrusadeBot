package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;

public class Poll extends Command {

    public Poll ()
    {
        this.name = "Poll";
        this.aliases = new String[] {"poll", "p"};
        this.requiredRole = CrusadeBot.config.get(Config.Field.OFFICERROLE);
        this.help = "Starts a poll in the #guild-runs channel";
        this.arguments = "none";
        this.guildOnly = true;
    }

    @Override
    protected void execute (CommandEvent command)
    {
        String PollType = command.getArgs();
        EmbedBuilder PollEmbedBuilder = new EmbedBuilder();
        MessageBuilder PollMessageBuilder = new MessageBuilder();

        HeadCountBuilder(command,
                PollMessageBuilder,
                PollEmbedBuilder,
                "<@&" + CrusadeBot.config.get(Config.Field.VERIFIEDROLEID) + ">",
                "Poll for Oryx 3, Void, or Cult",
                "React with the run type you are interested in and with key/vial/rune/inc if you have one",
                new Color(255, 255, 255),
                new String[] {CrusadeBot.config.get(Config.Field.ORYX_3REACTIONID),
                        CrusadeBot.config.get(Config.Field.VOIDREACTIONID),
                        CrusadeBot.config.get(Config.Field.MALUSREACTIONID),
                        CrusadeBot.config.get(Config.Field.HELMRUNEREACTIONID),
                        CrusadeBot.config.get(Config.Field.SWORDRUNEREACTIONID),
                        CrusadeBot.config.get(Config.Field.SHIELDRUNEREACTIONID),
                        CrusadeBot.config.get(Config.Field.INCREACTIONID),
                        CrusadeBot.config.get(Config.Field.LH_KEYREACTIONID),
                        CrusadeBot.config.get(Config.Field.VIALREACTIONID),}
        );
    }

    private boolean argEquals(String arg, String[] types)
    {
        for (int i = 0; i < types.length; i++) {
            if (arg.equalsIgnoreCase(types[i])) {
                return true;
            }
        }

        return false;
    }

    private void HeadCountBuilder(CommandEvent command, MessageBuilder PollMessageBuilder, EmbedBuilder PollEmbedBuilder, String PingMessage, String Title, String Description, Color color, String[] EmoteList)
    {
        PollMessageBuilder.append(PingMessage);

        PollEmbedBuilder.setTitle(Title);
        PollEmbedBuilder.setColor(color);
        PollEmbedBuilder.setDescription(Description);

        PollMessageBuilder.setEmbed(PollEmbedBuilder.build());

        Message HeadCount = command.getGuild().getTextChannelById(CrusadeBot.config.get(Config.Field.RAIDANNOUNCEMENTSCHANNELID)).sendMessage(PollMessageBuilder.build()).complete();

        for (int i = 0; i < EmoteList.length; i++)
        {
            HeadCount.addReaction(command.getGuild().getEmoteById(EmoteList[i])).queue();
        }
    }
}
