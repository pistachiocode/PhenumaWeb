package ontologizer.association;

import java.util.*;

import ontologizer.go.ParentTermID;
import ontologizer.go.Term;
import ontologizer.types.ByteString;

/**
 * After AssociationParser was used to parse the gene_association.XXX file, this
 * class is used to store and process the information about Associations.
 */
public final class AssociationContainer implements Iterable<PP2Associations>
{
	/** Mapping from gene (or gene product) names to Association objects */
	//UDT_JUL:rename gene2assocs variable
	//OLD: private HashMap<ByteString, PP2Associations> gene2assocs;
	private HashMap<ByteString, PP2Associations> pp2assocs;

	/** Mapping of synonyms to gene names */
	private HashMap<ByteString, ByteString> synonym2gene;

	/** <I>key</I>: dbObject <I>value</I>: main gene name (dbObject_Symbol) */
	private HashMap<ByteString, ByteString> dbObject2gene;
	
	/** <I>key</I>: dbReference <I>value</I>: main gene name (dbObject_Symbol) */
	//private HashMap<Integer, ByteString> dbReference2gene;

	/**
	 * Total number of annotations available for the genes in our dataset.
	 */
	private int totalAnnotations;

	/**
	 * The constructor receives data from the AssociationParser object, which
	 * does the basic work of Parsing a gene_association file. The constructor
	 * takes an array list of associations, and classifies them according to
	 * gene (one gene can have multiple annotations) in Gene2Association
	 * objects.
	 * 
	 * @param assocFile
	 *            name of the gene_associations.XXX file
	 * @param assocs
	 *            a list of all Associations refererring to genes of the current
	 *            dataset
	 * @param s2g
	 *            HashMap of synonyms for gene names extracted from the
	 *            association file
	 * @param dbo2g
	 *            HashMap of mappings from database objects (e.g., accession
	 *            numbers) to gene names.
	 * @param dbr2g
	 *            HashMap of mappings database reference (e.g., accession
	 *            numbers) to gene names.
	 * @see PP2Associations
	 * @see AssociationParser
	 */
	public AssociationContainer(
			ArrayList<Association> assocs, 
			HashMap<ByteString, ByteString> s2g,
			HashMap<ByteString, ByteString> dbo2g, boolean iea,
			ArrayList<Term> allowedTerms)
	{
                this(assocs, s2g, dbo2g, iea, null, allowedTerms);
	}


        
        /**
         * OJO!! Chapuzilla porque java.lang.NoSuchMethodError: ontologizer.association.AssociationContainer. para Distributions
         * @param assocs
         * @param s2g
         * @param dbo2g
         * @param allowedTerms 
         */
        public AssociationContainer(
			ArrayList<Association> assocs, 
			HashMap<ByteString, ByteString> s2g,
			HashMap<ByteString, ByteString> dbo2g, 
			ArrayList<Term> allowedTerms)
	{
                
                this(assocs, s2g, dbo2g, true, null, allowedTerms);
	}

                
        public AssociationContainer(ArrayList<Association> assocs, 
                                     HashMap<ByteString, ByteString> s2g,
                                     HashMap<ByteString, ByteString> dbo2g, boolean iea, ByteString dbName,
                                     ArrayList<Term> allowedTerms)
	{
                synonym2gene = s2g;
		dbObject2gene = dbo2g;

		totalAnnotations = 0;
		pp2assocs = new HashMap<ByteString, PP2Associations>();
	
		for (Association a : assocs)
                { 
                    if(dbName!=null)
                    {                       
                        if(dbName.equals(a.getDBName()))
                        {
                            if (iea){
                                if (a.getEvidence().equals(new ByteString("IEA")))
                                    addAssociation(a, allowedTerms);
                            }
                            else
                                addAssociation(a, allowedTerms);
                        }

                    }
                    else
                        addAssociation(a, allowedTerms);
                    
                }
	}
        
	/**
	 * Constructor for an empty container.
	 * 
	 * @see addAssociation
	 */
	public AssociationContainer()
	{
		synonym2gene = new HashMap<ByteString,ByteString>();
		dbObject2gene = new HashMap<ByteString, ByteString>();
		pp2assocs = new HashMap<ByteString, PP2Associations>();
	//	dbReference2gene = new HashMap<Integer, ByteString>();
		
		totalAnnotations = 0;
	}


