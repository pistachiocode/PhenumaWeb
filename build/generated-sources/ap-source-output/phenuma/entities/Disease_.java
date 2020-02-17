package phenuma.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import phenuma.entities.DiseaseGene;
import phenuma.entities.RareDisease;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-26T13:40:56")
@StaticMetamodel(Disease.class)
public class Disease_ { 

    public static volatile ListAttribute<Disease, RareDisease> rareDiseaseList;
    public static volatile SingularAttribute<Disease, Integer> omim;
    public static volatile SingularAttribute<Disease, String> name;
    public static volatile SingularAttribute<Disease, byte[]> description;
    public static volatile SingularAttribute<Disease, Integer> phenoMappingKey;
    public static volatile ListAttribute<Disease, DiseaseGene> diseaseGeneList;
    public static volatile SingularAttribute<Disease, Integer> type;

}