# Metamorfose-Code-Generator

## Description
MfCodeGenerator is a tool that generates Java code according to a NoSQL Schema, then enriched the generated code with ONM (Object-NoSQL Mapper) support. To represent the NoSQL Schema we use a Direct Acyclic Graph, in which the vertices and edges represent the structure of the entity. Each entity has a root vertex and can has nested vertices. The edges are used to define relationships between entity elements and to define relationships between entities in a NoSQL schema.

The figure below shows a schema for a document store. There are four collections of documents, being Customers, Orders, Products and Categories. There are reference and nesting relationships, e.g. Customers and Orders have  a one-to-many reference relationship, and Orders and Orderlines have a one-to-many nesting relationship.

FIGURE

MfCodeGenerator takes as input the NoSQL schema and an ONM config, and generates as result ONM code (annotated Java classes) to access data in NoSQL database. The figure below shows the execution flow of the tool. The developers can use the classes in a Java project to write and read data in NoSQL database. Currently, the MfCodeGenerator has support for Spring Data, Impetus Kundera and Data Nucleus and the target NoSQL database is MongoDB. However, it can be extended to support new ONMs and databases.

FIGURE

## Dependencies
MfCodeGenerator depend on the QBMetrics project, that defines a NoSQL Schema using Direct Acyclic Graphs. The repository of QBMetrics can be access in [NoSQL Query-Based Metrics](https://github.com/evandrokuszera/nosql-query-based-metrics).

```
git clone https://github.com/evandrokuszera/nosql-query-based-metrics.git
```

## How to use the MfCodeGenerator
The steps required to use MfCodeGenerator are:
1 - Clone the MfCodeGenerator repository and its dependencies.
2 - Create a new Java project and add the dependecies for MfCodeGenerator, MongoDB and the target ONM (Impetus Kundera, Data Nucleus or Spring Data).
3 - Create a new class with code below, then runs it.

```Java
public class Generator {
    // Path of NoSQL schema file
    String path = "..\\..\\input-nosql-schema\\dvd-store.json";
    
    public NoSQLSchema getNoSQLSchema(){
        return GraphUtils.loadNosqlSchema(path);
    }
    
    public void generate() throws FileNotFoundException{
        // Creating schema generator object
        MfDagSchemaGenerator schemaCodeGenerator = new MfDagSchemaGenerator(
                getNoSQLSchema(), 
                new MfDataNucleusMongoCustomization(), // or MfKunderaMongoCustomization() or MfSpringMongoCustomization()
                RdbTypeEnum.POSTGRES
        );
        // Generating the schema with its respective entities and related Java code.
        // The parameter is the target package where the code will be save.
        schemaCodeGenerator.generate("mf.dn.model.nosql", null);
        
        // Uncomment the line below to inspect the generated schema.
        // schemaCodeGenerator.getMfSchema();
        
        // Uncomment the line below to print the schema
        // schemaCodeGenerator.printSchema();
        
        // Saving the code into the disk.
        schemaCodeGenerator.saveFiles();
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        Generator generator = new Generator();
        generator.generate();
    }
}
```

The result is a set of annotated Java classes that follows the NoSQL Schema structure. After that, the developer is able of accces the NoSQL database.

## Subprojects
The subprojects mf-datanucleus, mf-kundera and mf-springdata illustrate how to use the MfCodeGenerator for ONMs. We use the NoSQL schema of the figure above and generate code to access a MongoDB database (the data of collections are in input-nosql-schema folder).

## Technologies
Java, ONM, Impetus Kundera, Data Nucleus, Spring Data, MongoDB.
