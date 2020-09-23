import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

import java.io.FileNotFoundException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Controller {
	protected Shell shell;
	
	private final JFileChooser csvSelect = new JFileChooser();
	private final JFileChooser xmlSelect = new JFileChooser();
	
	private Text csvTextHolder;
	private Text xmlTextholder;
	
	private static Converter converter;

	/**
	 * Open the window.
	 */
	public void open() {
		converter = new Converter(); // Init converter
		
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(552, 331);
		shell.setText("CSV Converter");
		shell.setLayout(new GridLayout(6, false));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button chooseCSVBtn = new Button(shell, SWT.NONE);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv", "csv");
		csvSelect.setFileFilter(filter);
		
		chooseCSVBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				int returnVal = csvSelect.showOpenDialog(null);
				if(returnVal == csvSelect.APPROVE_OPTION) {
					csvTextHolder.setText(csvSelect.getSelectedFile().getName());
					converter.setIn(csvSelect.getSelectedFile());
				}
			}
		});
		chooseCSVBtn.setText("Open CSV");
		
		csvTextHolder = new Text(shell, SWT.BORDER);
		csvTextHolder.setEditable(false);
		csvTextHolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Button chooseXMLBtn = new Button(shell, SWT.NONE);
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("XML files", "xml", "xml");
		xmlSelect.setFileFilter(filter2);
		chooseXMLBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				int returnVal = xmlSelect.showSaveDialog(null);
				if(returnVal == xmlSelect.APPROVE_OPTION) {
					xmlTextholder.setText(xmlSelect.getSelectedFile().getName());
					converter.setOut(xmlSelect.getSelectedFile());
				}
			}
		});
		chooseXMLBtn.setText("XML Path");
		
		xmlTextholder = new Text(shell, SWT.BORDER);
		xmlTextholder.setEditable(false);
		xmlTextholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		
		Button convertBtn = new Button(shell, SWT.NONE);


		Label statusLabel = new Label(shell, SWT.NONE);
		statusLabel.setText("Select files to convert");
		statusLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1));
		
		convertBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(csvSelect.getSelectedFile() == null) {
					statusLabel.setText("No CSV file selected");
					return;
				}
				
				if(xmlSelect.getSelectedFile() == null) {
					statusLabel.setText("No XML file selected");
					return;
				}
				
				statusLabel.setText("Converting files...");
				
				try {
					converter.convert();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					statusLabel.setText("Failed to read CSV file");
					
					return;
				} catch (ParserConfigurationException | TransformerException e1) {
					// TODO Auto-generated catch block
					statusLabel.setText("Failed to convert");
				}
				
				statusLabel.setText("Finished converting ");
			}
		});
		convertBtn.setText("Convert");
	}
	

}
