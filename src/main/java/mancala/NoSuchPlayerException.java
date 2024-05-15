package mancala;

public class NoSuchPlayerException extends Exception{

    private static final long serialVersionUID = 7791337417655979794L;

    public NoSuchPlayerException(){
        super("No Such Player Exists");
    }

    public NoSuchPlayerException(final String meassage){
        super(meassage);
    }
}