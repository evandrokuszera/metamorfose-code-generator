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
        
        String path = "..\\..\\input-nosql-schema\\dvd-store.json";
        
        NoSQLSchema schema = GraphUtils.loadNosqlSchema(path);
        
        MfSchemaGenerator generator = new MfDagSchemaGenerator(
                schema, 
                new MfKunderaMongoCustomization(), 
                RdbTypeEnum.POSTGRES);
        
        generator.generate("mf.ku.model.nosql");
        // targetSchema.getMfSchema();
        // targetSchema.printSchema();
        generator.saveFiles();
    }
}
