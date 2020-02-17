/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import phenuma.utils.PhenumaException;

/**
 *
 * @author Rocío Rodríguez López
 */
@Entity
@Table(name = "disease")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Disease.findAll", query = "SELECT d FROM Disease d"),
    @NamedQuery(name = "Disease.findByOmim", query = "SELECT d FROM Disease d WHERE d.omim = :omim"),
    @NamedQuery(name = "Disease.findByName", query = "SELECT d FROM Disease d WHERE d.name = :name")})
public class Disease implements Serializable {
    @Lob
    @Column(name = "description")
    private byte[] description;
    @Basic(optional = false)
    @Column(name = "pheno_mapping_key")
    private int phenoMappingKey;
    @JoinTable(name = "disease_rare_disease", joinColumns = {
        @JoinColumn(name = "omim", referencedColumnName = "omim")}, inverseJoinColumns = {
        @JoinColumn(name = "orphanum", referencedColumnName = "orphanum")})
    @ManyToMany
    private List<RareDisease> rareDiseaseList;
    @ManyToMany(mappedBy = "diseaseList")

    @Basic(optional = false)
    @Column(name = "type")
    private int type;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "omim")
    private Integer omim;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disease1", fetch = FetchType.LAZY)
    private List<DiseaseGene> diseaseGeneList;


    public Disease() {
    }


    public Disease(Integer omim) {
        this.omim = omim;
    }
    
    public Integer getOmim() {
        return omim;
    }

    public void setOmim(Integer omim) {
        this.omim = omim;
    }

    public String getName() throws PhenumaException {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<DiseaseGene> getDiseaseGeneList() throws PhenumaException {
        return diseaseGeneList;
    }

    public void setDiseaseGeneList(List<DiseaseGene> diseaseGeneList) {
        this.diseaseGeneList = diseaseGeneList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (omim != null ? omim.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Disease)) {
            return false;
        }
        Disease other = (Disease) object;
        if ((this.omim == null && other.omim != null) || (this.omim != null && !this.omim.equals(other.omim))) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return  omim.toString();
    }
    
    
    /**
     * List of genes related with a disease.
     * 
     * @return List<Gene> list of genes
     */
    public List<Gene> getGeneList() throws PhenumaException{

        List<Gene> geneList = new ArrayList<Gene>();
        
        Iterator<DiseaseGene> iter = this.getDiseaseGeneList().iterator();
        
        while(iter.hasNext()){
            DiseaseGene dg = iter.next();
            
            geneList.add(dg.getGene1());
        }
        
        return geneList;
    }

    public int getType() throws PhenumaException {          
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @XmlTransient
    public List<RareDisease> getRareDiseaseList() throws PhenumaException {
        return rareDiseaseList;
    }

    public void setRareDiseaseList(List<RareDisease> rareDiseaseList) {
        this.rareDiseaseList = rareDiseaseList;
    }

    public int getPhenoMappingKey() {
        return phenoMappingKey;
    }

    public void setPhenoMappingKey(int phenoMappingKey) {
        this.phenoMappingKey = phenoMappingKey;
    }

    public byte[] getDescription() {
        return description;
    }

    public void setDescription(byte[] description) {
        this.description = description;
    }

}
