package phenomizer.swt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class LabelPanel extends JPanel{
	
	private JLabel label;
	
	public static final int ERROR = 0;
	public static final int INFO = 1;
	
	
	public LabelPanel(){
		
		setLayout(new BorderLayout());
				
		label = new JLabel();
		label.setText("error");
		label.setFont(new Font("Serif", Font.BOLD, 14));
		
		label.setVisible(false);
		
		add(Utils.separator(50), BorderLayout.EAST);
		add(label, BorderLayout.CENTER);
		add(Utils.separator(50), BorderLayout.WEST);
		add(Utils.separator(30), BorderLayout.NORTH);
		add(Utils.separator(30), BorderLayout.SOUTH);
	}
	
	
	public void setErrorMessage(String message){
		label.setText(message);
		label.setForeground(Color.RED);
		label.setVisible(true);
	}
	
	public void setInfoMessage(String message){
		label.setText(message);
		label.setForeground(Color.BLUE);
		label.setVisible(true);
	}
	
	public void showMessage(){
		label.setVisible(true);
	}
	
	public void hidesMessage(){
		label.setVisible(false);
	}

}
