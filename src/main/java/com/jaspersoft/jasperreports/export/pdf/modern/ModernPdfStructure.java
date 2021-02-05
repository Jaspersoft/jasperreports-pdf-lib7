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

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;

import net.sf.jasperreports.export.pdf.PdfStructure;
import net.sf.jasperreports.export.pdf.PdfStructureEntry;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernPdfStructure implements PdfStructure
{

	private ModernPdfProducer pdfProducer;
	
	private Map<String, PdfName> pdfNames;

	public ModernPdfStructure(ModernPdfProducer pdfProducer)
	{
		this.pdfProducer = pdfProducer;
		this.pdfNames = new HashMap<>();
	}

	@Override
	public PdfStructureEntry createAllTag(String language)
	{
		PdfDocument pdfDocument = pdfProducer.getDocument().getPdfDocument();
		PdfStructTreeRoot root = pdfDocument.getStructTreeRoot();
		
		PdfName pdfNameALL = new PdfName("All");
		root.addRoleMapping(PdfName.All.getValue(), PdfName.Sect.getValue());
		root.addRoleMapping(PdfName.Image.getValue(), PdfName.Figure.getValue());
		root.addRoleMapping(PdfName.Text.getValue(), PdfName.Text.getValue());
		
		PdfStructElem allTag = new PdfStructElem(pdfDocument, pdfNameALL);
		root.addKid(allTag);

		if(pdfProducer.getWriter().getPdfaConformance() == PdfaConformanceEnum.PDFA_1A)
		{
			root.addRoleMapping("Anchor", PdfName.NonStruct.getValue());
			root.addRoleMapping(PdfName.Text.getValue(), PdfName.Span.getValue());
		}
		else
		{
			root.addRoleMapping("Anchor", PdfName.Text.getValue());
		}
		
		if (language != null)
		{
			allTag.put(PdfName.Lang, new PdfString(language));
		}
		
		return new ModernStructureEntry(this, allTag);
	}
	
	protected PdfName pdfName(String name)
	{
		PdfName pdfName = pdfNames.get(name);
		if (pdfName == null)
		{
			pdfName = new PdfName(name);
			pdfNames.put(name, pdfName);
		}
		return pdfName;
	}

	protected ModernStructureEntry createElement(PdfStructureEntry parent, String name)
	{
		PdfStructElem parentElement = ((ModernStructureEntry) parent).getElement();
		PdfDocument pdfDocument = pdfProducer.getDocument().getPdfDocument();
		PdfStructElem element = new PdfStructElem(pdfDocument, pdfName(name));
		parentElement.addKid(element);
		return new ModernStructureEntry(this, element);
	}

	@Override
	public PdfStructureEntry createTag(PdfStructureEntry parent, String name)
	{
		return createElement(parent, name);
	}

	@Override
	public PdfStructureEntry beginTag(PdfStructureEntry parent, String name)
	{
		ModernStructureEntry tag = createElement(parent, name);
		pdfProducer.getPdfCanvas().beginMarkedContent(tag.getElement().getRole());
		return tag;
	}

	@Override
	public PdfStructureEntry beginTag(PdfStructureEntry parent, String name, String text)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void endTag()
	{
		pdfProducer.getPdfCanvas().endMarkedContent();
	}

}
