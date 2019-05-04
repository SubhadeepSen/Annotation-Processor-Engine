## Annotation Processor Engine

#### The Annotation Processing Engine has the capability of scanning the annotated classes, fields and methods in a given package and processing them according to the instruction provided to the engine. _@DefineController_ and _@DefineComponent_ are the class level annotations by which the engine identifies a class and creates its object in the object container. _@AutoInject_ is a field level annotation which helps the engine to process dependency injection. _@HandlerMethod_ is a method level annotation which helps the engine to identify a method inside an annotated class and execute it by injecting the required arguments.
