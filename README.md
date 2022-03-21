# JasperReports PDF Exporter Lib Seven

This project contains a PDF producer implementation for the JasperReports Library based on iText version 7.

The JasperReports Library uses a built-in PDF producer based on iText version 2.1.7.
Starting with iText version 5, the iText library has changed its license from LGPL/MPL to AGPL, 
and also changed its package names, therefore the JasperReports Library built-in PDF producer does not 
work with iText versions newer than 2.1.7.

This separate project was created to allow the JasperReports PDF exporter to use iText 7 as PDF producer.
The project is released under AGPL so that it is compatible with the iText 7 license and
thus continue to be available for use in open source products.

## How to use

The iText 7 PDF producer can be plugged into the standard JasperReports PDF exporter by adding the required
jars and setting a configuration property in jasperreports.properties.

The configuration property that needs to be set is:

~~~
net.sf.jasperreports.export.pdf.producer.factory=com.jaspersoft.jasperreports.export.pdf.modern.ModernPdfProducerFactory
~~~

## Installation

The installation of the plugin consists of 3 basic steps:
1. Deploying the plugin artifact in the local .m2 repository.
2. Adding the configuration property to the jaspersoft studio/server properties.
3. Adding the required classpath entries to the classpath in the jaspersoft project.

The first step is the same for Studio and Server:

### Deploying the plugin artifact to the local .m2 repository
This can be done by either running
```sh
mvn clean install
```
from the project root.

Or opening this project in any IDE with maven support (e.g. IntelliJ) and executing the install phase.

### JasperSoft Studio
#### Add Jaspersoft Property
Jaspersoft Studio Properties can either be set for the entire ***workspace*** or a specific ***project***:

Go to `Project -> Properties -> JasperSoft Studio -> Properties` and choose either 

`Configure Workspace Settings` -> `Configure Workspace Settings` 

or `Use project Settings`

In the property list search for `pdf.producer.factory`.
The Property `net.sf.jasperreports.export.pdf.producer.factory` should appear.

If the Property is ***not*** present your Jaspersoft installation is likely not supported (< 8.0.0).

Change the default value for `net.sf.jasperreports.export.pdf.producer.factory` to
~~~
com.jaspersoft.jasperreports.export.pdf.modern.ModernPdfProducerFactory
~~~

#### Add classpath entries

The classpath entries in Eclipse / Jaspersoft Studio are ***project specific*** so they need to be set for any project, that wants to use the new pdf exporter.
Right click on a project and select `Properties` or in an open project select `Project -> Properties` from the menu bar.

Go to `Java Build Path` and select `Libraries`.

Add all the required jar files here.
(see Required Jar Files)

This can be semi-automated via the maven eclipse goal:
1. From this project root run `mvn eclipse:eclipse`
2. Open the generated .classpath file, which should look similar to

