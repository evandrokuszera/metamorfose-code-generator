package mf.sd.model.nosql.products;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import mf.sd.model.nosql.orders.Orderlines;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "Products")
public class Products {
    @Id
    private String _id;
    private Integer id_prod;
    private Inventory inventory;
    private Categories categories;
    @ReadOnlyProperty
    @DocumentReference(lookup = 
       "{'prod_id':?#{#self.id_prod}}")
    private List<Orderlines> orderlines;
    // remaining fields,
    //  getters and setters
    private java.lang.Integer category;
    private java.lang.String title;
    private java.lang.String actor;
    private java.lang.Double price;
    private java.lang.Integer special;
    private java.lang.Integer common_prod_id;
    
    

    public java.lang.Integer getId_prod() {
        return id_prod;
    }

    public void setId_prod(java.lang.Integer value) {
        this.id_prod = value;
    }

    public java.lang.Integer getCategory() {
        return category;
    }

    public void setCategory(java.lang.Integer value) {
        this.category = value;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setTitle(java.lang.String value) {
        this.title = value;
    }

    public java.lang.String getActor() {
        return actor;
    }

    public void setActor(java.lang.String value) {
        this.actor = value;
    }

    public java.lang.Double getPrice() {
        return price;
    }

    public void setPrice(java.lang.Double value) {
        this.price = value;
    }

    public java.lang.Integer getSpecial() {
        return special;
    }

    public void setSpecial(java.lang.Integer value) {
        this.special = value;
    }

    public java.lang.Integer getCommon_prod_id() {
        return common_prod_id;
    }

    public void setCommon_prod_id(java.lang.Integer value) {
        this.common_prod_id = value;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory value) {
        this.inventory = value;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories value) {
        this.categories = value;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String value) {
        this._id = value;
    }

    public java.util.List<Orderlines> getOrderlines() {
        return orderlines;
    }

    public void setOrderlines(java.util.List<Orderlines> value) {
        this.orderlines = value;
    }
}
