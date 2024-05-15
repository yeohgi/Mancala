package mancala;

import java.io.Serializable;

public class Player implements Serializable{

    private static final long serialVersionUID = -1180726359362985752L;

    private String playerName;

    private Store playerStore;

    private UserProfile profile;

    public Player(){
        profile = new UserProfile();
        setNameWithProfile();
        return;
    }

    public Player(final String name){
        profile = new UserProfile();
        playerName = name;
        return;
    }

    public String getName(){
        return playerName;
    }

    public Store getStore(){
        return playerStore;
    }

    public int getStoreCount(){
        return playerStore.getStoneCount();
    }

    public void setName(final String name){
        playerName = name;
    }

    public void setProfileName(final String name){
        profile.setName(name);
        setNameWithProfile();
    }


    public void setStore(final Store aStore){
        playerStore = aStore;
    }

    public void setProfile(final UserProfile userProfile){
        profile = userProfile;
        setNameWithProfile();
    }

    public UserProfile getProfile(){
        return profile;
    }

    public void setNameWithProfile(){
        playerName = profile.getName();
    }

    public String getGamesPlayed(){
        return "" + profile.getGamesPlayed();
    }

    public String getKalahWGP(){
        return profile.getKalahWGP();
    }

    public String getAyoWGP(){
        return profile.getAyoWGP();
    }

    public void gameWasPlayed(final String gameMode){

        final String defaultMode = "kalah";

        if(gameMode.equals(defaultMode)){
            profile.addKalahPlayed();
        }else{
            profile.addAyoPlayed();
        }
    }

    public void gameWasWon(final String gameMode){

        final String defaultMode = "kalah";
        
        if(gameMode.equals(defaultMode)){
            profile.addKalahWon();
        }else{
            profile.addAyoWon();
        }
    }

    @Override
      public String toString() {
        return getName();
      }
}