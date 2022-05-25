/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.classmetadata.odm;

import mf.classmetadata.Annotation;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassRelationshipFieldType;

/**
 *
 * @author evand
 */
public class MfHibernateODMCustomization extends MfClassODMCustomization {
    
    @Override
    public void generateCustomizations() {
        rootEntityCustomizations();
        embeddedEntitiesCustomizations();
    }
    
    private void rootEntityCustomizations(){
        this.getRootEntity().getAnnotations().clear();
        this.getRootEntity().getImports().clear();
        this.getRootEntity().getFields().clear();
        
        this.getRootEntity().getImports().add(new ClassImport("javax.persistence.Entity"));
        this.getRootEntity().getImports().add(new ClassImport("javax.persistence.GeneratedValue"));
        this.getRootEntity().getImports().add(new ClassImport("javax.persistence.Id"));
        this.getRootEntity().getImports().add(new ClassImport("javax.persistence.GenerationType"));
        this.getRootEntity().getImports().add(new ClassImport("javax.persistence.ElementCollection"));
        this.getRootEntity().getImports().add(new ClassImport("org.hibernate.annotations.Type"));
        this.getRootEntity().getAnnotations().add(new Annotation("@Entity(name = \""+this.getRootEntity().getName()+"\")"));
        
        // In the case above, all root entity will has an 'id' field.
        ClassField id = new ClassField("private", "String", "_id", ClassRelationshipFieldType.NONE);
        id.getAnnotations().add(new Annotation("@Id"));
        id.getAnnotations().add(new Annotation("@GeneratedValue(strategy = GenerationType.IDENTITY)"));
        id.getAnnotations().add(new Annotation("@Type(type = \"objectid\")"));
        this.getRootEntity().getFields().add(id);
        
        // The field below only has the attribute relationshipeType setted. This means it represents all the fields of type ARRAYS_OF_OBJECTS. 
        // As a result, all the fields of relationshipType ARRAYS_OF_OBJECTS will have @ElementCollection annotation setted.
        ClassField defaultListField = new ClassField("", "", "", ClassRelationshipFieldType.ARRAY_OF_OBJECTS);
        defaultListField.getAnnotations().add(new Annotation("@ElementCollection"));
        this.getRootEntity().getFields().add(defaultListField);        
    }
    
    private void embeddedEntitiesCustomizations(){
        this.getEmbeddedEntities().getImports().clear();
        this.getEmbeddedEntities().getAnnotations().clear();
        this.getEmbeddedEntities().getFields().clear();
        
        this.getEmbeddedEntities().getImports().add(new ClassImport("javax.persistence.Embeddable"));
        this.getEmbeddedEntities().getAnnotations().add(new Annotation("@Embeddable"));
        
        ClassField defaultListField = new ClassField("", "", "", ClassRelationshipFieldType.ARRAY_OF_OBJECTS);
        defaultListField.getAnnotations().add(new Annotation("@ElementCollection"));
        this.getEmbeddedEntities().getFields().add(defaultListField);
    }
    
}