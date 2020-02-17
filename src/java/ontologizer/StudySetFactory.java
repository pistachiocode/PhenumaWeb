package ontologizer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ontologizer.parser.AbstractItemParser;
import ontologizer.parser.IParserCallback;
import ontologizer.parser.ItemAttribute;
import ontologizer.parser.ParserFactory;
import ontologizer.types.ByteString;
import phenomizer.utils.StringUtils;
import phenuma.entities.Disease;
import phenuma.entities.Gene;
import phenuma.entities.RareDisease;

/**
 * Create study sets conveniently
 * 
 * @author Sebastian Bauer
 */
public class StudySetFactory
{
	private static Logger logger = Logger.getLogger(StudySetFactory.class.getCanonicalName());

	private StudySetFactory(){};
	
	/**
	 * Creates a study set from a file.
	 * 
	 * @param file
	 * @param isPopulation
	 * @return
	 * @throws IOException
	 */
	public static StudySet createFromFile(File file, boolean isPopulation) throws IOException
	{
		logger.info("Processing studyset " + file.toString());

		/* Removing suffix from filename */
		String name = file.getName();
		Pattern suffixPat = Pattern.compile("\\.[a-zA-Z0-9]+$");
		Matcher m = suffixPat.matcher(name);
		name = m.replaceAll("");

		AbstractItemParser itemParser = ParserFactory.getNewInstance(file);
		StudySet newStudySet = createFromParser(itemParser,isPopulation);
		newStudySet.setName(name);
		return newStudySet;
	}

	/**
	 * Creates a study set from an array.
	 * 
	 * @param entries
	 * @param isPopulation
	 * @return
	 * @throws IOException
	 */
	public static StudySet createFromArray(String [] entries, boolean isPopulation) throws IOException
	{
		AbstractItemParser itemParser = ParserFactory.getNewInstance(entries);
		StudySet study = createFromParser(itemParser,isPopulation);
		
		return study;
	}
        
        /**
         * Create a study set from a list
         * @param entries
         * @param isPopulation
         * @return
         * @throws IOException 
         */
        public static StudySet createFromList(List<String> entries, boolean isPopulation) throws IOException
        {
            String [] array = StringUtils.stringListToArray(entries);
            
            AbstractItemParser itemParser = ParserFactory.getNewInstance(array);
            StudySet study = createFromParser(itemParser,isPopulation);

		
            return study;
            
        }
        
        /**
         * Create a study set from a list
         * @param entries
         * @param isPopulation
         * @return
         * @throws IOException 
         */
        public static StudySet createFromIntegerSet(Set<Integer> entries, boolean isPopulation) throws IOException
        {
            String [] array = StringUtils.integerSetToArray(entries);
            
            AbstractItemParser itemParser = ParserFactory.getNewInstance(array);
            StudySet study = createFromParser(itemParser,isPopulation);

		
            return study;
            
        }
        
        
        
        /**
         * Create a study set from a set of ByteString
         * @param entries
         * @param isPopulation
         * @return
         * @throws IOException 
         */
        public static StudySet createFromByteStringSet(Set<ByteString> entries, boolean isPopulation) throws IOException
        {
            String [] array = StringUtils.byteStringSetToString(entries);
            
            AbstractItemParser itemParser = ParserFactory.getNewInstance(array);
            StudySet study = createFromParser(itemParser,isPopulation);

		
            return study;
            
        }
        
        /**
         * Create a study set from a list of disease.
         * 
         * @param entries
         * @param isPopulation
         * @return
         * @throws IOException 
         */
        public static StudySet createFromDiseaseList(List<Disease> entries, boolean isPopulation) throws IOException
        {
            String[] array = StringUtils.diseaseListToStringArray(entries);
            return createFromArray(array,isPopulation);
        }
 
        
        /**
         * Create a study set from a list of gene.
         * 
         * @param entries
         * @param isPopulation
         * @return
         * @throws IOException 
         */
        public static StudySet createFromGeneList(List<Gene> entries, boolean isPopulation) throws IOException
        {
            String[] array = StringUtils.geneListToStringArray(entries);
            return createFromArray(array,isPopulation);
        }
        
        /**
         * Create a study set from a list of rare diseases.
         * 
         * @param entries
         * @param isPopulation
         * @return
         * @throws IOException 
         */
        public static StudySet createFromRareDiseaseList(List<RareDisease> entries, boolean isPopulation) throws IOException
        {
            String[] array = StringUtils.rareDiseaseListToStringArray(entries);
            return createFromArray(array,isPopulation);
        }
        
        
         /**
         * Create a study set from a set of Object. This method is useful when we get a set
         * of objects from database, for example, the set of genes related with a  set
         * of diseases. We use the an entity manager object to get this information
         * so the result will be Gene or Disease objects but its will not be Strings.
         * 
         * @param entries
         * @param isPopulation
         * @return
         * @throws IOException 
         
        public static StudySet createFromSet(Set<Object> entries, boolean isPopulation) throws IOException
        {
         
            AbstractItemParser itemParser = ParserFactory.getNewInstance(array);
            StudySet study = createFromParser(itemParser,isPopulation);

		
            return study;
            
        }
	*/
        
	/**
	 * Creates a study set from an array. No parsing.
	 * 
	 * @param entries
	 * @param isPopulation
	 * @return
	 * @throws IOException
	 */
	public static StudySet createFromArrayOfId(String [] entries, boolean isPopulation) throws IOException
	{
		//AbstractItemParser itemParser = ParserFactory.getNewInstance(entries);
		//return createFromParser(itemParser,isPopulation);
		final StudySet studySet;
		if (isPopulation) studySet = new PopulationSet();
		else studySet = new StudySet();
		
	//	studySet.addElement(geneName, attribute)
		
		return studySet;
	}

	/**
	 * Creates a new study set.
	 * 
	 * @param itemParser
	 * @param isPopulation
	 * @return
	 * @throws IOException 
	 */
	public static StudySet createFromParser(AbstractItemParser itemParser, boolean isPopulation) throws IOException
	{
		final StudySet studySet;
		if (isPopulation) studySet = new PopulationSet();
		else studySet = new StudySet();

		/* IParserCallback es una interface, el método parse implementa el unico método de la
		 * interfaz.
		 * */
		itemParser.parse(new IParserCallback() {
			public void newEntry(ByteString gene, ItemAttribute attribute)
			{
				studySet.addElement(gene, attribute);
			}
		});
		
		return studySet;
	}
}
