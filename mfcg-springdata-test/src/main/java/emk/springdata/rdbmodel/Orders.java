package emk.springdata.rdbmodel;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Orders {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date orderdate;
    private Double total;
    @OneToMany(mappedBy = "orderid", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Orderlines> orderlinesList;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public List<Orderlines> getOrderlinesList() {
		return orderlinesList;
	}
	public void setOrderlinesList(List<Orderlines> orderlinesList) {
		this.orderlinesList = orderlinesList;
	}
}