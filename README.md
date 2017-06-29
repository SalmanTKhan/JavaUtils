# JavaUtils

My first project to help improve development for future projects.

## Getting Started

Not much yet copy classes or download the project as a module for IntelliJ.

### Sample

Need to improve documentation but concept is simple provide a class name, even though IntelliJ does a great job of creating the basic class this to allow quicker class creation with fields and accessors.

```
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
```

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUITING.md) for details on our code of conduct, and the process for submitting pull requests to us.


## Authors

* **Salman T. Khan** - *Initial work* - [SalmanTKhan](https://github.com/SalmanTKhan)

See also the list of [contributors](https://github.com/SalmanTKhan/JavaUtils/contributors) who participated in this project.

## License

This project is licensed under the Apache 2.0 - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* My Parents and My Family for allowing me to just pursue what I want to do in life.
