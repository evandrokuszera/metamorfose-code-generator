/*
 * NÃO ESTÁ PRONTA ESTA CLASSE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
package mf.pojogenerator.fromJPAClass;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mf.classmetadata.ClassField;
import mf.classmetadata.ClassMetadata;
import mf.classmetadata.ClassRelationshipFieldType;
import mf.classmetadata.odm.MfClassODMCustomization;
import mf.pojogenerator.MfPojoGenerator;

/**
 *
 * @author evand
 */
public class JPAClassPojoGenerator extends MfPojoGenerator {
    public static String FILE_PATH;
    private String className;

    public JPAClassPojoGenerator(String className, MfClassODMCustomization classODMCustomization) {
        super(classODMCustomization);
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public List<ClassMetadata> generateClassMetadataList(String packageBaseName) {
        
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JPAClassPojoGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        ClassMetadata classMetadata = new ClassMetadata();
        
        classMetadata.setPackageName(packageBaseName); 
        classMetadata.setName(clazz.getSimpleName());
        
//        for (java.lang.annotation.Annotation annotation : clazz.getAnnotations()){
//            classMetadata.getAnnotations().add(new Annotation(annotation.toString()));
//        }

        for (Field f : clazz.getDeclaredFields()){
            String fieldTypeDescription = f.getType().getName();
            ClassRelationshipFieldType fieldRelationshipeType = ClassRelationshipFieldType.NONE;
            
            // if the field is a subclass of Collection, then we should extract the generic type, if it exist!
            try{
                Class<?> possibleCollectionType = Class.forName(f.getType().getCanonicalName());
                if (Collection.class.isAssignableFrom(possibleCollectionType)){ // is of Collection type?
                    fieldTypeDescription = f.getType().getName();
                    ParameterizedType paramType = (ParameterizedType) f.getGenericType();
                    if (paramType.getActualTypeArguments() != null){
                        fieldTypeDescription += "<";                    
                        fieldTypeDescription += paramType.getActualTypeArguments()[0].getTypeName();
                        fieldTypeDescription += ">";
                        fieldRelationshipeType = ClassRelationshipFieldType.ARRAY_OF_OBJECTS;
                    }
                }
            } catch (Exception e){
                System.out.println(e);
            }
            
            classMetadata.getFields().add(new ClassField(Modifier.toString(f.getModifiers()), fieldTypeDescription, f.getName(), fieldRelationshipeType));
            
//            for (java.lang.annotation.Annotation annotation : f.getAnnotations()){
//                int lastFieldAdd = classMetadata.getFields().size() - 1;
//                classMetadata.getFields().get(lastFieldAdd).getAnnotations().add(new Annotation(annotation.toString()));
//            }
            
        }
        
        this.getClassMetadataList().add(classMetadata);
        
        return this.getClassMetadataList();
    }
    
}