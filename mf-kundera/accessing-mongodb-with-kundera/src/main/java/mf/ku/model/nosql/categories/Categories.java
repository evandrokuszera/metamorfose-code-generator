package mf.ku.model.nosql.categories;

import javax.persistence.Id;
import javax.persistence.Entity;

@Entity(name="Categories")
public class Categories{
	@Id
	private java.lang.Integer id_category;
	private java.lang.String categoryname;
	public java.lang.Integer getId_category(){
		return id_category;
	}
	public void setId_category(java.lang.Integer value) {
		this.id_category = value;
	}
	public java.lang.String getCategoryname(){
		return categoryname;
	}
	public void setCategoryname(java.lang.String value) {
		this.categoryname = value;
	}
}
