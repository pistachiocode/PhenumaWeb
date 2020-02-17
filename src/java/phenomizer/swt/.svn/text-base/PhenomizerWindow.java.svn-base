package phenomizer.swt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import ontologizer.go.OBOParserException;
import ontologizer.query.PhenomizerResult;
import ontologizer.query.PhenomizerResultList;

import phenomizer.hpo.Phenomizer;
import phenomizer.swt.PhenomizerListener;

public class PhenomizerWindow extends JFrame{

	Phenomizer phenomizer;
	
	private JTable table;
	private static DefaultTableModel model;
	
	//paneles
	private JScrollPane matrixTablePane;
	
	//error messages
	private LabelPanel message;
	
	//combo boxes
	private JComboBox queryType;
	private JComboBox adjustType;
	
	//fields
	private JTextField ontologyFileField;
	private JTextField diseasesFileField;
	private JTextField genesFileField;
	
	private final static int WIDTH = 1200;
	private final static int HEIGHT = 1000;
	
	//commnads
	public final static String DIAGNOSIS_COMMAND = "Get Diagnosis";
	public final static String CLEAR_COMMAND = "Clear";
	public final static String ALL_COMMAND = "All";
	public final static String ADJUST_COMMAND = "Adjust";
	public final static String QUERY_COMMAND = "QueryType";
	
	public final static String SAVE_COMMAND = "Save";
	
	public final static String LOAD_ONTO_COMMAND = "Load Ontology";
	public final static String LOAD_DISEASES_COMMAND = "Load Diseases";
	public final static String LOAD_GENES_COMMAND = "Load Genes";
	
	//query type
	public final static String HPO_COMMAND = "hpo";
	public final static String DISEASES_COMMAND = "diseases";
	public final static String GENES_COMMAND = "genes";
	
	//adjust type
	public final static String BONFERRONI_ADJUSTMENT = "Bonferroni";
	public final static String BENJAMINI_ADJUSTMENT = "Benjamini-Hochberg";
	
	public final static String TABLE_COMMAND = "table";
	public final static String COLUMNS_COMMAND = "columns";
	
	public final static String[] HEADERS_HPOTABLE = {"P-Value", "Adjust P-Value", "Score", "Omim"};
	public final static String[] HEADERS_PPTABLE = {"Omim", "Omim", "Score"};
	
	//components name
	public final static String QUERY_TEXTAREA = "query";

	/**
	 * Create phenomizer window
	 */
	public PhenomizerWindow(Phenomizer phenomizer)
	{
		super("Phenomizer 3.0");
		
		this.phenomizer = phenomizer;
		
		
		//listener
		PhenomizerListener listener = new PhenomizerListener(this);		
		Container cpane = this.getContentPane();
		cpane.setLayout(new BorderLayout());		
		

		//Combo box query type
		String[] queryTypeItems = {GENES_COMMAND, DISEASES_COMMAND, HPO_COMMAND};
		queryType = new JComboBox(queryTypeItems);
		queryType.setActionCommand(QUERY_COMMAND);
		queryType.setName(QUERY_COMMAND);
		
		//Combo box adjustment type
		String[] adjustmentItems = {BONFERRONI_ADJUSTMENT, BENJAMINI_ADJUSTMENT};
		adjustType = new JComboBox(adjustmentItems);
		adjustType.setActionCommand(ADJUST_COMMAND);
		adjustType.setName(ADJUST_COMMAND);
		adjustType.addActionListener(listener);
		
		
		JPanel queryConfiguration = new JPanel();
		queryConfiguration.setLayout(new BorderLayout());
		
		queryConfiguration.add(queryType,BorderLayout.WEST);
		queryConfiguration.add(adjustType,BorderLayout.EAST);
		
		JScrollPane hpoTermsPanel = new JScrollPane(this.addTextArea());
		
		//button
		String[] buttonsStr = {DIAGNOSIS_COMMAND, CLEAR_COMMAND, ALL_COMMAND};
		JPanel buttons = this.addButtonsPanel(buttonsStr, listener);
		
		
		//QUERY
		JPanel query = new JPanel();
		query.setLayout(new BorderLayout());
		query.add(queryConfiguration, BorderLayout.NORTH);
		query.add(hpoTermsPanel, BorderLayout.CENTER);
		query.add(Utils.separator(50), BorderLayout.WEST);
		query.add(buttons, BorderLayout.SOUTH);

		matrixTablePane = new JScrollPane();
		
		//FOOT
		//result options
		String[] resultOptionsStr = {TABLE_COMMAND, COLUMNS_COMMAND};
		JPanel resultOptions = this.addRadioButtonPanel(resultOptionsStr, listener, TABLE_COMMAND);
		
		//result buttons
		String[] resultButtonsStr = {SAVE_COMMAND};
		JPanel resultButtons = this.addButtonsPanel(resultButtonsStr, listener);
	
		JPanel foot = new JPanel();
		foot.setLayout(new BorderLayout());
		foot.add(resultOptions, BorderLayout.WEST);
		foot.add(resultButtons, BorderLayout.EAST);
		
		//Result Pane
		JPanel resultPane = new JPanel();
		resultPane.setLayout(new BorderLayout());
		
		
		
		JPanel resultCenter = new JPanel();
		resultCenter.setLayout(new BorderLayout());
		resultCenter.add(matrixTablePane, BorderLayout.CENTER);
		resultCenter.add(this.addFilesPanel(listener), BorderLayout.SOUTH);
		
		//message
		message = new LabelPanel();
		resultPane.add(message, BorderLayout.NORTH); 
		
		//resultado
		resultPane.add(resultCenter, BorderLayout.CENTER);		
		resultPane.add(Utils.separator(50), BorderLayout.EAST);
		resultPane.add(Utils.separator(50), BorderLayout.WEST);
		resultPane.add(foot, BorderLayout.SOUTH);
		
		//Result type
		cpane.add(query, BorderLayout.WEST);
		cpane.add(resultPane, BorderLayout.CENTER);
		//cpane.add(this.addFilesPanel(listener), BorderLayout.SOUTH);
	}


