// Recebe um NoSQL Schema (conjunto de DAGs) e gera um conjunto de classes POJO.
// Entradas:
// - um objeto NoSQLSchema
// - caminho de destino para gerar as classes POJO
// - tipo do ONDM
// Saídas:
// - um conjunto de classes geradas para cada entidade do NoSQLSchema, organizados por pacotes com o nome da entidade.

package pojogenerator.to_hibernate;

import mf.utils.GraphUtils;
import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import mf.classmetadata.odm.MfHibernateODMCustomization;
import mf.pojogenerator.fromDAG.MfPojoGeneratorFromDag;
import mf.pojogenerator.fromDAG.RdbTypeEnum;
import mf.pojogenerator.fromDAG.RdbToJavaDatatypeDictionary;

/**
 *
 * @author evand
 */
public class ToHibernateOGM {
    
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException {
        String path = "C:\\workspaces\\ws-metamorfose-codegenerator\\";
//        NoSQLSchema schema = GraphUtils.loadNosqlSchema(path + "schema-customers-orders-orderlines (ref).json");
        NoSQLSchema schema = GraphUtils.loadNosqlSchema(path + "ref.json");
        
//        DagSchemaPojoGenerator generator = new DagSchemaPojoGenerator(schema);
//        DagSchemaPojoGenerator.FILE_PATH = path + "mfgc-hibernate-ogm\\src\\main\\java\\";
//        
//        generator.setSchema(schema);
//        generator.setClassMetadataCustomization(new HibernateODMCustomization());
//        generator.generateClassMetadataList("emk.generated.collections");

        MfPojoGeneratorFromDag generator = new MfPojoGeneratorFromDag(
                schema, 
                new MfHibernateODMCustomization(), 
                RdbTypeEnum.POSTGRES);
        
        MfPojoGeneratorFromDag.OUTPUT_CLASSES_FILEPATH = path + "mfgc-hibernate-ogm\\src\\main\\java\\";
        
        generator.generateClassMetadataList("emk.generated.collections");
    }
    
}