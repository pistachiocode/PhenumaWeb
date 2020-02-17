/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.network;

import ontologizer.go.TermID;

/**
 *
 * @author Armando
 */
public class EdgeSemanticInfo {
    
    private TermID termX;
    private TermID termY;
    private TermID termMax;
    private String termMaxName;
    private Double score;

    public EdgeSemanticInfo(TermID termX, TermID termY, TermID termMax, Double score) {
        this.termX = termX;
        this.termY = termY;
        this.termMax = termMax;
        this.score = score;
    }
    
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public TermID getTermMax() {
        return termMax;
    }

    public void setTermMax(TermID termMax) {
        this.termMax = termMax;
    }

    public TermID getTermX() {
        return termX;
    }

    public void setTermX(TermID termX) {
        this.termX = termX;
    }

    public TermID getTermY() {
        return termY;
    }

    public void setTermY(TermID termY) {
        this.termY = termY;
    }

    public String getTermMaxName() {
        return termMaxName;
    }

    public void setTermMaxName(String termMaxName) {
        this.termMaxName = termMaxName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.termMax != null ? this.termMax.hashCode() : 0);
        return hash;
    }
    
    @Override
        public boolean equals(Object o)
    {
        if (o instanceof EdgeSemanticInfo)
        {
            EdgeSemanticInfo edge = (EdgeSemanticInfo)o;
            
            return edge.termMax.id == this.termMax.id;
            
        }
        else
        {
            return false;
        }
        
    }
    
    
    
}