	/**
	 * show window
	 */
	public void showWindow(){
		if (!phenomizer.dataLoaded())
			this.infoMessage("You must to provide an ontology (.obo file) and at least an annotation file.");
		
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	
	/**
	 * Radio Buttons (Query type)
	 * @return
	 */
	private JPanel addRadioButtonPanel(String[] buttons, ActionListener listener, String selectedOption){
		JPanel prb = new JPanel();
		ButtonGroup group = new ButtonGroup();
		
		prb.setLayout(new FlowLayout());
		
		int i = buttons.length-1;
		while(i>=0){
			JRadioButton rb = new JRadioButton(buttons[i], false);
			
			//actions
			rb.setName(buttons[i]);
			rb.setActionCommand(buttons[i].toUpperCase());
			rb.addActionListener(listener);
			rb.setSelected(buttons[i].equalsIgnoreCase(selectedOption));
			
			//add to group
			group.add(rb);
			//add to panel
			prb.add(rb);	
			
			i--;
		}
		prb.setSize(WIDTH/3, HEIGHT/6);
		
		return prb;
	}
	
	
	/**
	 * Add buttons panel
	 */
	private JPanel addButtonsPanel(String[] buttons, ActionListener listener){
		
		Dimension d = Utils.getButtonDimensions(buttons); //no funciona :'(
		
		//Buttons
		JPanel buttonpanel = new JPanel();
		buttonpanel.setLayout(new FlowLayout());
		
		int i = buttons.length-1;
		while(i>=0){
			JButton newButton = new JButton(buttons[i]);
			
			newButton.setName(buttons[i]);
			newButton.setActionCommand(buttons[i]);
			newButton.addActionListener(listener);
			newButton.setSize(d);
			
			buttonpanel.add(newButton);
			
			i--;
		}
		
		return buttonpanel;
	}
	
	
	/**
	 * @param query
	 */
	private JTextArea addTextArea() {
		//Text area
		JTextArea hpoterms = new JTextArea();
		hpoterms.setName(QUERY_TEXTAREA);
		
		return hpoterms;
	}

	
	/**
	 * Add table with the query result.
	 * @param headers
	 * @param data
	 */
	
	public void addTable(String[] headers, String[][] data){
		model = new DefaultTableModel(data,headers);
		table= new JTable(model);
		
		//sort on click in a header column
		TableRowSorter<DefaultTableModel> elQueOrdena = new TableRowSorter<DefaultTableModel>(model);
		table.setRowSorter(elQueOrdena);
		table.setSize(WIDTH/3, HEIGHT/6);
		
		this.matrixTablePane.setViewportView(table);
	}
	
	
	/** 
	 * Add row header matrix
	 * @param headers
	 * @param data
	 */
	public void addMatrix(String[] headers, String[][] data){
		matrixTablePane.setViewportView(RowHeaderTable.rowHeaderTable(headers, data));
	}
	
	public JPanel addFilesPanel(ActionListener listener){
		
		JPanel filesPanel = new JPanel();
		filesPanel.setLayout(new GridLayout(3,1));
		
		//Ontology file panel
		JPanel ontologyFilePanel = new JPanel(new BorderLayout());
		ontologyFileField = new JTextField(phenomizer.getOntologyPath());
		ontologyFileField.setName(LOAD_ONTO_COMMAND);
		JButton loadOntology = new JButton(LOAD_ONTO_COMMAND);
		loadOntology.setActionCommand(LOAD_ONTO_COMMAND);
		loadOntology.addActionListener(listener);
		
		ontologyFilePanel.add(new JLabel("Ontology: "), BorderLayout.WEST);
		ontologyFilePanel.add(ontologyFileField, BorderLayout.CENTER);
		ontologyFilePanel.add(loadOntology, BorderLayout.EAST);
		
		//Diseases file panel
		JPanel diseasesFilePanel = new JPanel(new BorderLayout());
		diseasesFileField = new JTextField(phenomizer.getAnnotationsPath());
		diseasesFileField.setName(LOAD_DISEASES_COMMAND);
		JButton loadDiseases = new JButton(LOAD_DISEASES_COMMAND);
		loadDiseases.setActionCommand(LOAD_DISEASES_COMMAND);
		loadDiseases.addActionListener(listener);
		
		diseasesFilePanel.add(new JLabel("Diseases Annotations: "), BorderLayout.WEST);
		diseasesFilePanel.add(diseasesFileField, BorderLayout.CENTER);
		diseasesFilePanel.add(loadDiseases, BorderLayout.EAST);
		
		//Genes file panel
		JPanel genesFilePanel = new JPanel(new BorderLayout());
//		genesFileField = new JTextField(phenomizer.getGenesAnnotationsPath());
		genesFileField.setName(LOAD_GENES_COMMAND);
		JButton loadGenes = new JButton(LOAD_GENES_COMMAND);
		loadGenes.setActionCommand(LOAD_GENES_COMMAND);
		loadGenes.addActionListener(listener);
		
		genesFilePanel.add(new JLabel("Genes Annotations: "), BorderLayout.WEST);
		genesFilePanel.add(genesFileField, BorderLayout.CENTER);
		genesFilePanel.add(loadGenes, BorderLayout.EAST);
		
		filesPanel.add(ontologyFilePanel);
		filesPanel.add(diseasesFilePanel);
		filesPanel.add(genesFilePanel);
	
		return filesPanel; 
	}
		
	
	/**
	 * set error message
	 * @return
	 */
	public void errorMessage(String str){
		message.setErrorMessage(str);		
	}
	
	/**
	 * set error message
	 * @return
	 */
	public void infoMessage(String str){
		message.setInfoMessage(str);		
	}
	
	/**
	 * hide message text
	 */
	public void hideErrorMessage(){
		message.hidesMessage();
	}
	
	
	/**
	 * Get text area value
	 * @return
	 */
	public String getContent(String componentName){
		Component c = Utils.findByName(this.getContentPane(), componentName);

		if (c!=null){
			if (c instanceof JTextArea)
				return ((JTextArea) c).getText();
			else
				return ((JTextField) c).getText();
		}
		
		return null;
	}
	
	
	/**
	 * Get text area value
	 * @return
	 */
	public void setContent(String componentName, String content){
		Component c = Utils.findByName(this.getContentPane(), componentName);

		if (c!=null){
			if (c instanceof JTextArea)
				((JTextArea) c).setText(content);
			else
				((JTextField) c).setText(content);
		}

	}
	
	/**
	 * Check if an item if selected in a combo box
	 * 
	 * @param option
	 * @return
	 */
	
	public String selectedItem(String componentName){	
		
		Component c = Utils.findByName(this.getContentPane(), componentName);
		
		if (c!=null)
			if (c instanceof JComboBox)
				return ((JComboBox) c).getSelectedItem().toString();

		return null;
	}
	

	
	/**
	 * Check if a radio button is selected
	 * 
	 * TODO: ampliar para checkbox
	 * 
	 * @param option
	 * @return
	 */
	
	public boolean optionSelected(String option){
		Component c = Utils.findByName(this.getContentPane(), option);
		
		if (c!=null && (c instanceof JRadioButton))
			return (((JRadioButton) c).isSelected());
			
		return false;
	}
	

}
