import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class MyWindow
      implements ActionListener {
   
   private JFrame mainFrame;
	private JPanel outputPanel;
	private JButton chooseOutputButton;
	private JLabel chooseOutputButtonLabel;
	private JFileChooser outputDirChooser;
   private UserInput userInput;
	
 
   public MyWindow(){
      mainFrame = new JFrame("Java Swing Examples");
      mainFrame.setSize(800,800);
      mainFrame.setLayout(new FlowLayout());
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
      userInput = new UserInput();
		outputDirChooser = new JFileChooser();
      outputDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      
		setupFileInput();
		setupFileOutput();
          
      mainFrame.setVisible(true);
   }
	
	private void setupFileInput() {
	
	}
   
   public void actionPerformed(ActionEvent e) {
   
      //Handle open button action.
      if (e.getSource() == chooseOutputButton) {
         int returnVal = outputDirChooser.showOpenDialog(mainFrame);
         
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            File directory = outputDirChooser.getSelectedFile();
            userInput.setOutputDirectory(directory);
         }
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