package mancala;

import java.io.Serializable;

import java.util.ArrayList;

public class KalahRules extends GameRules{

    private static final long serialVersionUID = -8882328801699670522L;

    private boolean p1sTurn;

    private boolean orignalP1IsMoving;

    public KalahRules(){
        super();
        p1sTurn = true;
        orignalP1IsMoving = true;
    }

    @Override
	public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException{

		final int convertedStartPit = convertIndex(startPit);

        if(((p1sTurn) && (convertedStartPit > 5 || convertedStartPit < 0)) 
            || ((!p1sTurn) && (convertedStartPit > 11 || convertedStartPit < 6))){
            throw new InvalidMoveException();
        }

        if(getNumStones(startPit) == 0){
            throw new InvalidMoveException();
        }

        int movingPlayer;

        if(p1sTurn){
            movingPlayer = 0;
        }else{
            movingPlayer = 1;
        }

        final int storeBefore = mSHGetTotalStones(movingPlayer);
        
        changeTurn();

        distributeStones(startPit);

        final int storeAfter = mSHGetTotalStones(movingPlayer);

        return storeAfter - storeBefore;
	}

	public int mSHGetTotalStones(final int playerNum){
        //return stores.get(playerNum).getTotalStones();
        return getDataStructure().getStoreCount(playerNum + 1);
    }

    @Override
	public int distributeStones(final int startPit){
		final int convStartPoint = convertIndex(startPit);

        boolean p1IsMoving;
        int movingPlayer;
        if(startPit < 7){
            p1IsMoving = true;
            orignalP1IsMoving =  true;
            movingPlayer = 0;
        }else{
            p1IsMoving = false;
            orignalP1IsMoving = false;
            movingPlayer = 1;
        }

        int saveNumStones = removeStones(startPit);
        int stonesDist = saveNumStones;

        final int maxPit = getMaxPit(p1IsMoving);

        distributeLoop((saveNumStones / 13), movingPlayer); //Check how many times it loops the board

        saveNumStones -= ((saveNumStones / 13) * 13); //Update stones in our hand

        final int distanceFromStore = maxPit-startPit;

        final boolean canCapture = anotherTurn(distanceFromStore, saveNumStones, p1IsMoving);

        if(distanceFromStore <= saveNumStones-1){
            getDataStructure().addToStore(movingPlayer + 1, 1);
            saveNumStones--;
        }

        for(int i = 1; i < saveNumStones + 1; i++){

            if(canCapture && i == saveNumStones && getNumStones((((convStartPoint + i) % 12) + 1)) == 0){

                final int stonesTaken = captureStones(((convStartPoint + i) % 12) + 1);
                
                if(stonesTaken == 0){
                    addStones(((convStartPoint + i) % 12) + 1,1);
                }else{
                    stonesDist += stonesTaken;
                    getDataStructure().addToStore(movingPlayer + 1, stonesTaken);
                }

            }else{
                addStones(((convStartPoint + i) % 12) + 1,1);
            }
        }
        return stonesDist;
	}

    @Override
	public int captureStones(final int stoppingPoint){

		final int convStoppingPoint = convertIndex(stoppingPoint);

		int stonesTaken = 0;

		if(((orignalP1IsMoving) && stoppingPoint < 7) || ((!orignalP1IsMoving) && stoppingPoint > 6)){
		    stonesTaken = removeStones(12 - convStoppingPoint);
            if(stonesTaken > 0){
                removeStones(stoppingPoint);
                stonesTaken++;
            }
		}

		return stonesTaken;
	}

    @Override
	public boolean isP1sTurn(){
        return p1sTurn;
    }

    @Override
    public void setP1sTurn(final boolean tOrF){
        p1sTurn = tOrF;

    }

    public void addStones(final int pit, final int numToAdd){
        getDataStructure().addStones(pit, numToAdd);
    }

    public int removeStones(final int pit){
        return getDataStructure().removeStones(pit);
    }

    public void changeTurn(){
        if(p1sTurn){
            setP1sTurn(false);
        }else{
            setP1sTurn(true);
        }

    }

    public int getMaxPit(final boolean p1IsMoving){
        int maxPit;
        if (p1IsMoving){
            maxPit = 6;
        }else{
            maxPit = 12;
        }
        return maxPit;
    }

    public void distributeLoop(final int loops, final int movingPlayer){
        for(int i = 0; i < loops; i++){
            for(int j = 1; j < 13; j++){
                addStones(j,1);
            }
            getDataStructure().addToStore(movingPlayer + 1, 1);
        }
    }    

    public boolean anotherTurn(final int distance, final int stones, final boolean p1IsMovingBool){

        boolean tOrF = true;

        if(distance == stones - 1){
            tOrF = false;
            if(p1IsMovingBool){
                setP1sTurn(true);
            }else{
                setP1sTurn(false);
            }
        }

        return tOrF;
    }

    public void setOriginalTurn(final boolean tOrF){
        orignalP1IsMoving = tOrF;
    }

    public int convertIndex(final int pitNum){
        return pitNum - 1;
    }
}