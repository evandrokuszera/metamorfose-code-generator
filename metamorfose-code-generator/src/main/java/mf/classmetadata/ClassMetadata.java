/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.classmetadata;

import java.util.ArrayList;

/**
 *
 * @author evand
 */
public class ClassMetadata {
    private String name;
    private String packageName;
    private ArrayList<ClassField> fields = new ArrayList<>();
    private ArrayList<Annotation> annotations = new ArrayList<>();
    private ArrayList<ClassImport> imports = new ArrayList<>();
    
    public ClassField existField(String name){
        for (ClassField f : fields){
            if (f.getName().equals(name)){
                return f;
            }
        }
        return null;
    }
    
    public void updateOrAddClassField(ClassField inputField){
        // try to update the ClassField with inputField values.
        for (ClassField field : fields){
            if (
                    (inputField.getModifier().trim().length() == 0 || inputField.getModifier().equals(field.getModifier())) &&
                    (inputField.getType().trim().length() == 0 || inputField.getType().equals(field.getType())) &&
                    (inputField.getName().trim().length() == 0 || inputField.getName().equals(field.getName())) &&
                    (inputField.getRelationshipType() == field.getRelationshipType())    
                ){
                if (inputField.getModifier().trim().length() > 0) field.setModifier(inputField.getModifier() );
                if (inputField.getType().trim().length() > 0) field.setType(inputField.getType() );
                if (inputField.getName().trim().length() > 0) field.setName(inputField.getName() );
                field.setRelationshipType(inputField.getRelationshipType());
                
                // add the annotation from inputField to field                
                for (Annotation inputAnnotation : inputField.getAnnotations()){
                    field.getAnnotations().add(inputAnnotation);
                }

            }
        }
        // otherwise, add the ClassField if it has all the fields with values
        if (inputField.getModifier().trim().length() > 0 && inputField.getType().trim().length() > 0 && inputField.getName().trim().length() > 0){
            fields.add(inputField);
        }
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ArrayList<ClassField> getFields() {
        return fields;
    }

    public void setFields(ArrayList<ClassField> fields) {
        this.fields = fields;
    }

    public ArrayList<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ArrayList<Annotation> annotations) {
        this.annotations = annotations;
    }

    public ArrayList<ClassImport> getImports() {
        return imports;
    }

    public void setImports(ArrayList<ClassImport> imports) {
        this.imports = imports;
    }
}