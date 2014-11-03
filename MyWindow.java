import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
<<<<<<< HEAD
import java.text.*;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
=======
import java.io.File;
>>>>>>> GUI

public class MyWindow implements ActionListener {
   
   private JFrame mainFrame;
<<<<<<< HEAD
   private JFrame alertFrame;
	private JPanel outputPanel;
   private JPanel controlPanel;
	private JButton chooseOutputButton;
	private JLabel chooseOutputButtonLabel;
	private JFileChooser outputDirChooser;
   private UserInput userInput;
   private JPanel inputPanel;
   private JButton chooseOutput;
   private JLabel chooseOutputLabel, input1, input2, statusLabel;
   private JFileChooser fcOutput;
   private File RFPSFile, SignalProFile;//instantiated when kml files are selected on GUI
 
   
   //class variable to hold the output filepath
   //initially set to current directory
   String datapath = "./"; 
 
   public MyWindow(){
      statusLabel = new JLabel("",JLabel.CENTER);

      controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
      
      inputPanel = new JPanel();
      inputPanel.setLayout(new FlowLayout());

      mainFrame = new JFrame("Compare Signal Coverage");
      mainFrame.setSize(800,800);
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      mainFrame.setLocation(dim.width/2-mainFrame.getSize().width/2, dim.height/2-mainFrame.getSize().height/2);
      mainFrame.setLayout(new FlowLayout());
      mainFrame.addWindowListener(new WindowAdapter(){
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }
      });
        
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
      mainFrame.add(statusLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(inputPanel);
      mainFrame.setVisible(true);
            
      setupFileInput();
      setupFileOutput();
   }//end MyWindow()
   
   
   //Method to insert KML files
   public String getKML() throws IOException {   
      FileDialog fd = new FileDialog(mainFrame, "Choose a file", FileDialog.LOAD);
      fd.setDirectory("C:\\");
      fd.setFile("*.kml");
      fd.setVisible(true);
      String filename = fd.getFile();
      if (filename == null)
        filename = "You cancelled the operation";
      return filename;
     
   }// End getKML method
   
   //Start showInputButton() function
=======
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
	
	//Start showInputButton() function
>>>>>>> GUI
   //Method that has 2 buttons to input one kmz and one kml file
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
<<<<<<< HEAD
	
  
   private void showRunButton(){
      JButton runButton = new JButton("RUN");
=======

	private void setupOutputOptions() {
>>>>>>> GUI
      
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
      
<<<<<<< HEAD
      controlPanel.add(runButton);
      mainFrame.setVisible(true);  
   }//end showRunButton() function
=======
	}
>>>>>>> GUI
   
   private void setupRunControls() {
      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.anchor = GridBagConstraints.LINE_START;
      
      constraints.gridx = 0;
      constraints.gridy = 3;
      runButton = new JButton("RUN");
      runButton.addActionListener(this); 
		mainFrame.add(runButton, constraints);
      
<<<<<<< HEAD
      try{
         PrintWriter writer = new PrintWriter(fileHandler);
         writer.println(kmlString);
         writer.close();
			alertWindow("Saved KML file as '" + datapath + "outfile.kml'");
         //statusLabel.setText("Saved KML file as '" + datapath + "outfile.kml'");
      }catch (Exception e){
			alertWindow("There was a problem creating the file.");
         //statusLabel.setText("There was a problem creating the file.");
      }
=======
>>>>>>> GUI
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
<<<<<<< HEAD
	
	 // ********************MAIN METHOD**********************
    public static void main(String[] args)
    {
  	     MyWindow program = new MyWindow();
		 
        program.showRunButton();
        program.showInputButton();
	 }
=======
   
   public void actionPerformed(ActionEvent e) {
>>>>>>> GUI
   
    public void actionPerformed(ActionEvent e)
    {
        //Handle open button action.
        if (e.getSource() == chooseOutputButton) {
            int returnVal = outputDirChooser.showOpenDialog(mainFrame);
         
<<<<<<< HEAD
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File directory = outputDirChooser.getSelectedFile();
                userInput.setOutputDirectory(directory);
            }
        }
    }

	 private void setupFileOutput()
    {
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
  
   
    public void alertWindow(String message)
    {
        JLabel alertLabel = new JLabel("Alert",JLabel.CENTER);
		  alertLabel.setText(message);
		
		  JPanel okPanel = new JPanel();
		  okPanel.setLayout(new FlowLayout());
		  JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
			       alertFrame.dispose();
            }          
        });
		  okPanel.add(okButton);
		  alertFrame = new JFrame("Alert");
        alertFrame.setSize(500,150);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        alertFrame.setLocation(dim.width/2-alertFrame.getSize().width/2, dim.height/2-alertFrame.getSize().height);
        GridLayout gridLayout = new GridLayout(0,1);
		  alertFrame.setLayout(gridLayout);
        alertFrame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent windowEvent){
                alertFrame.dispose();
            }
        });
        alertFrame.setDefaultCloseOperation(alertFrame.DISPOSE_ON_CLOSE);
		  alertFrame.add(alertLabel);
		  alertFrame.add(okPanel);
        alertFrame.setVisible(true);
   }//end alertWindow()
=======
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
>>>>>>> GUI

}