```xml
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
  <classpathentry kind="src" path="src/test/java" output="target/test-classes" including="**/*.java"/>
  <classpathentry kind="src" path="src/test/resources" output="target/test-classes" excluding="**/*.java"/>
  <classpathentry kind="src" path="src/main/java" including="**/*.java"/>
  <classpathentry kind="output" path="target/classes"/>
  <classpathentry kind="var" path="M2_REPO/javax/inject/javax.inject/1/javax.inject-1.jar"/>
  <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.8"/>
  <classpathentry kind="var" path="M2_REPO/net/sf/jasperreports/jasperreports/6.17.0/jasperreports-6.17.0.jar"/>
  <classpathentry kind="var" path="M2_REPO/commons-beanutils/commons-beanutils/1.9.4/commons-beanutils-1.9.4.jar"/>
  <classpathentry kind="var" path="M2_REPO/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar"/>
  <classpathentry kind="var" path="M2_REPO/commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar"/>
  <classpathentry kind="var" path="M2_REPO/commons-digester/commons-digester/2.1/commons-digester-2.1.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/commons/commons-collections4/4.2/commons-collections4-4.2.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/jfree/jcommon/1.0.23/jcommon-1.0.23.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/jfree/jfreechart/1.0.19/jfreechart-1.0.19.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/eclipse/jdt/ecj/3.21.0/ecj-3.21.0.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/codehaus/castor/castor-xml/1.4.1/castor-xml-1.4.1.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/codehaus/castor/castor-core/1.4.1/castor-core-1.4.1.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/fasterxml/jackson/core/jackson-core/2.12.2/jackson-core-2.12.2.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/fasterxml/jackson/core/jackson-databind/2.12.2/jackson-databind-2.12.2.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/fasterxml/jackson/core/jackson-annotations/2.12.2/jackson-annotations-2.12.2.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/itextpdf/kernel/7.1.14/kernel-7.1.14.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/itextpdf/io/7.1.14/io-7.1.14.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/bouncycastle/bcpkix-jdk15on/1.67/bcpkix-jdk15on-1.67.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/bouncycastle/bcprov-jdk15on/1.67/bcprov-jdk15on-1.67.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/itextpdf/layout/7.1.14/layout-7.1.14.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/itextpdf/pdfa/7.1.14/pdfa-7.1.14.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/itextpdf/forms/7.1.14/forms-7.1.14.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/itextpdf/svg/7.1.14/svg-7.1.14.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/itextpdf/styled-xml-parser/7.1.14/styled-xml-parser-7.1.14.jar"/>
  <classpathentry kind="var" path="M2_REPO/commons-codec/commons-codec/1.5/commons-codec-1.5.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/testng/testng/6.8.1/testng-6.8.1.jar"/>
  <classpathentry kind="var" path="M2_REPO/junit/junit/4.10/junit-4.10.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/hamcrest/hamcrest-core/1.1/hamcrest-core-1.1.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/beanshell/bsh/2.0b4/bsh-2.0b4.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/beust/jcommander/1.27/jcommander-1.27.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/yaml/snakeyaml/1.6/snakeyaml-1.6.jar"/>
  <classpathentry kind="var" path="M2_REPO/com/lowagie/itext/2.1.7.js8/itext-2.1.7.js8.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-svggen/1.11/batik-svggen-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-awt-util/1.11/batik-awt-util-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-util/1.11/batik-util-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-constants/1.11/batik-constants-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-i18n/1.11/batik-i18n-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/xmlgraphics-commons/2.3/xmlgraphics-commons-2.3.jar"/>
  <classpathentry kind="var" path="M2_REPO/commons-io/commons-io/1.3.1/commons-io-1.3.1.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-dom/1.11/batik-dom-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-css/1.11/batik-css-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/xml-apis/xml-apis-ext/1.3.04/xml-apis-ext-1.3.04.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-ext/1.11/batik-ext-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-xml/1.11/batik-xml-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/xalan/xalan/2.7.2/xalan-2.7.2.jar"/>
  <classpathentry kind="var" path="M2_REPO/xalan/serializer/2.7.2/serializer-2.7.2.jar"/>
  <classpathentry kind="var" path="M2_REPO/xml-apis/xml-apis/1.3.04/xml-apis-1.3.04.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-anim/1.11/batik-anim-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-parser/1.11/batik-parser-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/org/apache/xmlgraphics/batik-svg-dom/1.11/batik-svg-dom-1.11.jar"/>
  <classpathentry kind="var" path="M2_REPO/net/sf/jasperreports/jasperreports-fonts/master-SNAPSHOT/jasperreports-fonts-master-SNAPSHOT.jar"/>
</classpath>
```

3. Copy all the entries from `<classpathentry kind="var" path="M2_REPO/net/sf/jasperreports/jasperreports/6.17.0/jasperreports-6.17.0.jar"/>` downwards.
4. Open your Jaspersoft Workspace and navigate to the project you intend to use the plugin with.
5. Open the .classpath in that project
6. Paste the copied `<classpathentry>` tags beneath the existing ones.
7. If you refresh the Buildpath in the Studio they should now appear.
8. **IMPORTANT** now go to `Window -> Preferences -> Java -> Build Path -> Classpath Variables`
   and create a new classpath variable called M2_REPO and point it towards your .m2 user repository (the one where this project was deployed using mvn install previously).
   It's usually under %UserProfile%/.m2/repository  
   This will ensure that the placeholders in the added classpath entries gets resolved correctly to the maven repository.
10. Now all the dependencies for the plugin are in the classpath, but the plugin jar needs to be added manually:  
 - Go back to `Project -> Properties -> Java Build Path -> Libraries` and select classpath.
 - Click `Add External Jars...` and navigate to the place where the artifact was deployed in step one.
   To find that location check the output from mvn clean install right above the BUILD SUCCESS.

