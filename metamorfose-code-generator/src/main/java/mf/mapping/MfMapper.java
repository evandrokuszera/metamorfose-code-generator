/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.mapping;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.Mapping;

/**
 *
 * @author evand
 */
public class MfMapper {
    private ModelMapper modelMapper = new ModelMapper();
    private Class sourceClass;
    private Class targetClass;
    
    public MfMapper(Class sourceClass, Class targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.modelMapper.typeMap(sourceClass, targetClass);
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
    
    public void mapNestedEntity(Class sourceClass, Class targetClass){
        this.modelMapper.typeMap(sourceClass, targetClass);
    }
    
    public List convert(List sourceInstances){
    	List targetItems = new ArrayList<>();
    	for (Object item : sourceInstances) {
            targetItems.add( this.getModelMapper().map(item, this.getTargetClass()) );
        }
    	return targetItems;
    }
        
    public void convert(Object sourceInstance, Object targetInstance){
        targetInstance = this.getModelMapper().map(sourceInstance, this.getTargetClass());
        System.out.println(targetInstance);
    }
    
    public void printPropertyMappings(){
        for (TypeMap typeMap : this.getModelMapper().getTypeMaps()){
            System.out.println(typeMap);
            
            for (Object mapping : typeMap.getMappings()){
                Mapping m = (Mapping) mapping;
                
                System.out.print(m);
                System.out.print(" [");
                System.out.print(m.getSourceType().getTypeName());
                System.out.print(" -> ");
                System.out.print(m.getDestinationProperties().get(0).getType().getName());
                System.out.println("]");
            }
            
            System.out.printf("UNMAPPED_PROPERTIES (size: %s)...\n", typeMap.getUnmappedProperties().size());
            
            for (Object info : typeMap.getUnmappedProperties()){
                System.out.println(info);
            }
            
            System.out.println("");
        }
    }
    
    public void createMappingClass(String className, String packageName){
        // programar um método para criar uma classe com todos os mapeamentos 
        // entre as propriedades das entidades de origem e destino
        throw new UnsupportedOperationException("Not yet!");
    }
    
    public void setMappings(PropertyMap propertyMap){
        throw new UnsupportedOperationException("Not yet!");
    }
}
