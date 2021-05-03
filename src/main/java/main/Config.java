package main;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class Config
{
    private static FileWriter fileWriter;
    private static JSONObject config;

    public Config()
    {
        try {
            String path = Utils.getJarContainingFolder(CrusadeBot.class) + "/config.json";

            File file = new File(path);

            if (!file.exists()) {
                try {
                    JSONObject obj = new JSONObject();

                    obj.put("TRODAIREID", "null");
                    obj.put("ID", "null");
                    obj.put("OFFICERROLEID", "null");
                    obj.put("OFFICERROLE", "null");
                    obj.put("BIGREDDOGGID", "null");
                    obj.put("PREFIX", "null");
                    obj.put("RAIDANNOUNCEMENTSCHANNELID", "null");
                    obj.put("BOTCOMMANDSCHANNELID", "null");
                    obj.put("LOGSCHANNELID", "null");
                    obj.put("CRUSADERROLEID", "null");
                    obj.put("CRUSADERROLE", "null");
                    obj.put("VERIFIEDROLEID", "null");
                    obj.put("VERIFIEDROLE", "null");
                    obj.put("VOIDREACTIONID", "null");
                    obj.put("LH_KEYREACTIONID", "null");
                    obj.put("VIALREACTIONID", "null");
                    obj.put("PALADINREACTIONID", "null");
                    obj.put("WARRIORREACTIONID", "null");
                    obj.put("KNIGHTREACTIONID", "null");
                    obj.put("MYSTICREACTIONID", "null");
                    obj.put("TRICKSTERREACTIONID", "null");
                    obj.put("MALUSREACTIONID", "null");
                    obj.put("PLANEWALKERREACTIONID", "null");
                    obj.put("ORYX_3REACTIONID", "null");
                    obj.put("HELMRUNEREACTIONID", "null");
                    obj.put("SWORDRUNEREACTIONID", "null");
                    obj.put("SHIELDRUNEREACTIONID", "null");
                    obj.put("INCREACTIONID", "null");
                    obj.put("STAFFCOMMANDSCHANNELID", "null");
                    obj.put("QUEUECHANNELID", "null");
                    obj.put("PENDINGCHANNELID", "null");
                    obj.put("GUILDID", "null");
                    obj.put("APPLICANTROLEID", "null");
                    obj.put("MUTEDROLEID", "null");


                    fileWriter = new FileWriter(path);

                    fileWriter.write(obj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        fileWriter.flush();

                        fileWriter.close();
                    } catch (IOException e) {
                        System.exit(-0);
                    }
                }
            }
            else {
                JSONParser parser = new JSONParser();

                try (Reader reader = new FileReader(path);) {
                    config = (JSONObject) parser.parse(reader);
                    int beforeSize = config.keySet().size();

                    for (Field f: Field.values()) {
                        if (!config.containsKey(f.key)) {
                            config.put(f.key, "null");
                        }
                    }

                    if (beforeSize != Field.values().length) {
                        try {
                            fileWriter = new FileWriter(path);

                            fileWriter.write(config.toString());

                            fileWriter.flush();

                            fileWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String get(Field field)
    {
        if (!config.containsKey(field.key))
        {
            config.put(field.key, "null");

            try {
                String path = Utils.getJarContainingFolder(CrusadeBot.class) + "/config.json";

                fileWriter = new FileWriter(path);

                fileWriter.write(config.toString());

                fileWriter.flush();

                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return config.get(field.key).toString();
    }

    public enum Field{
        TRODAIREID ("TRODAIREID"),
        ID ("ID"),
        BIGREDDOGGID ("BIGREDDOGGID"),
        PREFIX ("PREFIX"),
        RAIDANNOUNCEMENTSCHANNELID("RAIDANNOUNCEMENTSCHANNELID"),
        BOTCOMMANDSCHANNELID ("BOTCOMMANDSCHANNELID"),
        LOGSCHANNELID ("LOGSCHANNELID"),
        OFFICERROLE ("OFFICERROLE"),
        OFFICERROLEID ("OFFICERROLEID"),
        CRUSADERROLEID ("CRUSADERROLEID"),
        CRUSADERROLE ("CRUSADERROLE"),
        VERIFIEDROLEID ("VERIFIEDROLEID"),
        VERIFIEDROLE ("VERIFIEDROLE"),
        VOIDREACTIONID ("VOIDREACTIONID"),
        LH_KEYREACTIONID ("LH_KEYREACTIONID"),
        VIALREACTIONID ("VIALREACTIONID"),
        PALADINREACTIONID ("PALADINREACTIONID"),
        WARRIORREACTIONID ("WARRIORREACTIONID"),
        KNIGHTREACTIONID ("KNIGHTREACTIONID"),
        MYSTICREACTIONID ("MYSTICREACTIONID"),
        TRICKSTERREACTIONID ("TRICKSTERREACTIONID"),
        MALUSREACTIONID ("MALUSREACTIONID"),
        PLANEWALKERREACTIONID ("PLANEWALKERREACTIONID"),
        ORYX_3REACTIONID ("ORYX_3REACTIONID"),
        HELMRUNEREACTIONID ("HELMRUNEREACTIONID"),
        SWORDRUNEREACTIONID ("SWORDRUNEREACTIONID"),
        SHIELDRUNEREACTIONID ("SHIELDRUNEREACTIONID"),
        INCREACTIONID ("INCREACTIONID"),
        STAFFCOMMANDSCHANNELID ("STAFFCOMMANDSCHANNELID"),
        QUEUECHANNELID("QUEUECHANNELID"),
        PENDINGCHANNELID("PENDINGCHANNELID"),
        GUILDID("GUILDID"),
        APPLICANTROLEID("APPLICANTROLEID"),
        MUTEDROLEID("MUTEDROLEID");

        public String key;

        Field(String key) {
            this.key = key;
        }
    }


}
