# Metamorfose-Code-Generator

## Description
MfCodeGenerator is a tool that generates Java code according to a NoSQL Schema, then enriched the generated code with ONM (Object-NoSQL Mapper) support. To represent the NoSQL Schema is used a Directed Acyclic Graph (DAG), in which the vertices and edges represent the structure of the entity. Each entity has a root vertex and can has nested vertices. The edges are used to define relationships between entity elements and to define relationships between entities in a NoSQL schema.

### NoSQL Schema
The figure below shows a schema for a document store. There are four collections of documents, being <i>Customers</i>, <i>Orders</i>, <i>Products</i> and <i>Categories</i>. There are reference and nesting relationships, e.g. <i>Customers</i> and <i>Orders</i> have  a one-to-many reference relationship, and <i>Orders</i> and <i>Orderlines</i> have a one-to-many nesting relationship.

<p align="center">
<img src="https://github.com/evandrokuszera/metamorfose-code-generator/blob/main/figures/nosql_schema.png" width="700" height="150" />
</p>

### MfCodeGenerator Architecture
MfCodeGenerator takes as input the NoSQL schema and an ONM config, and generates as result ONM code (annotated Java classes) to access data in NoSQL database. The figure below shows the execution flow of the tool. The developers can use the generated code in a Java project to write and read data in NoSQL database. Currently, the MfCodeGenerator has support for Spring Data, Impetus Kundera and Data Nucleus and the target NoSQL database is MongoDB. However, it can be extended to support new ONMs and databases.

<p align="center">
<img src="https://github.com/evandrokuszera/metamorfose-code-generator/blob/main/figures/architecture.png" width="700" height="200" />
</p>
    
## Dependencies
MfCodeGenerator depends on the QBMetrics project, that defines a NoSQL Schema using DAGs. The repository of QBMetrics can be access in [NoSQL Query-Based Metrics](https://github.com/evandrokuszera/nosql-query-based-metrics).

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
        // Generating the schema in memory (string parameter = target package for the classes).
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

The result is a set of annotated Java classes that follows the NoSQL Schema structure. 
After that, the developer can use the classes to acccess the NoSQL database.

## Structure of Repository
- `input-nosql-schema`: the NoSQL schema and the data (collections of documents) used to evaluate the MfCodeGeneratorClone.
- `mf-kundera`: a Java project that illustrate how to use the MfCodeGenerator for Impetus Kundera.
- `mf-datanucleus`: a Java project that illustrate how to use the MfCodeGenerator for Data Nucleus.
- `mf-springdata`: a Java project that illustrate how to use the MfCodeGenerator for Spring Data.

The Java projects use the NoSQL schema of the figure above to generate annotated Java classes to access the MongoDB database. 
Before run the code, it is necessary to create a MongoDB database and import the data from [\input-nosql-schema](https://github.com/evandrokuszera/metamorfose-code-generator/tree/main/input-nosql-schema).

## Technologies
Java 15, MongoDB, ONM, Impetus Kundera 3.13, Data Nucleus 6.0, Spring Data 3.03.
