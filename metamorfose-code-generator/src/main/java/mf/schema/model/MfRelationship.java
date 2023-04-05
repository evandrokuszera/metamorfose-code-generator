/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.schema.model;

import mf.classmetadata.ClassMetadata;

/**
 *
 * @author evand
 */
public class MfRelationship {
    private String oneSideEntity;
    private String manySideEntity;
    private String pkOneSide;
    private String fkManySide;
    private ClassMetadata oneSideClassMetadata;
    private ClassMetadata manySideClassMetadata;

    public String getOneSideEntity() {
        return oneSideEntity;
    }

    public void setOneSideEntity(String oneSideEntity) {
        this.oneSideEntity = oneSideEntity;
    }

    public String getManySideEntity() {
        return manySideEntity;
    }

    public void setManySideEntity(String manySideEntity) {
        this.manySideEntity = manySideEntity;
    }

    public String getPkOneSide() {
        return pkOneSide;
    }

    public void setPkOneSide(String pkOneSide) {
        this.pkOneSide = pkOneSide;
    }

    public String getFkManySide() {
        return fkManySide;
    }

    public void setFkManySide(String fkManySide) {
        this.fkManySide = fkManySide;
    }

    public ClassMetadata getManySideClassMetadata() {
        return manySideClassMetadata;
    }

    public void setManySideClassMetadata(ClassMetadata manySideClassMetadata) {
        this.manySideClassMetadata = manySideClassMetadata;
    }

    public ClassMetadata getOneSideClassMetadata() {
        return oneSideClassMetadata;
    }

    public void setOneSideClassMetadata(ClassMetadata oneSideClassMetadata) {
        this.oneSideClassMetadata = oneSideClassMetadata;
    }
}