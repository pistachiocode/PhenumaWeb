package phenuma.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import phenuma.entities.Disease;
import phenuma.entities.Gene;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-26T13:40:56")
@StaticMetamodel(RareDisease.class)
public class RareDisease_ { 

    public static volatile SingularAttribute<RareDisease, Integer> prevalence;
    public static volatile ListAttribute<RareDisease, Gene> geneList;
    public static volatile SingularAttribute<RareDisease, String> name;
    public static volatile SingularAttribute<RareDisease, byte[]> description;
    public static volatile ListAttribute<RareDisease, Disease> diseaseList;
    public static volatile SingularAttribute<RareDisease, Integer> orphanum;

}