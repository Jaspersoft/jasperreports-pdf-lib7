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

import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.export.GenericElementHandler;

/**
 * A generic print element PDF export handler.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public interface GenericElementPdfHandler extends GenericElementHandler
{

	/**
	 * Exports a generic element.
	 * 
	 * <p>
	 * Access to the exporter output and environment is provided via the
	 * {@link JRPdfExporterContext} argument.
	 * 
	 * @param exporterContext the exporter context
	 * @param element the generic element to export
	 */
	void exportElement(JRPdfExporterContext exporterContext, JRGenericPrintElement element);
	
}
