/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.utils;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.interceptor.Interceptor;
import javax.transaction.Transactional;
import phenuma.objects.PhenumaConstants;
import javax.faces.bean.ViewScoped;

/**
 * Massage class
 * 
 * @author Rocío Rodríguez López
 */
@ViewScoped
public class Messages implements Serializable {
    
    private FacesMessage messagesblock = new FacesMessage();
    private FacesMessage messagesuccess = new FacesMessage();
    
    public static final String NOT_FOUND = "Not found";

    private static Messages instance = null;
    
  
    /**
     * Constructor. 
     */
    private Messages()
    {
       // messages = new ArrayList<FacesMessage>();
        
        messagesblock = new FacesMessage();
        messagesblock.setSeverity(FacesMessage.SEVERITY_INFO);
        
        messagesuccess = new FacesMessage();
        messagesuccess.setSeverity(FacesMessage.SEVERITY_INFO);

    }

    /**
     * Create a Messages instance. If the instance is not null, this method create it.
     * @return
     */
    public static Messages getInstance()
    {
        if (instance == null){
                instance = new Messages();
        }
        return instance;
    }

    
    public void show()
    {
        if(messagesblock.getDetail()!=null && !messagesblock.getDetail().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("", messagesblock);
        }
        
        if(messagesuccess.getDetail()!=null && !messagesuccess.getDetail().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage("", messagesuccess);
        }
        
        messagesblock = new FacesMessage();
        messagesblock.setSeverity(FacesMessage.SEVERITY_INFO);

        messagesuccess = new FacesMessage();
        messagesuccess.setSeverity(FacesMessage.SEVERITY_INFO);
    
    }
    
    public void reset(){
        messagesblock = new FacesMessage();
        messagesblock.setSeverity(FacesMessage.SEVERITY_INFO);
        
        messagesuccess = new FacesMessage();
        messagesuccess.setSeverity(FacesMessage.SEVERITY_INFO);
    }
    
    
    public void warningMessage(String summary, String detail){
        
        String finaldetail = messagesblock.getDetail();
        
        if(finaldetail!=null) {
            messagesblock.setDetail(finaldetail+" "+detail);
        }
        else {
            messagesblock.setDetail(detail);
        }
    }
    
    public void doneMessage(String summary, String detail){

        messagesuccess.setDetail(detail);
        
    }
            
       

    public void tooLargeMessage(int confidence)
    {
        String msg_confidence = "";
        if(confidence == PhenumaConstants.CONFIDENCE_LOW_ID)
        {
            msg_confidence = " or try with Medium or High level of confidence.";
        }
        else if (confidence == PhenumaConstants.CONFIDENCE_MEDIUM_ID)
        {
            msg_confidence = " or try with High level of confidence.";
        }

        FacesContext contex = FacesContext.getCurrentInstance();
        if(contex.getViewRoot().getViewId().equals("/main.xhtml")) {
            warningMessage("Network Vizualizer", "The network is too large. You can download the network in a text file using the link 'Download Network'"+msg_confidence);
        }
        else {
            warningMessage("Network Vizualizer", "The network is too large. You can download the network in a text file using the link 'Download' in the toolbar"+msg_confidence );
        }
    }
    

    
    //Details
    /**
     * Item not found in the database.
     * @param type
     * @param itemId
     * @return 
     */
    public static String itemNotFound(int type, List<String> itemId){   
        
        String object = "";
        if(PhenumaConstants.ID_GENES_INPUT == type) {
            object = "Gene(s)";
        }
        else if (PhenumaConstants.ID_OMIM_INPUT == type) {
            object = "Omim id(s)";
        }
        else if (PhenumaConstants.ID_ORPHANUM_INPUT == type) {
            object = "Orphanum id(s)";
        }
        else if (PhenumaConstants.ID_PHENOTYPE_INPUT == type) {
            object = "Hpo id(s)";
        }
        
        String msg = object+" ";
        for(String item : itemId)
        {
            msg = msg + item+", ";
        }
        
        msg = msg.substring(0, msg.length()-2);
        msg.lastIndexOf(",");
        msg = msg + " not found. That is because: (1) The input is not in the database (2) you have used an identifier that is not supported by "
                + "PhenUMA (see 'Input Identifiers' in Help Section) or (3) the "
                + "you have selected an incorrect input type. (see 'Quick Start' in Help Section)";
        
        return msg;
        
    }
    

    
}
