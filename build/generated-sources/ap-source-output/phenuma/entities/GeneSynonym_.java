package phenuma.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import phenuma.entities.Gene;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-26T13:40:56")
@StaticMetamodel(GeneSynonym.class)
public class GeneSynonym_ { 

    public static volatile SingularAttribute<GeneSynonym, String> symbol;
    public static volatile SingularAttribute<GeneSynonym, String> dbreference;
    public static volatile SingularAttribute<GeneSynonym, Integer> id;
    public static volatile SingularAttribute<GeneSynonym, Gene> entrezid;

}