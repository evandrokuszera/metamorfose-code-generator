/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.pojogenerator;

import mf.classmetadata.odm.MfClassODMCustomization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import mf.classmetadata.Annotation;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;

/**
 *
 * @author evand
 */
public abstract class MfPojoGenerator {
    public static String OUTPUT_CLASSES_FILEPATH;
    public static String CLASS_PREFIX = null; // if CLASS_PREFIX != null, add a prefix in the name of class before save in the file.
    private List<ClassMetadata> classMetadataList = new ArrayList<>();
    private MfClassODMCustomization classODMCustomization;
    

    public MfPojoGenerator(MfClassODMCustomization classODMCustomization) {
        this.classODMCustomization = classODMCustomization;
    }
    
    // *************************************************
    // This is the main method of PojoGenerator class
    public abstract List<ClassMetadata> generateClassMetadataList(String packageBaseName) throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException;

    public List<ClassMetadata> getClassMetadataList() {
        return classMetadataList;
    }

    // The customizations should be applyed after ClassMetadata generation
    //  The customizations add imports, annotations and fields to the classes, and they are specific by ODM
    public void applyCustomizationsToClassMetadataList(){
        // if there are customizations to apply, then for each classMetadata object apply them.
        if (classODMCustomization != null){
            
            // Recuperar a classe que representa a entidade raiz, ou seja, a classe que corresponde a coleção de documentos (nível 0).
            //  De acordo com a estrutura do DAG, a última classe adicionada em classMetadataList corresponde a entidade raiz.
            ClassMetadata rootClassMetadata = this.getClassMetadataList().get(this.getClassMetadataList().size()-1);
            
            // It is necessary generating the customizations
            classODMCustomization.getRootEntity().setName(rootClassMetadata.getName());
            classODMCustomization.generateCustomizations();
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 1 - Applying customization for the rootClass
            for (ClassImport rootImport : classODMCustomization.getRootEntity().getImports()){
                rootClassMetadata.getImports().add(rootImport);
            }
            
            for (Annotation rootAnnotation : classODMCustomization.getRootEntity().getAnnotations()){
                rootClassMetadata.getAnnotations().add(rootAnnotation);
            }
            
            for (ClassField rootField : classODMCustomization.getRootEntity().getFields()){
                rootClassMetadata.updateOrAddClassField(rootField);
            }
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // 2 - Applying customization for the other classes (no root classes).
            for (ClassMetadata clazz : this.getClassMetadataList()){
                
                if (rootClassMetadata == clazz) return;
                
                for (ClassImport classEmbeddedImport : classODMCustomization.getEmbeddedEntities().getImports()){
                    clazz.getImports().add(classEmbeddedImport);
                }

                for (Annotation classEmbeddedAnnotation : classODMCustomization.getEmbeddedEntities().getAnnotations()){
                    clazz.getAnnotations().add(classEmbeddedAnnotation);
                }

                for (ClassField classEmbeddedField : classODMCustomization.getEmbeddedEntities().getFields()){
                    //clazz.getFields().add(classEmbeddedField);
                    clazz.updateOrAddClassField(classEmbeddedField);
                }
                
            }
        }
        
    }
    
    private static void addClassPrefix(ClassMetadata classMeta){
        if (CLASS_PREFIX != null){
            classMeta.setName( CLASS_PREFIX + classMeta.getName() );
            
            for (ClassField field : classMeta.getFields()){
                if (field.getRelationshipType() == ClassRelationshipFieldType.OBJECT){
                    field.setType(CLASS_PREFIX + field.getType());
                } else if (field.getRelationshipType() == ClassRelationshipFieldType.ARRAY_OF_OBJECTS){
                    
                    String temp = field.getType()
                            .replace("java.util.List<", "")
                            .replace(">", "");
                    
                    field.setType( field.getType().replace(temp, CLASS_PREFIX + temp) );
                    
                }
            }
        }
    }
    
    public void savePojoClassesToFile() throws FileNotFoundException{
        for (ClassMetadata classMeta : this.getClassMetadataList()){
            savePojoClassToFile(classMeta);
        }
    }
    
