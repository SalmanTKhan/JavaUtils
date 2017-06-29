package com.taskdesignsinc.tool;

import com.taskdesignsinc.util.TextUtils;

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
        String getParseString = "if (has(\"" + getString.toLowerCase() + "\"))\n\t\treturn get" + TextUtils.capitalizeFirstLetter(objectType) + "(" + getString.toLowerCase() + ");";
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
