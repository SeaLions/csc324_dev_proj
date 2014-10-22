import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.PrintWriter;
import java.io.File;

import java.sql.*;
import java.util.*;
import java.lang.*;
import java.text.*;

import java.awt.Desktop;
import java.io.*;

public class MyWindow {
   private JFrame mainFrame;
	private JFrame alertFrame;
   private JLabel statusLabel;
   private JPanel controlPanel;
   private JPanel outputPanel;
   private JPanel inputPanel;
   private JButton chooseOutput;
   private JLabel chooseOutputLabel;
   private JFileChooser fcOutput;
   
   //class variable to hold the output filepath
   //initially set to current directory
   String datapath = "./"; 
 
   public MyWindow(){
      statusLabel = new JLabel("",JLabel.CENTER);

      controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
      
      inputPanel = new JPanel();
      inputPanel.setLayout(new FlowLayout());

      mainFrame = new JFrame("Java Swing Examples");
      mainFrame.setSize(800,800);
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
   
   // Method to insert kml files
   public void getKML() throws IOException {   
      try{
         System.out.println(System.getProperty("user.dir")); //Try to get to user home directory
         String userHomePath = System.getProperty("user.dir");
         File userHome = new File(userHomePath);
         Desktop.getDesktop( ).open(userHome); 
      }catch(Exception e){
         System.out.println("user.home not found");
         }
   } //  Make sure it is a kml file. End getKML method
   
   private void showInputButton(){
      JButton chooseKMLFile = new JButton("Choose Input");
      
       chooseKMLFile.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try{
             getKML();
            }catch(Exception E){
            System.out.println("get KML function not called");
            }

         } 
      });  
      inputPanel.add(chooseKMLFile);
      
      mainFrame.setVisible(true);
   }// end showInputButton() function

	
	
   public void alertWindow(String message){
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

		alertFrame = new JFrame("Java Swing Examples");
      alertFrame.setSize(500,150);
		GridLayout gridLayout = new GridLayout(0,1);
		alertFrame.setLayout(gridLayout);
		alertFrame.setTitle("Alert");
      alertFrame.addWindowListener(new WindowAdapter(){
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }
      });
      alertFrame.setDefaultCloseOperation(alertFrame.HIDE_ON_CLOSE);
		alertFrame.add(alertLabel);
		alertFrame.add(okPanel);
      
      alertFrame.setVisible(true);

   }//end alertWindow()


   
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
   public static void main(String[] args){
  	    MyWindow program = new MyWindow( );
		 
       program.showRunButton();
       program.showInputButton();
   }
	
	private void setupFileOutput() {
		outputPanel = new JPanel();
		outputPanel.setLayout(new FlowLayout());
		
		chooseOutputLabel = new JLabel("Specifiy An Output Directory");
		outputPanel.add(chooseOutputLabel);
		
		chooseOutput = new JButton("Choose");
		chooseOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fcOutput = new JFileChooser();
				JFrame fileChooser = new JFrame();
				fileChooser.add(fcOutput);
				fileChooser.setSize(500,500);
				fileChooser.setVisible(true);
				}
			});
		outputPanel.add(chooseOutput);
		
		outputPanel.setVisible(true);
		mainFrame.add(outputPanel);
	}
   
}
