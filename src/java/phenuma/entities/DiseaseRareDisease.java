/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Armando
 */
@Entity
@Table(name = "disease_rare_disease")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiseaseRareDisease.findAll", query = "SELECT d FROM DiseaseRareDisease d"),
    @NamedQuery(name = "DiseaseRareDisease.findByOmim", query = "SELECT d FROM DiseaseRareDisease d WHERE d.diseaseRareDiseasePK.omim = :omim"),
    @NamedQuery(name = "DiseaseRareDisease.findByOrphanum", query = "SELECT d FROM DiseaseRareDisease d WHERE d.diseaseRareDiseasePK.orphanum = :orphanum")})
public class DiseaseRareDisease implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DiseaseRareDiseasePK diseaseRareDiseasePK;
    @JoinColumn(name = "orphanum", referencedColumnName = "orphanum", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RareDisease rareDisease;
    @JoinColumn(name = "omim", referencedColumnName = "omim", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Disease disease;

    public DiseaseRareDisease() {
    }

    public DiseaseRareDisease(DiseaseRareDiseasePK diseaseRareDiseasePK) {
        this.diseaseRareDiseasePK = diseaseRareDiseasePK;
    }

    public DiseaseRareDisease(int omim, int orphanum) {
        this.diseaseRareDiseasePK = new DiseaseRareDiseasePK(omim, orphanum);
    }

    public DiseaseRareDiseasePK getDiseaseRareDiseasePK() {
        return diseaseRareDiseasePK;
    }

    public void setDiseaseRareDiseasePK(DiseaseRareDiseasePK diseaseRareDiseasePK) {
        this.diseaseRareDiseasePK = diseaseRareDiseasePK;
    }

    public RareDisease getRareDisease() {
        return rareDisease;
    }

    public void setRareDisease(RareDisease rareDisease) {
        this.rareDisease = rareDisease;
    }

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (diseaseRareDiseasePK != null ? diseaseRareDiseasePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiseaseRareDisease)) {
            return false;
        }
        DiseaseRareDisease other = (DiseaseRareDisease) object;
        if ((this.diseaseRareDiseasePK == null && other.diseaseRareDiseasePK != null) || (this.diseaseRareDiseasePK != null && !this.diseaseRareDiseasePK.equals(other.diseaseRareDiseasePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "phenuma.entities.DiseaseRareDisease[ diseaseRareDiseasePK=" + diseaseRareDiseasePK + " ]";
    }
    
}
