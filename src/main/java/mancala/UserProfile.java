package mancala;

import java.io.Serializable;

public class UserProfile implements Serializable{

	private static final long serialVersionUID = -2905301491250072567L;

	private String username;

	private int gamesPlayed;

	private int kalahPlayed;

	private int kalahWon;

	private int ayoPlayed;

	private int ayoWon;

	//Winrate

	public UserProfile(){
		username = "Unknown Player";
		gamesPlayed = 0;
		kalahPlayed = 0;
		kalahWon = 0;
		ayoPlayed = 0;
		ayoWon = 0;
	}

	public UserProfile(final String name){
		username = name;
		gamesPlayed = 0;
		kalahPlayed = 0;
		kalahWon = 0;
		ayoPlayed = 0;
		ayoWon = 0;
	}

	public int getGamesPlayed(){
		return gamesPlayed;
	}

	public String getKalahWGP(){
		return kalahWon + " / " + kalahPlayed;
	}

	public String getAyoWGP(){
		return ayoWon + " / " + ayoPlayed;
	}

	public String getName(){
		return username;
	}

	public void setName(final String name){
		username = name;
	}

	//Adding Stats
	public void addGamePlayed(){
		gamesPlayed++;
	}

	public void addKalahPlayed(){
		kalahPlayed++;
		addGamePlayed();
	}

	public void addAyoPlayed(){
		ayoPlayed++;
		addGamePlayed();
	}

	public void addKalahWon(){
		kalahWon++;
	}

	public void addAyoWon(){
		ayoWon++;
	}

}