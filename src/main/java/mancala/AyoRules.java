package mancala;

import java.util.ArrayList;

public class AyoRules extends GameRules{

    private static final long serialVersionUID = -1836137927285591137L;

    private boolean p1sTurn;

    private boolean orignalP1IsMoving;

    private int firstStartPit;

    //private int count;

    public AyoRules(){
        super();
        p1sTurn = true;
        orignalP1IsMoving = true;
        firstStartPit = -1;
        //count = 0;
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
        return getDataStructure().getStoreCount(playerNum + 1);
    }

    @Override
	public int distributeStones(final int startPit){
		final int convStartPoint = convertIndex(startPit);
        //count++;

        boolean p1IsMoving;
        int movingPlayer;

        if(firstStartPit == -1){
            firstStartPit = startPit;
        }
    
        if(firstStartPit < 7){
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

        distributeLoop((saveNumStones / 12), movingPlayer, firstStartPit); //Check how many times it loops the board

        saveNumStones -= ((saveNumStones / 12) * 12); //Update stones in our hand

        if(saveNumStones == 0){
            distributeStones(startPit - 1);
        }

        final int distanceFromStore = distanceFromStore(startPit, p1IsMoving); //COULD BE NEGATIVE

        final boolean canCapture = canCapture(distanceFromStore, saveNumStones, p1IsMoving);

        if(distanceFromStore <= saveNumStones - 1){
            getDataStructure().addToStore(movingPlayer + 1, 1);
            saveNumStones--;
        }

        //int dupSaveNumStones = saveNumStones;
        for(int i = 1; i < saveNumStones + 1; i++){

            if((((convStartPoint + i) % 12) + 1) == firstStartPit){ //Do nothing if we land on start pit
                saveNumStones++;
            }else if(canCapture && i == saveNumStones && getNumStones((((convStartPoint + i) % 12) + 1)) == 0){ //last drop + empty + can capture

                final int stonesTaken = captureStones(((convStartPoint + i) % 12) + 1);
                
                addStones(((convStartPoint + i) % 12) + 1,1);

                if(stonesTaken > 0){
                    stonesDist += stonesTaken;
                    getDataStructure().addToStore(movingPlayer + 1, stonesTaken);
                }

            }else if(canCapture && i == saveNumStones && getNumStones(((convStartPoint + i) % 12) + 1) != 0){  //Land on not empty + last drop + cancapture
                //if(count < 5){
                    addStones(((convStartPoint + i) % 12) + 1, 1);
                    stonesDist += distributeStones(((convStartPoint + i) % 12) + 1);
                //}
            }else{  //Normal drop
                addStones(((convStartPoint + i) % 12) + 1, 1);
            }
        }

        firstStartPit = -1;

        return stonesDist;
	}

    @Override
    public int captureStones(final int stoppingPoint){

        final int convStoppingPoint = convertIndex(stoppingPoint);

        int stonesTaken = 0;

        if(((orignalP1IsMoving) && stoppingPoint < 7) || ((!orignalP1IsMoving) && stoppingPoint > 6)){
            stonesTaken = removeStones(12 - convStoppingPoint);
        }

        return stonesTaken;
    }

    @Override
	public boolean isP1sTurn(){
        return p1sTurn;
    }

    public int distanceFromStore(final int startPit, final boolean p1IsMoving){

        int distanceFromStore = getMaxPit(p1IsMoving) - startPit; //COULD BE NEGATIVE

        if(distanceFromStore < 0){
            distanceFromStore = 12 + distanceFromStore;
        }

        int distanceFromStart = firstStartPit - startPit;

        if(distanceFromStart <= 0){
            distanceFromStart = 12 + distanceFromStart;
        }

        if(distanceFromStart <= distanceFromStore){
            distanceFromStore--;
        }

        return distanceFromStore;
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

    public void distributeLoop(final int loops, final int movingPlayer, final int startPit){
        for(int i = 0; i < loops; i++){
            for(int j = 1; j < 13; j++){
                if(j != startPit){
                    addStones(j,1);
                }
            }
            getDataStructure().addToStore(movingPlayer + 1, 1);
        }
    }    

    public boolean canCapture(final int distance, final int stones, final boolean p1IsMovingBool){

        boolean tOrF = true;

        if(distance == stones - 1){
            tOrF = false;
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