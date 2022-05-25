/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.classmetadata.odm;

import mf.classmetadata.ClassMetadata;

/**
 *
 * @author evand
 */
public abstract class MfClassODMCustomization {
    private ClassMetadata rootEntity;
    private ClassMetadata embeddedEntities;
    
    public abstract void generateCustomizations();
    
    public MfClassODMCustomization(){
        this.rootEntity = new ClassMetadata();
        this.embeddedEntities = new ClassMetadata();
    }

    public ClassMetadata getRootEntity() {
        return rootEntity;
    }

    public void setRootEntity(ClassMetadata rootEntity) {
        this.rootEntity = rootEntity;
    }

    public ClassMetadata getEmbeddedEntities() {
        return embeddedEntities;
    }

    public void setEmbeddedEntities(ClassMetadata embeddedEntities) {
        this.embeddedEntities = embeddedEntities;
    }
    
}