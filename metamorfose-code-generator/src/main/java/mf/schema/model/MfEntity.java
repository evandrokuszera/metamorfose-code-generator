/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.schema.model;

import java.util.ArrayList;
import java.util.List;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassMetadata;

/**
 *
 * @author evand
 */
public class MfEntity {
    private String name;
    private List<ClassMetadata> classMetadataList;

    public MfEntity(String name) {
        this.name = name;
        this.classMetadataList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addClassMetadata(ClassMetadata classMetadataItem){
        this.classMetadataList.add(classMetadataItem);
    }
    
    public ClassMetadata getClassMetadata(String name){
        for (ClassMetadata c : this.getClassMetadataList()){
            if (c.getName().equalsIgnoreCase(name)){
                return c;
            }
        }
        return null;
    }
    
    public ClassMetadata getClassMetadata(int id){
        for (ClassMetadata c : this.getClassMetadataList()){
            if (c.getId() == id){
                return c;
            }
        }
        return null;
    }

    public List<ClassMetadata> getClassMetadataList() {
        return classMetadataList;
    }

    public void setClassMetadataList(List<ClassMetadata> classMetadataList) {
        this.classMetadataList = classMetadataList;
    }
    
    public ClassMetadata getRootClassMetadata(){
        return this.classMetadataList.get(this.classMetadataList.size() - 1);
    }
    
    public List<ClassField> getRootClassMetadataPK(){
        List<ClassField> pkFields = new ArrayList<>();
        
        for (ClassField field : getRootClassMetadata().getFields()){
            if (field.isPk()){
                pkFields.add(field);
            }
        }
        return pkFields;
    }
}