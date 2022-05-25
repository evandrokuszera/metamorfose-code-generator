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
    private List<Annotation> annotations = new ArrayList<>();

    public ClassField() {
    }

    public ClassField(String modifier, String type, String name, ClassRelationshipFieldType relationshipType) {
        this.modifier = modifier;
        this.type = type;
        this.name = name;
        this.relationshipType = relationshipType;
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
}
