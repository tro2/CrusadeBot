package main;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

public class Listeners extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (event.getGuild().getId().equalsIgnoreCase(CrusadeBot.config.get(Config.Field.GUILDID))) {
            welcomeMessage(event.getUser());
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (event.getGuild().getId().equalsIgnoreCase(CrusadeBot.config.get(Config.Field.GUILDID))) {
            queueCheckAlert(event.getMember().getId());
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bot Ready");

        new MuteCleaner();

        QueueCommand.updateQueue(CrusadeBot.jda.getGuildById(CrusadeBot.config.get(Config.Field.GUILDID)));
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        spongeMock(event);
    }

    public void welcomeMessage(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Welcome to the `\uD835\uDCD2 \uD835\uDCE1 \uD835\uDCE4 \uD835\uDCE2 \uD835\uDCD0 \uD835\uDCD3 \uD835\uDCD4 (\uD835\uDFD0.\uD835\uDFCE)` Discord!");
        embedBuilder.setColor(Color.orange);
        embedBuilder.addField("", "React with :one: if you are interested in joining the guild!", false);
        embedBuilder.addField("", "React with :two: if you are just here to hang out!", false);
        embedBuilder.setFooter("If you ever get stuck, head on over to #reception and ping @Officer");

        Message initial = user.openPrivateChannel().complete().sendMessage(embedBuilder.build()).complete();

        initial.addReaction("1\uFE0F\u20E3").complete();// :one:
        initial.addReaction("2\uFE0F\u20E3").complete();// :two:

        CrusadeBot.waiter.waitForEvent(PrivateMessageReactionAddEvent.class, e -> !e.getUser().isBot() &&
                                e.getMessageId().equals(initial.getId()) &&
                                e.getReactionEmote().isEmoji() &&
                                ("1\uFE0F\u20E32\uFE0F\u20E3").contains(e.getReactionEmote().getEmoji()),
                e -> {
                    String reaction = e.getReactionEmote().getEmoji();

                    if ("1\uFE0F\u20E3".contains(reaction)) { //:one: NEED TO DO str.contains(reaction) IDK WHY BUT IT DOESN'T WORK OTHERWISE
                        embedBuilder.clearFields();
                        embedBuilder.setDescription("You reacted with :one:, which means you are interested in joining the guild!");
                        embedBuilder.addField("", "Before you move on, make sure you meet the guild requirements listed below:", false);
                        embedBuilder.addField("Joining Requirements",
                                "`10k alive fame`\n" +
                                        "`2 8/8's`\n" +
                                        "`Experience in endgame dungeons (opt.)`",
                                false);
                        embedBuilder.addField("", "If you meet them, or are reasonably close, just enter your IGN", false);
                        embedBuilder.addBlankField(false);

                        initial.editMessage(embedBuilder.build()).queue();
                        initial.removeReaction("1\uFE0F\u20E3", CrusadeBot.jda.getSelfUser()).complete();
                        initial.removeReaction("2\uFE0F\u20E3", CrusadeBot.jda.getSelfUser()).complete();

                        CrusadeBot.waiter.waitForEvent(MessageReceivedEvent.class, m -> !m.getMessage().isFromGuild() && m.getAuthor().equals(user), m -> {
                            String IGN = m.getMessage().getContentRaw();

                            Guild guild = CrusadeBot.jda.getGuildById(CrusadeBot.config.get(Config.Field.GUILDID));

                            sendApplicantPanel(user, IGN);

                            try {
                                guild.addRoleToMember(guild.getMember(user), guild.getRoleById(CrusadeBot.config.get(Config.Field.APPLICANTROLEID)));
                            } catch (Exception exception) {

                            }

                            embedBuilder.clearFields();
                            embedBuilder.addField("", "**You have submitted an application to join the guild under the IGN: `" + IGN + "`!\n" +
                                    "If this was a mistake, ping an officer in #reception\n" +
                                    "Expect a response within a few hours, and welcome!**", false);
                            embedBuilder.addBlankField(false);

                            initial.editMessage(embedBuilder.build()).queue();
                        });

                    } else if ("2\uFE0F\u20E3".contains(reaction)) { //:two:
                        embedBuilder.clearFields();
                        embedBuilder.setDescription("You reacted with :two:, which means you are just here to hang out!");
                        embedBuilder.addField("", "**One last thing, please type your Rotmg IGN below**", false);
                        embedBuilder.addBlankField(false);

                        initial.editMessage(embedBuilder.build()).queue();
                        initial.removeReaction("1\uFE0F\u20E3", CrusadeBot.jda.getSelfUser()).complete();
                        initial.removeReaction("2\uFE0F\u20E3", CrusadeBot.jda.getSelfUser()).complete();

                        CrusadeBot.waiter.waitForEvent(MessageReceivedEvent.class, m -> !m.getMessage().isFromGuild() && m.getAuthor().equals(user), m -> {
                            String IGN = m.getMessage().getContentRaw();

                            embedBuilder.clearFields();
                            embedBuilder.addField("", "**You have submitted an application under the IGN: `" + IGN + "`!\n" +
                                    "if this was a mistake, ping an officer in #reception\n" +
                                    "Expect a response within a few hours, and welcome!**", false);
                            embedBuilder.addBlankField(false);

                            sendVerifiedPanel(user, IGN);


                            initial.editMessage(embedBuilder.build()).queue();
                        });

                    }
                });
    }

