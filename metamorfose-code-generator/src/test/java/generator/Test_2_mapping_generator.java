/*

  Objective: this class show how to load a NoSQL schema in DAG format and 
   generate the Java classes according to DAG structure and
   generate the mapping classes to map source classes to target classes.

 */
package generator;

import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import mf.customization.jpa.MfJpaClassCustomization;
import mf.customization.spring.MfSpringMongoCustomization;
import mf.generator.RdbTypeEnum;
import mf.mapping.MfJpaMapperGenerator;
import mf.schema.MfDagSchemaGenerator;
import mf.utils.GraphUtils;

/**
 *
 * @author evand
 */
public class Test_2_mapping_generator {
    public static void main(String[] args) throws FileNotFoundException {
        NoSQLSchema nosqlSchema = GraphUtils.loadNosqlSchema("..\\input-nosql-schema\\s-deep2-1_n.json");
        
        // Generating the Java classes using Jpa customizations.
        // The classes generated are used to load data from relational database.
        MfDagSchemaGenerator schemaGeneratorWithJpaCustomizations = new MfDagSchemaGenerator(
                nosqlSchema, 
                new MfJpaClassCustomization(),
                RdbTypeEnum.POSTGRES
        );
        schemaGeneratorWithJpaCustomizations.generate("model", "rdb");
        schemaGeneratorWithJpaCustomizations.saveFiles();

        // Generating the Java classes using Spring customizations.
        // The classes generated are used to save data in the MongoDB.        
        MfDagSchemaGenerator schemaGeneratorWithSpringCustomizations = new MfDagSchemaGenerator(
                nosqlSchema, 
                new MfSpringMongoCustomization(),
                RdbTypeEnum.POSTGRES
        );
        schemaGeneratorWithSpringCustomizations.generate("model", "nosql");
        schemaGeneratorWithSpringCustomizations.saveFiles();
        
        // In this point you generated two set of classes from a NoSQL schema.
        //  The first set is composed by Java classes annotated with Jpa ORM annotations.
        //  The second one is composed by Java classes annotated with Spring Data annotations with MongoDB support.
        // Now, you will generate the mapping between the two set of classes.
        
        MfJpaMapperGenerator mapperGenerator = new MfJpaMapperGenerator(
                schemaGeneratorWithJpaCustomizations.getMfSchema(), 
                schemaGeneratorWithSpringCustomizations.getMfSchema()
        );
        mapperGenerator.generate("model", "mapper"); // this method generate and save de mapper classes.
        
    }
}
