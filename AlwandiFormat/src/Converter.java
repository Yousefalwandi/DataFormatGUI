import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Converter {
	private File fileIn;
	private File fileOut;
	
	public void Converter() {}
	
	public void setIn(File in) {
		this.fileIn = in;
	}
	
	public void setOut(File out) {
		this.fileOut = out;
	}
	
	public void convert() throws ParserConfigurationException, TransformerException, FileNotFoundException {
		List<Map<Integer, String>> res = loadCSV();
		
		saveXML(res);
	}

	private List<Map<Integer ,String>> loadCSV() throws FileNotFoundException{
        List<Map<Integer, String>> csvData = new ArrayList<>();
 
        // Read whole file line by line
        Scanner scanner = new Scanner(fileIn);
            while(scanner.hasNextLine()){
 
                // Read each line and get their columns
                HashMap<Integer, String> csvRes = new HashMap<>();
 
                try(Scanner scanner2 = new Scanner(scanner.nextLine())){
                    scanner2.useDelimiter(","); // Split them by commas
 
                    // Add all data to row list
                    int i = 0;
                    while(scanner2.hasNext()){
                    	String val = scanner2.next();
                    	
                    	// Skip empty values
                    	if(!val.trim().isEmpty())
                    		csvRes.put(i, val);
                    	
                    	i++;
                    }
                }
 
                // Add row to overall data
                csvData.add(csvRes);
            }
        
 
        return csvData;
    }
 
    private void saveXML(List<Map<Integer, String>> data) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
 
        // Root element
        Element rootElement = doc.createElement("XMLdoc");
        doc.appendChild(rootElement);
 
 
        int j = 0;
        for(Map<Integer, String> row : data){
            // Root name of columns
            Element rowEl = null;
            if(j == 0)
                rowEl = doc.createElement("header");
            else
                rowEl = doc.createElement("row");
 
            rootElement.appendChild(rowEl);
 
            // Add data to row
            for (Map.Entry<Integer, String> set : row.entrySet()) {
            	// Create element ( a column in a row)
                Element current = doc.createElement("column" + set.getKey());
                current.appendChild(doc.createTextNode(set.getValue()));
                
                // Append column to row
                rowEl.appendChild(current);
            }
            j++;
        }
 
        // Create & write to XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
 
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(fileOut);
        transformer.transform(source, result);
    }
}
