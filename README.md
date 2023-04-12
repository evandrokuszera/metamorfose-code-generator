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
- `Step 1`: Clone the MfCodeGenerator repository and its dependencies.
- `Step 2`: Create a new Java project and add the dependecies for MfCodeGenerator, MongoDB and the target ONM (Impetus Kundera, Data Nucleus or Spring Data).
- `Step 3`: Create a new class with code below, then runs it.

```Java
public class Generator {
    public static void main(String[] args) throws FileNotFoundException {
        // Path of NoSQL schema file
        String path = "..\\..\\input-nosql-schema\\dvd-store.json";
        // Loading the NoSQL Schema from disk
        NoSQLSchema schema = GraphUtils.loadNosqlSchema(path);
        // Configuring the schema generator for generating Java classe from the schema using Kundera ODM
        MfSchemaGenerator schemaCodeGenerator = new MfDagSchemaGenerator(
                schema, 
                new MfKunderaMongoCustomization(), // or MfDataNucleusMongoCustomization() or MfSpringMongoCustomization()
                RdbTypeEnum.POSTGRES);
        // Generating the schema in memory (parameter = target package for the classes).
        schemaCodeGenerator.generate("mf.ku.model.nosql");
        // Uncomment the line bellow to inspect the generated schema
        // schemaCodeGenerator.getMfSchema();
        // Uncomment the line below to print the schema
        // schemaCodeGenerator.printSchema();
        // Saving the code into the disk.
        schemaCodeGenerator.saveFiles();
    }
}
```

The result is a set of annotated Java classes that follows the NoSQL Schema structure. After that, the developer is able of accces the NoSQL database.

## Subprojects
The subprojects mf-datanucleus, mf-kundera and mf-springdata illustrate how to use the MfCodeGenerator for ONMs. They use the NoSQL schema of the figure above and generate code to access a MongoDB database. To test the generated code we used the data in the folder [\input-nosql-schema folder](https://github.com/evandrokuszera/metamorfose-code-generator/tree/main/input-nosql-schema) to create a MongoDB database.

## Technologies
Java, ONM, Impetus Kundera, Data Nucleus, Spring Data, MongoDB.
