/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.classmetadata;

/**
 *
 * @author evand
 */
public class ClassImport {
    private String importEntry;

    public ClassImport() {
    }
    
    public ClassImport(String importEntry) {
        this.importEntry = importEntry;
    }

    public String getImportEntry() {
        return importEntry;
    }

    public void setImportEntry(String importEntry) {
        this.importEntry = importEntry;
    }
}