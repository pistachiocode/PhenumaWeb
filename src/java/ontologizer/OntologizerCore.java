package ontologizer;

import java.io.*;
import java.util.*;

import ontologizer.association.AssociationContainer;
import ontologizer.association.AssociationParser;
import ontologizer.calculation.CalculationRegistry;
import ontologizer.calculation.EnrichedGOTermsResult;
import ontologizer.calculation.ICalculation;
import ontologizer.go.Ontology;
import ontologizer.go.OBOParser;
import ontologizer.go.OBOParserException;
import ontologizer.go.TermContainer;
import ontologizer.statistics.AbstractTestCorrection;
import ontologizer.statistics.IResampling;
import ontologizer.statistics.TestCorrectionRegistry;
import ontologizer.types.ByteString;

/**
 * The OntologizerCore class controls parsing and output of Gene Ontology
 * cluster information. It forms a common interface between the main methods in
 * the class OntologizerCMD (Command line) and OntoFrame (GUI version) to the
 * rest of the program logic of the Ontologizer.
 * 
 * @author Peter Robinson, Sebastian Bauer
 */
public class OntologizerCore
{
	/**
	 * 
	 * @author Sebastian Bauer
	 *
	 * This class is indented to pass arguments to the OntologizerCore
	 * constructor.
	 */
	public static class Arguments
	{
		/** gene_ontology.obo file (and path) */
		public String goTermsOBOFile;

		/** gene_association.* file (and path) */
		public String associationFile;

		/** Name of the study file (or directory) */
		public String studySet;

		/** Only study input files with this suffix are considered */
		public String suffix;

		/** Name of the main output File */
		public String mainOutputName;
		
		/** Name of the calculation (e.g. singlestep) */
		public String calculationName;

		/** Name of the multiple test correction procedure */
		public String correctionName;

		/**
		 * Number of resampling steps in case of a resampling
		 * based mtc procedure
		 */
		public int resamplingSteps;
		
		/**
		 * Tolerance in percent for Westfall-Young-Approximate
		 */
		public int sizeTolerance;

		/** Minium number of association to be displayed */
		public int minAssociationCount;

		/** Specifies the population file containing the whole gene population, might be null */
		public String populationFile;
		
		/** Should genes be filtered out, from which there exists no annotation? */
		public boolean filterOutUnannotatedGenes;

		/** Specifies the filter file */
		public String filterFile;
	};

	/** Contains all avaiable GOTerms */
	private TermContainer goTerms;

	/** The graph to the gene ontolgy */
	private Ontology goGraph;

	/** List of all studies being analyzed */
	private StudySetList studySetList;
	
	/** Results of the analized studies */
	private StudySetResultList studySetResultList;

	/** Collection of all valid annotations for current dataset */
	private AssociationContainer goAssociations;

	/** The kind of calculation which should be performed */
	private ICalculation calculation;

	/** The correction which should be used */
	private AbstractTestCorrection testCorrection;
	
	/** The population object. If this is set we do the hypergeometric
	 * analysis basded on the scheme in GeneMerge. */
	private PopulationSet populationSet;

