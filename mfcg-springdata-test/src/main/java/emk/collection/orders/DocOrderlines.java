package emk.collection.orders;

public class DocOrderlines{
	private java.lang.Integer id;
	private java.util.Date orderlinedate;
	private java.lang.Double quantity;
	private java.lang.Double price;
	private java.lang.Integer orderid;
	private java.lang.Integer prodid;
	public java.lang.Integer getId(){
		return id;
	}
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.util.Date getOrderlinedate(){
		return orderlinedate;
	}
	public void setOrderlinedate(java.util.Date value) {
		this.orderlinedate = value;
	}
	public java.lang.Double getQuantity(){
		return quantity;
	}
	public void setQuantity(java.lang.Double value) {
		this.quantity = value;
	}
	public java.lang.Double getPrice(){
		return price;
	}
	public void setPrice(java.lang.Double value) {
		this.price = value;
	}
	public java.lang.Integer getOrderid(){
		return orderid;
	}
	public void setOrderid(java.lang.Integer value) {
		this.orderid = value;
	}
	public java.lang.Integer getProdid(){
		return prodid;
	}
	public void setProdid(java.lang.Integer value) {
		this.prodid = value;
	}
}
