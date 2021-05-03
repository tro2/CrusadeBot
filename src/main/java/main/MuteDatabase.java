package main;

import net.dv8tion.jda.api.entities.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MuteDatabase {

    public static boolean addMute(String id, String staffMemberId) { //no duration or reason
        String sql = "INSERT INTO mutes (id, timeAdded, staffMemberId) VALUES (?,?,?)";

        if (getMute(id) != null) {
            return false;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, id);
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.setString(3, staffMemberId);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public static boolean addMute(String id, Long durationInMillis, String staffMemberId) { //yes duration but no reason
        String sql = "INSERT INTO mutes (id, durationInMillis, timeAdded, timeToBeRemoved, staffMemberId) VALUES (?,?,?,?,?)";

        if (getMute(id) != null) {
            return false;
        }

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, id);
            statement.setLong(2, durationInMillis);
            statement.setTimestamp(3, currentTime);
            statement.setTimestamp(4, new Timestamp(currentTime.getTime() + durationInMillis));
            statement.setString(5, staffMemberId);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public static boolean addMute(String id, String reason, String staffMemberId) { //no duration but yes reason
        String sql = "INSERT INTO mutes (id, reason, timeAdded, staffMemberId) VALUES (?,?,?,?)";

        if (getMute(id) != null) {
            return false;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, id);
            statement.setString(2, reason);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setString(4, staffMemberId);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public static boolean addMute(String id, Long durationInMillis, String reason, String staffMemberId) { //duration and reason
        String sql = "INSERT INTO mutes (id, durationInMillis, timeAdded, timeToBeRemoved, reason, staffMemberId) VALUES (?,?,?,?,?,?)";

        if (getMute(id) != null) {
            return false;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, id);
            statement.setLong(2, durationInMillis);
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis() + durationInMillis));
            statement.setString(5, reason);
            statement.setString(6, staffMemberId);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public static boolean removeMute(String id) {

        String sql = "DELETE FROM mutes WHERE ID = '" + id + "'";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()){

            int deleted = statement.executeUpdate(sql);

            return deleted > 0;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
        }

    public static List<Mute> getMutes() {
        String sql = "SELECT * FROM mutes";
        List<Mute> muteList = new ArrayList<>();


        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()){

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String muteId = resultSet.getString("id");
                Long durationInMillis = resultSet.getLong("durationInMillis");
                Timestamp timeAdded = resultSet.getTimestamp("timeAdded");
                Timestamp timeToBeRemoved = resultSet.getTimestamp("timeToBeRemoved");
                String reason = resultSet.getString("reason");
                String staffMemberId = resultSet.getString("staffMemberId");

                Mute mute = new Mute(muteId, durationInMillis, timeAdded, timeToBeRemoved, reason, staffMemberId);

                muteList.add(mute);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return muteList;
    }

    public static Mute getMute(String id) {
        List<Mute> muteList = getMutes();

        return muteList.stream().filter( mute -> mute.id.equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public static List<Mute> getMutesToBeRemoved() {
        String sql = "SELECT * FROM mutes WHERE NOT timeToBeRemoved = 0 AND timeToBeRemoved < " + System.currentTimeMillis();
        List<Mute> muteList = new ArrayList<>();


        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()){

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String muteId = resultSet.getString("id");
                Long durationInMillis = resultSet.getLong("durationInMillis");
                Timestamp timeAdded = resultSet.getTimestamp("timeAdded");
                Timestamp timeToBeRemoved = resultSet.getTimestamp("timeToBeRemoved");
                String reason = resultSet.getString("reason");
                String staffMemberId = resultSet.getString("staffMemberId");

                Mute mute = new Mute(muteId, durationInMillis, timeAdded, timeToBeRemoved, reason, staffMemberId);

                muteList.add(mute);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return muteList;
    }
}
