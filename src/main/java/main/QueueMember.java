package main;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class QueueMember {

    public String ign;
    public String id;
    public int position;
    public Timestamp timeAdded;

    public QueueMember (String ign, String id, int position, Timestamp timeAdded)
    {
        this.ign = ign;
        this.id = id;
        this.position = position;
        this.timeAdded = timeAdded;
    }
}
