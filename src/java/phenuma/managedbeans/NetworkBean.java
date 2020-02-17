/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.managedbeans;

import java.io.Serializable;
import phenuma.objects.PhenumaConstants;

/**
 *
 * @author Rocío Rodríguez López
 */
public class NetworkBean implements Serializable{
    
 
    private int inputType;
    private int outputType;
    private String items;
    private Double threshold;
    private int confidence;
    
    /** Creates a new instance of NetworkBean */
    public NetworkBean() {
        
        this.inputType = PhenumaConstants.ID_GENES_INPUT;
        this.outputType = 0;
        this.items = "";
        this.threshold = 1.8;
        this.confidence = PhenumaConstants.CONFIDENCE_HIGH_ID;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) 
    {
        this.items = items;
    }

    public int getOutputType() {
        return outputType;
    }

    public void setOutputType(int outputType) {
        this.outputType = outputType;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }
    
    
    
    
    
    
}