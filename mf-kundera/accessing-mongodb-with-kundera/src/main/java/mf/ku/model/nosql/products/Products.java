package mf.ku.model.nosql.products;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Embedded;
import mf.ku.model.nosql.orders.Orderlines;
import javax.persistence.OneToMany;

@Entity(name="Products")
public class Products{
	@Id
	private java.lang.Integer id_prod;
	private java.lang.Integer category;
	private java.lang.String title;
	private java.lang.String actor;
	private java.lang.Double price;
	private java.lang.Integer special;
	private java.lang.Integer common_prod_id;
	@Embedded
	private Inventory inventory;
	@Embedded
	private Categories categories;
	@OneToMany(mappedBy="prod_id")
	private java.util.List<Orderlines> orderlines;
	public java.lang.Integer getId_prod(){
		return id_prod;
	}
	public void setId_prod(java.lang.Integer value) {
		this.id_prod = value;
	}
	public java.lang.Integer getCategory(){
		return category;
	}
	public void setCategory(java.lang.Integer value) {
		this.category = value;
	}
	public java.lang.String getTitle(){
		return title;
	}
	public void setTitle(java.lang.String value) {
		this.title = value;
	}
	public java.lang.String getActor(){
		return actor;
	}
	public void setActor(java.lang.String value) {
		this.actor = value;
	}
	public java.lang.Double getPrice(){
		return price;
	}
	public void setPrice(java.lang.Double value) {
		this.price = value;
	}
	public java.lang.Integer getSpecial(){
		return special;
	}
	public void setSpecial(java.lang.Integer value) {
		this.special = value;
	}
	public java.lang.Integer getCommon_prod_id(){
		return common_prod_id;
	}
	public void setCommon_prod_id(java.lang.Integer value) {
		this.common_prod_id = value;
	}
	public Inventory getInventory(){
		return inventory;
	}
	public void setInventory(Inventory value) {
		this.inventory = value;
	}
	public Categories getCategories(){
		return categories;
	}
	public void setCategories(Categories value) {
		this.categories = value;
	}
	public java.util.List<Orderlines> getOrderlines(){
		return orderlines;
	}
	public void setOrderlines(java.util.List<Orderlines> value) {
		this.orderlines = value;
	}
}
