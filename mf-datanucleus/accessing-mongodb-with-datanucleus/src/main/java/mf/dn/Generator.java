/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.dn;

import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import mf.customization.datanucleus.MfDataNucleusMongoCustomization;
import mf.generator.RdbTypeEnum;
import mf.schema.MfDagSchemaGenerator;
import mf.utils.GraphUtils;

public class Generator {
    public static void main(String[] args) throws FileNotFoundException{
        // Path of NoSQL schema file
        String path = "..\\..\\input-nosql-schema\\dvd-store.json";
        // Loading the NoSQL Schema from disk
        NoSQLSchema schema = GraphUtils.loadNosqlSchema(path);
        // Configuring the schema generator for generating Java classe from the schema using Data Nucleus ODM
        MfDagSchemaGenerator schemaCodeGenerator = new MfDagSchemaGenerator(
                schema, 
                new MfDataNucleusMongoCustomization(), // or MfKunderaMongoCustomization() or MfSpringMongoCustomization()
                RdbTypeEnum.POSTGRES);
        // Generating the schema in memory (parameter = target package for the classes).
        schemaCodeGenerator.generate("mf.dn.model.nosql");
        // Uncomment the line bellow to inspect the generated schema
        // schemaCodeGenerator.getMfSchema();
        // Uncomment the line below to print the schema
        // schemaCodeGenerator.printSchema();
        // Saving the code into the disk.
        schemaCodeGenerator.saveFiles();
    }
}
