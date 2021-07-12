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
            if (mentioned == member) {
                channel.sendMessage("Du kannst dich nicht selbst verifizieren!").queue(response -> response.delete().queueAfter(5, TimeUnit.SECONDS));
                return;
            }
            if (mentioned != null) {

                ResultSet alreadyExists = VerifyAccount.INSTANCE.database.onQuery("SELECT user_id FROM users WHERE user_id="+ member.getId());

                try {
                    boolean alreadyNext = alreadyExists.next();
                    alreadyExists.close();

                    if (alreadyNext) {
                        message.delete().queue();
                        channel.sendMessage("Du hast bereits einen Account verifiziert!").queue(result -> result.delete().queueAfter(5, TimeUnit.SECONDS));
                        return;
                    }

                    ResultSet mentionedAlreadyExists = VerifyAccount.INSTANCE.database.onQuery("SELECT second_id FROM users WHERE second_id=" + mentioned.getId());
                    boolean mentionedNext = mentionedAlreadyExists.next();
                    mentionedAlreadyExists.close();

                    if (mentionedNext) {
                        message.delete().queue();
                        channel.sendMessage("Der Account den du versuchst zu verifizieren ist bereits verifiziert!").queue(result -> result.delete().queueAfter(5, TimeUnit.SECONDS));
                        return;
                    }

                    if (!alreadyNext && !mentionedNext) {
                        VerifyAccount.INSTANCE.database.onUpdate("INSERT INTO users (user_id,second_id,verified) VALUES (" + member.getId() + "," + mentioned.getId() + ",0)");
                        channel.sendMessage(member.getAsMention() + " dein Zweit account " + mentioned.getAsMention() + " muss nun auf diese Nachricht antworten!").setActionRow(Button.success("verify", "Verifizieren")).queue();
                    } else {

                        ResultSet verifiedRS = VerifyAccount.INSTANCE.database.onQuery("SELECT verified FROM users WHERE user_id="+ member.getId());
                        if(verifiedRS.next()) {

                            int verified = verifiedRS.getInt("verified");
                            if (verified != 1) {
                                channel.sendMessage(member.getAsMention() + " dein Zweit account " + mentioned.getAsMention() + " muss nun auf diese Nachricht antworten!").setActionRow(Button.success("verify", "Verifizieren")).queue();
                            } else {
                                channel.sendMessage("Du hast bereits einen Account verifiziert!").queue(result -> result.delete().queueAfter(5, TimeUnit.SECONDS));
                            }
                        }
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }

    }
}
