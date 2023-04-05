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
    String path = "..\\..\\input-nosql-schema\\dvd-store.json";
    
    public NoSQLSchema getNoSQLSchema(){
        return GraphUtils.loadNosqlSchema(path);
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        Generator gerador = new Generator();
        gerador.gerar();
    }
    
    
    public void gerar() throws FileNotFoundException{
        // Fase 1 =========================================================================
        MfDagSchemaGenerator targetSchema = new MfDagSchemaGenerator(
                getNoSQLSchema(), 
                new MfDataNucleusMongoCustomization(), 
                RdbTypeEnum.POSTGRES
        );
        targetSchema.generate("mf.dn.model.nosql", null);
        // targetSchema.getMfSchema();
        // targetSchema.printSchema();
        targetSchema.saveFiles();
    }
}
