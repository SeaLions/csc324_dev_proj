import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyWindow {
   private JFrame mainFrame;
	private JPanel outputPanel;
	private JButton chooseOutput;
	private JLabel chooseOutputLabel;
	private JFileChooser fcOutput;
	
 
   public MyWindow(){
      mainFrame = new JFrame("Java Swing Examples");
      mainFrame.setSize(800,800);
      mainFrame.setLayout(new FlowLayout());
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
      mainFrame.setVisible(true);
		
		setupFileInput();
		setupFileOutput();
		
   }
	
	private void setupFileInput() {
	
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
	
	 public static void main(String[] args){
  		MyWindow demo = new MyWindow( );    
   }

}