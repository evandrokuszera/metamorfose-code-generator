package pojogenerator;

/*
 * O teste abaixo mostra como ler os metadados de uma classe existente, 
 *  gerar um objeto ClassMetadata, depois gerar uma nova classe com base
 *  nos dados encapsulados em ClassMetadata.
 *
 * O exemplo abaixo gera uma classe chamada Professor no pacote "emk.classes".
 *  - com imports  
 *  - dois campos
 *  - algumas anotações
 *
 * Estou usando a lib Lombok para gerar os getters and setters.
 */


import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import mf.pojogenerator.fromJPAClass.JPAClassPojoGenerator;
import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 * @author evand
 */
public class PojoGen_OtlherClass {
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {                
        JPAClassPojoGenerator pojoGenerator = new JPAClassPojoGenerator("Teste", null);
        
        // Ler os metadados de uma classe do projeto.
        pojoGenerator.setClassName("emk.rdb.Products");
        List<ClassMetadata> classMetaList = pojoGenerator.generateClassMetadataList("emk.classes");
        
        // Adicionar elementos adicionais, para personalizar a geração da classe.
        classMetaList.get(0).setPackageName("emk.classes");
        classMetaList.get(0).getImports().add(new ClassImport("lombok.Getter"));
        classMetaList.get(0).getImports().add(new ClassImport("lombok.Setter"));
        
        // Gerar uma nova classe.
        pojoGenerator.savePojoClassToFile(classMetaList.get(0));
    }
}