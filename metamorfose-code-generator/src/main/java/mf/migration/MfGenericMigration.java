/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.migration;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;

/**
 *
 * @author evand
 */
public class MfGenericMigration {
    private ModelMapper modelMapper = new ModelMapper();
    private Class sourceClass;
    private Class targetClass;
    private List sourceInstances;
    
    public MfGenericMigration(Class sourceClass, Class targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }
    
    public void setSourceInstances(List sourceInstances){
        this.sourceInstances = sourceInstances;
    }
    
    public List getTargetInstances(){
    	List targetItems = new ArrayList<>();
    	for (Object item : this.getSourceInstances()) {
            targetItems.add( this.getModelMapper().map(item, this.getTargetClass()) );
        }
    	return targetItems;
    }
    
    public void saveAllTargetInstances(){
        throw new UnsupportedOperationException("You must extend this class and implements saveAllTargetInstances method with code to save all the target instances using the selected ODM.");
    }
    
    public List getSourceInstances() {
		return sourceInstances;
	}
    
    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public Class getSourceClass() {
        return sourceClass;
    }

    public Class getTargetClass() {
        return targetClass;
    }
    
}
