package de.kimuna.verifyaccount.commands;

import de.kimuna.verifyaccount.VerifyAccount;
import de.kimuna.verifyaccount.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class ListVerifiedAccountsCommand implements ServerCommand {
    @Override
    public void performCommand(String[] args, Member member, TextChannel channel, Message message) {
        if (args.length == 0) {
            ResultSet rs = VerifyAccount.INSTANCE.database.onQuery("SELECT user_id,second_id FROM users");
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("Verified Accounts");
            eb.setColor(0xff6600);
            eb.setTimestamp(Instant.now());
            eb.setDescription("");

            try {
                while (rs.next()) {
                    String user_id = rs.getString("user_id");
                    Member sender = message.getGuild().retrieveMemberById(user_id).complete();

                    String second_id = rs.getString("second_id");
                    Member target = message.getGuild().retrieveMemberById(second_id).complete();
                    eb.appendDescription("\n**" + sender.getEffectiveName() + "** -> **" + target.getEffectiveName() + "**");
                }
                channel.sendMessage(eb.build()).queue();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
