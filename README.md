# xsd2model
Java project. Xsd to java model. java2xml, xml2java, java2json, json2java.

Since JDK 102 this code should be changed because of 
https://bugs.openjdk.java.net/browse/JDK-8134111

If you don't need package-info.java' files then you can just add '<arg>-npa</arg>' it to maven-jaxb2-plugin.
Without 'package-info.java' the jaxb-marshaller would generate code without namespace in a root element.
