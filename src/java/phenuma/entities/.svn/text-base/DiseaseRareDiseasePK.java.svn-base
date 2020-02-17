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
public class DiseaseRareDiseasePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "omim")
    private int omim;
    @Basic(optional = false)
    @Column(name = "orphanum")
    private int orphanum;

    public DiseaseRareDiseasePK() {
    }

    public DiseaseRareDiseasePK(int omim, int orphanum) {
        this.omim = omim;
        this.orphanum = orphanum;
    }

    public int getOmim() {
        return omim;
    }

    public void setOmim(int omim) {
        this.omim = omim;
    }

    public int getOrphanum() {
        return orphanum;
    }

    public void setOrphanum(int orphanum) {
        this.orphanum = orphanum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) omim;
        hash += (int) orphanum;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiseaseRareDiseasePK)) {
            return false;
        }
        DiseaseRareDiseasePK other = (DiseaseRareDiseasePK) object;
        if (this.omim != other.omim) {
            return false;
        }
        if (this.orphanum != other.orphanum) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "phenuma.entities.DiseaseRareDiseasePK[ omim=" + omim + ", orphanum=" + orphanum + " ]";
    }
    
}
