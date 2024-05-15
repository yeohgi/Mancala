package mancala;

import java.io.Serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Saver{

	public static void saveObject(final Serializable toSave, final String filename) throws IOException{

		try(ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(filename))){
			objectOut.writeObject(toSave);
		}
	}

	public static Serializable loadObject(final String filename) throws IOException{

		Serializable loadedMancalaGame;

		try(ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(filename))){
			try{
				loadedMancalaGame = (Serializable) objectIn.readObject();
			}catch(ClassNotFoundException e){
				throw new IOException();
			}
		}
		
		return loadedMancalaGame;
	}

}