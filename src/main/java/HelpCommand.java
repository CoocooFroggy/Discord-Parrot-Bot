import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class HelpCommand {
    public static void helpCommand(User user) {
        //Open DM
        user.openPrivateChannel().queue((dmChannel) -> {
            EmbedBuilder eb = new EmbedBuilder();

            //Make embed
            eb.setTitle("Help Menu");
            eb.setFooter("Parrot Bot", "https://cdn.discordapp.com/avatars/776521471392350288/43cc5b41517bea49d0d2551e3322cd84.png?size=256");
            eb.addField("`/parrot [Message*] [Channel]`",
                    "Type `/parrot` followed by your message that you want the bot to parrot. Optionally, include which channel you want the bot to send the message in.", true);
            eb.addField("`/help`",
                    "Brings up this help menu.", true);

            MessageEmbed finalEmbed = eb.build();

            //Send embed
            dmChannel.sendMessage(finalEmbed).queue();
        });


    }
}
