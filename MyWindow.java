import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyWindow {
   private JFrame mainFrame;
 
   public MyWindow(){
      mainFrame = new JFrame("Java Swing Examples");
      mainFrame.setSize(800,800);
      mainFrame.setLayout(new FlowLayout());
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
      mainFrame.setVisible(true);
		
		setupFileInput();
   }
	
	private void setupFileInput() {
	
	}
	
	 public static void main(String[] args){
  		MyWindow demo = new MyWindow( );    
   }

}