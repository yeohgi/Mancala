package mancala;

public class PitNotFoundException extends Exception{

    private static final long serialVersionUID = 7971644609546703985L;
    
    public PitNotFoundException(){
        super("Pit Not Found");
    }

    public PitNotFoundException(final String meassage){
        super(meassage);
    }
}