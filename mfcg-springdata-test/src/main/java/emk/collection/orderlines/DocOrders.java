package emk.collection.orderlines;

public class DocOrders{
	private java.lang.Integer id;
	private java.util.Date orderdate;
	private java.lang.Double total;
	public java.lang.Integer getId(){
		return id;
	}
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.util.Date getOrderdate(){
		return orderdate;
	}
	public void setOrderdate(java.util.Date value) {
		this.orderdate = value;
	}
	public java.lang.Double getTotal(){
		return total;
	}
	public void setTotal(java.lang.Double value) {
		this.total = value;
	}
}
