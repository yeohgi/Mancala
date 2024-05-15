package mancala;

import java.util.ArrayList;

import java.io.Serializable;

public class MancalaGame implements Serializable{

    private static final long serialVersionUID = -9095764425185717918L;

    private GameRules board;

    private ArrayList<Player> players;

    private int currentPlayer;

    public MancalaGame(){

        setupMancalaGame(new KalahRules());
    }

    public MancalaGame(final GameRules rules){

        setupMancalaGame(rules);
    }

    protected void setupMancalaGame(final GameRules rules){

        setBoard(rules);
        players = new ArrayList<>();
        setPlayers(new Player(), new Player());
        startNewGame();
    }

    public GameRules getBoard(){
        return board;
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public Player getPlayer(final int playerNum){
        return players.get(playerNum - 1);
    }

    public void overwritePlayer(final Player loadedPlayer, final int playerNum){
        players.get(playerNum - 1).setProfile(loadedPlayer.getProfile());
    }

    public String getPlayerName(final int playerNum){
        final Player player = players.get(playerNum - 1);
        return player.getName();
    }

    public int getNumStones(final int pitNum){
        return board.getDataStructure().getNumStones(pitNum); 
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }

    public int getStoreCount(final Player player) throws NoSuchPlayerException{

        //if(player != players.get(0) && player != players.get(1)){
        if(!player.equals(players.get(0)) && !player.equals(players.get(1))){
            throw new NoSuchPlayerException();
        }

        return player.getStoreCount();
    }

    public Player getWinner() throws GameNotOverException{

        if(!isGameOver()){
            throw new GameNotOverException();
        }

        Player winningPlayer;

        for(int i = 1; i < 7; i++){
            transferPlayerStonesPitToStores(1, i);
        }
        
        for(int i = 7; i < 13; i++){
            transferPlayerStonesPitToStores(2, i);
        }

        if(playerTotalStones(1) > playerTotalStones(2)){
            winningPlayer = players.get(0);
        }else{
            winningPlayer = players.get(1);
        }

        if(board instanceof KalahRules){
            winningPlayer.gameWasWon("kalah");
        }else{
            winningPlayer.gameWasWon("ayo");
        }

        return winningPlayer;
    }

    public boolean isGameOver(){

        boolean gameOver;

        try{
            if((board.isSideEmpty(1)) || (board.isSideEmpty(7))){
                gameOver = true;
            }else{
                gameOver = false;
            }   
        }catch(PitNotFoundException e){
            gameOver = false;
        }


        return gameOver;
    }

    public int move(final int startPit) throws InvalidMoveException{

        try{
            board.moveStones(startPit, currentPlayer);
        }catch(InvalidMoveException e){
            throw new InvalidMoveException();
        }

        int pSideStones = 0;

        if(startPit < 7){
            for(int i = 1; i < 7; i++){
                pSideStones += getStoreCountPlus(i);
            }
        }else{
            for(int i = 7; i < 13; i++){
                pSideStones += getStoreCountPlus(i);
            }
        }

        return pSideStones;
    }

    public void setBoard(final GameRules theBoard){
        board = theBoard;
    }

    public void setCurrentPlayer(final int player){
        currentPlayer = player;
    }

    public void setPlayers(final Player onePlayer, final Player twoPlayer){
        players.add(onePlayer);
        players.add(twoPlayer);
        board.registerPlayers(onePlayer, twoPlayer);
        setCurrentPlayer(1);
    }

    public void startNewGame(){
        board.resetBoard();
        board.setP1sTurn(true);
    }

    public boolean isP1sTurn(){
        return board.isP1sTurn();
    }

    public int playerTotalStones(final int playerNum){
        return board.getDataStructure().getStoreCount(playerNum);
    }

    public void transferPlayerStonesPitToStores(final int playerNum, final int index){
        //board.getStores().get(playerNum).addStones(board.getPits().get(index).removeStones());
        board.getDataStructure().addToStore(playerNum, board.getDataStructure().removeStones(index));
    }

    public int getStoreCountPlus(final int index){
        return board.getDataStructure().getNumStones(index);
    }

    public int getPlayerStoreCount(final int index){
        return board.getDataStructure().getStoreCount(index);
    }

    public void setPlayerName(final String name, final int playerNum){
        getPlayer(playerNum).setProfileName(name);
    }

    @Override
      public String toString() {
          return board.toString();
      }
}