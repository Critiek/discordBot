import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.handle.obj.IMessage;

/**Rough admin command for forcing the discord bot to disconnect from all KAG servers it is supposed to be connected to. Must be used in command channel. 
 * @author cameron
 * @see GatherObject#disconnectKAGServers()
 */
public class CommandDisconnect implements CommandExecutor
{
	/**The function that is called when the command is used
	 * @param message
	 * @param args
	 * @see https://github.com/BtoBastian/sdcf4j
	 * @see #CommandDisconnect
	 */
	@Command(aliases = {"!disconnect", "!disconn", "!discon"}, description = "Admin only - disconnect from the kag servers")
	public void onCommand(IMessage message, String[] args)
	{
		GatherObject gather = DiscordBot.getGatherObjectForChannel(message.getChannel());
		if(gather==null) return;

		if(!gather.isAdmin(message.getAuthor()))
		{
			DiscordBot.sendMessage(gather.getCommandChannel(), "Only **admins** can do that" + " "+message.getAuthor().getDisplayName(message.getGuild())+"!");
			return;
		
		}

		gather.disconnectKAGServers();
		return;
	}
}