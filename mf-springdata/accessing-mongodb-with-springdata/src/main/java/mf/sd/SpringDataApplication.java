package mf.sd;

import dag.nosql_schema.NoSQLSchema;
import java.io.FileNotFoundException;
import mf.customization.spring.MfSpringMongoCustomization;
import mf.generator.RdbTypeEnum;
import mf.schema.MfDagSchemaGenerator;
import mf.sd.run.SpringQueries;
import mf.sd.run.SpringCRUDQueries;
import mf.utils.GraphUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringDataApplication implements CommandLineRunner {
    @Autowired private SpringQueries runQueries;
    @Autowired private SpringCRUDQueries crudQueries;
    
    public static void main(String[] args) {
        SpringApplication.run(SpringDataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        codeGen();
//        runQueries.run();
        crudQueries.run();
    }

    // **********************************************************************
    // Generating classes for NoSQL Schema (DAG)
    // **********************************************************************
    public void codeGen() throws FileNotFoundException {
        // Loading the NoSQL Schema from disk
        String path = "..\\..\\input-nosql-schema\\dvd-store.json";
        NoSQLSchema nosqlSchema = GraphUtils.loadNosqlSchema(path);

        // Configuring the schema generator for generating Java classe from the schema using Spring Data ODM
        MfDagSchemaGenerator targetSchemaSpring = new MfDagSchemaGenerator(
                nosqlSchema,
                new MfSpringMongoCustomization(),
                RdbTypeEnum.POSTGRES
        );
        // Generating the schema in memory (parameter = target package for the classes).
        targetSchemaSpring.generate("mf.sd.model.nosql", null);
        // targetSchemaSpring.getMfSchema();
        // targetSchemaSpring.printSchema();
        targetSchemaSpring.saveFiles();
    }

}
