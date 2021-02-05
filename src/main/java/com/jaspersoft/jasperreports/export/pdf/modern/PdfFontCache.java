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

import com.itextpdf.kernel.font.PdfFont;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PdfFontCache
{

	private static final PdfFontCache INSTANCE = new PdfFontCache();
	
	public static PdfFontCache instance()
	{
		return INSTANCE;
	}
	
	private static String cacheFontKey(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded)
	{
		//same as BaseFont in iText 2
		return pdfFontName + "\n" + pdfEncoding + "\n" + isPdfEmbedded;
	}
	
	private Map<String, PdfFont> fontCache = new HashMap<>();

	public PdfFontCache()
	{
		// TODO Auto-generated constructor stub
	}
	
	public PdfFont getCachedFont(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded)
	{
		String fontKey = cacheFontKey(pdfFontName, pdfEncoding, isPdfEmbedded);
		synchronized (fontCache)
		{
			return fontCache.get(fontKey);
		}
	}
	
	public PdfFont cacheFont(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded, PdfFont font)
	{
		String fontKey = cacheFontKey(pdfFontName, pdfEncoding, isPdfEmbedded);
		synchronized (fontKey)
		{
			PdfFont cachedFont = fontCache.get(fontKey);
			if (cachedFont == null)
			{
				fontCache.put(fontKey, font);
				cachedFont = font;
			}
			return cachedFont;
		}
	}

}
