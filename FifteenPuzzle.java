/**
 * Fifteen Puzzle
 * CSCIE-10B Homework 6
 * 
 * Version of the fifteen puzzle. Goal is to order a matrix of number tiles,
 * labeled 1-15, in increasing order given a row-major traversal.
 * 
 * Includes a button to shuffle the tiles, a text box to input how much the tiles
 * should be shuffled, and a move counter. A victory message is displayed when the
 * puzzle is successfully ordered.
 * 
 * @author Jon Janelle
 * @version Last modified on 4/8/16
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FifteenPuzzle extends JFrame{
	
	private JButton[][] buttons;
	private JPanel buttonPanel;
	private JButton shuffleButton;
	private JTextField shuffleAmount;
	private JLabel moveLabel;
	private JPanel shufflePanel;
	private int movesMade;
	private Font buttonFont = new Font ("Helvetica", Font.BOLD, 40);
	
	/**
	 * Constructor initializes puzzle tiles and shuffle button, adds
	 * all components to appropriate panels, and makes the frame visible
	 */
	public FifteenPuzzle()
	{
		movesMade = 0;
		moveLabel = new JLabel("Moves: "+movesMade);
		//Setup game tile buttons and button panel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4,4));
		buttons = new JButton[4][4];
		initButtons();
		
		//Setup shuffle button and panel
		shufflePanel = new JPanel();
		shufflePanel.setLayout(new FlowLayout());
		shuffleAmount = new JTextField("10",5);
		shuffleButton = new JButton("Shuffle");
		shuffleButton.addActionListener( //declare anonymous class to react to tile button clicks
				new ActionListener()  
				{          
					public void actionPerformed(ActionEvent e) {
						try {
							shuffle(Integer.parseInt(shuffleAmount.getText()));
							movesMade = 0;
							moveLabel.setText(("Moves: "+movesMade));
						}
						catch (IllegalArgumentException ex){}
					}
				});
		shufflePanel.add(shuffleButton);
		shufflePanel.add(new JLabel("Shuffle Amount: "));
		shufflePanel.add(shuffleAmount);
		shufflePanel.add(moveLabel);
		
		//Setup and add panels to content panel of main frame
		setLayout(new BorderLayout());	
		add(buttonPanel, BorderLayout.CENTER);
		add(shufflePanel, BorderLayout.SOUTH);
	
		
		setTitle("Fifteen Puzzle"); //Set puzzle title, frame size, and make visible
		setSize(400, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}

	/**
	 * Initialize a square grid of buttons
	 * Labels each button with next unused positive integer value, adds buttons to button panel,
	 * and attached an ActionListener to each button to react to clicks
	 */
	private void initButtons()
	{
		for (int i = 0; i < buttons.length;i++){
			for (int j = 0; j < buttons[i].length;j++){
				buttons[i][j] = new JButton();
				buttons[i][j].setFont(buttonFont);
				buttons[i][j].setText(""+(buttons.length*i+j+1)); //Each button will be labeled with integer one greater than previous button
				if (i == buttons.length-1 && j == buttons.length-1) {
					buttons[i][j].setText(""); //Last button labeled as blank space
				}
				
				//add button to grid
				buttonPanel.add(buttons[i][j]);
				
				buttons[i][j].addActionListener( //declare anonymous class to react to tile button clicks
						new ActionListener()  
						{          
							public void actionPerformed(ActionEvent e) {
								if (makeMove(e.getActionCommand())){
									movesMade++;
									moveLabel.setText(("Moves: "+movesMade));
								}
								if (!e.getActionCommand().equals("")&&checkWin()) {
									winMessage();
								}
							}
						});
			}
		}
	}
	
	
	/**
	 * Move a tile to an unoccupied adjacent space if possible. 
	 * If adjacent space is empty, switch current and adjacent space.
	 * Otherwise, do nothing
	 * @param buttonName The name of the button to attempt to use.
	 */
	public boolean makeMove(String buttonName)
	{
		int i = 0;
		int j = 0;
		for (int n = 0; n < buttonPanel.getComponentCount(); n++){
				JButton b = (JButton)buttonPanel.getComponent(n);
				if (b.getText().equals(buttonName)) {
					i=n/4;		//row index of button pressed
					j = n%4;	//column index of button pressed
					break;
				}
		}
		
		if (buttons[i][j].getText().equals("")) return false; //cannot move an empty space
		else if (j!=buttons.length-1 && buttons[i][j+1].getText().equals("")) { //check down
			swap(i,j,i,j+1); 
			return true;
		}
		else if (j!=0 && buttons[i][j-1].getText().equals("")) { //check up
			swap(i,j,i,j-1);
			return true;
		}
		else if (i!=0 && buttons[i-1][j].getText().equals("")) { //check left
			swap(i,j,i-1,j);
			return true;
		}
		else if (i!=buttons.length-1 && buttons[i+1][j].getText().equals("")) { //check right
			swap(i,j,i+1,j);
			return true;
		}
		return false; //button pressed not adjacent to empty space
	}

	/**
	 * Swap the text of two buttons given the indices of their position in the buttons array.
	 * (i, j) and (m, n) are the indices of the buttons to swap.
	 * @param i The x-position of the first button 
	 * @param j The y-position of the first button 
	 * @param m The x-position of the second button
	 * @param n The y-position of the second button
	 */
	private void swap(int i,int j,int m, int n)
	{
		String temp = buttons[i][j].getText();
		buttons[i][j].setText(buttons[m][n].getText());
		buttons[m][n].setText(temp);
	}

	/**
	 * Randomly make a specified number of tile moves on a puzzle.
	 * Used to scramble the puzzle before playing.
	 * @param n The number of moves to make while shuffling the tiles
	 */
	private void shuffle(int n)
	{
		int movesMade = 0;
		int i = 0;
		int j = 0;
		while (movesMade < n)
		{
			i = (int)(Math.random()*4);
			j = (int)(Math.random()*4);
			if (makeMove(buttons[i][j].getText())){
				movesMade++;
			}
		}
	}
	
	/**
	 * Check to determine if the puzzle tiles are correctly ordered
	 * @return True if winning order detected, false otherwise
	 */
	private boolean checkWin()
	{
		JButton b1, b2;
		for (int i = 0; i < buttonPanel.getComponentCount()-2; i++)
		{
			b1 = (JButton)buttonPanel.getComponent(i);
			b2 = (JButton)buttonPanel.getComponent(i+1);
			
			if (b1.getText().equals("")||b2.getText().equals("")) { 
				return false;
			}
			else if (Integer.parseInt(b1.getText())> Integer.parseInt(b2.getText())) {
				return false; //if not in increasing order, then no win.
			}
		}
		return true;
	}
	
	/**
	 * Display a message dialog box containing a victory message and the number of moves required for a win
	 */
	private void winMessage()
	{
		JOptionPane.showMessageDialog(null, "Congratulations, you won in "+movesMade+" moves!\n"
											+"Enter a shuffle amount and click Shuffle to play again.");	
	}
	
	/**
	 * Create a new Fifteen Puzzle to start the game
	 */
	public static void main(String[] args) {
		FifteenPuzzle myPuzzle = new FifteenPuzzle();
	}

}