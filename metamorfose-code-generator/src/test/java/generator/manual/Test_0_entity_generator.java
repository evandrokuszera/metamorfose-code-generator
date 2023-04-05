package generator.manual;

/*

    The code below demostrate how to generate Java classes from a NoSQL schema represented by a DAG.
    All generated classes are based in the structure of one entity of the DAG.
    After generated, each class is customized with some annotations according to ODM or ORM used.

*/

import mf.utils.GraphUtils;
import dag.model.RelationshipEdge;
import dag.model.TableVertex;
import dag.nosql_schema.NoSQLSchema;
import mf.classmetadata.Annotation;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import java.io.FileNotFoundException;
import java.util.List;
import mf.classmetadata.ClassRelationshipFieldType;
import mf.generator.RdbTypeEnum;
import mf.schema.MfDagEntityGenerator;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 *
 * @author evand
 */
public class Test_0_entity_generator {
    
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException {
        NoSQLSchema schema = GraphUtils.loadNosqlSchema("..\\input-nosql-schema\\s-deep2-1_n.json");
//        MfPojoGeneratorFromDag generator;   
        MfDagEntityGenerator generator;
                       
        // a partir de um esquema NoSQL, percorrer as entidades (DAGs) e gerar classes necessárias para representar a estrutura dos documentos de uma coleção.
        for (DirectedAcyclicGraph<TableVertex, RelationshipEdge> entity : schema.getEntities()){
            // Parte 1: seta o DAG que representa a entidade para o gerador de POJO
            generator = new MfDagEntityGenerator(entity, null, RdbTypeEnum.POSTGRES);
            // Parte 2: gera a coleção de classMetadata a partir de um DAG (entidade NoSQL)
            generator.generate("model.collections");
            List<ClassMetadata> classMetadataCollection = generator.getMfEntity().getClassMetadataList();
            // Parte 3: recuperar a classe que representa a entidade raiz, ou seja, a raiz da coleção de documentos
            //  de acordo com a estrutura do DAG, a última classe adicionada em classMetadataCollection corresponde a raiz da coleção de documentos
            ClassMetadata rootClassMetadata = classMetadataCollection.get(classMetadataCollection.size() - 1);
            
            // Parte 4 - INICIO: customizar as classes de acordo com um ODM específico
            /////////////////////////////////////////////////////////////////            
            // 4.1 Customizar a classe que representa a coleção de documentos.
            rootClassMetadata.getImports().add(new ClassImport("org.springframework.data.annotation.Id"));
            rootClassMetadata.getImports().add(new ClassImport("org.springframework.data.mongodb.core.mapping.Document"));
            rootClassMetadata.getAnnotations().add(new Annotation("@Document(collection = \""+rootClassMetadata.getName().toLowerCase()+"\")"));
            // 4.2 Adicionar campo id e anotação do campo id
            ClassField id = new ClassField("private", "String", "_id", ClassRelationshipFieldType.NONE, true, false);
            id.getAnnotations().add(new Annotation("@Id"));
            rootClassMetadata.getFields().add(0, id);
            // 4.3 Para cada objeto ClassMetadata é possível adicionar elementos adicionais (imports, annotation, etc) dependente do tipo de ODM usado.
            for (ClassMetadata classMeta : classMetadataCollection){
                classMeta.setPackageName("emk.collections." + rootClassMetadata.getName().toLowerCase());
                classMeta.getImports().add(new ClassImport("lombok.Getter"));
                classMeta.getImports().add(new ClassImport("lombok.Setter"));
                classMeta.getImports().add(new ClassImport("lombok.Data"));
                classMeta.getAnnotations().add( new Annotation("@Data"));
            }
            /////////////////////////////////////////////////////////////////
            // Parte 4 - FIM: customizar as classes de acordo com um ODM específico
            
            // Parte 5: salvar cada classMetadata como um arquivo .java em disco
            generator.saveFiles();
        }
    }
    
}