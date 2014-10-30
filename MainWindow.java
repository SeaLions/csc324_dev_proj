import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class MainWindow extends JFrame
{
   
   private JPanel inputPanel;
   
   public MainWindow(){
      setTitle("Program for the Comparison of Radio Prediction Plots");
      setSize(800,800);
      setLayout(new FlowLayout());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      setVisible(true);
   }
	
	public static void main(String[] args){
  		MainWindow mainWindow = new MainWindow();
   }
   
}