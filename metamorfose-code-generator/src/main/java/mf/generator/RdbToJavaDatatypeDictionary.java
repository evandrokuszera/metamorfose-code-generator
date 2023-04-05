/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mf.generator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author evand
 */
public class RdbToJavaDatatypeDictionary {
    private Map<String,String> dictionary;
    private RdbTypeEnum rdbType;

    public RdbToJavaDatatypeDictionary(RdbTypeEnum rdbType) {
        this.rdbType = rdbType;
        
        switch (this.rdbType){
            case POSTGRES: {
                createPostgresDictionary();
                return;
            }
            //case ... add other types of RDB here.
        }
    }
    
    public String getJavaDataType(String rdbDataType){
        String returnJavaDataType = dictionary.get(rdbDataType);
        checkReturnValue(rdbDataType, returnJavaDataType);
        return returnJavaDataType;
    }
    
    private void createPostgresDictionary(){
        dictionary = new HashMap();
        
        dictionary.put("integer", "java.lang.Integer");
        dictionary.put("serial", "java.lang.Integer");
        dictionary.put("int2", "java.lang.Integer");
        dictionary.put("int4", "java.lang.Integer");
        dictionary.put("decimal", "java.lang.Double");
        dictionary.put("numeric", "java.lang.Double");
        dictionary.put("float4", "java.lang.Double");
        dictionary.put("float8", "java.lang.Double");
        dictionary.put("float16", "java.lang.Double");
        dictionary.put("date", "java.util.Date");
        dictionary.put("char", "java.lang.String");
        dictionary.put("varchar", "java.lang.String");
        dictionary.put("text", "java.lang.String");
    }
    
    private void checkReturnValue(String key, String value){
        if (value == null){
            System.out.printf("RdbToJavaTypeDictionary WARNING: type %s not found in dictionary.\n", key);
        }
    }

    public RdbTypeEnum getRdbType() {
        return rdbType;
    }
}