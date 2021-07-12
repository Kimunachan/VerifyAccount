package de.kimuna.verifyaccount.handler;

import de.kimuna.verifyaccount.commands.HelpCommand;
import de.kimuna.verifyaccount.commands.ListVerifiedAccountsCommand;
import de.kimuna.verifyaccount.commands.VerifyAccountCommand;
import de.kimuna.verifyaccount.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.ConcurrentHashMap;

public class CommandHandler {

    public ConcurrentHashMap<String, ServerCommand> commands;

    public CommandHandler() {
        this.commands = new ConcurrentHashMap<>();
        this.commands.put("verify", new VerifyAccountCommand());
        this.commands.put("listverified", new ListVerifiedAccountsCommand());
        this.commands.put("help", new HelpCommand());
    }

    public boolean perform(String command, String[] args, Member member, TextChannel channel, Message message) {

        ServerCommand cmd;

        if ((cmd = this.commands.get(command.toLowerCase())) != null) {
            cmd.performCommand(args, member, channel, message);

            System.out.println();
            return true;
        }

        return false;
    }
}
