package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class QueueDatabase {

    public static boolean addMember(String id, String ign)
    {
        String sql = "INSERT INTO queue (ID, IGN, lowerIGN, timeAdded) VALUES (?,?,?,?)";

        if (getMember(id) != null) {
            return false;

        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, id);
            statement.setString(2, ign);
            statement.setString(3, ign.toLowerCase());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public static boolean removeMember(String string)
    {
        String sql = "DELETE FROM queue WHERE (ID = ? OR lowerIGN = ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, string);
            statement.setString(2, string.toLowerCase());
            int deleted = statement.executeUpdate();

            return deleted > 0;

        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public static List<QueueMember> getQueue()
    {
        String sql = "SELECT * FROM queue ORDER BY timeAdded ASC";
        List<QueueMember> queue = new ArrayList<>();


        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
             Statement statement = conn.createStatement()){

            ResultSet resultSet = statement.executeQuery(sql);

            int i = 1;

            while (resultSet.next()) {
                String ign = resultSet.getString("IGN");
                String id = resultSet.getString("ID");
                Timestamp timeAdded = resultSet.getTimestamp("timeAdded");

                QueueMember member = new QueueMember(ign, id, i, timeAdded);

                queue.add(member);

                i++;
            }



        } catch (SQLException e){
            e.printStackTrace();
        }

        return queue;
    }

    public static boolean editMember (String memberIdentifier, String string) {
        List<QueueMember> memberList = QueueDatabase.getQueue();
        String sql = null;

        QueueMember member = memberList.stream().filter(m -> m.ign.equalsIgnoreCase(memberIdentifier) || String.valueOf(m.position).equals(memberIdentifier) || m.id.equals(memberIdentifier)).findFirst().orElse(null);

        if (member == null) {
            return false;
        }

        if (memberIdentifier.equalsIgnoreCase(member.ign)) {
            sql = "UPDATE queue SET IGN = '" + string + "' WHERE ID = '" + member.id + "'";
        }
        else if (memberIdentifier.equalsIgnoreCase(member.id)) {
            sql = "UPDATE queue SET id = '" + string + "' WHERE ID = '" + member.id + "'";
        }

        if (!(sql==null)) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CrusadeBot.db");
                 Statement statement = conn.createStatement()) {

                int deleted = statement.executeUpdate(sql);

                return deleted > 0;

            } catch (SQLException e){
                e.printStackTrace();
            }
        }

        return false;
    }

    public static QueueMember getMember (String string) {
        List<QueueMember> memberList = QueueDatabase.getQueue();

        return memberList.stream().filter( member -> member.ign.equalsIgnoreCase(string) || String.valueOf(member.position).equals(string) || member.id.equals(string)).findFirst().orElse(null);
    }
}