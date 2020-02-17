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
@Table(name = "avaliable_gene_symbol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AvaliableGeneSymbol.findAll", query = "SELECT a FROM AvaliableGeneSymbol a"),
    @NamedQuery(name = "AvaliableGeneSymbol.findByGene", query = "SELECT a FROM AvaliableGeneSymbol a WHERE a.gene = :gene"),
    @NamedQuery(name = "AvaliableGeneSymbol.findBySymbol", query = "SELECT a FROM AvaliableGeneSymbol a WHERE a.symbol = :symbol")})
public class AvaliableGeneSymbol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "gene")
    @Id
    private int gene;
    @Column(name = "symbol")
    private String symbol;

    public AvaliableGeneSymbol() {
    }

    public int getGene() {
        return gene;
    }

    public void setGene(int gene) {
        this.gene = gene;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
}
