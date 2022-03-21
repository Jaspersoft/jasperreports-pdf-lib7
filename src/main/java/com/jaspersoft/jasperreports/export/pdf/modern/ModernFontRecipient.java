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
import java.io.IOException;

import com.itextpdf.io.font.constants.FontStyles;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.export.pdf.FontRecipient;
import net.sf.jasperreports.export.pdf.PdfFontStyle;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernFontRecipient implements FontRecipient
{

	private PdfFontCache fontCache;
	private PdfFontAttributes font;

	public ModernFontRecipient(PdfFontCache fontCache)
	{
		this.fontCache = fontCache;
	}
	
	@Override
	public boolean hasFont()
	{
		return font != null;
	}
	
	public PdfFontAttributes getFont()
	{
		return font;
	}

	@Override
	public void setFont(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded, 
			float size, PdfFontStyle pdfFontStyle, Color forecolor)
	{
		try
		{
			PdfFont pdfFont = PdfFontFactory.createRegisteredFont(pdfFontName, pdfEncoding, 
					isPdfEmbedded ? EmbeddingStrategy.PREFER_EMBEDDED : EmbeddingStrategy.PREFER_NOT_EMBEDDED,
					toITextFontStyle(pdfFontStyle));
			
			// check if FontFactory didn't find the font
			if (pdfFont == null || pdfFont.getFontProgram() == null)
			{
				pdfFont = fontCache.getCachedFont(pdfFontName, pdfEncoding, isPdfEmbedded);
			}
			
			this.font = pdfFont == null ? null
					: new PdfFontAttributes(pdfFont, size, pdfFontStyle, forecolor);
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}
	
	protected static int toITextFontStyle(PdfFontStyle pdfFontStyle)
	{
		return (pdfFontStyle.isBold() ? FontStyles.BOLD : 0)
				| (pdfFontStyle.isItalic() ? FontStyles.ITALIC : 0);
	}

	@Override
	public void setFont(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded,
			float size, PdfFontStyle pdfFontStyle, Color forecolor,
			byte[] fontData)
	{
		PdfFont pdfFont = fontCache.cacheFont(pdfFontName, pdfEncoding, isPdfEmbedded, fontData);

		font = new PdfFontAttributes(pdfFont, size, pdfFontStyle, forecolor);
	}
	
}
