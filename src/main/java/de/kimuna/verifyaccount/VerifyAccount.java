package de.kimuna.verifyaccount;

import de.kimuna.verifyaccount.handler.CommandHandler;
import de.kimuna.verifyaccount.listener.CommandListener;
import de.kimuna.verifyaccount.listener.VerifyButtonListener;
import de.kimuna.verifyaccount.sql.SQLite;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class VerifyAccount {


    public static VerifyAccount INSTANCE;

    public ShardManager shardManager;
    public Properties prop = new Properties();
    public SQLite database;
    private CommandHandler commandManager;

    public VerifyAccount() throws LoginException, IOException {
        INSTANCE = this;
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        prop.load(new FileInputStream("src/main/resources/config.properties"));
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(dotenv.get("TOKEN"));
        builder.setActivity(Activity.playing("$$$verify"));

        builder.setStatus(OnlineStatus.ONLINE);
        this.commandManager = new CommandHandler();
        builder.addEventListeners(new CommandListener());
        builder.addEventListeners(new VerifyButtonListener());
        database = new SQLite();
        shardManager = builder.build();
        System.out.println("Bot online.");

        shutdown();


    }

    public static void main(String[] args) throws LoginException, IOException {
        new VerifyAccount();
    }

    public void shutdown() {
        new Thread(() -> {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        if (shardManager != null) {
                            shardManager.setStatus(OnlineStatus.OFFLINE);
                            database.disconnect();
                            shardManager.shutdown();
                            System.out.println("Bot offline.");
                        }

                        break;
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public CommandHandler getCommandManager() {
        return commandManager;
    }


}
