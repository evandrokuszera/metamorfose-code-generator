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
public class MfSpringDataODMCustomization extends MfClassODMCustomization {
    private MfClassODMCustomization customization;
    
    @Override
    public void generateCustomizations() {
        this.getRootEntity().getAnnotations().clear();
        this.getRootEntity().getImports().clear();
        this.getRootEntity().getFields().clear();
        
        this.getRootEntity().getImports().add(new ClassImport("org.springframework.data.annotation.Id"));
        this.getRootEntity().getImports().add(new ClassImport("org.springframework.data.mongodb.core.mapping.Document"));
        this.getRootEntity().getAnnotations().add(new Annotation("@Document(collection = \""+this.getRootEntity().getName()+"\")"));
        
        ClassField id = new ClassField("private", "String", "_id", ClassRelationshipFieldType.NONE);
        id.getAnnotations().add(new Annotation("@Id"));
        this.getRootEntity().getFields().add(id);
        
        this.getEmbeddedEntities().getAnnotations().clear();
        this.getEmbeddedEntities().getImports().clear();
        this.getEmbeddedEntities().getFields().clear();
        
        this.getEmbeddedEntities().getImports().add(new ClassImport("lombok.Getter"));
        this.getEmbeddedEntities().getImports().add(new ClassImport("lombok.Setter"));
        this.getEmbeddedEntities().getImports().add(new ClassImport("lombok.Data"));
        this.getEmbeddedEntities().getAnnotations().add( new Annotation("@Data"));
    }
    
}