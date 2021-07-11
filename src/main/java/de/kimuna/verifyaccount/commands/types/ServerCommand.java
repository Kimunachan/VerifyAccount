package de.kimuna.verifyaccount.commands.types;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ServerCommand {

    void performCommand(String[] args,Member member, TextChannel channel, Message message);
}