    public void sendApplicantPanel(User user, String ign) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle(ign + "'s GUILD APPLICATION");
        embedBuilder.setDescription("Chadded?");
        embedBuilder.addField("", "<@" + user.getId() + ">", false);
        embedBuilder.addField("", "Realmeye: [" + ign + "](https://www.realmeye.com/player/" + ign + ")", false);
        embedBuilder.setAuthor("", null, user.getAvatarUrl());

        CrusadeBot.jda.getTextChannelById(CrusadeBot.config.get(Config.Field.PENDINGCHANNELID)).sendMessage(embedBuilder.build()).queue();
    }

    public void sendVerifiedPanel(User user, String ign) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTitle(ign + "'s VERIFIED APPLICATION");
        embedBuilder.setDescription("For some reason this mofk is here and they aren't trying to join the guild");
        embedBuilder.addField("", "<@" + user.getId() + ">", false);
        embedBuilder.addField("", "Realmeye: [" + ign + "](https://www.realmeye.com/player/" + ign + ")", false);
        embedBuilder.setAuthor("", null, user.getAvatarUrl());

        CrusadeBot.jda.getTextChannelById(CrusadeBot.config.get(Config.Field.PENDINGCHANNELID)).sendMessage(embedBuilder.build()).queue();
    }

    public void queueCheckAlert(String id) {
        QueueMember queueMember = QueueDatabase.getMember(id);

        if (queueMember != null) {
            QueueDatabase.removeMember(id);

            String message = "<@&" + CrusadeBot.config.get(Config.Field.OFFICERROLEID) + "> `" + queueMember.ign + "` (<@" + queueMember.id + ">)  has left the server while in the queue, remove them from it if needed";

            CrusadeBot.jda.getTextChannelById(CrusadeBot.config.get(Config.Field.LOGSCHANNELID)).sendMessage(message).queue();
        }
    }

    public void spongeMock(MessageReactionAddEvent e) {
        if (e.getReactionEmote().isEmoji())
            return;

        if (e.getReactionEmote().getId().equals("812959258638549022")) {
            e.getReaction().removeReaction(e.getUser()).queue();

            Message text = e.getChannel().retrieveMessageById(e.getMessageId()).complete();
            String StringText = text.getContentRaw().toLowerCase();
            Random random = new Random();

            for (int i = 0; i < StringText.length(); i++) {
                if(random.nextBoolean()) {
                    StringText = StringText.substring(0, i) + StringText.substring(i, i+1).toUpperCase() + StringText.substring(i+1);
                }
            }

            //Embed
            EmbedBuilder embed = new EmbedBuilder();
            embed.setDescription(StringText);
            embed.setColor(new Color(245, 252, 60));
            embed.setThumbnail("https://res.cloudinary.com/nashex/image/upload/v1613698392/assets/759584001131544597_im3kgg.png");
            text.reply(embed.build()).queue();
        }
    }
}
