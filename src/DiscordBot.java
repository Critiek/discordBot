import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.Discord4JHandler;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class DiscordBot
{
	
	public IDiscordClient client; // The instance of the discord client.
	
	public static DiscordBot bot;
	
	public static GatherObject gatherInfo;
	
	
	public void startBot(String token)
	{
		ClientBuilder builder = new ClientBuilder(); // Creates a new client builder instance
		
		builder.withToken(token); // Sets the bot token for the client
		
		try {
			client = builder.login(); // Builds the IDiscordClient instance and logs it in
			System.out.println("logged in");
		} catch (DiscordException e) { // Error occurred logging in
			System.err.println("Error occurred while logging in!");
			e.printStackTrace();
		}

		CommandHandler cmdHandler = new Discord4JHandler(client);
		
		//add all the commands
		cmdHandler.registerCommand(new CommandPing());
		cmdHandler.registerCommand(new CommandAdd());
		cmdHandler.registerCommand(new CommandRem());
	}
	
	
	public static void main(String[] args)
	{
		gatherInfo = new GatherObject();
		bot = new DiscordBot();
		bot.startBot(args[0]);
	}
	
	public void setPlayingText(String newText)
	{
		client.changePlayingText(newText);
	}
}