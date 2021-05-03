package main;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.List;

public class Feedback {

    //Guild-specific data
    public String guildId;
    public Color feedbackColor;

    //Data to get when the rl does the command
    public String receiverId;
    public String feedbackerId;
    public String assisterId;
    public String feedbackType;
    public String mapLink;
    public String feedbackBody;
    public String assisterFeedbackBody;

    //generic feedback
    public Feedback(String guildId, Color feedbackColor, String receiverId, String feedbackerId, String feedbackType, String feedbackBody) {
        this.guildId = guildId;
        this.feedbackColor = feedbackColor;

        this.receiverId = receiverId;
        this.feedbackerId = feedbackerId;
        this.feedbackType = feedbackType;
        this.feedbackBody = feedbackBody;

        //unused variables, set to default values
        this.assisterId = "none";
        this.mapLink = "none";
        this.assisterFeedbackBody = "none";
    }

    //unassisted feedback with map
    public Feedback(String guildId, Color feedbackColor, String receiverId, String feedbackerId, String feedbackType, String mapLink, String feedbackBody) {
        this.guildId = guildId;
        this.feedbackColor = feedbackColor;

        this.receiverId = receiverId;
        this.feedbackerId = feedbackerId;
        this.feedbackType = feedbackType;
        this.mapLink = mapLink;
        this.feedbackBody = feedbackBody;

        //unused variables, set to default values
        this.assisterId = "none";
        this.assisterFeedbackBody = "none";
    }

    //assisted feedback with map
    public Feedback(String guildId, Color feedbackColor, String receiverId, String feedbackerId, String assisterId, String feedbackType, String mapLink, String feedbackBody, String assisterFeedbackBody) {
        this.guildId = guildId;
        this.feedbackColor = feedbackColor;

        this.receiverId = receiverId;
        this.feedbackerId = feedbackerId;
        this.assisterId = assisterId;
        this.feedbackType = feedbackType;
        this.mapLink = mapLink;
        this.feedbackBody = feedbackBody;
        this.assisterFeedbackBody = assisterFeedbackBody;

    }

    public MessageEmbed getFeedbackEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Member receiver = CrusadeBot.jda.getGuildById(guildId).getMemberById(receiverId);
        Member feedbacker = CrusadeBot.jda.getGuildById(guildId).getMemberById(feedbackerId);

        embedBuilder.setColor(feedbackColor);

        if (mapLink != "none") {
            embedBuilder.setImage(mapLink);
        }

        embedBuilder.setTitle(feedbackType + " feedback");
        addFeedbackFields(feedbackBody, embedBuilder);

        if (assisterId != "none") {
            Member assister = CrusadeBot.jda.getGuildById(guildId).getMemberById(assisterId);

            embedBuilder.setDescription("**Feedback for:** <@" + receiverId + "> (" + receiver.getEffectiveName() + ")\n"
                    + "**Feedback by:** <@" + feedbackerId + "> (" + feedbacker.getEffectiveName() + ")\n"
                    + "**Assisted by:** <@" + assisterId + "> (" + assister.getEffectiveName() + ")");

            embedBuilder.addBlankField(false);
            embedBuilder.addField("Feedback for " + assister.getEffectiveName(), assisterFeedbackBody, false);
        }
        else {
            embedBuilder.setDescription("Feedback for <@" + receiverId + "> (" + receiver.getEffectiveName() + ") by <@" + feedbackerId + "> (" + feedbacker.getEffectiveName() + ")");
        }

        return embedBuilder.build();
    }

    private void addFeedbackFields(String feedbackBody, EmbedBuilder embedBuilder) {
        embedBuilder.addField("", "sample feedback", false);
        embedBuilder.addField("", "sample feedback", false);
        embedBuilder.addField("", "sample feedback", false);
        embedBuilder.addField("", "sample feedback", false);
        embedBuilder.addField("", "sample feedback", false);
        embedBuilder.addField("", "sample feedback", false);
    }

}
