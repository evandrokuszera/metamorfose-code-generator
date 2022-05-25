package pojogenerator;

// Esta classe testa o FromDagPojoGenerator.
// Entradas:
// - a partir de um objeto NoSQLSchema é recuperado um objeto entidade (DAG)
// Saídas:
// - um conjunto de classes geradas para cada entidade do NoSQLSchema, organizados por pacotes com o nome da entidade.
//
// Processamento:
// - para cada DAG entity um conjunto de classMetadata é gerado.
// - customizações (novos imports, anotações, etc) são aplicadas sobre o conjunto de classMetadata, de acordo com o ODM selecionado (exemplo abaixo é baseado nas anotações do Spring Data)
// - o conjunto de classMetadata é salvo como um conjunto de arquivos .java no diretório src/main/java/'nome do pacote'. 
        


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
import mf.pojogenerator.fromDAG.MfPojoGeneratorFromDag;
import mf.pojogenerator.fromDAG.RdbTypeEnum;
import mf.pojogenerator.fromDAG.RdbToJavaDatatypeDictionary;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 *
 * @author evand
 */
public class PojoGen_FromDagEntity {
    
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException {
        NoSQLSchema schema = GraphUtils.loadNosqlSchema("C:\\workspaces\\ws-geral\\A-149-novo.json");
        MfPojoGeneratorFromDag generator;   
                       
        // a partir de um esquema NoSQL, percorrer as entidades (DAGs) e gerar classes necessárias para representar a estrutura dos documentos de uma coleção.
        for (DirectedAcyclicGraph<TableVertex, RelationshipEdge> entity : schema.getEntities()){
            // Parte 1: seta o DAG que representa a entidade para o gerador de POJO
            generator = new MfPojoGeneratorFromDag(entity, null, RdbTypeEnum.POSTGRES);
            // Parte 2: gera a coleção de classMetadata a partir de um DAG (entidade NoSQL)
            List<ClassMetadata> classMetadataCollection = generator.generateClassMetadataList("emk.collections");
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
            ClassField id = new ClassField("private", "String", "_id", ClassRelationshipFieldType.NONE);
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
            generator.savePojoClassesToFile();
        }
    }
    
}