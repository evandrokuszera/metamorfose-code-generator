// Esta classe testa o PojoGeneratorFromDagSchema (que simplemente chama PojoGeneratorFromDagEntity para realizar o trabalho)
// Entradas: - um objeto NoSQLSchema
// Saídas: - um conjunto de classes geradas para cada entidade do NoSQLSchema, organizados por pacotes com o nome da entidade.

package pojogenerator;

import mf.utils.GraphUtils;
import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import mf.classmetadata.odm.MfHibernateODMCustomization;
import mf.pojogenerator.fromDAG.MfPojoGeneratorFromDag;
import mf.pojogenerator.fromDAG.RdbTypeEnum;
/**
 *
 * @author evand
 */
public class PojoGen_FromDagSchema {
    
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException {
        NoSQLSchema schema = GraphUtils.loadNosqlSchema("C:\\workspaces\\ws-geral\\A-149-novo.json");
//        NoSQLSchema schema = GraphUtils.loadNosqlSchema("C:\\workspaces\\ws-geral\\SchemaA.json");
        
        MfPojoGeneratorFromDag generator = new MfPojoGeneratorFromDag(
                schema, 
                new MfHibernateODMCustomization(), 
                RdbTypeEnum.POSTGRES);
        
        MfPojoGeneratorFromDag.OUTPUT_CLASSES_FILEPATH = "C:\\workspaces\\ws-geral\\hibernate-ogm\\src\\main\\java\\";
        
        generator.generateClassMetadataList("emk.generated.collections");
        
    }
    
}