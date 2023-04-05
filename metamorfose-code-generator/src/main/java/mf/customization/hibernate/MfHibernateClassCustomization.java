/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.customization.hibernate;

import mf.classmetadata.Annotation;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;
import mf.customization.MfClassCustomization;
import mf.schema.model.MfEntity;
import mf.schema.model.MfSchema;

/**
 *
 * @author evand
 */
public class MfHibernateClassCustomization extends MfClassCustomization {
    
    private void forRootClass(MfEntity entityClassMetadata){
        ClassMetadata rootClassMetadata = entityClassMetadata.getClassMetadataList().get(entityClassMetadata.getClassMetadataList().size() - 1);
                
        rootClassMetadata.getImports().add(new ClassImport("javax.persistence.Entity"));
        rootClassMetadata.getImports().add(new ClassImport("javax.persistence.GeneratedValue"));
        rootClassMetadata.getImports().add(new ClassImport("javax.persistence.Id"));
        rootClassMetadata.getImports().add(new ClassImport("javax.persistence.GenerationType"));
        rootClassMetadata.getImports().add(new ClassImport("javax.persistence.ElementCollection"));
        rootClassMetadata.getImports().add(new ClassImport("org.hibernate.annotations.Type"));
        rootClassMetadata.getAnnotations().add(new Annotation("@Entity(name = \""+rootClassMetadata.getName()+"\")"));
        
        // In the case above, all root entity will has an 'id' field.
        ClassField id = new ClassField("private", "String", "_id", ClassRelationshipFieldType.NONE, true, false);
        id.getAnnotations().add(new Annotation("@Id"));
        id.getAnnotations().add(new Annotation("@GeneratedValue(strategy = GenerationType.IDENTITY)"));
        id.getAnnotations().add(new Annotation("@Type(type = \"objectid\")"));
        rootClassMetadata.getFields().add(id);
        
        // The field below only has the attribute relationshipeType setted. This means it represents all the fields of type ARRAYS_OF_OBJECTS. 
        // As a result, all the fields of relationshipType ARRAYS_OF_OBJECTS will have @ElementCollection annotation setted.
        ClassField defaultListField = new ClassField("", "", "", ClassRelationshipFieldType.ARRAY_OF_OBJECTS, false, false);
        defaultListField.getAnnotations().add(new Annotation("@ElementCollection"));
        rootClassMetadata.getFields().add(defaultListField);        
    }
    
    
    private void forAllNestedClasses(MfEntity entityClassMetadata){
        ClassMetadata rootClassMetadata = entityClassMetadata.getClassMetadataList().get(entityClassMetadata.getClassMetadataList().size() - 1);
        
        for (ClassMetadata clazz : entityClassMetadata.getClassMetadataList()) {
            
            if (rootClassMetadata == clazz) {
                break;
            }
            
            // if you need imports, annotations or new class fields for the nested entities, then add it below.
            clazz.getImports().add(new ClassImport("javax.persistence.Embeddable"));
            clazz.getAnnotations().add(new Annotation("@Embeddable"));

            ClassField defaultListField = new ClassField("", "", "", ClassRelationshipFieldType.ARRAY_OF_OBJECTS, false, false);
            defaultListField.getAnnotations().add(new Annotation("@ElementCollection"));
            clazz.getFields().add(defaultListField);
        }
    }
    
    @Override
    public MfEntity applyCustomizationsTo(MfEntity entityClassMetadata) {
        
        forRootClass(entityClassMetadata);
        
        forAllNestedClasses(entityClassMetadata);
        
        return entityClassMetadata;
    
    }

    @Override
    public MfSchema applyCustomizationsTo(MfSchema schema) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}