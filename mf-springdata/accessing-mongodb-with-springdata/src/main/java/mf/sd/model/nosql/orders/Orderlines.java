package mf.sd.model.nosql.orders;

import java.util.Date;
import mf.sd.model.nosql.products.Products;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

public class Orderlines {
    private Integer orderlineid;
    private Integer orderid;
    private Integer prod_id;
    private Integer quantity;
    private Date orderlinedate;
//    @DocumentReference(lookup =
//       "{'id_prod':?#{prod_id}}")
    @DBRef
    private Products products;
    // getters and setters

    public java.lang.Integer getOrderlineid() {
        return orderlineid;
    }

    public void setOrderlineid(java.lang.Integer value) {
        this.orderlineid = value;
    }

    public java.lang.Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(java.lang.Integer value) {
        this.orderid = value;
    }

    public java.lang.Integer getProd_id() {
        return prod_id;
    }

    public void setProd_id(java.lang.Integer value) {
        this.prod_id = value;
    }

    public java.lang.Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(java.lang.Integer value) {
        this.quantity = value;
    }

    public java.util.Date getOrderlinedate() {
        return orderlinedate;
    }

    public void setOrderlinedate(java.util.Date value) {
        this.orderlinedate = value;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products value) {
        this.products = value;
    }
}
