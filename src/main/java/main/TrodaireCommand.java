package main;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.io.File;
import java.util.Random;

public class TrodaireCommand extends Command {

    public TrodaireCommand() {
        this.name = "Trodaire";
        this.aliases = new String[] {"trodaire", "tro"};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent command) {
        File[] files = new File("Resources/Trodaire").listFiles();

        Random rand = new Random();

        File file = files[rand.nextInt(files.length)];

        command.reply(file, file.getName());
    }
}
