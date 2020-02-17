package phenuma.entities.one;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import phenuma.entities.DiseaseGene;
import phenuma.entities.Gene;
import phenuma.entities.RareDisease;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-26T13:40:56")
@StaticMetamodel(Gene.class)
public class Gene_ { 

    public static volatile SingularAttribute<Gene, String> symbol;
    public static volatile SingularAttribute<Gene, String> genetype;
    public static volatile ListAttribute<Gene, RareDisease> rareDiseaseList;
    public static volatile SingularAttribute<Gene, String> chromosome;
    public static volatile SingularAttribute<Gene, String> name;
    public static volatile SingularAttribute<Gene, byte[]> description;
    public static volatile ListAttribute<Gene, DiseaseGene> diseaseGeneList;
    public static volatile SingularAttribute<Gene, String> location;
    public static volatile SingularAttribute<Gene, Integer> entrezid;

}