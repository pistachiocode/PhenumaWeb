/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.entities;

import java.io.Serializable;
import javax.persistence.Column;
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
@Table(name = "disease_gene")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiseaseGene.findAll", query = "SELECT d FROM DiseaseGene d"),
    @NamedQuery(name = "DiseaseGene.findByGene", query = "SELECT d FROM DiseaseGene d WHERE d.diseaseGenePK.gene = :gene"),
    @NamedQuery(name = "DiseaseGene.findByDisease", query = "SELECT d FROM DiseaseGene d WHERE d.diseaseGenePK.disease = :disease"),
    @NamedQuery(name = "DiseaseGene.findByScore", query = "SELECT d FROM DiseaseGene d WHERE d.score = :score")})
public class DiseaseGene implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DiseaseGenePK diseaseGenePK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "score")
    private Double score;
    @JoinColumn(name = "gene", referencedColumnName = "entrezid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Gene gene1;
    @JoinColumn(name = "disease", referencedColumnName = "omim", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Disease disease1;

    public DiseaseGene() {
    }

    public DiseaseGene(DiseaseGenePK diseaseGenePK) {
        this.diseaseGenePK = diseaseGenePK;
    }

    public DiseaseGene(int gene, int disease) {
        this.diseaseGenePK = new DiseaseGenePK(gene, disease);
    }

    public DiseaseGenePK getDiseaseGenePK() {
        return diseaseGenePK;
    }

    public void setDiseaseGenePK(DiseaseGenePK diseaseGenePK) {
        this.diseaseGenePK = diseaseGenePK;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Gene getGene1() {
        return gene1;
    }

    public void setGene1(Gene gene1) {
        this.gene1 = gene1;
    }

    public Disease getDisease1() {
        return disease1;
    }

    public void setDisease1(Disease disease1) {
        this.disease1 = disease1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (diseaseGenePK != null ? diseaseGenePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiseaseGene)) {
            return false;
        }
        DiseaseGene other = (DiseaseGene) object;
        if ((this.diseaseGenePK == null && other.diseaseGenePK != null) || (this.diseaseGenePK != null && !this.diseaseGenePK.equals(other.diseaseGenePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "phenuma.entities.DiseaseGene[ diseaseGenePK=" + diseaseGenePK + " ]";
    }
    
}
