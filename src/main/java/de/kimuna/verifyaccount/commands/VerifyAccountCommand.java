package de.kimuna.verifyaccount.commands;

import de.kimuna.verifyaccount.VerifyAccount;
import de.kimuna.verifyaccount.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.Button;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VerifyAccountCommand implements ServerCommand {
    @Override
    public void performCommand(String[] args, Member member, TextChannel channel, Message message) {
        if (message.getMentionedMembers().size() >= 1) {
            Member mentioned = message.getMentionedMembers().get(0);
            if (mentioned != null) {
                ResultSet alreadyExists = VerifyAccount.INSTANCE.database.onQuery("SELECT second_id,verified FROM users WHERE user_id=" + member.getId());

                try {
                    if (!alreadyExists.next()) {
                        VerifyAccount.INSTANCE.database.onUpdate("INSERT INTO users (user_id,second_id,verified) VALUES ("+member.getId()+","+mentioned.getId()+",0)");
                    } else {
                        int verified = alreadyExists.getInt("verified");
                        if(verified != 1) {
                            channel.sendMessage(member.getAsMention() + " dein Zweit account " + mentioned.getAsMention() + " muss nun auf diese Nachricht antworten!").setActionRow(Button.success("verify","Verifizieren")).queue();
                        } else {
                            message.reply("Tut mir leid aber es sieht so aus als wenn du bereits einen Zweit account hast").queue();
                        }
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }

    }
}
