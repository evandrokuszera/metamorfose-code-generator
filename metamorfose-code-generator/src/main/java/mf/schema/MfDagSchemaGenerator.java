/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.schema;

import dag.model.RelationshipEdge;
import dag.model.TableVertex;
import dag.nosql_schema.NoSQLSchema;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;
import mf.customization.MfClassCustomization;
import static mf.generator.MfCodeGenerator.getStringWithFirstCapitalLetter;
import mf.generator.RdbTypeEnum;
import mf.schema.model.MfRelationship;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 *
 * @author evand
 */
public class MfDagSchemaGenerator extends MfSchemaGenerator {
    private String auxSubPackageName;

    public MfDagSchemaGenerator(NoSQLSchema noSQLschema, MfClassCustomization classODMCustomization, RdbTypeEnum typeRDBDictionary) {
        super(noSQLschema, classODMCustomization, typeRDBDictionary);
    }
    
    public void generate(String packageBaseName, String optionSubPackageName){
        this.auxSubPackageName = optionSubPackageName;
        generate(packageBaseName);
    }
    
    public void generate(String packageBaseName){
        this.newMfSchema();
        
        // For each entity in the DAG...
        for (DirectedAcyclicGraph<TableVertex, RelationshipEdge> entity : this.getNoSQLschema().getEntities()){
            // Instantiate a MfEntityGenerator...
            MfDagEntityGenerator entityGenerator = new MfDagEntityGenerator(
                    entity, 
                    this.getClassODMCustomization(), 
                    this.getTypeDictionary().getRdbType()
            );
            // Configuring the package name of the entity
            String packageName = buildPackageName(packageBaseName, entity);
            // Generating the entities with their ClassMetadata objects...
            entityGenerator.generate(packageName);
            // Add to collection.
            this.getMfSchema().addEntity( entityGenerator.getMfEntity() );
        }
        
        System.out.println("MfDagSchemaGenerator.generate(): entities OK");
        
        // For each relationship of type REFERENCING between entities in the DAG schema...
        for (RelationshipEdge refEdge : this.getNoSQLschema().getRefEntities()){
            // Creating a relationship object.
            MfRelationship relationship = new MfRelationship();
            // Finding the ClassMetadata used to establish the relationship and their fields.
            relationship.setOneSideClassMetadata(this.getMfSchema().getEntityClassMetadata(refEdge.getOneSideEntityId()));
            relationship.setManySideClassMetadata(this.getMfSchema().getEntityClassMetadata(refEdge.getManySideEntityId()));
            relationship.setOneSideEntity(refEdge.getOneSideEntity());
            relationship.setManySideEntity(refEdge.getManySideEntity());
            relationship.setPkOneSide(refEdge.getPkOneSide());
            relationship.setFkManySide(refEdge.getFkManySide());
            // Add the relationship to the MfSchema
            this.getMfSchema().getRelationships().add(relationship);
            // Adding fields to one and many ClassMetadata objects, to establish the relationship
            addRefRelationshipFieldsToClass(relationship, refEdge);
        }
        
        // Applying customizations after creating the schema.
        // E.g. here, we can add annotations to establish reference relationships, like ones in RDB.
        if (this.getClassODMCustomization() != null){
            this.getClassODMCustomization().applyCustomizationsTo(this.getMfSchema());
        }
        
        System.out.println("MfDagSchemaGenerator.generate(): relationship between entities OK");
        
    }
    
    private void addRefRelationshipFieldsToClass(MfRelationship relationship, RelationshipEdge refEdge){
        ClassMetadata one = relationship.getOneSideClassMetadata();
        ClassMetadata many = relationship.getManySideClassMetadata();
        
        String nameOfOneSideAttribute = this.getNoSQLschema().getTableVertexById(refEdge.getOneSideEntityId()).getName();
        String nameOfManySideAttribute = this.getNoSQLschema().getTableVertexById(refEdge.getManySideEntityId()).getName();
                    
        // Adding field to side ONE
        ClassField oneField = new ClassField();
        oneField.setModifier("private");
        oneField.setType("java.util.List<" + getStringWithFirstCapitalLetter(many.getName()) + ">");
        oneField.setName(nameOfManySideAttribute); //        oneField.setName(many.getName().toLowerCase());
        oneField.setRelationshipType(ClassRelationshipFieldType.ARRAY_OF_OBJECTS);
        oneField.setRefLocalFieldName(relationship.getPkOneSide());
        oneField.setRefForeignFieldName(relationship.getFkManySide());
        one.getFields().add(oneField);

        // Adding field to side MANY        
        ClassField manyField = new ClassField();
        manyField.setModifier("private");
        manyField.setType( getStringWithFirstCapitalLetter(one.getName()) );
        manyField.setName(nameOfOneSideAttribute); //        manyField.setName(one.getName().toLowerCase());
        manyField.setRelationshipType(ClassRelationshipFieldType.OBJECT);
        manyField.setRefLocalFieldName(relationship.getFkManySide());
        manyField.setRefForeignFieldName(relationship.getPkOneSide());
        many.getFields().add(manyField);
        
        // Adding imports to both classes
        one.getImports().add(new ClassImport(many.getPackageName()+"."+many.getName()));
        many.getImports().add(new ClassImport(one.getPackageName()+"."+one.getName()));
    }
    
    // Build a package name based on the pattern: package + entity name + subpackage
    private String buildPackageName(String packageBaseName, DirectedAcyclicGraph<TableVertex, RelationshipEdge> entity){
        String packageName = packageBaseName + "." + MfDagEntityGenerator.getRootVertex(entity).getTableName(); // getting the entity name as the Root Vertex of the entity graph
        if (auxSubPackageName != null && auxSubPackageName.length() > 0) {
            packageName += "." + auxSubPackageName;
        }
        return packageName;
    }
    
}