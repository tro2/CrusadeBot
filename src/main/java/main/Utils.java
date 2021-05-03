package main;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.io.File;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.List;

public class Utils {

    public static String getJarContainingFolder(Class aclass) throws Exception {
        CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();

        File jarFile;

        if (codeSource.getLocation() != null) {
            jarFile = new File(codeSource.getLocation().toURI());
        }
        else {
            String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
            String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
            jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
            jarFile = new File(jarFilePath);
        }
        return jarFile.getParentFile().getAbsolutePath();
    }

    /*
    returns -1 if the string is not a valid nickname or id of a member of the specified guild
    returns 0 if the string is a valid id of a member of the specified guild
    returns 1 if the string is a valid nickname of a member of the specified guild
    returns 2 if the string is a valid nickname of multiple members
    */
    public static int isValidMember(String string, Guild guild) {
        List<Member> memberList = guild.getMembersByEffectiveName(string, true);

        if (memberList.isEmpty()) {
            try {
                Long id = Long.parseLong(string.replaceAll("[<@!>]", ""));

                Member member = guild.getMemberById(id);

                if (member != null) {
                    return 0;
                }
            } catch (NumberFormatException e) {

            }

            return -1;
        }
        if (memberList.size() == 1) {
            return 1;
        }
        return 2;
    }
}
