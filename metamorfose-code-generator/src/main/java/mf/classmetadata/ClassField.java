/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.classmetadata;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author evand
 */
public class ClassField {
    private String modifier;
    private String type;
    private String name;
    private ClassRelationshipFieldType relationshipType;
    private String refLocalFieldName;
    private String refForeignFieldName;
    private boolean pk;
    private boolean fk;
    private List<Annotation> annotations = new ArrayList<>();

    public ClassField() {
    }

    public ClassField(String modifier, String type, String name, ClassRelationshipFieldType relationshipType, boolean isPk, boolean isFk) {
        this.modifier = modifier;
        this.type = type;
        this.name = name;
        this.relationshipType = relationshipType;
        this.pk = isPk;
        this.fk = isFk;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassRelationshipFieldType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(ClassRelationshipFieldType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public String getRefForeignFieldName() {
        return refForeignFieldName;
    }

    public void setRefForeignFieldName(String refForeignFieldName) {
        this.refForeignFieldName = refForeignFieldName;
    }

    public String getRefLocalFieldName() {
        return refLocalFieldName;
    }

    public void setRefLocalFieldName(String refLocalFieldName) {
        this.refLocalFieldName = refLocalFieldName;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public boolean isFk() {
        return fk;
    }

    public void setFk(boolean fk) {
        this.fk = fk;
    }
}
