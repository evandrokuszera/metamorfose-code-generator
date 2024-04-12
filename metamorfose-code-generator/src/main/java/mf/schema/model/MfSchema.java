/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.schema.model;

import java.util.ArrayList;
import java.util.List;
import mf.classmetadata.ClassMetadata;

/**
 *
 * @author evand
 */
public class MfSchema {
    private List<MfEntity> entities;
    private List<MfRelationship> relationships;

    public MfSchema() {
        this.entities = new ArrayList<>();
        this.relationships = new ArrayList<>();
    }

    public void addEntity(MfEntity entity){
        this.entities.add(entity);
    }
    
    public List<MfEntity> getEntities() {
        return entities;
    }
    
    public MfEntity getEntity(String name){
        for (MfEntity e : entities){
            if (e.getName().equalsIgnoreCase(name)){
                return e;
            }
        }
        return null;
    }

    public MfEntity getEntity(ClassMetadata classMetadata){
        for (MfEntity e : entities){
            for (ClassMetadata clazz : e.getClassMetadataList()){
                if (clazz == classMetadata){
                    return e;
                }
            }
        }
        return null;
    }
    
    public ClassMetadata getEntityClassMetadata(int id){
        for (MfEntity e : entities){
            ClassMetadata clazz = e.getClassMetadata(id);
            if (clazz != null){
                return clazz;
            }
        }
        return null;
    }
    
    public List<MfRelationship> getRelationships() {
        return relationships;
    }
}