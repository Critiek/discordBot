
public class StatsObject {
	StatsObject()
	{
		kagname = "";
		discordid = 0L;
		gamesPlayed = 0;
		wins = 0;
		losses = 0;
		draws = 0;
		desertions = 0;
	}
	String kagname;
	long discordid;
	int gamesPlayed;
	int wins;
	int losses;
	int draws;
	int desertions;
	
	@Override
	public String toString()
	{
		float winRate = ((float)wins/((float)wins+(float)losses+(float)desertions))*100;
		return "Games Played: "+gamesPlayed+" Win Rate: "+String.format("%.2f", winRate)+"% Desertions: "+desertions;
	}
}
