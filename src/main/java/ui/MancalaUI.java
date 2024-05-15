package ui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

import java.util.ArrayList;

import mancala.MancalaGame;
import mancala.GameRules;
import mancala.KalahRules;
import mancala.AyoRules;
import mancala.Player;
import mancala.Saver;

import mancala.InvalidMoveException;
import mancala.GameNotOverException;
import java.io.IOException;

public class MancalaUI extends JFrame{

	private JPanel gameContainer;
	private JPanel playerStatsPanel;

	private JButton newGameButton;

	private JLabel whoVsWho;
	private JLabel p1Name;
	private JLabel p1Played;
	private JLabel p1KalahStats;
	private JLabel p1AyoStats;
	private JLabel p2Name;
	private JLabel p2Played;
	private JLabel p2KalahStats;
	private JLabel p2AyoStats;
	private JLabel invalidMoveMeassage;
	private JLabel displayTurn;

	private JMenuItem saveP1;
	private JMenuItem saveP2;
	private JMenuItem loadP1;
	private JMenuItem loadP2;
	private JMenuItem toggleGameMode;
	private JMenuItem changeP1Name;
	private JMenuItem changeP2Name;
	private JMenuItem stats;

	private PositionAwareButton[][] pits;
	private PositionAwareButton[] stores = new PositionAwareButton[2];

	private JMenuBar menuBar;

	private MancalaGame game;
	
	public MancalaUI(String title) {
 		super();

 		JOptionPane.showMessageDialog(null, "Welcome to Mancala");
 		GameRules rules = getGameModeWithDialog();
 		game = new MancalaGame(rules);
 		
 		basicSetUp(title);

 		setupGameContainer();

 		add(whoVsWho(), BorderLayout.NORTH);
 		add(playerStats(), BorderLayout.WEST);
 		add(gameContainer, BorderLayout.CENTER);
 		add(makeDisplayTurnPanel(), BorderLayout.SOUTH);
 	
 		makeMenu();
 		setJMenuBar(menuBar);

 		setupPlayers();

 		pack();

 		updateView();
 	}

	private void basicSetUp(String title){
 		this.setTitle(title);
 		gameContainer = new JPanel();
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		setLayout(new BorderLayout());
 	}

 	private GameRules getGameModeWithDialog(){

 		GameRules rules;

 		Object[] options = {"Kalah", "Ayo"};

 		JOptionPane optionPane = new JOptionPane("Select Ruleset", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options);
 		JDialog dialog = optionPane.createDialog(null, "Ruleset");
 		dialog.setVisible(true);

 		Object selectedValue = optionPane.getValue();

 		if(selectedValue == null){
 			selectedValue = options[0];
 		}

 		if(selectedValue.equals(options[0])){
 			rules = new KalahRules();
 		}else{
 			rules = new AyoRules();
 		}

 		return rules;
 	}

