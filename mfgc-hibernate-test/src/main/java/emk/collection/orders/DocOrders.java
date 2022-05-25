package emk.collection.orders;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.ElementCollection;
import org.hibernate.annotations.Type;

@Entity(name = "DocOrders")
public class DocOrders{
	private java.lang.Integer id;
	private java.util.Date orderdate;
	private java.lang.Double total;
	@ElementCollection
	private java.util.List<DocOrderlines> orderlines;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type = "objectid")
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
