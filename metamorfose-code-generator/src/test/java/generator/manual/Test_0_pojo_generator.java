package generator.manual;
/*
    This class demostrate how to generate a Java class using the ClassMetadata class.
    A ClassMetadata denotes the metadata of one Java class, with package, imports, annotation, field.
    
    In the example below a class Professor will be generated
    The MfCodeGenerator is responsible to persist the ClassMetadata as a .java file.

*/


import mf.classmetadata.Annotation;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import mf.generator.MfCodeGenerator;
import java.io.FileNotFoundException;
import mf.classmetadata.ClassRelationshipFieldType;

/**
 *
 * @author evand
 */
public class Test_0_pojo_generator {
    public static void main(String[] args) throws FileNotFoundException {                
        ClassMetadata classMeta = new ClassMetadata();
        
        // Defining the class metadata elements
        classMeta.setName("Professor");
        classMeta.setPackageName("emk.classes");
        classMeta.getImports().add(new ClassImport("lombok.Getter"));
        classMeta.getImports().add(new ClassImport("lombok.Setter"));
        classMeta.getImports().add(new ClassImport("javax.persistence.*"));
        classMeta.getAnnotations().add(new Annotation("@Entity"));
        
        ClassField f1 = new ClassField("private", "String", "_id", ClassRelationshipFieldType.NONE, true, false);
        f1.getAnnotations().add(new Annotation("@Id"));
        f1.getAnnotations().add(new Annotation("@GeneratedValue(strategy = GenerationType.IDENTITY)"));
        f1.getAnnotations().add(new Annotation("@Getter"));
        f1.getAnnotations().add(new Annotation("@Setter"));
        
        ClassField f2 = new ClassField("private", "String", "nome", ClassRelationshipFieldType.NONE, false, false);
        f2.getAnnotations().add(new Annotation("@Getter"));
        f2.getAnnotations().add(new Annotation("@Setter"));
        
        classMeta.getFields().add(f1);
        classMeta.getFields().add(f2);
        
        // Persisting the ClassMetadata as .java file
        MfCodeGenerator.OUTPUT_CLASSES_FILEPATH = MfCodeGenerator.getProjectSrcFolder();
        String codeClass = MfCodeGenerator.createPojoCodeFrom(classMeta);
        MfCodeGenerator.saveCodeToFile(classMeta.getPackageName(), classMeta.getName(), codeClass);
    }
}