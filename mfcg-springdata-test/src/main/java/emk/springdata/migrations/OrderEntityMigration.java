package emk.springdata.migrations;

import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import emk.collection.orders.DocOrderlines;
import emk.collection.orders.DocOrders;
import emk.springdata.nosqlrepositories.OrdersNoSQLRepository;
import emk.springdata.rdbmodel.Orderlines;
import emk.springdata.rdbmodel.Orders;
import emk.springdata.rdbrepositories.OrdersRdbRepository;
import mf.migration.MfGenericMigration;

@Component
public class OrderEntityMigration {
	@Autowired
	private OrdersRdbRepository ordersRdbRepo;
	@Autowired
	private OrdersNoSQLRepository ordersNoSQLRepo;
	
	public void run() {
		MfGenericMigration migration = new MfGenericMigration(
				Orders.class, 
				DocOrders.class
		);
		
		migration.setSourceInstances(ordersRdbRepo.findAll());
		
		migration.getModelMapper().addMappings(
			new PropertyMap<Orderlines, DocOrderlines>() {
	            @Override
	            protected void configure() {
	                map().setOrderid( source.getOrderid().getId() );
	                map().setProdid( source.getProdid().getId() );
	            }
	        }
		);
		
		ordersNoSQLRepo.saveAll( migration.getTargetInstances() );
	}
}