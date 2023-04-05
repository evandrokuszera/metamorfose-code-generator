/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.mapping;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import mf.classmetadata.ClassMetadata;
import mf.generator.MfCodeGenerator;
import mf.schema.model.MfEntity;
import mf.schema.model.MfSchema;

/**
 *
 * @author evand
 */
public class MfJpaMapperGenerator {
    private List<MfMapping> mappings;
    private MfSchema sourceSchema;
    private MfSchema targetSchema;
    private String classNameSufix = "Mapper";

    public MfJpaMapperGenerator(MfSchema sourceSchema, MfSchema targetSchema) {
        this.sourceSchema = sourceSchema;
        this.targetSchema = targetSchema;
        this.mappings = new ArrayList<>();
    }

    public String getClassNameSufix() {
        return classNameSufix;
    }

    public void setClassNameSufix(String classNameSufix) {
        this.classNameSufix = classNameSufix;
    }

    public List<MfMapping> getMappings() {
        return mappings;
    }
    
    public void printMappgins(){
        System.out.println("\n-----------------------------------------------------");
        System.out.println("PRINTING GENERATED MAPPERs ");
        System.out.println("Number of mapping: " + this.mappings.size());
        System.out.println("-----------------------------------------------------");
        for (MfMapping m : this.mappings){
            m.print();
        }
        System.out.println("Done - Printing generated mappers... ");
    }
    
    /*
     * This method generate a list of Mappers between source and target schemas.
     * Steps:
     *  a)  
     *  for each target entity
     *     for each source entity
     *        if match(target, source) then
     *           create a mapping(source,target)
     *             matching(source.classMetadata,target.classMetadata)
     *     end for each
     *  b)
     *     for the created mapping, generate the mapper class
     *  c)
     *     save the mapper classes as Java files.
     *  end for each 
    */
    public void generate(String packageBaseName, String optionSubPackageName) throws FileNotFoundException {

        List<ClassMetadata> sourceClassMetadataListTemp = new ArrayList<>();
        List<ClassMetadata> targetClassMetadataListTemp = new ArrayList<>();

        // For each entity of target schema we will find a related source entity.
        for (MfEntity targetEntity : targetSchema.getEntities()) {
            // Creating a new mapping object
            MfMapping newMapping = new MfMapping("", targetEntity.getName()+classNameSufix, targetEntity.getName());
            
            newMapping.getTargetMfEntities().add(targetEntity);

            sourceClassMetadataListTemp.clear();
            targetClassMetadataListTemp.clear();

            // Step 1: creating a list of class metadata for mappings, where one source is mapped to one target.
            for (ClassMetadata targetClassMetadata:  targetEntity.getClassMetadataList()) {
                // Finding a corresponding source entity for the target entity, by matching the entity name
                MfEntity sourceEntityMatched = sourceSchema.getEntity(targetClassMetadata.getName());
                // if there is a matching between source and target entities in the schemas, then...
                if (sourceEntityMatched != null) {
                    // fetch the source classMetadata that match with target classMetadata
                    ClassMetadata sourceClassMetadata = sourceEntityMatched.getClassMetadata(targetClassMetadata.getName());
                    // adding the matching class metadata between source and target schema.
                    targetClassMetadataListTemp.add(targetClassMetadata);
                    sourceClassMetadataListTemp.add(sourceClassMetadata);

                    newMapping.getSourceMfEntities().add(sourceEntityMatched);
                }

            }
            
            // Step 2: creating the java class code for the mappings.
            String packageName = MfCodeGenerator.packageNameBuilder(packageBaseName, targetEntity.getName(), optionSubPackageName);
            String classCode = createMapperClassCode(
                    packageName,
                    targetEntity.getName()+classNameSufix,
                    sourceClassMetadataListTemp,
                    targetClassMetadataListTemp
            );
            
            MfCodeGenerator.saveCodeToFile(
                    packageName, 
                    targetEntity.getName()+classNameSufix, 
                    classCode
            );
            
            // Step 3: registering which mappings were created.
            newMapping.setPackageName(packageName);
            mappings.add(newMapping);
        } // end for entity
    }

    /*
    * Method to create a MapperClass for the MfEntity
    *  As each MfEntity has a collection of ClassMetadata objects, 
    *   the MapperClass uses libraries to map each ClassMetadata field from source and target entities.
    */
    private String createMapperClassCode(String packageName, String className, List<ClassMetadata> sourceClassMetadata, List<ClassMetadata> targetClassMetadata) {
        StringBuilder strBuilder = new StringBuilder();
        
        // PACKAGE
        strBuilder.append("package ").append(packageName.toLowerCase()).append(";").append("\n\n");
        // IMPORTS
        strBuilder.append("import ").append("mf.mapping.MfMapper").append(";").append("\n");
        strBuilder.append("import ").append("org.modelmapper.PropertyMap").append(";").append("\n");
        strBuilder.append("\n");
        // CLASS
        strBuilder.append("public class ").append(className).append(" extends MfMapper ").append("{").append("\n");
        // CONSTRUCTOR
        strBuilder.append(createConstrutorOfClassMapper(className, sourceClassMetadata, targetClassMetadata)); 
        // CUSTOM MAPPINGS
        strBuilder.append(createCustomPropertiesMappings(sourceClassMetadata, targetClassMetadata)); 
        // END OF CLASS
        strBuilder.append("}").append("\n");
        
        System.out.println(strBuilder.toString());
        return strBuilder.toString();
    }

