/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.migrator;

import java.io.FileNotFoundException;
import java.util.List;
import mf.mapping.MfMapping;

/**
 *
 * @author evand
 */
public abstract class MfMigratorGenerator {
    private List<MfMapping> mappings;

    public MfMigratorGenerator(List<MfMapping> mappings) {
        this.mappings = mappings;
    }

    public List<MfMapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<MfMapping> mappings) {
        this.mappings = mappings;
    }
    
    public abstract void generate(String packageBaseName, String optionSubPackageName) throws FileNotFoundException;
}