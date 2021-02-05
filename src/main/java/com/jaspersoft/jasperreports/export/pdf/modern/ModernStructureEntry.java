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

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;

import net.sf.jasperreports.export.pdf.PdfStructureEntry;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernStructureEntry implements PdfStructureEntry
{

	private ModernPdfStructure pdfStructure;
	private PdfStructElem element;
	
	public ModernStructureEntry(ModernPdfStructure pdfStructure, PdfStructElem element)
	{
		this.pdfStructure = pdfStructure;
		this.element = element;
	}

	public PdfStructElem getElement()
	{
		return element;
	}

	@Override
	public void putString(String name, String value)
	{
		element.put(pdfStructure.pdfName(name), new PdfString(value));
	}

	@Override
	public void putArray(String name)
	{
		element.put(pdfStructure.pdfName(name), new PdfArray());
	}

	@Override
	public void setSpan(int colSpan, int rowSpan)
	{
		PdfArray a = new PdfArray();
		PdfDictionary dict = new PdfDictionary();
		if (colSpan > 1)
		{
			dict.put(pdfStructure.pdfName("ColSpan"), new PdfNumber(colSpan));
		}
		if (rowSpan > 1)
		{
			dict.put(pdfStructure.pdfName("RowSpan"), new PdfNumber(rowSpan));
		}
		dict.put(PdfName.O, PdfName.Table);
		a.add(dict);
		element.put(PdfName.A, a);
	}
	
}
