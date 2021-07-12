package de.kimuna.verifyaccount.listener;

import de.kimuna.verifyaccount.VerifyAccount;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String prefix;
        prefix = VerifyAccount.INSTANCE.prop.getProperty("prefix") != null ? VerifyAccount.INSTANCE.prop.getProperty("prefix") : "$$$";

        if (message.startsWith(prefix)) {
            String[] split = message.substring(prefix.length() + 1).split(" ");
            String[] args = Arrays.copyOfRange(split, 1, split.length);
            String command = split[0];

            if (!VerifyAccount.INSTANCE.getCommandManager().perform(command, args, event.getMember(), event.getChannel(), event.getMessage())) {
                event.getChannel().sendMessage("Unknown command!").queue();
            }
        }

    }
}

