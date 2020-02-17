/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Armando
 */
@Entity
@Table(name = "avaliable_genes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvaliableGenes.findAll", query = "SELECT a FROM AvaliableGenes a"),
    @NamedQuery(name = "AvaliableGenes.findByGene", query = "SELECT a FROM AvaliableGenes a WHERE a.gene = :gene")})
public class AvaliableGenes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "gene")
    @Id
    private int gene;

    public AvaliableGenes() {
    }

    public int getGene() {
        return gene;
    }

    public void setGene(int gene) {
        this.gene = gene;
    }
    
}
