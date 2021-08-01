import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

public class Main extends ListenerAdapter {
    public static JDA jda;
    public static String token;

    public static void main(String[] args) {
        token = System.getenv("PARROT_TOKEN");
        try {
            boolean result = startBot();
            if (!result) {
                System.exit(1);
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createSlashCommands();
    }


    public static boolean startBot() throws InterruptedException {
        JDABuilder preBuild = JDABuilder.createDefault(token);
        preBuild.setActivity(Activity.of(Activity.ActivityType.WATCHING, "for /help!"));
        try {
            jda = preBuild.build();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        jda.addEventListener(new Main());
        jda.awaitReady();
        return true;
    }

    public static void createSlashCommands() {
        /*
        Guild testGuild = jda.getGuildById("685606700929384489");
        assert testGuild != null;
        */

//        testGuild.updateCommands().addCommands(new CommandData("parrot", "Parrots what you say")).queue();

        jda.upsertCommand("parrot", "Parrots what you say")
                .addOption(OptionType.STRING, "message", "Message to parrot", true)
                .addOption(OptionType.CHANNEL, "channel", "Channel to send message to", false)
                .queue();

        jda.upsertCommand("help", "Help menu for Parrot bot").queue();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        switch (event.getName()) {
            case "parrot": {
                MessageChannel channelToSend;
                // If they didn't specify a channel to parrot to
                if (event.getOption("channel") == null) {
                    channelToSend = event.getChannel();
                } else {
                    channelToSend = event.getOption("channel").getAsMessageChannel();
                }
                try {
                    channelToSend.sendMessage(event.getOption("message").getAsString()).complete();
                } catch (Exception e) {
                    event.reply("Unable to parrot message. Do I have permissions to send messages in <#" + channelToSend.getId() + ">?").queue();
                    break;
                }
                event.reply("Message parroted.").setEphemeral(true).queue();
                break;
            }

            case "help": {
                HelpCommand.helpCommand(event.getUser());
                event.reply("Check your DMs for the help menu.").setEphemeral(true).queue();
            }
        }
    }
}
