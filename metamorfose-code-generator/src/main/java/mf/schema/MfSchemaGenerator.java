/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.schema;

import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import mf.classmetadata.ClassMetadata;
import mf.customization.MfClassCustomization;
import mf.generator.MfCodeGenerator;
import mf.generator.RdbToJavaDatatypeDictionary;
import mf.generator.RdbTypeEnum;
import mf.schema.model.MfEntity;
import mf.schema.model.MfSchema;

/**
 *
 * @author evand
 */
public abstract class MfSchemaGenerator {
    private MfSchema mfSchema;
    private MfClassCustomization classODMCustomization;
    private RdbToJavaDatatypeDictionary typeDictionary;
    private NoSQLSchema noSQLschema;
    
    public MfSchemaGenerator(NoSQLSchema noSQLschema, MfClassCustomization classODMCustomization, RdbTypeEnum typeRDBDictionary) {
        this.typeDictionary = new RdbToJavaDatatypeDictionary(typeRDBDictionary);
        this.noSQLschema = noSQLschema;
        this.classODMCustomization = classODMCustomization;
    }
    
    public abstract void generate(String packageBaseName);
    
    public MfSchema getMfSchema() {
        return mfSchema;
    }
    
    public void newMfSchema(){
        this.mfSchema = new MfSchema();
    }
    
    public NoSQLSchema getNoSQLschema() {
        return noSQLschema;
    }

    public RdbToJavaDatatypeDictionary getTypeDictionary() {
        return typeDictionary;
    }

    public MfClassCustomization getClassODMCustomization() {
        return classODMCustomization;
    }
    
    public void saveFiles() throws FileNotFoundException{
        for (MfEntity entity : mfSchema.getEntities()){
            for (ClassMetadata clazz : entity.getClassMetadataList()) {
                String classCode = MfCodeGenerator.createPojoCodeFrom(clazz);
                MfCodeGenerator.saveCodeToFile(clazz.getPackageName(), clazz.getName(), classCode);
            }
            System.out.printf("MfSchemaGenerator: save entity (%s) - OK\n", entity.getName());
        }
    }
    
    public void printSchema(){
        System.out.println("\n-----------------------------------------------------");
        System.out.println("PRINTING GENERATED SCHEMA ");
        System.out.println("Number of entities: " + mfSchema.getEntities().size());
        System.out.println("-----------------------------------------------------");
        for (MfEntity entity : mfSchema.getEntities()) {
            System.out.println("-----------------------------------------------------");
            System.out.println("Entity: " + entity.getName());
            System.out.println("Number of classes: " + entity.getClassMetadataList().size());
            System.out.println("-----------------------------------------------------");
            for (ClassMetadata clazz : entity.getClassMetadataList()) {
                String classCode = MfCodeGenerator.createPojoCodeFrom(clazz);
                System.out.println(classCode);
            }
        }
        System.out.println("Done - Printing generated schema... ");
    }
}
