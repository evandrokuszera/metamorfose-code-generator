/*

  Objective: this class show how to load a NoSQL schema in DAG format and 
   generate the Java classes according to DAG structure.

  In the example below, all the generated classes are customized with Jpa annotations and fields.
  
  After, the classes are persisted in the model package, inside the this project.

 */
package generator;

import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import mf.customization.jpa.MfJpaClassCustomization;
import mf.customization.spring.MfSpringMongoCustomization;
import mf.generator.MfCodeGenerator;
import mf.generator.RdbTypeEnum;
import mf.schema.MfDagSchemaGenerator;
import mf.utils.GraphUtils;

/**
 *
 * @author evand
 */
public class Test_1_schema_generator {
    public static void main(String[] args) throws FileNotFoundException {
        NoSQLSchema schema = GraphUtils.loadNosqlSchema("..\\input-nosql-schema\\s-deep2-1_n.json");
        
        // Below the code generator parameters:
        //MfCodeGenerator.OUTPUT_CLASSES_FILEPATH = "";  // where the generated classes will be save.
        //MfCodeGenerator.CLASS_PREFIX = "";  // if the generated classes will receive a prefix.
        
        MfDagSchemaGenerator schemaGeneratorWithJpaCustomizations = new MfDagSchemaGenerator(
                schema, 
                new MfJpaClassCustomization(), // new MfSpringClassCustomization(),
                RdbTypeEnum.POSTGRES
        );
        
        schemaGeneratorWithJpaCustomizations.generate("model", "rdb");
        schemaGeneratorWithJpaCustomizations.saveFiles();
        
        // You could access the generated class metadata through:
        // schemaGeneratorWithJpaCustomizations.getSchema();
        // schemaGeneratorWithJpaCustomizations.getSchema().getEntities();
    }
}
