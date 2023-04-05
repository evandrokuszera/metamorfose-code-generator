package mf.dn.model.nosql.orders;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Embedded;
import mf.dn.model.nosql.customers.Customers;
import javax.persistence.ManyToOne;

@Entity(name="Orders")
public class Orders{
	@Id
	private java.lang.Integer id_order;
	private java.util.Date orderdate;
	private java.lang.Integer customerid;
	private java.lang.Double netamount;
	private java.lang.Double tax;
	private java.lang.Double totalamount;
	@Embedded
	private java.util.List<Orderlines> orderlines;
	@ManyToOne
	private Customers customers;
	public java.lang.Integer getId_order(){
		return id_order;
	}
	public void setId_order(java.lang.Integer value) {
		this.id_order = value;
	}
	public java.util.Date getOrderdate(){
		return orderdate;
	}
	public void setOrderdate(java.util.Date value) {
		this.orderdate = value;
	}
	public java.lang.Integer getCustomerid(){
		return customerid;
	}
	public void setCustomerid(java.lang.Integer value) {
		this.customerid = value;
	}
	public java.lang.Double getNetamount(){
		return netamount;
	}
	public void setNetamount(java.lang.Double value) {
		this.netamount = value;
	}
	public java.lang.Double getTax(){
		return tax;
	}
	public void setTax(java.lang.Double value) {
		this.tax = value;
	}
	public java.lang.Double getTotalamount(){
		return totalamount;
	}
	public void setTotalamount(java.lang.Double value) {
		this.totalamount = value;
	}
	public java.util.List<Orderlines> getOrderlines(){
		return orderlines;
	}
	public void setOrderlines(java.util.List<Orderlines> value) {
		this.orderlines = value;
	}
	public Customers getCustomers(){
		return customers;
	}
	public void setCustomers(Customers value) {
		this.customers = value;
	}
}
