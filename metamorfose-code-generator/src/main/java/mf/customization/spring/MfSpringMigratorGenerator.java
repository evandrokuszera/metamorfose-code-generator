/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.customization.spring;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mf.classmetadata.Annotation;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassImport;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;
import mf.generator.MfCodeGenerator;
import mf.migrator.MfMigratorGenerator;
import mf.mapping.MfMapping;

/**
 *
 * @author evand
 */
public class MfSpringMigratorGenerator extends MfMigratorGenerator {

    public MfSpringMigratorGenerator(List<MfMapping> mappings) {
        super(mappings);
    }

    @Override
    public void generate(String packageBaseName, String optionSubPackageName) throws FileNotFoundException {
        String packageName = MfCodeGenerator.packageNameBuilder(packageBaseName, null, optionSubPackageName);
        
        // Creating a Migrator class called MigratorRun
        ClassMetadata classMigrator = new ClassMetadata();
        classMigrator.setPackageName( packageName );
        classMigrator.setName("MigratorRun");
        
        // Spring Data Customization
        classMigrator.getAnnotations().add(new Annotation("@Component"));
        classMigrator.getImports().add( new ClassImport("org.springframework.beans.factory.annotation.Autowired") );
        classMigrator.getImports().add( new ClassImport("org.springframework.stereotype.Component") );
        
        // For each mapping between entities...
        for (MfMapping mapping : this.getMappings()){
            String pkEntityType = "";
            
            Map<String,String> propsSource = new HashMap<>();
            propsSource.put("import_repository", "org.springframework.data.jpa.repository.JpaRepository");
            propsSource.put("package_repository", classMigrator.getPackageName()+".rdb");
            propsSource.put("extends_repository", "JpaRepository");
            propsSource.put("interface_sufix_name", "JpaRepository");
            propsSource.put("entity_fullname", mapping.getMfRootSourceEntity().getRootClassMetadata().getClassQualifiedName());
            propsSource.put("entity_name", mapping.getMfRootSourceEntity().getName());
            pkEntityType = mapping.getMfRootSourceEntity().getRootClassMetadataPK().get(0).getType();
            propsSource.put("pk_entity", pkEntityType);
            
            createRepository(classMigrator, propsSource);
            
            Map<String,String> propsTarget = new HashMap<>();
            propsTarget.put("import_repository", "org.springframework.data.mongodb.repository.MongoRepository");
            propsTarget.put("package_repository", classMigrator.getPackageName()+".nosql");
            propsTarget.put("extends_repository", "MongoRepository");
            propsTarget.put("interface_sufix_name", "MongoRepository");
            propsTarget.put("entity_fullname", mapping.getMfRootTargetEntity().getRootClassMetadata().getClassQualifiedName());
            propsTarget.put("entity_name", mapping.getMfRootTargetEntity().getName());
            pkEntityType = mapping.getMfRootTargetEntity().getRootClassMetadataPK().get(0).getType();
            propsTarget.put("pk_entity", pkEntityType);
            
            createRepository(classMigrator, propsTarget);
            
            
        } // end for each: mapping object
        
        // Creating a Spring Data Migration class, with a generic code to call the migration.
        MfCodeGenerator.saveCodeToFile(packageName, "SpringMigrator", getSpringDataMigrationCode(packageName));
        
        // Creating run() method that (i) instantiate one SpringMigrator for each migration and (ii) call run´s methods to start the migrations
        String code = "\tpublic void run() { \n";
        int i = 0, index = 0;
        while (i<classMigrator.getFields().size()){
            code += String.format(
                        "\t\tSpringMigrator mig%d = new SpringMigrator(%s, %s, new %s());",
                        index,
                        classMigrator.getFields().get(i).getName(),
                        classMigrator.getFields().get(i+1).getName(),
                        this.getMappings().get(index).getClassName()
                    );
            code += "\n";
            code += String.format("\t\tmig%d.run();", index);
            code += "\n";
            
            String mapperPackageAndClassName = this.getMappings().get(index).getPackageName() +"."+ this.getMappings().get(index).getClassName();
            classMigrator.getImports().add( new ClassImport(mapperPackageAndClassName) ); 
                    
            i = i + 2;
            index++;
        }
        code += "\t}";
        classMigrator.getManualInputCodes().add(code);
        
        // Saving the Migrator class code as .java file
        MfCodeGenerator.GENERATE_GETTERS_AND_SETTERS = false;
        MfCodeGenerator.saveCodeToFile(packageName, classMigrator.getName(), MfCodeGenerator.createPojoCodeFrom(classMigrator));
        MfCodeGenerator.GENERATE_GETTERS_AND_SETTERS = true;
    } // end of generate method.
    
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // Creates a Spring Data Repositories and add (inject) into the Migrator class all the repositories as properties
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void createRepository(ClassMetadata classMetadataMigrator, Map<String,String> propsRepository) throws FileNotFoundException {
               
        String interfacePackageName = propsRepository.get("package_repository");
        String interfaceRepositoryName = propsRepository.get("entity_name") + propsRepository.get("interface_sufix_name");
        String interfaceRepositoryCode = getRepositoryInterfaceCode(interfacePackageName, propsRepository);
        
        MfCodeGenerator.saveCodeToFile(interfacePackageName, interfaceRepositoryName, interfaceRepositoryCode);
        
        // Creating a class field of the source repository interface for the Migrator class
        ClassField fieldRepoSource = new ClassField(
                "private", 
                interfacePackageName +"."+interfaceRepositoryName, 
                interfaceRepositoryName,
                ClassRelationshipFieldType.NONE, false, false
        );
        fieldRepoSource.getAnnotations().add(new Annotation("@Autowired"));
        // Adding the new field to the Migrator class
        classMetadataMigrator.getFields().add(fieldRepoSource);
        
    }
    
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // Returns code from the template for the Repository Interface of Spring Data, with props data
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private String getRepositoryInterfaceCode(String packageName, Map<String,String> props){
        StringBuilder strBuilder = new StringBuilder();
        // PACKAGE
        strBuilder.append("package ").append( packageName ).append(";").append("\n\n");
        // IMPORTS
        strBuilder.append("import ").append(props.get("import_repository")).append(";").append("\n");
        strBuilder.append("import ").append(props.get("entity_fullname")).append(";").append("\n");
        strBuilder.append("\n");
        // CLASS
        strBuilder.append("public interface ").append(props.get("entity_name")).append(props.get("interface_sufix_name"))
                .append(" extends ")
                .append(props.get("extends_repository"))
                .append("<")
                .append(props.get("entity_name"))
                .append(",")
                .append(props.get("pk_entity")) 
                .append(">")
                .append(" {}")
                .append("\n");

        return strBuilder.toString();
    } // End of: getInterfaceRepositoryCode 
    
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // Returns a generic code that call the migrations using Spring Data pagination
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private String getSpringDataMigrationCode(String packageName){
        
        String code = "package " + packageName + ";\n" +
                        "import mf.mapping.MfMapper;\n" +
                        "import org.springframework.data.domain.Page;\n" +
                        "import org.springframework.data.domain.PageRequest;\n" +
                        "import org.springframework.data.domain.Pageable;\n" +
                        "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                        "import org.springframework.data.mongodb.repository.MongoRepository;\n" +
                        "\n" +
                        "public class SpringMigrator<S extends JpaRepository, T extends MongoRepository, M extends MfMapper> {\n" +
                        "    private final S sourceRepo;\n" +
                        "    private final T targetRepo;\n" +
                        "    private final M mapper;\n" +
                        "    private int pageSize = 100;\n" +
                        "    private int pageLimit = 0;\n" +
                        "\n" +
                        "    public SpringMigrator(S sourceRepo, T targetRepo, M mapper) {\n" +
                        "        this.sourceRepo = sourceRepo;\n" +
                        "        this.targetRepo = targetRepo;\n" +
                        "        this.mapper = mapper;\n" +
                        "    }\n" +
                        "    \n" +
                        "    public SpringMigrator(S sourceRepo, T targetRepo, M mapper, int pageSize, int pageLimit) {\n" +
                        "        this(sourceRepo, targetRepo, mapper);\n" +
                        "        this.pageSize = pageSize;\n" +
                        "        this.pageLimit = pageLimit;\n" +
                        "    }\n" +
                        "\n" +
                        "    public void run() {\n" +
                        "        int pageNumber = 0;\n" +
                        "        int limit = pageLimit;\n" +
                        "        Page page = null;\n" +
                        "        Pageable pageConf = PageRequest.of(0, pageSize);\n" +
                        "        boolean isMigrateAllRecords = false;\n" +
                        "        \n" +
                        "        if (pageLimit == 0){\n" +
                        "            isMigrateAllRecords = true;\n" +
                        "            limit = 1;\n" +
                        "        }\n" +
                        "        \n" +
                        "        System.out.println(\"SpringMigrator.run(): \" + mapper.getSourceClass().getName() + \" -> \" + mapper.getTargetClass().getName());\n" +
                        "        while (pageNumber < limit) {\n" +
                        "            page = sourceRepo.findAll(pageConf);\n" +
                        "            if (page.hasContent()) {\n" +
                        "                targetRepo.saveAll(\n" +
                        "                        mapper.convert(page.getContent())\n" +
                        "                );\n" +
                        "                \n" +
                        "                System.out.printf(\"Page: %d, PageSize: %d, Objects migrated: %d\\n\", \n" +
                        "                    pageNumber, \n" +
                        "                    pageSize, \n" +
                        "                    (pageNumber + 1) * pageSize\n" +
                        "                );\n" +
                        "                \n" +
                        "                pageNumber++;\n" +
                        "                if (isMigrateAllRecords) limit = pageNumber + 1;\n" +
                        "            }\n" +
                        "            \n" +
                        "            if (!page.hasNext()) {\n" +
                        "                break;\n" +
                        "            }\n" +
                        "            \n" +
                        "            pageConf = pageConf.next();           \n" +
                        "        }\n" +
                        "    }\n" +
                        "}";
        return code;
    }
}