    /*
    * The method below create the constructor for the MapperClass
    */
    private String createConstrutorOfClassMapper(String className, List<ClassMetadata> sourceClassMetadata, List<ClassMetadata> targetClassMetadata) {
        StringBuilder strBuilder = new StringBuilder();
        
        String sourcePackageName = sourceClassMetadata.get(sourceClassMetadata.size()-1).getPackageName();
        String targetPackageName = targetClassMetadata.get(targetClassMetadata.size()-1).getPackageName();
        String sourceClassName = sourceClassMetadata.get(sourceClassMetadata.size()-1).getName();
        String targetClassName = targetClassMetadata.get(targetClassMetadata.size()-1).getName();

        // BEGIN OF CONSTRUCTOR        
        strBuilder.append("\t").append("public ").append(className).append("() {").append("\n");
        // SUPER STATEMENT: main mapping
        strBuilder.append("\t").append("\t").append("super(").append("\n");
        strBuilder.append("\t\t\t").append(sourcePackageName).append(".").append(sourceClassName).append(".class");
        strBuilder.append(", ").append("\n");
        strBuilder.append("\t\t\t").append(targetPackageName).append(".").append(targetClassName).append(".class");
        strBuilder.append("\n").append("\t\t").append(");");
        strBuilder.append("\n").append("\n");

        // We discard the last ClassMetadata object, because it is the Root ClassMetadata and it was used above.
        int sizeCollections = targetClassMetadata.size() - 1; 
        
        // NESTING MAPPINGS: remaining mappings when the entity is composed by nested classes
        for (int i = 0; i < sizeCollections; i++) {
            sourcePackageName = sourceClassMetadata.get(i).getPackageName();
            targetPackageName = targetClassMetadata.get(i).getPackageName();
            sourceClassName = sourceClassMetadata.get(i).getName();
            targetClassName = targetClassMetadata.get(i).getName();

            strBuilder.append("\t").append("\t").append("this.mapNestedEntity(").append("\n");
            strBuilder.append("\t\t\t").append(sourcePackageName).append(".").append(sourceClassName).append(".class");
            strBuilder.append(", ").append("\n");
            strBuilder.append("\t\t\t").append(targetPackageName).append(".").append(targetClassName).append(".class");
            strBuilder.append("\n").append("\t\t").append(");");
            strBuilder.append("\n").append("\n");
        }
        
        strBuilder.append("\t\t").append("customPropertyMapping();").append("\n");
        
        strBuilder.append("\t\t").append("this.printPropertyMappings();").append("\n");
        
        // END OF CONSTRUTOR
        strBuilder.append("\t").append("}").append("\n");
        
        return strBuilder.toString();
    }
    
    private String createCustomPropertiesMappings(List<ClassMetadata> sourceClassMetadata, List<ClassMetadata> targetClassMetadata) {
        StringBuilder strBuilder = new StringBuilder();
        
        // BEGIN OF METHOD        
        strBuilder.append("\t").append("private void ").append("customPropertyMapping() {").append("\n");

        // ALL MAPPINGS: here the user can add custom properties mappings
        for (int i = 0; i < targetClassMetadata.size(); i++) {
            String sourcePackageName = sourceClassMetadata.get(i).getPackageName();
            String targetPackageName = targetClassMetadata.get(i).getPackageName();
            String sourceClassName = sourceClassMetadata.get(i).getName();
            String targetClassName = targetClassMetadata.get(i).getName();
            
            strBuilder.append("\t\t").append("PropertyMap ").append(targetClassName+"Maps");
            strBuilder.append(" = new PropertyMap<");
            strBuilder.append(sourcePackageName).append(".").append(sourceClassName);
            strBuilder.append(", ");
            strBuilder.append(targetPackageName).append(".").append(targetClassName);
            strBuilder.append(">() {\n");
            strBuilder.append("\t\t\t").append("protected void configure() {\n");
            strBuilder.append("\t\t\t\t").append("// map().set_id(null);").append(" // Example of custom property mapping: the 'id' field will be set to null (constant value).\n");
            strBuilder.append("\t\t\t").append("}\n");
            strBuilder.append("\t\t").append("};\n");
            
            strBuilder.append("\t\t").append("this.getModelMapper().addMappings(").append(targetClassName+"Maps").append(");\n");
            
        }
        strBuilder.append("\n");
        strBuilder.append("\t").append("}");
        
        return strBuilder.toString();
    }
}