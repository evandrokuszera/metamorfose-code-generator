package pojogenerator;

/*
 * Esta classe tem por objetivo gerar uma classe Java com base em um objeto
 *  da classe ClassMetadata. A classe ClassMetadata encapsula as informações 
 *  para a geração, como imports, anotações e campos.
 *
 * O exemplo abaixo define uma classe Professor com:
 *  - pacote 
 *  - imports  
 *  - dois campos
 *  - algumas anotações (@)
 *
 * A classe PojoGenerator salva a classe em disco, na pasta /src/main/java
 *  - o nome do pacote é usado para gerar as subpastas "emk/classes".
 *  - estou usando a lib Lombok para gerar os getters and setters.
 */


import mf.classmetadata.Annotation;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import mf.pojogenerator.MfPojoGenerator;
import java.io.FileNotFoundException;
import mf.classmetadata.ClassRelationshipFieldType;

/**
 *
 * @author evand
 */
public class PojoGen_Manual {
    public static void main(String[] args) throws FileNotFoundException {                
        ClassMetadata classMeta = new ClassMetadata();
        
        // Definindo os metadados da classe que será gerada
        // Início
        classMeta.setName("Professor");
        classMeta.setPackageName("emk.classes");
        classMeta.getImports().add(new ClassImport("lombok.Getter"));
        classMeta.getImports().add(new ClassImport("lombok.Setter"));
        classMeta.getImports().add(new ClassImport("javax.persistence.*"));
        classMeta.getAnnotations().add(new Annotation("@Entity"));
        
        ClassField f1 = new ClassField("private", "String", "_id", ClassRelationshipFieldType.NONE);
        f1.getAnnotations().add(new Annotation("@Id"));
        f1.getAnnotations().add(new Annotation("@GeneratedValue(strategy = GenerationType.IDENTITY)"));
        f1.getAnnotations().add(new Annotation("@Getter"));
        f1.getAnnotations().add(new Annotation("@Setter"));
        
        ClassField f2 = new ClassField("private", "String", "nome", ClassRelationshipFieldType.NONE);
        f2.getAnnotations().add(new Annotation("@Getter"));
        f2.getAnnotations().add(new Annotation("@Setter"));
        
        classMeta.getFields().add(f1);
        classMeta.getFields().add(f2);
        // Fim
        
        // Gerando a classe
        MfPojoGenerator.OUTPUT_CLASSES_FILEPATH = MfPojoGenerator.getProjectSrcFolder();
        MfPojoGenerator.savePojoClassToFile(classMeta);
    }
}