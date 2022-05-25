package emk.springdata.rdbrepositories;

import org.springframework.data.jpa.repository.JpaRepository;

import emk.springdata.rdbmodel.Orderlines;

public interface OrderlinesRdbRepository extends JpaRepository<Orderlines, Integer> {

}
