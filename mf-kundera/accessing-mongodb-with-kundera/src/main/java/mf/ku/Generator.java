package mf.ku;


import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import mf.customization.kundera.MfKunderaMongoCustomization;
import mf.generator.RdbTypeEnum;
import mf.schema.MfDagSchemaGenerator;
import mf.schema.MfSchemaGenerator;
import mf.utils.GraphUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author evand
 */
public class Generator {
    public static void main(String[] args) throws FileNotFoundException {
        // Path of NoSQL schema file
        String path = "..\\..\\input-nosql-schema\\dvd-store.json";
        // Loading the NoSQL Schema from disk
        NoSQLSchema schema = GraphUtils.loadNosqlSchema(path);
        // Configuring the schema generator for generating Java classe from the schema using Kundera ODM
        MfSchemaGenerator schemaCodeGenerator = new MfDagSchemaGenerator(
                schema, 
                new MfKunderaMongoCustomization(), // or MfDataNucleusMongoCustomization() or MfSpringMongoCustomization()
                RdbTypeEnum.POSTGRES);
        // Generating the schema in memory (parameter = target package for the classes).
        schemaCodeGenerator.generate("mf.ku.model.nosql");
        // Uncomment the line bellow to inspect the generated schema
        // schemaCodeGenerator.getMfSchema();
        // Uncomment the line below to print the schema
        // schemaCodeGenerator.printSchema();
        // Saving the code into the disk.
        schemaCodeGenerator.saveFiles();
    }
}
