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

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfBoolean;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.layout.Document;
import com.itextpdf.pdfa.PdfADocument;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.export.pdf.PdfDocument;
import net.sf.jasperreports.export.type.PdfPrintScalingEnum;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernDocument implements PdfDocument
{

	private ModernPdfProducer producer;
	private PageSize pageSize;
	private Document document;
	
	private String title;
	private String author;
	private String subject;
	private String keywords;
	private String creator;

	public ModernDocument(ModernPdfProducer producer, PageSize pageSize)
	{
		this.producer = producer;
		this.pageSize = pageSize;
	}
	
	public Document getDocument()
	{
		return document;
	}

	@Override
	public void addTitle(String title)
	{
		this.title = title;
	}

	@Override
	public void addAuthor(String author)
	{
		this.author = author;
	}

	@Override
	public void addSubject(String subject)
	{
		this.subject = subject;
	}

	@Override
	public void addKeywords(String keywords)
	{
		this.keywords = keywords;
	}

	@Override
	public void addCreator(String creator)
	{
		this.creator = creator;
	}

	@Override
	public void open()
	{
		ModernPdfWriter writer = producer.getWriter();
		writer.open();
		
		com.itextpdf.kernel.pdf.PdfDocument pdfDocument;
		PdfaConformanceEnum pdfaConformance = writer.getPdfaConformance();
		if (pdfaConformance == null || pdfaConformance == PdfaConformanceEnum.NONE)
		{
			pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer.getPdfWriter());
		}
		else
		{
			PdfAConformanceLevel conformanceLevel;
			switch (pdfaConformance)
			{
			case PDFA_1A:
				conformanceLevel = PdfAConformanceLevel.PDF_A_1A;
				break;
			case PDFA_1B:
				conformanceLevel = PdfAConformanceLevel.PDF_A_1B;
				break;
			default:
				throw new JRRuntimeException("Unknown PDFA conformance " + pdfaConformance);
			}
			
			pdfDocument = new PdfADocument(writer.getPdfWriter(), conformanceLevel, null);
		}
		
		if (producer.isTagged())
		{
			pdfDocument.setTagged();
		}

		PdfPrintScalingEnum printScaling = writer.getPrintScaling();
		if (printScaling != null)
		{
			PdfViewerPreferences viewerPreferences = new PdfViewerPreferences();
			if (PdfPrintScalingEnum.DEFAULT == printScaling)
			{
				viewerPreferences.put(PdfName.PrintScaling, PdfName.AppDefault);
			}
			else if (PdfPrintScalingEnum.NONE == printScaling)
			{
				viewerPreferences.put(PdfName.PrintScaling, PdfName.None);
			}
			pdfDocument.getCatalog().setViewerPreferences(viewerPreferences);
		}
		
		if (creator != null)
		{
			pdfDocument.getDocumentInfo().setCreator(creator);
		}
		if (keywords != null)
		{
			pdfDocument.getDocumentInfo().setKeywords(keywords);
		}
		if (title != null)
		{
			pdfDocument.getDocumentInfo().setTitle(title);
		}
		if (author != null)
		{
			pdfDocument.getDocumentInfo().setAuthor(author);			
		}
		if (subject != null)
		{
			pdfDocument.getDocumentInfo().setSubject(subject);
		}
		
		if (writer.isDisplayMetadataTitle())
		{
			pdfDocument.getCatalog().getViewerPreferences().put(PdfName.DisplayDocTitle, new PdfBoolean(true));			
		}
		
		String language = writer.getLanguage();
		if (language != null)
		{
			pdfDocument.getCatalog().put(PdfName.Lang, new PdfString(language));
		}
		
		document = new Document(pdfDocument, pageSize);
	}
	
}
