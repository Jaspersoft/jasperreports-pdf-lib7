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

import com.itextpdf.kernel.font.PdfFont;

import net.sf.jasperreports.export.pdf.PdfFontStyle;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PdfFontAttributes
{

	private PdfFont pdfFont;
	private float size;
	private Color forecolor;
	private PdfFontStyle style;
	
	public PdfFontAttributes(PdfFont pdfFont, float size, PdfFontStyle style, Color forecolor)
	{
		this.pdfFont = pdfFont;
		this.size =size;
		this.style = style;
		this.forecolor = forecolor;
	}

	public PdfFont getPdfFont()
	{
		return pdfFont;
	}

	public void setPdfFont(PdfFont pdfFont)
	{
		this.pdfFont = pdfFont;
	}

	public float getSize()
	{
		return size;
	}

	public void setSize(float size)
	{
		this.size = size;
	}

	public Color getForecolor()
	{
		return forecolor;
	}

	public void setForecolor(Color forecolor)
	{
		this.forecolor = forecolor;
	}

	public PdfFontStyle getStyle()
	{
		return style;
	}

	public void setStyle(PdfFontStyle style)
	{
		this.style = style;
	}

}
