import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class MyWindow implements ActionListener {
   
   private JFrame mainFrame;
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
   //Method that has 2 buttons to input one kmz and one kml file
   private void showInputButton(){
   
      JButton KMLFile1 = new JButton("Input RFPS file");//KML button
      JButton KMLFile2 = new JButton("Input SignalPro file");//KMZ button
      
       //Listener for RFPS file button
       KMLFile1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try{
             String RFPSName = getKML();
             input1.setText(RFPSName);
             RFPSFile = new File(RFPSName);//this is the file to be compared with SignalProFile
             System.out.println("RFPS File Created and saved as 'RFPSFile'");
            }catch(Exception E){
            System.out.println("getKML function not called");
            }
         } 
      }); //end listener for RFPS
      
       //Listener for SignalPro file button
       KMLFile2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try{
             String SignalProName = getKML();
             input2.setText(SignalProName);
             SignalProFile = new File(SignalProName);//this is the file to be compared with RFPSFile
             System.out.println("SignalPro File Created and saved as 'SignalProFile'");

             
            }catch(Exception E){
            System.out.println("getKMZ function not called");
            }
         } 
      });//end listener for SignalPro
      
      inputPanel.add(KMLFile1);
      input1 = new JLabel("No file chosen");
      inputPanel.add(input1);
 
      inputPanel.add(KMLFile2);
      input2 = new JLabel("No file chosen");
      inputPanel.add(input2);

      mainFrame.setVisible(true);
   }//end showInputButton() function
	
  
   private void showRunButton(){
      JButton runButton = new JButton("RUN");
      
      runButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            createKML();
         }          
      });
      
      controlPanel.add(runButton);
      mainFrame.setVisible(true);  
   }//end showRunButton() function
   
   private void createKML(){
      //string that will hold the initial .kml file
      String kmlString = "";
      
      // Template for generating base .kml example file:
      kmlString+= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; //begin tag
      kmlString+= "<kml xmlns=\"http://www.opengis.net/kml/2.2\">"; //begin kml tag
      kmlString+= "</kml>"; //end kml tag
		
		//date for file name---
      java.util.Date date= new java.util.Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String s = formatter.format(date);
		
      File fileHandler = new File(datapath + "" + s + "_outfile.kml");
      fileHandler.getParentFile().mkdirs();
      
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
   }
   
   private void setupFileInput() {
	
   }
	
	 // ********************MAIN METHOD**********************
    public static void main(String[] args)
    {
  	     MyWindow program = new MyWindow();
		 
        program.showRunButton();
        program.showInputButton();
	 }
   
    public void actionPerformed(ActionEvent e)
    {
        //Handle open button action.
        if (e.getSource() == chooseOutputButton) {
            int returnVal = outputDirChooser.showOpenDialog(mainFrame);
         
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

}