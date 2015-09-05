gradle-project-apt-example
==========================

gradle wrapper (2.6) 

gradle modules layout - single build.gradle file is used

Example Annotation Processor that creates some dummy classes.

~~~
/annotations -- just annotation class for processor to use
/application -- annotated classes used for generation
/processor -- annotation processor executed by compile task in application
~~~
