package mancala;

import java.io.Serializable;

public class Pit implements Serializable, Countable{

    private static final long serialVersionUID = 3908297928557001422L;

    private int numStones;

    public Pit(){
        numStones = 0;
        return;
    }

    public Pit(final int stones){
        numStones = stones;
        return;
    }

    @Override
    public int getStoneCount(){
        return numStones;
    }

    @Override
    public void addStone(){
        numStones++;
    }

    @Override
    public void addStones(final int stones){
        numStones += stones;
    }

    @Override
    public int removeStones(){
        final int saveNumStones = getStoneCount();
        numStones = 0;
        return saveNumStones;
    }

    public void setStones(final int num){
        numStones = num;
    }

    @Override
      public String toString() {
        return "Stones in pit: " + getStoneCount();
      }
}