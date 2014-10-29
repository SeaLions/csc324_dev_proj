import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class MyWindow implements ActionListener {
   
   private JFrame mainFrame;
   private JFrame alertFrame;
	private JPanel outputPanel;
   private JPanel controlPanel;
	private JButton chooseOutputButton;
	private JLabel chooseOutputButtonLabel;
	private JFileChooser outputDirChooser;
    private UserInput userInput;
 
    public MyWindow()
    {
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
       mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
       userInput = new UserInput();
		 outputDirChooser = new JFileChooser();
       outputDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      
		 setupFileInput();
  	    setupFileOutput();
          
       mainFrame.setVisible(true);
   }
   
   
    private void setupFileInput()
    {
	
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