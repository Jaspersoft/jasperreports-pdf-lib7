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

### JasperSoft Studio

#### Add Jaspersoft Property
Jaspersoft Studio Properties can either be set for the entire ***workspace*** or a specific ***project***:

Go to `Project -> Properties -> JasperSoft Studio -> Properties` and choose either 

`Configure Workspace Settings` -> `Configure Workspace Settings` 

or `Use project Settings`

In the property list search for `pdf.producer factory`.
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

### Jaspersoft Reports Server

~~~java
// TODO
~~~

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
