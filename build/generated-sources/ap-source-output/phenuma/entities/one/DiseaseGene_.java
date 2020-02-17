package phenuma.entities.one;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import phenuma.entities.Disease;
import phenuma.entities.DiseaseGene;
import phenuma.entities.DiseaseGenePK;
import phenuma.entities.Gene;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-26T13:40:56")
@StaticMetamodel(DiseaseGene.class)
public class DiseaseGene_ { 

    public static volatile SingularAttribute<DiseaseGene, Double> score;
    public static volatile SingularAttribute<DiseaseGene, Gene> gene1;
    public static volatile SingularAttribute<DiseaseGene, DiseaseGenePK> diseaseGenePK;
    public static volatile SingularAttribute<DiseaseGene, Disease> disease1;

}