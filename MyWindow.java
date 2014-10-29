import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class MyWindow implements ActionListener {
   
   private JFrame mainFrame;
	private JFrame alertFrame;
   private JLabel statusLabel;
   private JPanel controlPanel;
	private JPanel outputPanel;
	private JPanel inputPanel;
	private JButton runButton, rfpsFileChooseButton, signalProChooseButton, chooseOutputButton;
	private JLabel rfpsInputLabel, signalProInputLabel, chooseOutputButtonLabel;
	private JFileChooser fileChooser, outputDirChooser;
   private UserInput userInput;
	
 
   public MyWindow(){
      mainFrame = new JFrame("GPS Coordinate Comparison Program");
      mainFrame.setSize(800,800);
      mainFrame.setLayout(new FlowLayout());
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
      userInput = new UserInput();
		fileChooser = new JFileChooser();
		outputDirChooser = new JFileChooser();
      outputDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      
		showInputPanel();
		setupFileOutput();
          
      mainFrame.setVisible(true);
   }
	
	//Start showInputButton() function
   //Method that has 2 buttons to input one kmz and one kml file
   private void showInputPanel(){
  		inputPanel = new JPanel();  
		inputPanel.setLayout(new FlowLayout());
		
		runButton = new JButton("RUN"); 
		inputPanel.add(runButton);
		
		rfpsFileChooseButton = new JButton("Input RFPS file");		//KML button
		inputPanel.add(rfpsFileChooseButton);
            
      rfpsInputLabel = new JLabel("No file chosen");
      inputPanel.add(rfpsInputLabel);
		
		signalProChooseButton = new JButton("Input SignalPro file");	//KMZ button
		inputPanel.add(signalProChooseButton);
 
      signalProInputLabel = new JLabel("No file chosen");
      inputPanel.add(signalProInputLabel);

      mainFrame.add(inputPanel);
   }//end showInputButton() function
   
   public void actionPerformed(ActionEvent e) {
   
      //Handle open button action.
      if (e.getSource() == chooseOutputButton) {
         int returnVal = outputDirChooser.showOpenDialog(mainFrame);
         
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            File directory = outputDirChooser.getSelectedFile();
            userInput.setOutputDirectory(directory);
         }
      }
		
		else if (e.getSource() == rfpsFileChooseButton) {
		
		}

		else if (e.getSource() == signalProChooseButton) {
		
		}
		
		else if (e.getSource() == runButton) {
		
		}

   }

	private void setupFileOutput() {
		outputPanel = new JPanel();
		outputPanel.setLayout(new FlowLayout());
		
		chooseOutputButtonLabel = new JLabel("Specifiy An Output Directory");
		outputPanel.add(chooseOutputButtonLabel);
		
		chooseOutputButton = new JButton("Choose");
		chooseOutputButton.addActionListener(this);
		outputPanel.add(chooseOutputButton);
		
		mainFrame.add(outputPanel);
		outputPanel.setVisible(true);
	}
	
	 public static void main(String[] args){
  		MyWindow demo = new MyWindow( );
   }

}