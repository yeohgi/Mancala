package mancala;

import java.io.Serializable;

public class Store implements Serializable, Countable{

       private static final long serialVersionUID = -8731325716248438973L;

       private int numStones;

       private Player player;

       public Store(){
              numStones = 0;
              //player = null;
              return;
       }

       @Override
       public void addStone(){ //1/5
              numStones++;
       }

       @Override
       public void addStones(final int amount){ //1/5
              numStones += amount;

       }

       @Override
       public int removeStones(){ //2/5
              
              final int saveNumStones = getStoneCount();

              numStones = 0;

              return saveNumStones;
       }

       public void setOwner(final Player owner){ //3/5
              player = owner;
       }

       public Player getOwner(){ //4/5
              return player;
       }

       @Override
       public int getStoneCount(){ //5/5
              return numStones;
       }

       @Override
         public String toString() {
           return "Stones in store: " + getStoneCount();
         }

}