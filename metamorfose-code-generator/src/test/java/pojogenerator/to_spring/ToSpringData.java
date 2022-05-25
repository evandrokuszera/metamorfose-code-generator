// Esta classe testa o PojoGeneratorFromDagSchema (que simplemente chama PojoGeneratorFromDagEntity para realizar o trabalho)
// Entradas:
// - um objeto NoSQLSchema
// Saídas:
// - um conjunto de classes geradas para cada entidade do NoSQLSchema, organizados por pacotes com o nome da entidade.

package pojogenerator.to_spring;

import mf.utils.GraphUtils;
import dag.nosql_schema.NoSQLSchema;
import mf.classmetadata.odm.MfSpringDataODMCustomization;
import java.io.FileNotFoundException;
import mf.classmetadata.odm.MfHibernateODMCustomization;
import mf.pojogenerator.fromDAG.MfPojoGeneratorFromDag;
import mf.pojogenerator.fromDAG.RdbTypeEnum;
import mf.pojogenerator.fromDAG.RdbToJavaDatatypeDictionary;

/**
 *
 * @author evand
 */
public class ToSpringData {
    
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException {
        //NoSQLSchema schema = GraphUtils.loadNosqlSchema("C:\\workspaces\\ws-geral\\A-149-novo.json");
        //NoSQLSchema schema = GraphUtils.loadNosqlSchema("C:\\workspaces\\ws-geral\\SchemaA.json");
        NoSQLSchema schema = GraphUtils.loadNosqlSchema("C:\\workspaces\\sexta.json");
        
        MfPojoGeneratorFromDag generator = new MfPojoGeneratorFromDag(
                schema, 
                new MfSpringDataODMCustomization(), 
                RdbTypeEnum.POSTGRES);
        
        //MfPojoGeneratorFromDag.OUTPUT_CLASSES_FILEPATH = "C:\\workspaces\\ws-mongodb\\rdb_to_nosql_springdata\\src\\main\\java\\";
        MfPojoGeneratorFromDag.OUTPUT_CLASSES_FILEPATH = "C:\\workspaces\\";
        
        generator.generateClassMetadataList("emk.generated.collections");
    }
    
}