/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.customization.datanucleus;

import java.util.ArrayList;
import mf.classmetadata.Annotation;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;
import mf.customization.MfClassCustomization;
import mf.schema.model.MfEntity;
import mf.schema.model.MfRelationship;
import mf.schema.model.MfSchema;

/**
 *
 * @author evand
 */
public class MfDataNucleusMongoCustomization extends MfClassCustomization {
    
    public MfEntity applyCustomizationsTo(MfEntity entityClassMetadata) {  
        
        forRootClass(entityClassMetadata);        
        forNestedClasses(entityClassMetadata);        
        return entityClassMetadata;      
        
    }
    
    // 1 - Applying customization for the rootClass
    private void forRootClass(MfEntity entityClassMetadata){
        // The entity was generated by DAG structure and it can has a plan or a hierarchy structure.
        // If the entity has an hierachy structure, then we need to get the Root Entity and customize with some annotations, as @Document.        
        // According the structure of the DAG, the last added class in classMetadataList is the Root Entity.
        ClassMetadata rootClassMetadata = entityClassMetadata.getClassMetadataList().get(entityClassMetadata.getClassMetadataList().size() - 1);
        // Adding imports to Root Class
        rootClassMetadata.getImports().add(new ClassImport("javax.persistence.Id"));
        rootClassMetadata.getImports().add(new ClassImport("javax.persistence.Entity"));
        rootClassMetadata.getImports().add(new ClassImport("javax.persistence.Embedded"));
        // Adding the @Entity tag to Root Class
        rootClassMetadata.getAnnotations().add(new Annotation("@Entity(name=\"" + rootClassMetadata.getName() + "\")"));
        // Adding the @Id for the Root Class, according to DAG metadata. _id field wasn't used in this version of MfCodeGenerator.
        for (ClassField field : rootClassMetadata.getFields()){
            if (field.isPk()){
                field.getAnnotations().add(new Annotation("@Id"));
            }
        }
        // Adding annotations and imports for nesting relationship between classes of same entities.
        for (ClassField field : rootClassMetadata.getFields()){
            if (field.getRelationshipType() == ClassRelationshipFieldType.ARRAY_OF_OBJECTS ||
                    field.getRelationshipType() == ClassRelationshipFieldType.OBJECT){
                // Adding annotation to an attribute of Root Class
                field.getAnnotations().add(new Annotation("@Embedded"));
                // Retrieving the nesting classMetada type
                ClassMetadata nestingClassMetadata = entityClassMetadata.getClassMetadata( field.getType() );
                // If found, then
                if (nestingClassMetadata != null){
                    // Adding imports to Root Class
                    rootClassMetadata.addImport(new ClassImport("javax.persistence.AttributeOverride"));
                    rootClassMetadata.addImport(new ClassImport("javax.persistence.Column"));
                    // Adding annotation for the attributes that represents nesting relationships.
                    for (ClassField fieldOfNestingClassMetadata: nestingClassMetadata.getFields()){
                        field.getAnnotations().add(new Annotation("@AttributeOverride(name=\""+fieldOfNestingClassMetadata.getName()+"\", column=@Column(name=\""+fieldOfNestingClassMetadata.getName()+"\"))"));
                    }
                }
            }
        }
    }
    
    
    // 2 - Applying customization for the other classes (no root classes).
    private void forNestedClasses(MfEntity entityClassMetadata){
        // According the structure of the DAG, the last added class in classMetadataList is the Root Entity.
        ClassMetadata rootClassMetadata = entityClassMetadata.getClassMetadataList().get(entityClassMetadata.getClassMetadataList().size() - 1);
        // For each nested Class of Entity
        for (ClassMetadata clazz : entityClassMetadata.getClassMetadataList()) {
            // Ignoring Root Class Metadata
            if (rootClassMetadata == clazz) {
                break;
            }
            
            // For each nested class, you can add:
            //clazz.getImports().add( ? );
            //clazz.getAnnotations().add( ? );
            //clazz.updateOrAddClassField( ? );
            //clazz.getFields();
            
            // Adding imports to Nested Class
            clazz.getImports().add(new ClassImport("javax.persistence.Embeddable"));
            // Adding annotations to Nested Class
            clazz.getAnnotations().add(new Annotation("@Embeddable"));
            // Adding embedded annotation to Nested Classes
            boolean annotationEmbeddedFlag = false;
            for (ClassField field : clazz.getFields()){
                if (field.getRelationshipType() == ClassRelationshipFieldType.ARRAY_OF_OBJECTS ||
                        field.getRelationshipType() == ClassRelationshipFieldType.OBJECT){
                    field.getAnnotations().add(new Annotation("@Embedded"));
                    annotationEmbeddedFlag = true;
                }
            }
            
            if (annotationEmbeddedFlag) clazz.getImports().add(new ClassImport("javax.persistence.Embedded"));
        }
    }

    @Override
    public MfSchema applyCustomizationsTo(MfSchema schema) {
        
        ArrayList<ClassField> listOfFieldsAlreadyProcessed = new ArrayList<>();
        
        // Adding annotations and imports to establish relationship (References) between Entities of the schema.
        for (MfRelationship rel : schema.getRelationships()){
            // Retrieving the ClassMetadata of the relationship (type reference)
            ClassMetadata onesideClazz = rel.getOneSideClassMetadata();
            ClassMetadata manysideClazz = rel.getManySideClassMetadata();
            
            // Adding imports
            onesideClazz.getImports().add(new ClassImport("javax.persistence.OneToMany"));
            manysideClazz.getImports().add(new ClassImport("javax.persistence.ManyToOne"));
            
            // In the one side, if the relationship between classes is of type ARRAY_OF_OBJECTS, then
            for (ClassField clazzField : onesideClazz.getFields()) {
                if (clazzField.getRelationshipType() == ClassRelationshipFieldType.ARRAY_OF_OBJECTS && !listOfFieldsAlreadyProcessed.contains(clazzField)){
                    clazzField.getAnnotations().add(new Annotation("@OneToMany(mappedBy=\"" + clazzField.getRefForeignFieldName() + "\")"));
                    listOfFieldsAlreadyProcessed.add(clazzField);
                }
            }
            
            // In the many side, if the relationship between classes is of type OBJECT, then
            for (ClassField clazzField : manysideClazz.getFields()) {
                if (clazzField.getRelationshipType() == ClassRelationshipFieldType.OBJECT && !listOfFieldsAlreadyProcessed.contains(clazzField)){
                    clazzField.getAnnotations().add(new Annotation("@ManyToOne"));
                    listOfFieldsAlreadyProcessed.add(clazzField);
                }
            }
        }
        
        return schema;
    }
}