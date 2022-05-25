package emk.springdata.rdbrepositories;

import org.springframework.data.jpa.repository.JpaRepository;

import emk.springdata.rdbmodel.Orders;

public interface OrdersRdbRepository extends JpaRepository<Orders, Integer> {

}
