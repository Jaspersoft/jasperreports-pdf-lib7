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

import com.itextpdf.layout.element.ILeafElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.BaseDirection;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.export.pdf.PdfChunk;
import net.sf.jasperreports.export.pdf.PdfPhrase;
import net.sf.jasperreports.export.pdf.PdfTextAlignment;
import net.sf.jasperreports.export.pdf.TextDirection;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernPhrase implements PdfPhrase
{

	private ModernPdfProducer pdfProducer;
	private Paragraph paragraph;

	public ModernPhrase(ModernPdfProducer pdfProducer, Paragraph paragraph)
	{
		this.pdfProducer = pdfProducer;
		this.paragraph = paragraph;
	}

	@Override
	public void add(PdfChunk chunk)
	{
		paragraph.add((ILeafElement) ((ModernChunk) chunk).getElement());
	}

	@Override
	public float go(float llx, float lly, float urx, float ury, 
			float fixedLeading, float multipliedLeading, 
			PdfTextAlignment alignment, TextDirection runDirection)
	{
		if (fixedLeading != 0f)
		{
			paragraph.setFixedLeading(fixedLeading);
		}
		if (multipliedLeading != 0f)
		{
			paragraph.setMultipliedLeading(multipliedLeading);
		}
		
		paragraph.setTextAlignment(ModernPdfUtils.toITextAlignment(alignment));
		paragraph.setBaseDirection(toITextRunDirection(runDirection));
		
		paragraph.setFixedPosition(pdfProducer.getCurrentPageNumber(), llx, ury, urx - llx);
		paragraph.setHeight(lly - ury);//TODO lucian -2
		
		ModernParagraphRenderer renderer = new ModernParagraphRenderer(paragraph);
		paragraph.setNextRenderer(renderer);
		pdfProducer.getDocument().add(paragraph);
		
		Float yLine = renderer.getYLine();
		return yLine == null ? lly : yLine;
	}
	
	protected static BaseDirection toITextRunDirection(TextDirection direction)
	{
		BaseDirection iTextDirection;
		switch (direction)
		{
		case DEFAULT:
			iTextDirection = BaseDirection.DEFAULT_BIDI;
			break;
		case LTR:
			iTextDirection = BaseDirection.LEFT_TO_RIGHT;
			break;
		case RTL:
			iTextDirection = BaseDirection.RIGHT_TO_LEFT;
			break;
		default:
			throw new JRRuntimeException("Unknown text direction " + direction);
		}
		return iTextDirection;
	}

}
