package net.thetowncraft.townbot.listeners.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.thetowncraft.townbot.api.command_handler.CommandEvent;
import net.thetowncraft.townbot.api.command_handler.discord.DiscordCommand;
import net.thetowncraft.townbot.api.command_handler.minecraft.MinecraftCommand;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadJar extends DiscordCommand {


    @Override
    public void execute(CommandEvent.Discord event) {
        Message message = event.getMessage();
        List<Message.Attachment> attachments = message.getAttachments();
        if(attachments.size() == 0) {
            event.getChannel().sendMessage(":x: **Error**! Please specify a file to upload!").queue();
            return;
        }

        List<String> uploadStrings = new ArrayList<>();
        int successfulUploads = 0;

        for(Message.Attachment attachment : attachments) {
            if(attachment.getFileExtension() == null) {
                uploadStrings.add(":x: **Error**! Failed to upload **" + attachment.getFileName() + "** because it is not a valid JAR file.");
                continue;
            }
            if(!attachment.getFileExtension().equals("jar")) {
                uploadStrings.add(":x: **Error**! Failed to upload **" + attachment.getFileName() + "** because it is not a valid JAR file.");
                continue;
            }

            File file = new File("plugins/" + attachment.getFileName());
            if(file.exists()) {
                if(!file.delete()) {
                    uploadStrings.add(":x: **Error**! Failed to upload **" + attachment.getFileName() + "** because I could not delete it's previous version with the same name.");
                    continue;
                }
            }
            try {
                attachment.downloadToFile(file).complete(file);
                successfulUploads++;
                uploadStrings.add(":white_check_mark: **Success**! **" + attachment.getFileName() + "** was uploaded to the server!");
            }
            catch (Exception ex) {
                uploadStrings.add(":x: **Error**! Failed to upload **" + attachment.getFileName() + "** (" + ex.getMessage() + ")");
                ex.printStackTrace();
            }
        }

        for(String s : uploadStrings) {
            event.getChannel().sendMessage(s).queue();
        }

        if(successfulUploads == 0) return;

        event.getChannel().sendMessage(":gear: **Restarting Server**...").queue();

        try {
            Bukkit.spigot().restart();
        }
        catch (Exception ex) {
            event.getChannel().sendMessage(":x: **Error**! Could not restart the server! (" + ex.getMessage() + ")").queue();
            ex.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "upload";
    }

    @Override
    public String getDescription() {
        return "Upload a new plugin to the server.";
    }

    @Override
    public Permission getRequiredPermission() {
        return Permission.ADMINISTRATOR;
    }
}