 	private boolean newOrLoad(){

 		boolean tOrF;

 		Object[] options = {"New", "Load"};

 		JOptionPane optionPane = new JOptionPane("Would you like create a new player or load an existing profile?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options);
 		JDialog dialog = optionPane.createDialog(null, "Load Players");
 		dialog.setVisible(true);

 		Object selectedValue = optionPane.getValue();

 		if(selectedValue == null){
 			selectedValue = options[0];
 		}

 		if(selectedValue.equals(options[0])){
 			tOrF = false;
 		}else{
 			tOrF = true;
 		}

 		return tOrF;
 	}

 	private void setupPlayers(){
 		if(!newOrLoad()){
 			game.setPlayerName(JOptionPane.showInputDialog("Enter Player 1's Name: "), 1);
 			game.setPlayerName(JOptionPane.showInputDialog("Enter Player 2's Name: "), 2);

 			if(game.getPlayerName(1) == null){
 				game.setPlayerName("Player 1", 1);
 			}

 			if(game.getPlayerName(2) == null){
 				game.setPlayerName("Player 2", 2);
 			}

 		}else{
 			loadPlayer(1);
 			loadPlayer(2);
 		}
 	}

	private JPanel whoVsWho() {
		JPanel whoVsWhoPanel = new JPanel();
		whoVsWho = new JLabel(game.getPlayerName(1) + " Vs " + game.getPlayerName(2));
		whoVsWhoPanel.add(whoVsWho);
	 	return whoVsWhoPanel;
	}

	private JPanel playerStats(){
		playerStatsPanel = new JPanel();
		JPanel player1StatsPanel = new JPanel();
		JPanel player2StatsPanel = new JPanel();

		playerStatsPanel.setLayout(new BoxLayout(playerStatsPanel, BoxLayout.Y_AXIS));
		player1StatsPanel.setLayout(new BoxLayout(player1StatsPanel, BoxLayout.Y_AXIS));
		player2StatsPanel.setLayout(new BoxLayout(player2StatsPanel, BoxLayout.Y_AXIS));

		//Player 1
		p1Name = new JLabel();
		p1Played = new JLabel();
		p1KalahStats = new JLabel();
		p1AyoStats = new JLabel();

		JLabel space = new JLabel(" ");

		//Player 2
		p2Name = new JLabel();
		p2Played = new JLabel();
		p2KalahStats = new JLabel();
		p2AyoStats = new JLabel();

		setPlayerStatsText();

		player1StatsPanel.add(p1Name);
		player1StatsPanel.add(p1Played);
		player1StatsPanel.add(p1KalahStats);
		player1StatsPanel.add(p1AyoStats);
		player1StatsPanel.add(space);
		player2StatsPanel.add(p2Name);
		player2StatsPanel.add(p2Played);
		player2StatsPanel.add(p2KalahStats);
		player2StatsPanel.add(p2AyoStats);

		playerStatsPanel.add(player1StatsPanel);
		playerStatsPanel.add(player2StatsPanel);

	 	return playerStatsPanel;
	}

	private void setPlayerStatsText(){

		whoVsWho.setText(game.getPlayerName(1) + " Vs " + game.getPlayerName(2));
		p1Name.setText(game.getPlayerName(1));
		p1Played.setText("Games Played: " + game.getPlayer(1).getGamesPlayed());
		p1KalahStats.setText("Kalah Wins/Played: " + game.getPlayer(1).getKalahWGP());
		p1AyoStats.setText("Ayo Wins/Played: " + game.getPlayer(1).getAyoWGP());
		p2Name.setText(game.getPlayerName(2));
		p2Played.setText("Games Played: " + game.getPlayer(2).getGamesPlayed());
		p2KalahStats.setText("Kalah Wins/Played: " + game.getPlayer(2).getKalahWGP());
		p2AyoStats.setText("Ayo Wins/Played: " + game.getPlayer(2).getAyoWGP());
	}

	private JPanel makeButtonPanel() {
 		JPanel buttonPanel = new JPanel();
 		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
 		buttonPanel.add(makeNewGameButton());
 		return buttonPanel;
	}

	private JPanel makeAStorePanel(int storeNum) {
	 	JPanel buttonPanel = new JPanel();
	 	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	 	buttonPanel.add(makeStoreButton(storeNum));
	 	return buttonPanel;
	}

	private JButton makeNewGameButton() {
		newGameButton = new JButton("Start New Game");
		newGameButton.addActionListener(e -> newGame());
	 	return newGameButton;
	}

	private JPanel makeDisplayTurnPanel(){
		JPanel textFieldPanel = new JPanel();
		textFieldPanel.setLayout(new GridLayout(2, 1));

		invalidMoveMeassage = new JLabel("Invalid Move", SwingConstants.CENTER);
		invalidMoveMeassage.setVisible(false);

		textFieldPanel.add(invalidMoveMeassage);
		textFieldPanel.add(makeDisplayTurn());
		return textFieldPanel;
	}

	private JLabel makeDisplayTurn(){
		displayTurn = new JLabel(game.getPlayerName(1) + "'s Turn", SwingConstants.CENTER);
		return displayTurn;
	}

	protected void newGame(){
		game.startNewGame();
		newGameButton.setText("Start New Game");
		updateView();
	}

	protected void updateView() {
	 	
		updatePits();

		updateStores();

		if(game.isGameOver() == false){
			updateTextField();
		}

		updateStats();

		updateMenu();
	}

	protected void updatePits(){
		for(int x = 0; x < 6; x++){
			for(int y = 0; y < 2; y++){
				// Updates the text on the buttons
				pits[x][y].setText("" + game.getNumStones(buttonToIndex(x,y)));
			}
		}
	}

	protected void updateStores(){
		for(int i = 0; i < 2; i++){
			stores[i].setText("" + game.getPlayerStoreCount(i + 1));
		}
	}

	protected void updateTextField(){
		if(game.isP1sTurn() == true){
			displayTurn.setText(game.getPlayerName(1) + "'s Turn");
		}else{
			displayTurn.setText(game.getPlayerName(2) + "'s Turn");
		}
	}

	protected void updateStats(){
		setPlayerStatsText();
	}

	protected void updateMenu(){
		saveP1.setText("Save " + game.getPlayerName(1));
		saveP2.setText("Save " + game.getPlayerName(2));
		loadP1.setText("Load " + game.getPlayerName(1));
		loadP2.setText("Load " + game.getPlayerName(2));
		if(game.getBoard() instanceof KalahRules){
			toggleGameMode.setText("Toggle Ruleset To Ayo");
		}else{
			toggleGameMode.setText("Toggle Ruleset To Kalah");
		}
		changeP1Name.setText("Rename " + game.getPlayerName(1)); 
		changeP2Name.setText("Rename " + game.getPlayerName(2)); 
	}

	private PositionAwareButton makeStoreButton(int storeNum){
		stores[storeNum] = new PositionAwareButton();
		return stores[storeNum];
	}

	private JPanel makeMancalaGrid(int wide, int tall){
		JPanel panel = new JPanel();

		pits = new PositionAwareButton[wide][tall];
		panel.setLayout(new GridLayout(tall, wide));

		for(int y = 0; y < tall; y++){
			for(int x = 0; x < wide; x++){
				final int xAL = x;
				final int yAL = y;
				pits[x][y] = new PositionAwareButton();
				pits[x][y].setAcross(x);
				pits[x][y].setDown(y);
				pits[x][y].addActionListener(e -> move(pits[xAL][yAL].getAcross(), pits[xAL][yAL].getDown()));
				panel.add(pits[x][y]);	
			}
		}

		return panel;
	}

	public void move(int x, int y){

		try{
			game.move(buttonToIndex(x,y));
			toggleInvalidMove(false);
		}catch(InvalidMoveException e){
			toggleInvalidMove(true);
		}

		isGameOver();
		
		updateView();
	}

	public void toggleInvalidMove(boolean tOrF){
		invalidMoveMeassage.setVisible(tOrF);
	}

	public void isGameOver(){
		try{
			if(game.isGameOver() == true){
				incrementStats();
				newGameButton.setText("Play Again");
				displayTurn.setText(game.getWinner().getName() + " Wins");
			}
		}catch(GameNotOverException e){
			JOptionPane.showMessageDialog(null, "Player not found when checking if game is over.");
		}
	}

	public void incrementStats(){

		String gameMode = "ayo";

		if(game.getBoard() instanceof KalahRules){
			gameMode = "kalah";
		}

		game.getPlayer(1).gameWasPlayed(gameMode);
		game.getPlayer(2).gameWasPlayed(gameMode);
	}

	private void makeMenu() {
	 	menuBar = new JMenuBar();

	 	JMenu fileMenu = new JMenu("File");
	 	JMenu editMenu = new JMenu("Edit");
	 	JMenu viewMenu = new JMenu("View");

		JMenu save = new JMenu("Save");
		JMenu load = new JMenu("Load");

		JMenuItem saveGame = new JMenuItem("Save Game");
		saveGame.addActionListener(e -> saveGame());
		JMenu savePlayer = new JMenu("Save Player");
		JMenuItem loadGame = new JMenuItem("Load Game");
		loadGame.addActionListener(e -> loadGame());
		JMenu loadPlayer = new JMenu("Load Player");

		saveP1 = new JMenuItem();
		saveP1.addActionListener(e -> savePlayer(1));
		saveP2 = new JMenuItem();
		saveP2.addActionListener(e -> savePlayer(2));
		loadP1 = new JMenuItem();
		loadP1.addActionListener(e -> loadPlayer(1));
		loadP2 = new JMenuItem();
		loadP2.addActionListener(e -> loadPlayer(2));

		toggleGameMode = new JMenuItem(); 
		toggleGameMode.addActionListener(e -> toggleGameMode());
		JMenu changePlayerName = new JMenu("Edit Player Names");
		changeP1Name = new JMenuItem(); 
		changeP1Name.addActionListener(e -> renamePlayer(1));
		changeP2Name = new JMenuItem(); 
		changeP2Name.addActionListener(e -> renamePlayer(2));

		stats = new JMenuItem("Turn Off Player Stats");
		stats.addActionListener(e -> toggleStatsView());

		fileMenu.add(save);
		save.add(saveGame);
		save.add(savePlayer);
		savePlayer.add(saveP1);
		savePlayer.add(saveP2);
		fileMenu.add(load);
		load.add(loadGame);
		load.add(loadPlayer);
		loadPlayer.add(loadP1);
		loadPlayer.add(loadP2);

		editMenu.add(toggleGameMode);
		editMenu.add(changePlayerName);
		changePlayerName.add(changeP1Name);
		changePlayerName.add(changeP2Name);

		viewMenu.add(stats);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);

		updateView();
	}

