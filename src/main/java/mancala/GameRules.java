package mancala;

import java.io.Serializable;

/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable{

    private static final long serialVersionUID = -2466711140133375603L;

    private final MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)

    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        gameBoard = new MancalaDataStructure();
        resetBoard();
    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(final int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    boolean isSideEmpty(final int pitNum) throws PitNotFoundException{
        // This method can be implemented in the abstract class.

        if(pitNum < 1 || pitNum > 12){
            throw new PitNotFoundException();
        }

        int startAtPit;
        int endAtPit;
        boolean tOrF = true;
        
        if(pitNum < 7){
            startAtPit = 1;
        }else{
            startAtPit = 7;
        }

        endAtPit = startAtPit + 6;

        for(int i = startAtPit; i < endAtPit; i++){
            if(getDataStructure().getNumStones(i) != 0){
                tOrF = false;
            }
        }

        return tOrF;
    }

    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    public void setPlayer(final int playerNum) {
        currentPlayer = playerNum;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    protected abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    protected abstract int captureStones(int stoppingPoint);

    /**
     * Sets player 1s turn to true or false.
     *
     * @param tOrF Sets player 1s turn to true or false.
     */
    protected abstract void setP1sTurn(boolean tOrF);

    /**
     * Returns status of player ones turn.
     *
     * @return True if player ones turn, false otherwise.
     */
    protected abstract boolean isP1sTurn();

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(final Player one, final Player two) {
        // this method can be implemented in the abstract class.

        final Store store1 = new Store();
        final Store store2 = new Store();

        getDataStructure().setStore(store1, 1);
        getDataStructure().setStore(store2, 2);
        one.setStore(store1);
        two.setStore(store2);
        store1.setOwner(one);
        store2.setOwner(two);

        /* make a new store in this method, set the owner
         then use the setStore(store,playerNum) method of the data structure*/
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    @Override
    public String toString() {
        return "PLEASE HELP ME";
    }
}
