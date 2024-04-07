package mf.ku.model.nosql.orders;

import javax.persistence.Embeddable;
import mf.ku.model.nosql.products.Products;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Embeddable
public class Orderlines{
	private java.lang.Integer orderlineid;
	private java.lang.Integer orderid;
	private java.lang.Integer prod_id;
	private java.lang.Integer quantity;
	private java.util.Date orderlinedate;
//	@ManyToOne
//	@JoinColumn(name="prod_id")
	private Products products;
	public java.lang.Integer getOrderlineid(){
		return orderlineid;
	}
	public void setOrderlineid(java.lang.Integer value) {
		this.orderlineid = value;
	}
	public java.lang.Integer getOrderid(){
		return orderid;
	}
	public void setOrderid(java.lang.Integer value) {
		this.orderid = value;
	}
	public java.lang.Integer getProd_id(){
		return prod_id;
	}
	public void setProd_id(java.lang.Integer value) {
		this.prod_id = value;
	}
	public java.lang.Integer getQuantity(){
		return quantity;
	}
	public void setQuantity(java.lang.Integer value) {
		this.quantity = value;
	}
	public java.util.Date getOrderlinedate(){
		return orderlinedate;
	}
	public void setOrderlinedate(java.util.Date value) {
		this.orderlinedate = value;
	}
	public Products getProducts(){
		return products;
	}
	public void setProducts(Products value) {
		this.products = value;
	}
}
