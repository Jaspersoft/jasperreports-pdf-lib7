/*
 * JasperReports Modern PDF Exporter
 * Copyright © 2005 - 2018 TIBCO Software Inc.
 * http://www.jaspersoft.com.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperreports.export.pdf;

import com.itextpdf.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.export.JRExporterContext;


/**
 * A context that represents information about an PDF export process.
 * 
 * @see JRPdfExporter
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public interface JRPdfExporterContext extends JRExporterContext
{

	/**
	 * Returns the {@link PdfWriter} instance used by the exporter.
	 * 
	 * @return the exporter's {@link PdfWriter} instance
	 */
	PdfWriter getPdfWriter();
	
}
