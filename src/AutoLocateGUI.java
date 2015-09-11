/*
 * THis program was created by Aaron Girard (aarongirard.com) in order to increase his efficiency at his internship at Human Rights Watch
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
public class AutoLocateGUI
{
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel saveFileLabel;
	private JLabel statusLabel;
	private JPanel controlPanel1;
	private JPanel controlPanel2;
	private JPanel controlPanel3;


	private File inputFile;
	private File outputFile;

	//private FileDialog fileDialog;

	public AutoLocateGUI()
	{
		prepareGUI();
	}

	public static void main(String[] args) 
	{
		AutoLocateGUI autoLocateGUI = new AutoLocateGUI();
 		autoLocateGUI.showChoice();
	}

	private void prepareGUI()
	{
		//initialize mainframe
		mainFrame = new JFrame("Inmate and Representative AutoLocator");
		//set its size
		mainFrame.setSize(400,400);
		//set the layout as 3 rows by 1 col
		mainFrame.setLayout(new GridLayout(6, 1));
		//if window is closed, exit program
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		headerLabel = new JLabel("", JLabel.CENTER);

		saveFileLabel = new JLabel("", JLabel.CENTER);

      	statusLabel = new JLabel("", JLabel.CENTER);        
     	statusLabel.setSize(350,100);

     	

     	controlPanel1 = new JPanel();
     	controlPanel1.setLayout(new FlowLayout());
     	controlPanel2 = new JPanel();
     	controlPanel2.setLayout(new FlowLayout());
     	controlPanel3 = new JPanel();
     	controlPanel3.setLayout(new FlowLayout());

     	mainFrame.add(headerLabel); //layout at 1
	    mainFrame.add(controlPanel1); //layout at 2
	    mainFrame.add(saveFileLabel); //layout at 3
	    mainFrame.add(controlPanel2);//layout at 4
	    mainFrame.add(controlPanel3); //layout 5
	    mainFrame.add(statusLabel); //layout at 6
	    mainFrame.setVisible(true);
	}

	private void showChoice()
	{
		//set text explaining choice
		headerLabel.setText("1) Are you looking up inmates or representatives?");
		saveFileLabel.setText("2) Where would you like to save the output file to? (choose a folder)");  //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//object which represents the choice dropdown menu
		final Choice locatorChoice = new Choice();
		//add the choices
		locatorChoice.add("Inmate Locator");
		locatorChoice.add("Representative Locator");

		JButton button1 = new JButton("Open a file");
		JFileChooser fc1 = new JFileChooser();

		button1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int returnVal = fc1.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
            		 inputFile = fc1.getSelectedFile();
            		//System.out.println(inputFile);
            	}
			}
		});

		controlPanel1.add(locatorChoice);
		controlPanel1.add(button1);
		
		//button to add file
		JButton button2 = new JButton("Open a folder");
		JFileChooser fc2 = new JFileChooser();
		fc2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //choose a director

		button2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int returnVal =fc2.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
            		 outputFile = fc2.getSelectedFile();
            		 //System.out.println(outputFile);
            	}
			}
		});

		controlPanel2.add(button2);
		
		//button to execute command
		JButton button3 = new JButton("Go!");
		
		button3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//set status
				statusLabel.setText("I'm Working Here...");
				
				String whichLocator = locatorChoice.getSelectedItem();
				//error, need files
				if(inputFile == null || outputFile == null){
					System.exit(0); //error message needs to be added
				}
				
				if(whichLocator.equals("Representative Locator")){
					try {
						AutoLocateMainRep autoLocateMainRep = new AutoLocateMainRep(inputFile.toString(), outputFile.toString());
						autoLocateMainRep.run();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else if (whichLocator.equals("Inmate Locator")){
					try {
						AutoLocateMainInmate autoLocateMainInmate = new AutoLocateMainInmate(inputFile.toString(), outputFile.toString());
						autoLocateMainInmate.run();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					//some where needs to be implemented?
				}
				statusLabel.setText("Done");
			}
		});
		
		controlPanel3.add(button3);
		
	}
}