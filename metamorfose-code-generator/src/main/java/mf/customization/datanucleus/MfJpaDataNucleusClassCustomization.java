/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.customization.datanucleus;

import java.util.List;
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
public class MfJpaDataNucleusClassCustomization extends MfClassCustomization {
    
    @Override
    public MfEntity applyCustomizationsTo(MfEntity entityClassMetadata) {
        //List<ClassMetadata> classMetadataListCustomizated = getClone(classMetadataList);
        
        forAllClasses(entityClassMetadata.getClassMetadataList());
        
        ifPkAttribute(entityClassMetadata.getClassMetadataList());
        
        ifFkAttribute(entityClassMetadata.getClassMetadataList());
        
        ifRelationshipField(entityClassMetadata.getClassMetadataList());
        
        return entityClassMetadata;
    }

    private void forAllClasses(List<ClassMetadata> classMetadataList) {
        for (ClassMetadata clazz : classMetadataList) {
            clazz.getImports().add(new ClassImport("javax.persistence.Entity"));
            clazz.getAnnotations().add(new Annotation("@Entity(name=\""+clazz.getName()+"\")"));
            
            clazz.getImports().add(new ClassImport("javax.persistence.Table"));
            clazz.getAnnotations().add(new Annotation("@Table(name=\""+clazz.getName().toLowerCase()+"\")")); // the name of RDB table should be the same that is in clazz.getName
        }
    }
    
    private void ifPkAttribute(List<ClassMetadata> classMetadataList) {
        for (ClassMetadata clazz : classMetadataList) {
            for (ClassField clazzField : clazz.getFields()) {
                if (clazzField.isPk()){
                    clazz.getImports().add(new ClassImport("javax.persistence.Id"));
                    clazzField.getAnnotations().add(new Annotation("@Id"));
                }
            }
        }
    }
    
    private void ifFkAttribute(List<ClassMetadata> classMetadataList) {
        for (ClassMetadata clazz : classMetadataList) {
            for (ClassField clazzField : clazz.getFields()) {
                if (clazzField.isFk()){
                    clazz.getImports().add(new ClassImport("javax.persistence.Column"));
                    clazzField.getAnnotations().add(new Annotation("@Column(insertable = false, updatable = false)"));
                }
            }
        }
    }
    
    private void ifRelationshipField(List<ClassMetadata> classMetadataList) {
        for (ClassMetadata clazz : classMetadataList) {
            for (ClassField clazzField : clazz.getFields()) {
                if (clazzField.getRelationshipType() == ClassRelationshipFieldType.ARRAY_OF_OBJECTS){
                    
                    clazz.getImports().add(new ClassImport("javax.persistence.OneToMany"));
                    clazz.getImports().add(new ClassImport("javax.persistence.FetchType"));
                    clazz.getImports().add(new ClassImport("javax.persistence.JoinColumn"));
                    clazzField.getAnnotations().add(new Annotation("@OneToMany(mappedBy = \""+clazz.getName().toLowerCase()+"\", fetch = FetchType.EAGER)"));
                    clazzField.getAnnotations().add(new Annotation("@JoinColumn(name = \""+clazzField.getRefForeignFieldName()+"\")"));
                
                } else if (clazzField.getRelationshipType() == ClassRelationshipFieldType.OBJECT){
                    
                    clazz.getImports().add(new ClassImport("javax.persistence.ManyToOne"));
                    clazz.getImports().add(new ClassImport("javax.persistence.JoinColumn"));
                    clazzField.getAnnotations().add(new Annotation("@ManyToOne"));
                    clazzField.getAnnotations().add(new Annotation("@JoinColumn(name = \""+clazzField.getRefLocalFieldName()+"\", referencedColumnName = \""+clazzField.getRefForeignFieldName()+"\")"));
                    
                }
            }
        }
    }

    @Override
    public MfSchema applyCustomizationsTo(MfSchema schema) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}