	/**
	 * Adds a new association.
         * 
         * allowedTerms: Only the associations with allowedTerms will be added. The allowed
         * terms are the terms included in the current subontology used.
	 * 
	 * @param a
	 */
	public void addAssociation(Association a, ArrayList<Term> allowedTerms)
	{
		totalAnnotations++;
		PP2Associations g2a = null;

		ParentTermID [] parents = null;
		Term term = new Term(a.getTermID(), null, parents); //create a Term object with the termID value of the associations
		
		if (allowedTerms.contains(term)){
		
			/*UDT_JUN: db_object_id instead of object symbol*/
			//OLD: if (gene2assocs.containsKey(a.getObjectSymbol()))
			if(pp2assocs.containsKey(a.getDB_Object()))
			{
				//OLD: g2a = gene2assocs.get(a.getObjectSymbol());
				g2a = pp2assocs.get(a.getDB_Object());
                                
				g2a.add(a); // Add the Association to existing g2a
			} 
			else
			{
				// Otherwise create new Gene2Associations object
				// for this gene.
				//OLD: g2a = new Gene2Associations(a.getObjectSymbol());
				g2a = new PP2Associations(a.getDB_Object(), a.getObjectSymbol(), a.getDbReference());
				g2a.add(a);
			
				//OLD: gene2assocs.put(a.getObjectSymbol(), g2a);
				pp2assocs.put(a.getDB_Object(), g2a); //get the db object instead of object symbol
			}
			
		}
	}
        
        
        /**
	 * Adds a new association.
         * 
         * allowerTerms: Only the associations with allowedTerms will be added. The allowed
         * terms are the terms included in the current subontology used.
         * 
         * evide
	 * 
	 * @param a
	 */
	public void addAssociation(Association a, ArrayList<Term> allowedTerms, ArrayList<ByteString> evidenceCodes)
	{
		totalAnnotations++;
		PP2Associations g2a = null;

		ParentTermID [] parents = null;
		Term term = new Term(a.getTermID(), null, parents); //create a Term object with the termID value of the associations
		
		if (allowedTerms.contains(term)){
		
			/*UDT_JUN: db_object_id instead of object symbol*/
			//OLD: if (gene2assocs.containsKey(a.getObjectSymbol()))
			if(pp2assocs.containsKey(a.getDB_Object()))
			{
				//OLD: g2a = gene2assocs.get(a.getObjectSymbol());
				g2a = pp2assocs.get(a.getDB_Object());
				g2a.add(a); // Add the Association to existing g2a
			} 
			else
			{
				// Otherwise create new Gene2Associations object
				// for this gene.
				//OLD: g2a = new Gene2Associations(a.getObjectSymbol());
				g2a = new PP2Associations(a.getDB_Object(), a.getObjectSymbol(), a.getDbReference());
				g2a.add(a);
			
				//OLD: gene2assocs.put(a.getObjectSymbol(), g2a);
				pp2assocs.put(a.getDB_Object(), g2a); //get the db object instead of object symbol
			}
			
		}
	}
	
	

	/** For debugging */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("*****\n---AssociationContainer---\n*****\n");
		sb.append("Total annotations: " + totalAnnotations + "\n");
		sb.append("Number of genes with associations: " + pp2assocs.size()
				+ "\n");
		sb.append("Number of synonyms: " + synonym2gene.size() + "\n");
		sb.append("Number of dbo -> genename mappings: " + dbObject2gene.size()
				+ "\n");
		return sb.toString();
	}

	/**
	 * get a Gene2Associations object corresponding to a given gene name. If the
	 * name is not initially found as dbObject Symbol, (which is usually a
	 * database name with meaning to a biologist), try dbObject (which may be an
	 * accession number or some other term from the association database), and
	 * finally, look for a synonym (another entry in the gene_association file
	 * that will have been parsed into the present object).
	 * 
	 * @param geneName
	 */
	public PP2Associations get(ByteString geneName)
	{
		PP2Associations g2a = pp2assocs.get(geneName);
		if (g2a == null)
		{
			ByteString dbObject = dbObject2gene.get(geneName);
			g2a = pp2assocs.get(dbObject);
		}
		if (g2a == null)
		{
			ByteString synonym = synonym2gene.get(geneName);
			g2a = pp2assocs.get(synonym);
		}
		return g2a;
		
	}

	/**
	 * Returns whether the given name is a object symbol. 
	 * 
	 * @param name
	 * @return
	 */
	public boolean isObjectSymbol(ByteString name)
	{
		return pp2assocs.containsKey(name);
	}
	
	/**
	 * Returns whether the given name is an object id. 
	 * 
	 * @param name
	 * @return
	 */
	public boolean isObjectID(ByteString name)
	{
		return dbObject2gene.containsKey(name);
	}
	
	/**
	 * Returns whether the given name is a synonym.
	 * 
	 * @param name
	 * @return
	 */
	public boolean isSynonym(ByteString name)
	{
		return synonym2gene.containsKey(name);
	}

	/**
	 * A way to get all annotated genes in the container
	 * 
	 * @author Steffen Grossmann
	 * 
	 * @return The annotated genes as a Set
	 */
	public Set<ByteString> getAllAnnotatedPP()
	{
		return pp2assocs.keySet();
	}
	
	/**
	 * Get all annotated Phenotipic Profile of a type
	 * @param type
	 * @return
	 */
/*	
	public Set<ByteString> getAllAnnotatedPP(int type)
	{
		Set<ByteString> pp_names = new HashSet<ByteString>();
		Iterator<ByteString> iter = pp2assocs.keySet().iterator();
		while(iter.hasNext()){
			PP2Associations a = pp2assocs.get(iter.next());
			if (a.getType() == type)
				pp_names.add(a.name());
		}
		return pp_names;
	}*/

	public boolean containsPP(ByteString g1)
	{
		return get(g1) != null;
	}

	public Iterator<PP2Associations> iterator()
	{
		return pp2assocs.values().iterator();
	}


	
	
}