	public void saveGame(){

		JFileChooser chooser = new JFileChooser();

		int valid = chooser.showSaveDialog(null);

		if (valid == JFileChooser.APPROVE_OPTION){

			try{
				Saver.saveObject(game, chooser.getSelectedFile().getAbsolutePath());
			}catch(IOException e){
				JOptionPane.showMessageDialog(null, "Error Saving Game");
			}
		}
	}

	public void savePlayer(int playerNum){

		JFileChooser chooser = new JFileChooser();

		int valid = chooser.showSaveDialog(null);

		if (valid == JFileChooser.APPROVE_OPTION){

			try{
				Saver.saveObject(game.getPlayer(playerNum), chooser.getSelectedFile().getAbsolutePath());
			}catch(IOException e){
				JOptionPane.showMessageDialog(null, "Error Saving Player");
			}
		}
	}

	public void loadGame(){

		JFileChooser chooser = new JFileChooser();

		int valid = chooser.showOpenDialog(null);

		if (valid == JFileChooser.APPROVE_OPTION){

			try{
				game = (MancalaGame) Saver.loadObject(chooser.getSelectedFile().getAbsolutePath());
				updateView();
			}catch(IOException e){
				JOptionPane.showMessageDialog(null, "Error Loading Game");
			}
		}	
	}