	/**
	 * Construct the object.
	 *
	 * @param args specifies the arguments
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws myException 
	 */
	public OntologizerCore(Arguments args) throws FileNotFoundException, IOException, OBOParserException
	{
		/* Set the desired calculation method or the default */
		calculation = CalculationRegistry.getCalculationByName(args.calculationName);
		if (calculation == null)
			calculation = CalculationRegistry.getDefault();

		/* Set the desired test correction or set the default */
		testCorrection = TestCorrectionRegistry.getCorrectionByName(args.correctionName);
		if (testCorrection == null)
			testCorrection = TestCorrectionRegistry.getDefault();
		/*Empty cache for resampling based MTCs and set number of sampling steps */
		if (testCorrection instanceof IResampling) {
			IResampling resampling = (IResampling) testCorrection;
			resampling.resetCache();
			if (args.resamplingSteps > 0) {
				resampling.setNumberOfResamplingSteps(args.resamplingSteps);
				
			}
			if (args.sizeTolerance > 0) {
				resampling.setSizeTolerance(args.sizeTolerance);
			}
		}

		/* Parse the gene_ontology.obo file to get information about all terms.
		 * Transfer the information to a TermContainer object.
		 */
		System.out.println("Parse obo file");
		OBOParser oboParser = new OBOParser(args.goTermsOBOFile);
		System.out.println(oboParser.doParse());
		goTerms = new TermContainer(oboParser.getTermMap(), oboParser.getFormatVersion(), oboParser.getDate());
		System.out.println("Building graph");
		goGraph = new Ontology(goTerms); 

		/* create the study list. A directory or a single file might be given */
		File studyFile = new File(args.studySet);
		if (studyFile.isDirectory())
		{
			studySetList = new StudySetList(args.studySet,args.suffix);
		} else
		{
			/* Create a study list with a dummy name and add the study manually */
			studySetList = new StudySetList("study");
			studySetList.addStudySet(StudySetFactory.createFromFile(studyFile, false));
		}

		/* create the population set TODO: Get rid of the casting */
		populationSet = (PopulationSet)StudySetFactory.createFromFile(new File(args.populationFile), true);

		/* Apply the optional gene name mapping given by the supplied filter file */
		if (args.filterFile != null)
		{
			System.err.println("Parsing filter");
			GeneFilter filter = new GeneFilter(new File(args.filterFile));
			
			System.err.println("Appling filter");
			populationSet.applyFilter(filter);

			for (StudySet studySet : studySetList)
				studySet.applyFilter(filter);
		}

		/* Check now if all study genes are included within the population,
		 * if a partiulcar gene is not contained, add it */
		for (ByteString geneName : studySetList.getGeneSet())
		{
			if (!populationSet.contains(geneName))
				populationSet.addElement(geneName,"");
		}

		/* Parse the GO association file containing GO annotations for genes or gene
		 * products. Results are placed in associationparser.
		 */
		AssociationParser ap = new AssociationParser(args.associationFile,goTerms,populationSet.getAllNodesNames());
		goAssociations = new AssociationContainer(ap.getAssociations(), ap.getSynonym2gene(), ap.getDbObject2gene(), true, null);

		/* Filter out duplicate genes (i.e. different gene names refering
		 * to the same gene) */
		populationSet.filterOutDuplicateGenes(goAssociations);
		for (StudySet study : studySetList)
			study.filterOutDuplicateGenes(goAssociations);

		if (args.filterOutUnannotatedGenes)
		{
			/* Filter out genes within the study without any annotations */
			for (StudySet study : studySetList)
				study.filterOutAssociationlessGenes(goAssociations);

			/* Filter out genes within the population which doesn't have an annotation */
			populationSet.filterOutAssociationlessGenes(goAssociations);
		}
	}

	/**
	 * Returns the iterator over all results.
	 * 
	 * @return the iterator.
	 */
	public Iterator<EnrichedGOTermsResult> studySetResultIterator()
	{
		/* Create a dummy list in case no results were available */
		if (studySetResultList == null)
			studySetResultList = new StudySetResultList();

		return studySetResultList.iterator();
	}

	/**
	 * Perform the statistical calculation using the given calculation
	 * procedure accumulation the results into the studySetResultList
	 */
	public void calculate()
	{
		assert(populationSet != null);
		studySetResultList = new StudySetResultList();
		
		for (StudySet studySet : studySetList)
		{
			studySetResultList.addStudySetResult(
					calculation.calculateStudySet(goGraph,goAssociations,populationSet,studySet,testCorrection)
					);

			/* Reset the counter and enumerator items here. It is not necessarily
			 * nice to place it here, but for the moment it's the easiest way
			 */
			studySet.resetCounterAndEnumerator();
		}
	}
	
	private Iterator<StudySet> studySetIter;

	/**
	 * Perform the statistical calculation of the next study. When called
	 * first, the first study is considered as the next study.
	 * 
	 * @return the result of the calculation or null, if no more studies
	 *         are available.
	 */
	public EnrichedGOTermsResult calculateNextStudy()
	{
		assert(populationSet != null);
		if (studySetIter == null) studySetIter = studySetList.iterator();
		if (!studySetIter.hasNext())
		{
			return null;
		}

		StudySet studySet = studySetIter.next();
		EnrichedGOTermsResult studySetResult = calculation.calculateStudySet(goGraph,goAssociations,populationSet,studySet,testCorrection);

		/* Reset the counter and enumerator items here. It is not necessarily
		 * nice to place it here, but for the moment it's the easiest way
		 */
		studySet.resetCounterAndEnumerator();
		return studySetResult;
	}

	public AssociationContainer getGoAssociations()
	{
		return goAssociations;
	}

	public Ontology getGoGraph()
	{
		return goGraph;
	}

	public TermContainer getGoTerms()
	{
		return goTerms;
	}

	public PopulationSet getPopulationSet()
	{
		return populationSet;
	}
	
	public String getCalculationName()
	{
		return calculation.getName();
	}
	
	public String getTestCorrectionName()
	{
		return testCorrection.getName();
	}
}
