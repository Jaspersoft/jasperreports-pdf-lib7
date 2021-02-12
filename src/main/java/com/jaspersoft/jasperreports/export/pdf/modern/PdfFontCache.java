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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class PdfFontCache
{
	
	private static String cacheFontKey(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded)
	{
		//same as BaseFont in iText 2
		return pdfFontName + "\n" + pdfEncoding + "\n" + isPdfEmbedded;
	}
	
	private static Map<String, FontProgram> fontProgramCache = new HashMap<>();
	
	private static FontProgram getCachedFontProgram(String key)
	{
		synchronized (fontProgramCache)
		{
			return fontProgramCache.get(key);
		}
	}
	
	private static FontProgram cacheFontProgram(String key, FontProgram fontProgram)
	{
		synchronized (fontProgramCache)
		{
			FontProgram cachedFontProgram = fontProgramCache.get(key);
			if (cachedFontProgram == null)
			{
				fontProgramCache.put(key, fontProgram);
				cachedFontProgram = fontProgram;
			}
			return cachedFontProgram;
		}
	}
	
	private Map<String, PdfFont> fontCache = new HashMap<>();

	public PdfFontCache()
	{
	}
	
	public PdfFont getCachedFont(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded)
	{
		String fontKey = cacheFontKey(pdfFontName, pdfEncoding, isPdfEmbedded);
		PdfFont pdfFont;
		synchronized (fontCache)
		{
			pdfFont = fontCache.get(fontKey);
		}
		
		if (pdfFont == null)
		{
			FontProgram fontProgram = getCachedFontProgram(fontKey);
			if (fontProgram != null)
			{
				pdfFont = PdfFontFactory.createFont(fontProgram, pdfEncoding, isPdfEmbedded);
				pdfFont = cacheFont(fontKey, pdfFont);
			}
		}
		return pdfFont;
	}
	
	public PdfFont cacheFont(String pdfFontName, String pdfEncoding, boolean isPdfEmbedded, byte[] fontData)
	{
		String fontKey = cacheFontKey(pdfFontName, pdfEncoding, isPdfEmbedded);
		FontProgram fontProgram = getCachedFontProgram(fontKey);
		if (fontProgram == null)
		{
			try
			{
				fontProgram = FontProgramFactory.createFont(fontData, true);
			}
			catch (IOException e)
			{
				throw new JRRuntimeException(e);
			}
			
			fontProgram = cacheFontProgram(fontKey, fontProgram);
		}
		
		PdfFont pdfFont = PdfFontFactory.createFont(fontProgram, pdfEncoding, isPdfEmbedded);
		pdfFont = cacheFont(fontKey, pdfFont);
		return pdfFont;
	}

	protected PdfFont cacheFont(String fontKey, PdfFont font)
	{
		synchronized (fontCache)
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
