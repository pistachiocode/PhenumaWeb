/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.objects;

/**
 *
 * @author Rocío Rodríguez López
 */
public class SubnetworkType {
    
    public int    idNetwork;
    public String name;
    public String title;
    public String image;
    public String url;
    public String color;
    public String showHandle;
    public double step;

    /* Max/min score displayed. These variables 
     * are used to manage the sliders */
    public double maxValue;
    public double minValue;
    
    
    public SubnetworkType(int idNetwork, String name, String title, String image, String url) {
        this.idNetwork = idNetwork;
        this.name = name;
        this.title = title;
        this.image = image;
        this.url = url;
        
        this.maxValue = 0;
        this.minValue = 0;
        
        this.showHandle = "true";
    }

    public int getIdNetwork() {
        return idNetwork;
    }

    public void setIdNetwork(int idNetwork) {
        this.idNetwork = idNetwork;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShowHandle() {
        return showHandle;
    }

    public void setShowHandle(String showHandle) {
        this.showHandle = showHandle;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }
    
    
    
    
    
}