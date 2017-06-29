package com.taskdesignsinc.tool;

import com.taskdesignsinc.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Salman T. Khan on 6/27/2017.
 */
public class JavaClassGenerator {

    protected final String mClassName;
    protected String mPackageName;
    protected String mParentClass;
    protected HashMap<String, String> mInterfaceMap = new HashMap<>();
    protected ArrayList<String> mImplementedInterfaces = new ArrayList<>();
    protected ArrayList<String> mArrayFields = new ArrayList<>();
    protected ArrayList<String> mArrayAccessors = new ArrayList<>();

    public JavaClassGenerator(String className) {
        mClassName = className;
    }

    public JavaClassGenerator setPackageName(String packageName) {
        mPackageName = packageName;
        return this;
    }

    public JavaClassGenerator extendsClass(String parentClassName) {
        mParentClass = parentClassName;
        return this;
    }

    public JavaClassGenerator implementsInterface(String interfaceName) {
        if (!mInterfaceMap.containsKey(interfaceName)) {
            mImplementedInterfaces.add(interfaceName);
            mInterfaceMap.put(interfaceName, interfaceName);
        } else {
            System.out.println("Interface " + interfaceName + "already added ");
        }
        return this;
    }

    public void print() {
        if (!TextUtils.isEmpty(mPackageName))
            System.out.println("package " + mPackageName + ";\n");

        System.out.print("public class " + mClassName);
        if (!TextUtils.isEmpty(mParentClass))
            System.out.print(" extends " + mParentClass);
        if (!mImplementedInterfaces.isEmpty()) {
            System.out.print(" implements ");
            StringBuilder stringBuilder = new StringBuilder();
            for (String interfaceName : mImplementedInterfaces) {
                stringBuilder.append(interfaceName + ", ");
            }
            String interfaces = stringBuilder.toString();
            interfaces = interfaces.substring(0, interfaces.lastIndexOf(","));
            System.out.print(interfaces);
        }
        System.out.println(" {\n");
        for (String s : mArrayFields) {
            System.out.println("\t" + s);
        }
        System.out.println();
        for (String s : mArrayAccessors) {
            System.out.println(s);
        }
        System.out.println("}");
    }

    public void printToFile(String parentPath) {
        File classFile;
        PrintWriter writer;
        try {
            if (TextUtils.isEmpty(mPackageName))
                classFile = new File(parentPath, mClassName + ".java");
            else {
                String[] packageNames = mPackageName.split("\\.");
                StringBuilder builder = new StringBuilder();
                if (packageNames != null) {
                    for (String packageName : packageNames) {
                        //com.taskdesignsinc.android
                        builder.append(packageName + "/");
                    }
                }
                if (!parentPath.endsWith("/"))
                    parentPath += "/";
                classFile = new File(parentPath + builder.toString(), mClassName + ".java");
            }
            classFile.getParentFile().mkdirs();
            writer = new PrintWriter(classFile);
            if (!TextUtils.isEmpty(mPackageName))
                writer.println("package " + mPackageName + ";\n");

            writer.print("public class " + mClassName);
            if (!TextUtils.isEmpty(mParentClass))
                writer.print(" extends " + mParentClass);
            if (!mImplementedInterfaces.isEmpty()) {
                writer.print(" implements ");
                StringBuilder stringBuilder = new StringBuilder();
                for (String interfaceName : mImplementedInterfaces) {
                    stringBuilder.append(interfaceName + ", ");
                }
                String interfaces = stringBuilder.toString();
                interfaces = interfaces.substring(0, interfaces.lastIndexOf(","));
                writer.print(interfaces);
            }
            writer.println(" {\n");
            for (String s : mArrayFields) {
                writer.println("\t" + s);
            }
            writer.println();
            for (String s : mArrayAccessors) {
                writer.println(s);
            }
            writer.println("}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JavaClassGenerator createField(String name, ObjectType type) {
        createField(true, name, type, null, true);
        return this;
    }

    public JavaClassGenerator createField(String name, ObjectType type, Object object) {
        createField(true, name, type, object, true);
        return this;
    }

    /**
     * @param isPrivate    If true set's field access to private otherwise public
     * @param name         Name of the field
     * @param type         Type
     * @param object       Enumeration of different Object Types
     * @param genAccessors Generate field accessors (getter and setter)
     * @return
     */
    public JavaClassGenerator createField(boolean isPrivate, String name, ObjectType type, Object object, boolean genAccessors) {
        String objectAccessStrength;
        String objectType = "";
        if (isPrivate)
            objectAccessStrength = "private";
        else
            objectAccessStrength = "public";
        switch (type) {
            case Integer:
                objectType = "int";
                break;
            case Long:
                objectType = "long";
                break;
            case Float:
                objectType = "float";
                break;
            case Double:
                objectType = "double";
                break;
            case String:
                objectType = "String";
                break;
            case Boolean:
                objectType = "boolean";
                break;
            case Object:
                if (object != null)
                    objectType = object.getClass().getSimpleName();
                break;
        }
        mArrayFields.add(objectAccessStrength + " " + objectType + " " + name + ";");
        if (isPrivate && genAccessors) {
            switch (type) {
                default:
                    mArrayAccessors.add(createGetter(name, objectType));
                    mArrayAccessors.add(createSetter(name, objectType));
                    break;
            }
        }
        return this;
    }

    public String createGetter(String fieldName, String objectType) {
        String getString;
        if (Character.isUpperCase(fieldName.charAt(1)))
            getString = fieldName.substring(1);
        else
            getString = fieldName;
        getString = TextUtils.capitalizeFirstLetter(getString);
        return "\tpublic " + objectType + " get" + getString + "() {\n"
                + "\t\treturn " + fieldName + ";"
                + "\n\t}";
    }

    public String createSetter(String fieldName, String objectType) {
        return createSetter(fieldName, objectType, null);
    }

    public String createSetter(String fieldName, String objectType, String setterName) {
        String setString;
        String setStringTitle;
        if (Character.isUpperCase(fieldName.charAt(1))) {
            setStringTitle = fieldName.substring(1);
            if (TextUtils.isEmpty(setterName))
                setterName = fieldName.substring(1).toLowerCase();
        } else {
            setStringTitle = fieldName;
        }
        if (TextUtils.isEmpty(setterName)) {
            if (Character.isUpperCase(fieldName.charAt(1)))
                setterName = fieldName.substring(1).toLowerCase();
            else
                setterName = fieldName;
        }
        if (!fieldName.equals(setterName)) {
            setString = fieldName + " = " + setterName + ";";
        } else {
            setString = "this." + fieldName + " = " + setterName + ";";
        }
        return "\tpublic void set" + TextUtils.capitalizeFirstLetter(setStringTitle) + "(" + objectType + " " + setterName + ") {\n"
                + "\t\t" + setString
                + "\n\t}";
    }
}
