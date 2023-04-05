/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.mapping;

import java.util.ArrayList;
import java.util.List;
import mf.schema.model.MfEntity;

/**
 *
 * @author evand
 */
public class MfMapping {
    private String packageName;
    private String className;
    private String entityName;
    private List<MfEntity> sourceMfEntities = new ArrayList<>();
    private List<MfEntity> targetMfEntities = new ArrayList<>();
    
    public MfMapping(String packageName, String className, String entityName) {
        this.packageName = packageName;
        this.className = className;
        this.entityName = entityName;
    }
    
    public List<MfEntity> getSourceMfEntities() {
        return sourceMfEntities;
    }
    
    public List<MfEntity> getTargetMfEntities() {
        return targetMfEntities;
    }
    
    public MfEntity getMfSourceEntity(String name){
        for (MfEntity e : sourceMfEntities){
            if (e.getName().equalsIgnoreCase(name)){
                return e;
            }
        }
        return null;
    }
    public MfEntity getMfTargetEntity(String name){
        for (MfEntity e : targetMfEntities){
            if (e.getName().equalsIgnoreCase(name)){
                return e;
            }
        }
        return null;
    }
    
    public MfEntity getMfRootSourceEntity(){
        if (!sourceMfEntities.isEmpty()){
            int lastEntityIndex = sourceMfEntities.size() - 1; // root entity is the last add into the array as the entities are add according to DAG order.
            return sourceMfEntities.get(lastEntityIndex);
        } else {
            return null;
        }
    }
    
    public MfEntity getMfRootTargetEntity(){
        if (!targetMfEntities.isEmpty()){
            int lastEntityIndex = targetMfEntities.size() - 1; // root entity is the last add into the array as the entities are add according to DAG order.
            return targetMfEntities.get(lastEntityIndex);
        } else {
            return null;
        }
    }
    
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public void print(){
        System.out.printf("Mapping.entityName = %s\n", this.entityName);
        System.out.printf("Mapping.package = %s\n", this.packageName);
        System.out.printf("Mapping.class = %s\n", this.className);

        List<String> sourceClassList = new ArrayList<>();
        List<String> targetClassList = new ArrayList<>();
        
        // Creating the source ClassMetadata list
        for (int i=0; i<this.getSourceMfEntities().size(); i++){
            MfEntity sourceEntity = this.getSourceMfEntities().get(i);
            for (int j=0; j<sourceEntity.getClassMetadataList().size(); j++){
                sourceClassList.add(sourceEntity.getClassMetadataList().get(j).getClassQualifiedName());
            }
        }

        // Creating the target ClassMetadata list
        for (int i=0; i<this.getTargetMfEntities().size(); i++){
            MfEntity targetEntity = this.getTargetMfEntities().get(i);
            for (int j=0; j<targetEntity.getClassMetadataList().size(); j++){
                targetClassList.add(targetEntity.getClassMetadataList().get(j).getClassQualifiedName());
            }
        }
        
        // Printing the list of ClassMetadata objects of the Mapping between source and target MfEntities.
        int size = sourceClassList.size();
        for (int i=0; i<size; i++){
            System.out.printf("\tMapping.sourceClassList(%d) = %s\n", i, sourceClassList.get(i));
            System.out.printf("\tMapping.targetClassList(%d) = %s\n", i, targetClassList.get(i));
        }
    }
}