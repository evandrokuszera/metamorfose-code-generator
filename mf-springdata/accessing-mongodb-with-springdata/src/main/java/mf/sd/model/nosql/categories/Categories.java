package mf.sd.model.nosql.categories;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Categories")
public class Categories {
    @Id
    private String _id;
    private Integer id_category;
    private String categoryname;

    public java.lang.Integer getId_category() {
        return id_category;
    }

    public void setId_category(java.lang.Integer value) {
        this.id_category = value;
    }

    public java.lang.String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(java.lang.String value) {
        this.categoryname = value;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String value) {
        this._id = value;
    }
}
