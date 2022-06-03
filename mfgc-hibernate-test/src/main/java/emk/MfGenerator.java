/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emk;

import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import mf.classmetadata.odm.MfHibernateODMCustomization;
import mf.pojogenerator.fromDAG.MfPojoGeneratorFromDag;
import mf.pojogenerator.fromDAG.RdbTypeEnum;
import mf.utils.GraphUtils;

/**
 *
 * @author evand
 */
public class MfGenerator {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException {
        String path = "..\\input-nosql-schema\\orders-db-schema.json";
        NoSQLSchema schema = GraphUtils.loadNosqlSchema(path);
        
        MfPojoGeneratorFromDag generator = new MfPojoGeneratorFromDag(
                schema, 
                new MfHibernateODMCustomization(),
                RdbTypeEnum.POSTGRES
        );
        
        MfPojoGeneratorFromDag.CLASS_PREFIX = "Doc";
        
        generator.generateAndSavePojosByNoSQLSchema("emk.collection");
    }
}
