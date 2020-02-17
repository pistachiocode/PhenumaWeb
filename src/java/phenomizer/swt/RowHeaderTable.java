package phenomizer.swt;

import java.awt.Component;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;



class RowHeaderRenderer extends JLabel implements ListCellRenderer {
	   
	  RowHeaderRenderer(JTable table) {
		  JTableHeader header = table.getTableHeader();
		  setOpaque(true);
		  setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		  setHorizontalAlignment(CENTER);
		  setForeground(header.getForeground());
		  setBackground(header.getBackground());
		  setFont(header.getFont());
	  }
	  
	  public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    setText((value == null) ? "" : value.toString());
	    return this;
	  }
}

class RowHeaderListModel extends AbstractListModel {

    String headers[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i"};
    public RowHeaderListModel(String headers[]){
    	this.headers = headers;
    	
    }
    
    public int getSize() { 
    	return headers.length; 
    }
   
    public Object getElementAt(int index) {
      return headers[index];
    }
	
	
}
	 
	public class RowHeaderTable extends JScrollPane{
		 
		public static JScrollPane rowHeaderTable(String headers[], String[][] data) {
		
			RowHeaderListModel lm = new RowHeaderListModel(headers);
			 
			DefaultTableModel dm = new DefaultTableModel(data, headers);
			JTable table = new JTable(dm);
			//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			    
			JList rowHeader = new JList(lm);    
			rowHeader.setFixedCellWidth(50);
			    
			rowHeader.setFixedCellHeight(table.getRowHeight()
			                           + table.getRowMargin()
			                           + table.getIntercellSpacing().height);
			
			rowHeader.setCellRenderer(new RowHeaderRenderer(table));
			 
			JScrollPane scroll = new JScrollPane( table );
			scroll.setRowHeaderView(rowHeader);
			
			
			return scroll;
		}
	
	}
