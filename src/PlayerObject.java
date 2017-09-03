import sx.blah.discord.handle.obj.IUser;

public class PlayerObject
{
	
	private IUser discordUserInfo;
	private boolean captainsVote;
	private boolean inQueue;
	
	

	PlayerObject(IUser user, boolean capVote)
	{
		setDiscordUserInfo(user);
		setCaptainsVote(capVote);
	}

	public boolean equals(Object player)
	{
		if (player == null)
		{
			return false;
		}
		if(this.discordUserInfo.equals(((PlayerObject)player).discordUserInfo))
		{
			return true;
		}
		return false;
	}
	
	public IUser getDiscordUserInfo() {
		return discordUserInfo;
	}

	public void setDiscordUserInfo(IUser author) {
		this.discordUserInfo = author;
	}

	public boolean isCaptainsVote() {
		return captainsVote;
	}

	public void setCaptainsVote(boolean captainsVote) {
		this.captainsVote = captainsVote;
	}
	
	public boolean isInQueue() {
		return inQueue;
	}

	public void setInQueue(boolean inQueue) {
		this.inQueue = inQueue;
	}
	
	public String toString()
	{
		return discordUserInfo.getName()+"#"+discordUserInfo.getDiscriminator();
	}
}