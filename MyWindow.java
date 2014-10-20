import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyWindow {
   private JFrame mainFrame;
   private JLabel statusLabel;
   private JPanel controlPanel;
   private JPanel outputPanel;
   private JButton chooseOutput;
   private JLabel chooseOutputLabel;
   private JFileChooser fcOutput;
 
   public MyWindow(){
      statusLabel = new JLabel("",JLabel.CENTER);

      controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

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
      mainFrame.setVisible(true);
            
      setupFileInput();
      setupFileOutput();
   }//end MyWindow()
   
   
   private void showRunButton(){
      JButton runButton = new JButton("RUN");
      
      runButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            statusLabel.setText("Ok Button clicked.");
         }          
      });
      
      controlPanel.add(runButton);

      mainFrame.setVisible(true);  
   }//end showRunButton() function
   
   private void createKML(){
   
      //File testexists = new File(datapath+"/"+name+".kml");
   
   }
   
   
    private void setupFileInput() {
	
    }
	
	
   public static void main(String[] args){
  	    MyWindow program = new MyWindow( ); 
       program.showRunButton();
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