	public void loadPlayer(int playerNum){

		JFileChooser chooser = new JFileChooser();

		int valid = chooser.showOpenDialog(null);

		if (valid == JFileChooser.APPROVE_OPTION){

			try{
				game.overwritePlayer((Player) Saver.loadObject(chooser.getSelectedFile().getAbsolutePath()), playerNum);
				updateView();
			}catch(IOException e){
				JOptionPane.showMessageDialog(null, "Error Loading Player");
			}
		}	
	}

	public void toggleGameMode(){

		if(game.getBoard() instanceof KalahRules){
			game.setBoard(new AyoRules());
		}else{
			game.setBoard(new KalahRules());
		}

		newGame();
	}

	public void renamePlayer(int playerNum){

		String name = game.getPlayerName(playerNum);

		game.setPlayerName(JOptionPane.showInputDialog("Enter Player " + playerNum + "'s Name: "), playerNum);

		if(game.getPlayerName(playerNum) == null){
			game.setPlayerName(name, playerNum);
		}
		
		updateView();
	}

	public void toggleStatsView(){
		if(playerStatsPanel.isVisible()){
			playerStatsPanel.setVisible(false);
			stats.setText("Turn On Player Stats");
		}else{
			playerStatsPanel.setVisible(true);
			stats.setText("Turn Off Player Stats");
		}
	}

 	public void setupGameContainer(){
 		gameContainer.add(makeAStorePanel(1));
 		gameContainer.add(makeMancalaGrid(6, 2));
 		gameContainer.add(makeAStorePanel(0));
 		gameContainer.add(makeButtonPanel());
 	}

 	public int buttonToIndex(int x, int y){
 		return x*y + y + 12*(1-y) - x*(1-y);
 	}

 	public static void main(String[] args) {
 		MancalaUI mancala = new MancalaUI("Mancala");
 		mancala.setVisible(true);
	}


}

