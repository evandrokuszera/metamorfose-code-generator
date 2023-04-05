/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import mf.classmetadata.Annotation;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;

/**
 *
 * @author evand
 */
public abstract class MfCodeGenerator {
    public static String OUTPUT_CLASSES_FILEPATH;
    public static String CLASS_PREFIX = null; // if CLASS_PREFIX != null, add a prefix in the name of class before save in the file.
    public static boolean GENERATE_GETTERS_AND_SETTERS = true;
    
    public static String createPojoCodeFrom(ClassMetadata classMetadata){
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
        if (GENERATE_GETTERS_AND_SETTERS){
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
        }
        
        // MANUAL CODES
        for (String code : classMetadata.getManualInputCodes()){
            strBuilder.append("\n");
            strBuilder.append(code);
            strBuilder.append("\n");
        }
        
        strBuilder.append("}").append("\n"); // end of the class
        //System.out.println(strBuilder.toString());
        
        return strBuilder.toString();
    }
    
    public static void saveCodeToFile(String packageName, String className, String classCodeString) throws FileNotFoundException{
        
        // Retrieving the package path to save the generated class in right place. 
        String classPackagePath = packageName.replace(".", System.getProperty("file.separator")) + System.getProperty("file.separator");
        
        // If the user do not inform the path to save the classes, then we use the directory 'project/src/main/java' as default path.
        if (OUTPUT_CLASSES_FILEPATH == null || OUTPUT_CLASSES_FILEPATH.length() == 0){
            OUTPUT_CLASSES_FILEPATH = getProjectSrcFolder();
        }
        
        // If the package directory do not exist, the we try to create it.
        File diretorio = new File(OUTPUT_CLASSES_FILEPATH + classPackagePath);
        if (!diretorio.exists()){
            if (!diretorio.mkdirs()){
                System.out.println("MfCodeGenerator: ERROR: it was not possibly to create the directory: " + diretorio.getAbsolutePath());
            }
        }
        
        // Save the file in the code package.
        System.out.printf("MfCodeGenerator: writing POJO to: %s.java\n", OUTPUT_CLASSES_FILEPATH + classPackagePath + className);
        PrintWriter printer = new PrintWriter(OUTPUT_CLASSES_FILEPATH + classPackagePath + className + ".java");
        printer.print(classCodeString);
        printer.close();
    }
    
    public static void addClassPrefix(ClassMetadata classMeta){
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
    
    public static String packageNameBuilder(String packageBaseName, String optionalMiddlePackageName, String optionalLastPackageName){
        if (optionalMiddlePackageName == null) optionalMiddlePackageName = "";  
        if (optionalLastPackageName == null) optionalLastPackageName = "";   
        
        String packageName = packageBaseName;
        packageName += optionalMiddlePackageName.length() == 0 ? "" : "."+optionalMiddlePackageName;
        packageName += optionalLastPackageName.length() == 0 ? "" : "."+optionalLastPackageName;
        
        return packageName.toLowerCase();
    }
    
    public static String getClassNameFromFullPackageName(String fullPackageName){
        String vet[] = fullPackageName.replace(".", ":").split(":");
        return vet[vet.length-1];
    }
}
