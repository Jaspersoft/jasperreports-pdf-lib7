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

import java.awt.Color;

import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants.LineCapStyle;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Text;

import net.sf.jasperreports.export.pdf.PdfTextChunk;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernTextChunk extends ModernChunk implements PdfTextChunk
{
	
	private static final Style STYLE_UNDERLINE = createUnderlineStyle();
	
	private static final Style STYLE_STRIKETHROUGH = createStrikethroughStyle();

	private static Style createUnderlineStyle()
	{
		Style style = new Style();
		style.setUnderline(null, 0, 1f / 18, 0, -1f / 12, LineCapStyle.BUTT);
		return style;
	}

	private static Style createStrikethroughStyle()
	{
		// using the same thickness as sun.font.Fond2D.
		// the position is calculated in Fond2D based on the ascent, defaulting 
		// to iText default position which depends on the font size
		Style style = new Style();
		style.setUnderline(null, 0, 1f / 18, 0, 1f / 3, LineCapStyle.BUTT);
		return style;
	}
	
	private Text text;
	private PdfFontAttributes fontAttributes;

	public ModernTextChunk(ModernPdfProducer pdfProducer, Text text, PdfFontAttributes fontAttributes)
	{
		super(pdfProducer, text);
		
		this.text = text;
		this.fontAttributes = fontAttributes;
	}

	public Text getText()
	{
		return text;
	}
	
	@Override
	public void setUnderline()
	{
		text.addStyle(STYLE_UNDERLINE);
	}

	@Override
	public void setStrikethrough()
	{
		text.addStyle(STYLE_STRIKETHROUGH);
	}

	@Override
	public void setSuperscript()
	{
		text.setTextRise(fontAttributes.getSize()/2);
	}

	@Override
	public void setSubscript()
	{
		text.setTextRise(-fontAttributes.getSize()/2);
	}

	@Override
	public void setBackground(Color backcolor)
	{
		text.setBackgroundColor(ModernPdfUtils.toPdfColor(backcolor));
	}

}
