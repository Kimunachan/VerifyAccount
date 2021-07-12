package de.kimuna.verifyaccount.listener;

import de.kimuna.verifyaccount.VerifyAccount;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VerifyButtonListener extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        Member m = event.getInteraction().getMember();

        if (event.getComponentId().equals("verify")) {
            ResultSet rs = VerifyAccount.INSTANCE.database.onQuery("SELECT * FROM users WHERE second_id=" + m.getId());
            try {
                if (!rs.next()) {
                    event.deferReply().setContent("Tut mir leid aber irgendwas ist schief gelaufen!").setEphemeral(true).queue();

                } else {
                    if (rs.getInt("verified") != 1 && !rs.getString("user_id").equals(m.getId())) {
                        VerifyAccount.INSTANCE.database.onUpdate("UPDATE users SET verified=1 WHERE second_id=" + m.getId());
                        event.getMessage().delete().queue();
                    } else {
                        event.deferReply().setContent("Tut mir leid, dieser Account ist bereits verifiziert!").setEphemeral(true).queue();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