    public static void savePojoClassToFile(ClassMetadata classMetadata) throws FileNotFoundException{
        StringBuilder strBuilder = new StringBuilder();
        
        addClassPrefix(classMetadata); // add a prefix in the class, if CLASS_PREFIX is different of null.
        
        strBuilder.append("package ").append(classMetadata.getPackageName()).append(";").append("\n\n");
        
        // IMPORTS
        for (ClassImport importItem : classMetadata.getImports()){
            strBuilder.append("import ").append(importItem.getImportEntry()).append(";").append("\n");
        }
        strBuilder.append("\n");

        // CLASS ANNOTATIONS
        for (Annotation annotation : classMetadata.getAnnotations()){
            strBuilder.append(annotation.getAnnotation()).append("\n");
        }
        
        // CLASS
        strBuilder.append("public class ").append(classMetadata.getName()).append("{").append("\n");
        // FIELDS
        for (ClassField field : classMetadata.getFields()){
            // FIELD ANNOTATIONS
            for (Annotation annotation : field.getAnnotations()){
                strBuilder.append("\t").append(annotation.getAnnotation()).append("\n");
            }
            // FIELD
            strBuilder.append("\t")
                      .append(field.getModifier()+" ")
                      .append(field.getType()+" ")
                      .append(field.getName())
                      .append(";").append("\n");
        }
        
        // GETTERS and SETTERs
        for (ClassField field : classMetadata.getFields()){
            // GETTER
            strBuilder.append("\t").append("public ")
                      .append(field.getType()+" ")
                      .append("get").append(getStringWithFirstCapitalLetter(field.getName())).append("(){").append("\n")
                      .append("\t\t").append("return ").append(field.getName()).append(";").append("\n")
                      .append("\t").append("}").append("\n");
            // SETTER
            strBuilder.append("\t").append("public void ")
                      .append("set").append(getStringWithFirstCapitalLetter(field.getName())).append("("+field.getType()+" value) {").append("\n")
                      .append("\t\t").append("this."+field.getName()).append(" = value;").append("\n")
                      .append("\t").append("}").append("\n");
        }
        
        strBuilder.append("}").append("\n");
        System.out.println(strBuilder.toString());
        
        // Recuperando o caminho do pacote, para depois salvar a classe gerada no lugar correto. 
        String classPackagePath = classMetadata.getPackageName().replace(".", System.getProperty("file.separator")) + System.getProperty("file.separator");
        
        // Se o usuário não informou um caminho para salvar as classes, então usar o diretório 'projeto/src/main/java' para tal.
        if (OUTPUT_CLASSES_FILEPATH == null || OUTPUT_CLASSES_FILEPATH.length() == 0){
            OUTPUT_CLASSES_FILEPATH = getProjectSrcFolder();
        }
        
        // Se o diretório do pacote de código-fonte não existe, então tenta criá-lo.
        File diretorio = new File(OUTPUT_CLASSES_FILEPATH + classPackagePath);
        if (!diretorio.exists()){
            if (!diretorio.mkdirs()){
                System.out.println("ERRO: não foi possível criar o diretoório: " + diretorio.getAbsolutePath());
            }
        }
        
        // Salva arquivo no diretório do pacote de código-fonte.
        System.out.printf("Writing POJO to: %s.java\n", OUTPUT_CLASSES_FILEPATH + classPackagePath + classMetadata.getName());
        PrintWriter printer = new PrintWriter(OUTPUT_CLASSES_FILEPATH + classPackagePath + classMetadata.getName() + ".java");
        printer.print(strBuilder.toString());
        printer.close();
    }
    
    // returns the absolute path of src project folder.
    public static String getProjectSrcFolder(){
        String sep = System.getProperty("file.separator");        
        StringBuilder strBuilder = new StringBuilder();
        
        strBuilder.append(System.getProperty("user.dir")).append(sep);
        strBuilder.append("src").append(sep).append("main").append(sep).append("java").append(sep);
        
        return strBuilder.toString();
    }
    
    public static String getStringWithFirstCapitalLetter(String name){
        return name.substring(0, 1).toUpperCase() + name.substring(1,name.length());
    }
}
