package phenomizer.swt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;



public class Utils {
	
	/**
	 * Get all components in a container
	 * @param c
	 * @return
	 */
	  public static List<Component> getAllComponents(final Container c) {
	    Component[] comps = c.getComponents();
	    List<Component> compList = new ArrayList<Component>();
	    for (Component comp : comps) {
	      compList.add(comp);
	      if (comp instanceof Container) {
	        compList.addAll(getAllComponents((Container) comp));
	      }
	    }
	    return compList;
	  }
	  
	  
	  /**
	   * Find component by name
	   */
	  
	  public static Component findByName(final Container c, String name) {
		List<Component> components = Utils.getAllComponents(c);
		
		int i = components.size()-1;
		
		Component component = components.get(i);
		while(i>=0 && (component.getName() == null || !component.getName().equalsIgnoreCase(name))){
			i--;
			if(i>=0)
				component = components.get(i);
		}
		
		if(i>=0)
			return component;
		
		return null;
	  }
	  
	  /**
	   * Get the best dimensions for each button in a group button
	   */
	  
	  public static Dimension getButtonDimensions(String[] buttonsName){

		  int maxlength = phenomizer.utils.StringUtils.maxLenght(buttonsName);
		  
		  Dimension d = new Dimension();
		  d.setSize(maxlength+50, 50);
		  
		  return d;
		  
	  }
	  
	  
	  /**
	   * Separator
	   */
	  public static JPanel separator(int size){
		  JPanel panel = new JPanel();
		  panel.setSize(size, size);
		  return panel;
		  
	  }
	  
	  /**
	   * Load file
	   * @param f
	   * @param title
	   * @param defDir
	   * @param fileType
	   * @return
	   */
	  
	  public static String loadFile (Frame f, String title, String defDir, String fileType) {
		  FileDialog fd = new FileDialog(f, title, FileDialog.LOAD);
		  fd.setFile(fileType);
		  fd.setDirectory(defDir);
		  fd.setLocation(50, 50);
		  fd.show();
		  return fd.getDirectory()+fd.getFile();
	  }
	  
	  /**
	   * Save file
	   * @param f
	   * @param title
	   * @param defDir
	   * @param fileType
	   * @return
	   */

	  public static String saveFile (Frame f, String title, String defDir, String fileType) {
	    FileDialog fd = new FileDialog(f, title,  FileDialog.SAVE);
	    fd.setFile(fileType);
	    fd.setDirectory(defDir);
	    fd.setLocation(50, 50);
	    fd.show(true);
	    return fd.getDirectory()+fd.getFile();
	    }

}
