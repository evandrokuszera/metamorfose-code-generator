package emk.collection.orders;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Orders")
public class DocOrders{
	private java.lang.Integer id;
	private java.util.Date orderdate;
	private java.lang.Double total;
	private java.util.List<DocOrderlines> orderlines;
	@Id
	private String _id;
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
	public java.util.List<DocOrderlines> getOrderlines(){
		return orderlines;
	}
	public void setOrderlines(java.util.List<DocOrderlines> value) {
		this.orderlines = value;
	}
	public String get_id(){
		return _id;
	}
	public void set_id(String value) {
		this._id = value;
	}
}
