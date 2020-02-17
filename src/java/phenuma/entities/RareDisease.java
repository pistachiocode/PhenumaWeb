/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Armando
 */
@Entity
@Table(name = "rare_disease")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RareDisease.findAll", query = "SELECT r FROM RareDisease r"),
    @NamedQuery(name = "RareDisease.findByOrphanum", query = "SELECT r FROM RareDisease r WHERE r.orphanum = :orphanum"),
    @NamedQuery(name = "RareDisease.findByName", query = "SELECT r FROM RareDisease r WHERE r.name = :name")})
public class RareDisease implements Serializable {
    @Lob
    @Column(name = "description")
    private byte[] description;
    @Column(name = "prevalence")
    private Integer prevalence;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "orphanum")
    private Integer orphanum;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "rareDiseaseList")
    private List<Disease> diseaseList;
    @ManyToMany(mappedBy = "rareDiseaseList")
    private List<Gene> geneList;

    public RareDisease() {
    }

    public RareDisease(Integer orphanum) {
        this.orphanum = orphanum;
    }

    public RareDisease(Integer orphanum, String name) {
        this.orphanum = orphanum;
        this.name = name;
    }

    public Integer getOrphanum() {
        return orphanum;
    }

    public void setOrphanum(Integer orphanum) {
        this.orphanum = orphanum;
    }

    public String getName() throws PhenumaException {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Disease> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }

    @XmlTransient
    public List<Gene> getGeneList() {
        return geneList;
    }

    public void setGeneList(List<Gene> geneList) {
        this.geneList = geneList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orphanum != null ? orphanum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RareDisease)) {
            return false;
        }
        RareDisease other = (RareDisease) object;
        if ((this.orphanum == null && other.orphanum != null) || (this.orphanum != null && !this.orphanum.equals(other.orphanum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "phenuma.entities.RareDisease[ orphanum=" + orphanum + " ]";
    }

    public Integer getPrevalence() {
        return prevalence;
    }

    public void setPrevalence(Integer prevalence) {
        this.prevalence = prevalence;
    }

    public byte[] getDescription() {
        return description;
    }

    public void setDescription(byte[] description) {
        this.description = description;
    }
    
}
