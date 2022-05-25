package emk.col.products;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.ElementCollection;
import org.hibernate.annotations.Type;

@Entity(name = "Products")
public class DocProducts{
	private java.lang.Integer id;
	private java.lang.String description;
	private java.lang.Double price;
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
	public java.lang.String getDescription(){
		return description;
	}
	public void setDescription(java.lang.String value) {
		this.description = value;
	}
	public java.lang.Double getPrice(){
		return price;
	}
	public void setPrice(java.lang.Double value) {
		this.price = value;
	}
	public String get_id(){
		return _id;
	}
	public void set_id(String value) {
		this._id = value;
	}
}
