/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package phenuma.networkproyection;

import phenuma.network.Network;



import phenuma.network.NetworkDB;

/**
 * This class is used to manage the queries. All kind of queries have to extends this class.
 * 
 * @author Armando
 */
public abstract class NetworkQuery {
    
    protected int queryType;
    
    /*Query options*/
    protected int ontology;
    protected int associations;
    
    protected int semanticSimilarity;
    protected double threshold;
    
    protected Network result;
    protected NetworkDB resultDB;
    
    
    public NetworkQuery(int queryType) {
        this.queryType = queryType;
        this.threshold = 0.0;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public Network getResult() {
        return result;
    }

    public void setResult(Network result) {
        this.result = result;
    }

    public NetworkDB getResultDB() {
        return resultDB;
    }

    public void setResultDB(NetworkDB resultDB) {
        this.resultDB = resultDB;
    }
    
    public int getOntology() {
        return ontology;
    }

    public void setOntology(int ontology) {
        this.ontology = ontology;
    }

    public int getAssociations() {
        return associations;
    }

    public void setAssociations(int associations) {
        this.associations = associations;
    }

    public int getSemanticSimilarity() {
        return semanticSimilarity;
    }

    public void setSemanticSimilarity(int semanticSimilarity) {
        this.semanticSimilarity = semanticSimilarity;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

}
