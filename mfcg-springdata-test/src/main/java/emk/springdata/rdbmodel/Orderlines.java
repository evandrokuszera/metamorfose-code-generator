package emk.springdata.rdbmodel;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Orderlines {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date orderlinedate;
    private Double quantity;
    private Double price;
    @JoinColumn(name = "orderid", referencedColumnName = "id")
    @ManyToOne
    private Orders orderid;
    @JoinColumn(name = "prodid", referencedColumnName = "id")
    @ManyToOne
    private Products prodid;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getOrderlinedate() {
		return orderlinedate;
	}
	public void setOrderlinedate(Date orderlinedate) {
		this.orderlinedate = orderlinedate;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Orders getOrderid() {
		return orderid;
	}
	public void setOrderid(Orders orderid) {
		this.orderid = orderid;
	}
	public Products getProdid() {
		return prodid;
	}
	public void setProdid(Products prodid) {
		this.prodid = prodid;
	}
    
}
