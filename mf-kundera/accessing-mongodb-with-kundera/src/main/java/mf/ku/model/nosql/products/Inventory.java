package mf.ku.model.nosql.products;

import javax.persistence.Embeddable;

@Embeddable
public class Inventory{
	private java.lang.Integer prod_id;
	private java.lang.Integer quan_in_stock;
	private java.lang.Integer sales;
	public java.lang.Integer getProd_id(){
		return prod_id;
	}
	public void setProd_id(java.lang.Integer value) {
		this.prod_id = value;
	}
	public java.lang.Integer getQuan_in_stock(){
		return quan_in_stock;
	}
	public void setQuan_in_stock(java.lang.Integer value) {
		this.quan_in_stock = value;
	}
	public java.lang.Integer getSales(){
		return sales;
	}
	public void setSales(java.lang.Integer value) {
		this.sales = value;
	}
}
