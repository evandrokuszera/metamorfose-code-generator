package emk.springdata;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dag.nosql_schema.NoSQLSchema;
import emk.springdata.migrations.OrderEntityMigration;
import emk.springdata.migrations.OrderlineEntityMigration;
import emk.springdata.migrations.ProductEntityMigration;
import mf.classmetadata.odm.MfSpringDataODMCustomization;
import mf.pojogenerator.fromDAG.MfPojoGeneratorFromDag;
import mf.pojogenerator.fromDAG.RdbTypeEnum;
import mf.utils.GraphUtils;

@SpringBootApplication
public class MfcgSpringdataTestApplication implements CommandLineRunner {
	@Autowired OrderEntityMigration orderEntityMigration;
	@Autowired OrderlineEntityMigration orderlineEntityMigration;
	@Autowired ProductEntityMigration productEntityMigration;
	
	public static void main(String[] args) {
		SpringApplication.run(MfcgSpringdataTestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		this.pojoGenerator(); // deve ser executado apenas uma vez para gerar as classes.
		
		orderEntityMigration.run();
		orderlineEntityMigration.run();
		productEntityMigration.run();
		
	}
	
//	public void testarAcessoRDB() {
//		List<Orders> items = ordersRdbRepo.findAll();
//		for (Orders item : items) {
//			for (Orderlines ol : item.getOrderlinesList()) {
//				System.out.println(ol.getId());
//				System.out.println(ol.getOrderid().getId());
//				System.out.println(ol.getOrderlinedate());
//				System.out.println(ol.getPrice());
//			}
//		}
//	}
	
	public void pojoGenerator() throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException {
        String path = "C:\\workspaces\\ws-metamorfose-codegenerator\\input-nosql-schema\\orders-db-schema.json";
        NoSQLSchema schema = GraphUtils.loadNosqlSchema(path);
        
        MfPojoGeneratorFromDag generator = new MfPojoGeneratorFromDag(
                schema, 
                new MfSpringDataODMCustomization(),
                RdbTypeEnum.POSTGRES
        );
        
        MfPojoGeneratorFromDag.CLASS_PREFIX = "Doc";
        
        generator.generateAndSavePojosByNoSQLSchema("emk.collection");
    }
}