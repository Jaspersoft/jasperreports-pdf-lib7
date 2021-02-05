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
package com.jaspersoft.jasperreports.export.pdf.modern;


import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;

import net.sf.jasperreports.export.pdf.PdfOutlineEntry;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernPdfOutline implements PdfOutlineEntry
{

	private ModernPdfProducer pdfProducer;
	private PdfOutline pdfOutline;

	public ModernPdfOutline(ModernPdfProducer pdfProducer, PdfOutline pdfOutline)
	{
		this.pdfProducer = pdfProducer;
		this.pdfOutline = pdfOutline;
	}

	@Override
	public PdfOutlineEntry createChild(String title)
	{
		PdfOutline childOutline = pdfOutline.addOutline(title);
		childOutline.addDestination(pdfOutline.getDestination());
		childOutline.setOpen(false);
		return new ModernPdfOutline(pdfProducer, childOutline);
	}

	@Override
	public PdfOutlineEntry createChild(String title, float left, float top)
	{
		PdfOutline childOutline = pdfOutline.addOutline(title);
		PdfDestination destination = PdfExplicitDestination.createXYZ(pdfProducer.getCurrentPage(), left, top, 0);
		childOutline.addDestination(destination);
		childOutline.setOpen(false);
		return new ModernPdfOutline(pdfProducer, childOutline);
	}

}
