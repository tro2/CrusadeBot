package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;

public class HeadCount extends Command {

    public HeadCount ()
    {
        this.name = "HeadCount";
        this.aliases = new String[] {"headcount", "hc"};
        this.requiredRole = CrusadeBot.config.get(Config.Field.OFFICERROLE);
        this.help = "Starts a headcount in the #guild-runs channel";
        this.arguments = "type";
        this.guildOnly = true;
    }

    @Override
    protected void execute (CommandEvent command)
    {
        String HeadCountType = command.getArgs();
        EmbedBuilder HeadCountEmbedBuilder = new EmbedBuilder();
        MessageBuilder HeadCountMessageBuilder = new MessageBuilder();

         if (argEquals(HeadCountType, new String[] {"void", "v"}))
         {
             HeadCountBuilder(command,
                     HeadCountMessageBuilder,
                     HeadCountEmbedBuilder,
                     "<@&" + CrusadeBot.config.get(Config.Field.VERIFIEDROLEID) + ">",
                     "Headcount for Void",
                     "React with key/vial if you have one \n Otherwise react with any applicable class choices below",
                     new Color(72, 0, 181),
                     new String[] {CrusadeBot.config.get(Config.Field.VOIDREACTIONID),
                             CrusadeBot.config.get(Config.Field.LH_KEYREACTIONID),
                             CrusadeBot.config.get(Config.Field.VIALREACTIONID),
                             CrusadeBot.config.get(Config.Field.PALADINREACTIONID),
                             CrusadeBot.config.get(Config.Field.WARRIORREACTIONID),
                             CrusadeBot.config.get(Config.Field.KNIGHTREACTIONID),
                             CrusadeBot.config.get(Config.Field.MYSTICREACTIONID),
                             CrusadeBot.config.get(Config.Field.TRICKSTERREACTIONID)}
             );
         }
         else if (argEquals(HeadCountType, new String[] {"o3", "oryx", "oryx3", "o"}))
         {
             HeadCountBuilder(command,
                     HeadCountMessageBuilder,
                     HeadCountEmbedBuilder,
                     "<@&" + CrusadeBot.config.get(Config.Field.VERIFIEDROLEID) + ">",
                     "Headcount for Oryx 3",
                     "React with appropriate rune/inc if you have one \n Otherwise react any applicable class choices below",
                     new Color(37, 0, 208),
                     new String[] {CrusadeBot.config.get(Config.Field.ORYX_3REACTIONID),
                             CrusadeBot.config.get(Config.Field.HELMRUNEREACTIONID),
                             CrusadeBot.config.get(Config.Field.SWORDRUNEREACTIONID),
                             CrusadeBot.config.get(Config.Field.SHIELDRUNEREACTIONID),
                             CrusadeBot.config.get(Config.Field.INCREACTIONID),
                             CrusadeBot.config.get(Config.Field.PALADINREACTIONID),
                             CrusadeBot.config.get(Config.Field.WARRIORREACTIONID),
                             CrusadeBot.config.get(Config.Field.MYSTICREACTIONID),
                             CrusadeBot.config.get(Config.Field.TRICKSTERREACTIONID)}
             );
         }
         else if (argEquals(HeadCountType, new String[] {"cult", "c"}))
         {
             HeadCountBuilder(command,
                     HeadCountMessageBuilder,
                     HeadCountEmbedBuilder,
                     "<@&" + CrusadeBot.config.get(Config.Field.VERIFIEDROLEID) + ">",
                     "Headcount for Cult",
                     "React with key if you have one \n Otherwise react any applicable class choices below",
                     new Color(231, 0, 0),
                     new String[] {CrusadeBot.config.get(Config.Field.MALUSREACTIONID),
                             CrusadeBot.config.get(Config.Field.LH_KEYREACTIONID),
                             CrusadeBot.config.get(Config.Field.PLANEWALKERREACTIONID),
                             CrusadeBot.config.get(Config.Field.PALADINREACTIONID),
                             CrusadeBot.config.get(Config.Field.WARRIORREACTIONID),
                             CrusadeBot.config.get(Config.Field.KNIGHTREACTIONID),
                             CrusadeBot.config.get(Config.Field.MYSTICREACTIONID),
                             CrusadeBot.config.get(Config.Field.TRICKSTERREACTIONID)}
             );
         }
         else {
             command.reply("Invalid argument");
         }

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

    private void HeadCountBuilder(CommandEvent command, MessageBuilder HeadCountMessageBuilder, EmbedBuilder HeadCountEmbedBuilder, String PingMessage, String Title, String Description, Color color, String[] EmoteList)
    {
        HeadCountMessageBuilder.append(PingMessage);

        HeadCountEmbedBuilder.setTitle(Title);
        HeadCountEmbedBuilder.setColor(color);
        HeadCountEmbedBuilder.setDescription(Description);

        HeadCountMessageBuilder.setEmbed(HeadCountEmbedBuilder.build());

        Message HeadCount = command.getGuild().getTextChannelById(CrusadeBot.config.get(Config.Field.RAIDANNOUNCEMENTSCHANNELID)).sendMessage(HeadCountMessageBuilder.build()).complete();

        for (int i = 0; i < EmoteList.length; i++)
        {
            HeadCount.addReaction(command.getGuild().getEmoteById(EmoteList[i])).queue();
        }
    }
}
