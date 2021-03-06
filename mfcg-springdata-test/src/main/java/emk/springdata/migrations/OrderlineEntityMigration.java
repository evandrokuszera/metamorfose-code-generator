package emk.springdata.migrations;


import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import emk.springdata.nosqlrepositories.OrderlinesNoSQLRepository;
import emk.springdata.rdbmodel.Orderlines;
import emk.springdata.rdbrepositories.OrderlinesRdbRepository;
import mf.migration.MfGenericMigration;

@Component
public class OrderlineEntityMigration {
	@Autowired
	private OrderlinesRdbRepository sourceRepo;
	@Autowired
	private OrderlinesNoSQLRepository targetRepo;
	
	public void run() {
		MfGenericMigration migration = new MfGenericMigration(
				Orderlines.class, 
				emk.collection.orderlines.DocOrderlines.class
		);
		
		migration.setSourceInstances(sourceRepo.findAll());
		
		migration.getModelMapper().getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		
		migration.getModelMapper().addMappings(
			new PropertyMap<Orderlines, emk.collection.orderlines.DocOrderlines>() {
	            protected void configure() {
	                map().set_id( null );
	                map().setProdid( source.getProdid().getId() );
	                map().setOrderid( source.getOrderid().getId() );
	            }
	        }
		);
		
		targetRepo.saveAll( migration.getTargetInstances() );
	}
}