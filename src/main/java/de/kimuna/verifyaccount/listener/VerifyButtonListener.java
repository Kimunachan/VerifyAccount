package de.kimuna.verifyaccount.listener;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VerifyButtonListener extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        Member m = event.getInteraction().getMember();
        if (event.getComponentId().equals("attack")) {

            event.deferEdit().queue();
        }
    }

}
