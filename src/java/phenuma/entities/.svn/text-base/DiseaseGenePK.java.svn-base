/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Armando
 */
@Embeddable
public class DiseaseGenePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "gene")
    private int gene;
    @Basic(optional = false)
    @Column(name = "disease")
    private int disease;

    public DiseaseGenePK() {
    }

    public DiseaseGenePK(int gene, int disease) {
        this.gene = gene;
        this.disease = disease;
    }

    public int getGene() {
        return gene;
    }

    public void setGene(int gene) {
        this.gene = gene;
    }

    public int getDisease() {
        return disease;
    }

    public void setDisease(int disease) {
        this.disease = disease;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) gene;
        hash += (int) disease;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiseaseGenePK)) {
            return false;
        }
        DiseaseGenePK other = (DiseaseGenePK) object;
        if (this.gene != other.gene) {
            return false;
        }
        if (this.disease != other.disease) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "phenuma.entities.DiseaseGenePK[ gene=" + gene + ", disease=" + disease + " ]";
    }
    
}
