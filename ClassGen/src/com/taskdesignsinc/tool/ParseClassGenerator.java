package com.taskdesignsinc.tool;

import com.taskdesignsinc.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Salman T. Khan on 6/29/2017.
 * To be used with Parse Server's Java/Android SDK
 */
public class ParseClassGenerator extends JavaClassGenerator {

    private final String mParseClassName;

    public ParseClassGenerator(String className) {
        super(className);
        mParseClassName = className;
    }

    public ParseClassGenerator(String className, String parseClassName) {
        super(className);
        mParseClassName = parseClassName;
    }

    @Override
    public void print() {
        System.out.println("@ParseClassName(\"" + mClassName + "\")");
        super.print();
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
            if (classFile.exists()) {
                System.out.println("Class exists");
                return;
            }
            writer = new PrintWriter(classFile);
            if (!TextUtils.isEmpty(mPackageName))
                writer.println("package " + mPackageName + ";\n");

            writer.println("@ParseClassName(\"" + mClassName + "\")");
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

    public ParseClassGenerator createParseField(String name, ObjectType type) {
        return createParseField(true, name, type, null, true);
    }

    public ParseClassGenerator createParseField(String name, ObjectType type, Object object) {
        return createParseField(true, name, type, object, true);
    }

    /**
     * @param isPrivate    If true set's field access to private otherwise public
     * @param name         Name of the field
     * @param type         Type
     * @param object       Enumeration of different Object Types
     * @param genAccessors Generate field accessors (getter and setter)
     * @return
     */
    public ParseClassGenerator createParseField(boolean isPrivate, String name, ObjectType type, Object object, boolean genAccessors) {
        String objectAccessStrength = "";
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
        if (genAccessors) {
            switch (type) {
                default:
                    mArrayAccessors.add(createParseGetter(name, objectType));
                    mArrayAccessors.add(createParseSetter(name, objectType, null));
                    break;
            }
        }
        return this;
    }

    public String createParseGetter(String fieldName, String objectType) {
        String getString = "";
        if (Character.isUpperCase(fieldName.charAt(1)))
            getString = fieldName.substring(1);
        else
            getString = fieldName;
        String getParseString = "if (has(\"" + getString.toLowerCase() + "\"))\n\t\treturn get" + TextUtils.capitalizeFirstLetter(objectType) + "(\"" + getString.toLowerCase() + "\");";
        getString = TextUtils.capitalizeFirstLetter(getString);
        return "\tpublic " + objectType + " get" + getString + "() {\n"
                + "\t\t" + getParseString + "\n"
                + "\t\treturn " + fieldName + ";"
                + "\n\t}";
    }

    public String createParserSetter(String fieldName, String objectType) {
        return createParseSetter(fieldName, objectType, null);
    }

    public String createParseSetter(String fieldName, String objectType, String setterName) {
        String setString = "";
        String setStringTitle = "";
        String setParseString = "";
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
        setParseString = "put(\"" + setStringTitle.toLowerCase() + "\", " + fieldName + ");";
        if (!fieldName.equals(setterName)) {
            setString = fieldName + " = " + setterName + ";";
        } else {
            setString = "this." + fieldName + " = " + setterName + ";";
        }
        return "\tpublic void set" + TextUtils.capitalizeFirstLetter(setStringTitle) + "(" + objectType + " " + setterName + ") {\n"
                + "\t\t" + setParseString + "\n"
                + "\t\t" + setString
                + "\n\t}";
    }
}
