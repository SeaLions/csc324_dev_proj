import java.awt.event.*;
import javax.swing.*;

class MyWindow extends JFrame {
	public MyWindow() {
		setTitle("Map Comparison Program");
		setSize(1000, 800);
		setLocation(20,20);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		JFrame f = new MyWindow( );
		f.show( );
	}
}