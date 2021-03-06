package emk.springdata.migrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import emk.collection.products.DocProducts;
import emk.springdata.nosqlrepositories.ProductsNoSQLRepository;
import emk.springdata.rdbmodel.Products;
import emk.springdata.rdbrepositories.ProductsRdbRepository;
import mf.migration.MfGenericMigration;

@Component
public class ProductEntityMigrationComponent {
	@Autowired
	private ProductsRdbRepository sourceRepo;
	@Autowired
	private ProductsNoSQLRepository targetRepo;
	
	public void run() {
		MfGenericMigration migration = new MfGenericMigration(
				Products.class, 
				DocProducts.class
		);
		
		migration.setSourceInstances(sourceRepo.findAll());		
		targetRepo.saveAll( migration.getTargetInstances() );
	}
}