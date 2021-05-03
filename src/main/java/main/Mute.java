package main;

import net.dv8tion.jda.api.entities.Member;

import java.sql.Timestamp;

public class Mute {

    public String id;
    public long durationInMillis;
    public Timestamp timeAdded;
    public Timestamp timeToBeRemoved;
    public String reason;
    public String staffMemberId;

    public Mute (String id, long durationInMillis, Timestamp timeAdded, Timestamp timeToBeRemoved, String reason, String staffMemberId) {
        this.id = id;
        this.durationInMillis = durationInMillis;
        this.timeAdded = timeAdded;
        this.timeToBeRemoved = timeToBeRemoved;
        this.reason = reason;
        this.staffMemberId = staffMemberId;
    }
}