```sh
[INFO] --- maven-install-plugin:2.4:install (default-install) @ jasperreports-pdf-lib7 ---
[INFO] Installing /home/ferdinand/Downloads/jasperreports-pdf-lib7/target/jasperreports-pdf-lib7-1.0.0-SNAPSHOT.jar to /home/ferdinand/.m2/repository/com/jaspersoft/jasperreports/jasperreports-pdf-lib7/1.0.0-SNAPSHOT/jasperreports-pdf-lib7-1.0.0-SNAPSHOT.jar
[INFO] Installing /home/ferdinand/Downloads/jasperreports-pdf-lib7/pom.xml to /home/ferdinand/.m2/repository/com/jaspersoft/jasperreports/jasperreports-pdf-lib7/1.0.0-SNAPSHOT/jasperreports-pdf-lib7-1.0.0-SNAPSHOT.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.929 s
[INFO] Finished at: 2022-01-24T17:51:47+01:00
[INFO] ------------------------------------------------------------------------
```

   The line with Installing (...) to (...)/jasperreports-pdf-lib7-1.0.0-SNAPSHOT.jar is the important one.

Now everything is setup and the plugin can be used.

### Jaspersoft Reports Server

1. Setting the property

    In the file
    ```
    <JasperServerRoot>/apache-tomcat/webapps/jasperserver-pro/WEB-INF/classes/jasperreports.properties
    ```
    add the line
    ```jproperties
    net.sf.jasperreports.export.pdf.producer.factory=com.jaspersoft.jasperreports.export.pdf.modern.ModernPdfProducerFactory
    ```

2. Classpath

The jar files either need to be added to the classpath or copied to the `lib` folder in apache-tomcat:

#### Copy jar files to library folder
```sh
mvn install dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=<JasperServerRoot>/apache-tomcat/lib
cp target/jasperreports-pdf-lib7-1.0.0-SNAPSHOT.jar <JasperServerRoot>/apache-tomcat/lib
```
(<JasperServerRoot> must be replaced with the directory of the Jasperserver installation)

#### Via Classpath Entry:
1. Generate the classpath:

    Execute
    ```sh
    mvn dependency:build-classpath -Dmdep.outputFile=cp.txt -Dinclude-scope=runtime
    ```
    to write the classpath to cp.txt or
    ```sh
    mvn dependency:build-classpath -Dinclude-scope=runtime
    ```
    to print the classpath to the terminal.

    Additionally the artifact jar file needs to be added to the classpath - it's location is logged during the install phase:
    ```sh
    ...    
    [INFO] Installing /home/ferdinand/Downloads/jasperreports-pdf-lib7/target/jasperreports-pdf-lib7-1.0.0-SNAPSHOT.jar to /home/ferdinand/.m2/repository/com/jaspersoft/jasperreports/jasperreports-pdf-lib7/1.0.0-SNAPSHOT/jasperreports-pdf-lib7-1.0.0-SNAPSHOT.jar
    ...
    ```
    
2. Export the classpath:
  - **Linux**: In `$JASPERSERVER_DIR/apache-tomcat/bin/setenv.sh`
    ```sh
    ...
    export CLASSPATH = <insert classpath here>
    ...
    ```
  - **Windows**: In `$JASPERSERVER_DIR/apache-tomcat/bin/setenv.bat`
    ```bat
    ...
    set CLASSPATH = <insert classpath here>
    ...
    ```
  
### Required Jar Files

The following jars need to present on the classpath:

* jasperreports-pdf-lib7-{x.y.z}.jar (published by this project)
* iText 7 jars:
    * kernel-7.{x.y}.jar
    * io-7.{x.y}.jar
    * layout-7.{x.y}.jar
    * pdfa-7.{x.y}.jar
    * forms-7.{x.y}.jar
    * svg-7.{x.y}.jar
    * styled-xml-parser-7.{x.y}.jar
* Third party jars:
    * slf4j-api-1.{x.y}.jar
    * Apache Batik jars
    * serializer-2.7.2.jar (part of the Apache Xalan project)

Note that iText 2.1.7 still needs to be present on the classpath due to some dependencies
from JasperReports Library code.

## Drawbacks

iText 7 contains several parts that have been rewritten, removed or significantly modified since iText 2 and 5.
Due to these changes the JasperReports Library cannot create PDF output identical to the one produced with iText 2.

There are several areas where differences to the built-in PDF producer can be observed:

* Since PdfGraphics2D has been removed from iText 7, SVG images and Graphics2D based images are no longer rendered via Apache Batik.
    * SVG images are directly rendered via iText's SvgConverter.  Some SVGs are however not rendered properly.
    * Graphics2D images (such as JFreeChart elements) are converted to SVGs via Batik and then rendered to PDF via SvgConverter.  This can have an impact on performance.
* Text line spacing/leading cannot be precisely controlled to exactly match the way text is rendered in AWT.  Therefore a slower AWT/iText hybrid renderer is used.
* PDF form elements are created differently than in iText 2, and some styling options are not implemented.
* Complex script text layout (used for Asian languages) has not been tested as it requires a license file.
