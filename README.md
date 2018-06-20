# JasperReports PDF Exporter Lib Seven

This project contains an add-on PDF exporter for the JasperReports Library, which is based on iText version 7.

Note that the JasperReports Library already has a built-in PDF exporter based on iText version 2.1.7.
Starting with iText version 5, the iText library has changed its license from LGPL/MPL to AGPL, 
and also changed its package names, so the JasperReports Library built-in PDF exporter could not 
be upgraded past iText 2.1.7.

Instead, this separate project was created, which is based on the original JasperReports Library built-in PDF exporter code,
but uses the iText 7 API and is released under AGPL, so that it is compatible with the iText 7 license and
thus continue to be available for use in open source products.
