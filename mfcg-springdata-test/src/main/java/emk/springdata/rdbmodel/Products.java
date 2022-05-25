package emk.springdata.rdbmodel;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Products {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private Double price;
    @OneToMany(mappedBy = "prodid")
    private List<Orderlines> orderlinesList;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public List<Orderlines> getOrderlinesList() {
		return orderlinesList;
	}
	public void setOrderlinesList(List<Orderlines> orderlinesList) {
		this.orderlinesList = orderlinesList;
	}
}