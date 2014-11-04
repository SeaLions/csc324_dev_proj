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
	private JLabel rfpsInputLabel, signalProInputLabel, directoryOutputLabel, outputLocationLabel;
	private JFileChooser fileChooser, outputDirChooser, inputFileChooser;
   private UserInput userInput;
	
 
   public MyWindow(){
      mainFrame = new JFrame("GPS Coordinate Comparison Program");
      mainFrame.setSize(1200,1200);
      mainFrame.setLayout(new GridBagLayout());
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
      userInput = new UserInput();
		fileChooser = new JFileChooser();
		outputDirChooser = new JFileChooser();
      outputDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      inputFileChooser = new JFileChooser(); 
		
		setupInputOptions();
		setupOutputOptions();
      setupRunControls();
      setupResultsDisplay();
          
      mainFrame.setVisible(true);
   }
	
	private void setupInputOptions(){
      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.anchor = GridBagConstraints.LINE_START;
      
		rfpsFileChooseButton = new JButton("Input RFPS file");		//KML button
      rfpsFileChooseButton.addActionListener(this);
      constraints.gridx = 0;
      constraints.gridy = 0;
		mainFrame.add(rfpsFileChooseButton, constraints);
      
      rfpsInputLabel = new JLabel("No file chosen");
      constraints.gridx = 1;
      constraints.gridy = 0;
      mainFrame.add(rfpsInputLabel, constraints);
		
		signalProChooseButton = new JButton("Input SignalPro file");	//KMZ button
		signalProChooseButton.addActionListener(this);  
      constraints.gridx = 0;
      constraints.gridy = 1;
		mainFrame.add(signalProChooseButton, constraints);
 
      signalProInputLabel = new JLabel("No file chosen");
      constraints.gridx = 1;
      constraints.gridy = 1;
      mainFrame.add(signalProInputLabel, constraints);
      
   }//end showInputButton() function

	private void setupOutputOptions() {
      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.anchor = GridBagConstraints.LINE_START;
		
		chooseOutputButton = new JButton("Choose Output Directory");
		chooseOutputButton.addActionListener(this);
      constraints.gridx = 0;
      constraints.gridy = 2;
		mainFrame.add(chooseOutputButton, constraints);
		
		directoryOutputLabel = new JLabel("No directory chosen");
      constraints.gridx = 1;
      constraints.gridy = 2;
		mainFrame.add(directoryOutputLabel, constraints);
      
	}
   
   private void setupRunControls() {
      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.anchor = GridBagConstraints.LINE_START;
      
      constraints.gridx = 0;
      constraints.gridy = 3;
      runButton = new JButton("RUN");
      runButton.addActionListener(this); 
		mainFrame.add(runButton, constraints);
      
   }
   
   private void setupResultsDisplay() {
      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.anchor = GridBagConstraints.LINE_START;
      
      constraints.gridx = 0;
      constraints.gridy = 4;
      constraints.gridwidth = 2;
      outputLocationLabel = new JLabel("Select an RFPS file, SignalPro file, and output directory.");
      mainFrame.add(outputLocationLabel, constraints);
      
   }
   
   public void actionPerformed(ActionEvent e) {
   
      //Handle open button action.
      if (e.getSource() == chooseOutputButton) {
         int returnVal = outputDirChooser.showOpenDialog(mainFrame);
         
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            File directory = outputDirChooser.getSelectedFile();
            userInput.setOutputDirectory(directory);
            directoryOutputLabel.setText(directory.getAbsolutePath());
         }
      }
		
		else if (e.getSource() == rfpsFileChooseButton) {
			try{
				 inputFileChooser.showOpenDialog(mainFrame);
				 userInput.setRfpsFile(inputFileChooser.getSelectedFile());
             String RFPSName = inputFileChooser.getSelectedFile().getName();
             rfpsInputLabel.setText(RFPSName);
				 
             }
			catch(Exception E){
             }
		}

		else if (e.getSource() == signalProChooseButton) {
			try{
				 inputFileChooser.showOpenDialog(mainFrame);
				 userInput.setSignalproFile(inputFileChooser.getSelectedFile());
             String SignalProName = inputFileChooser.getSelectedFile().getName();
             signalProInputLabel.setText(SignalProName);
     			       
				 }
			catch(Exception E){
             }
		}
		
		else if (e.getSource() == runButton) {
         
         if (validUserInput()) {
         
   		   Comparison comparison = new Comparison();
            boolean success = comparison.createKmlOutputFile(userInput.getOutputDirectory());
            String outputLocationLabelText;
            if (success) {
               outputLocationLabelText = "File output to " + userInput.getOutputDirectory().getAbsolutePath();
            }
            else {
               outputLocationLabelText = "Select an RFPS file, SignalPro file, and output directory.";
            }
            outputLocationLabel.setText(outputLocationLabelText);
         }
		}

   }
   
   boolean validUserInput() {
      if (userInput.getRfpsFile() == null)
         return false;
      if (userInput.getSignalproFile() == null)
         return false;
      if (userInput.getOutputDirectory() == null)
         return false;
      return true;
   }
	
	 public static void main(String[] args){
  		MyWindow demo = new MyWindow( );
   }

}