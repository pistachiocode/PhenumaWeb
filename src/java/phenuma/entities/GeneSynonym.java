/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "gene_synonym")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GeneSynonym.findAll", query = "SELECT g FROM GeneSynonym g"),
    @NamedQuery(name = "GeneSynonym.findBySymbol", query = "SELECT g FROM GeneSynonym g WHERE g.symbol = :symbol"),
    @NamedQuery(name = "GeneSynonym.findByDbreference", query = "SELECT g FROM GeneSynonym g WHERE g.dbreference = :dbreference"),
    @NamedQuery(name = "GeneSynonym.findBySymbolandDbReference", query = "SELECT s FROM GeneSynonym s WHERE s.dbreference = :dbreference AND s.symbol = :symbol"),
    @NamedQuery(name = "GeneSynonym.findById", query = "SELECT g FROM GeneSynonym g WHERE g.id = :id")})
public class GeneSynonym implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "symbol")
    private String symbol;
    @Column(name = "dbreference")
    private String dbreference;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "entrezid", referencedColumnName = "entrezid")
    @ManyToOne(optional = false)
    private Gene entrezid;

    public GeneSynonym() {
    }

    public GeneSynonym(Integer id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDbreference() {
        return dbreference;
    }

    public void setDbreference(String dbreference) {
        this.dbreference = dbreference;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Gene getEntrezid() {
        return entrezid;
    }

    public void setEntrezid(Gene entrezid) {
        this.entrezid = entrezid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneSynonym)) {
            return false;
        }
        GeneSynonym other = (GeneSynonym) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "phenuma.entities.GeneSynonym[ id=" + id + " ]";
    }
    
}
