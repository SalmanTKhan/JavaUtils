package com.taskdesignsinc;

import com.taskdesignsinc.tool.JavaClassGenerator;
import com.taskdesignsinc.tool.ObjectType;
import com.taskdesignsinc.tool.ParseClassGenerator;

public class TestRunner {

    public static void main(String[] args) {
        // write your code here
        new JavaClassGenerator("MyClass")
                .extendsClass("Object")
                .implementsInterface("Comparable<T>")
                .createField(false, "isBoolean", ObjectType.Boolean, Boolean.class, false)
                .createField("name", ObjectType.String)
                .createField("isValid", ObjectType.Boolean)
                .print();
        ((ParseClassGenerator) new ParseClassGenerator("MyClass", "myClass")
                .setPackageName("com.taskdesignsinc.android")
                .extendsClass("ParseObject")
                .implementsInterface("Comparable<T>")
                .implementsInterface("Uncomparable<T>")
                .implementsInterface("Comparable<T>"))
                .createParseField("name", ObjectType.String)
                .createParseField("isValid", ObjectType.Boolean)
                .printToFile("C:\\Test");
    }
}
