package emk.springdata.rdbrepositories;

import org.springframework.data.jpa.repository.JpaRepository;

import emk.springdata.rdbmodel.Products;

public interface ProductsRdbRepository extends JpaRepository<Products, Integer> {

}
