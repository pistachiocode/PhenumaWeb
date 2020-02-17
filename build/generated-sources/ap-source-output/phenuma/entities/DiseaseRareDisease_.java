package phenuma.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import phenuma.entities.Disease;
import phenuma.entities.DiseaseRareDiseasePK;
import phenuma.entities.RareDisease;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-26T13:40:56")
@StaticMetamodel(DiseaseRareDisease.class)
public class DiseaseRareDisease_ { 

    public static volatile SingularAttribute<DiseaseRareDisease, Disease> disease;
    public static volatile SingularAttribute<DiseaseRareDisease, DiseaseRareDiseasePK> diseaseRareDiseasePK;
    public static volatile SingularAttribute<DiseaseRareDisease, RareDisease> rareDisease;

}