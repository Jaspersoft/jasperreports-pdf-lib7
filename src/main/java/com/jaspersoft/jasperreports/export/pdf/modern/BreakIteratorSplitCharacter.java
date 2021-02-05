/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2019 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package com.jaspersoft.jasperreports.export.pdf.modern;

import java.text.BreakIterator;

import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.layout.splitting.ISplitCharacters;


/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class BreakIteratorSplitCharacter implements ISplitCharacters
{

	private GlyphLine lastGlyphLine;
	private String text;
	private int[] glyphsCharStart;
	
	private boolean[] boundary;
	private int lastBoundary;
	private final BreakIterator breakIter;
	
	public BreakIteratorSplitCharacter()
	{
		this(BreakIterator.getLineInstance());
	}
	
	public BreakIteratorSplitCharacter(BreakIterator breakIter)
	{
		this.breakIter = breakIter;
	}

	@Override
	public boolean isSplitCharacter(GlyphLine glyphLine, int glyphPos)
	{
        if (!glyphLine.get(glyphPos).hasValidUnicode())
        {
            return false;
        }

		if (this.lastGlyphLine != glyphLine)
		{
			loadChars(glyphLine);

			lastBoundary = breakIter.first();
			if (lastBoundary != BreakIterator.DONE)
			{
				boundary[lastBoundary] = true;
			}
		}

		int glyphCharStart = glyphsCharStart[glyphPos];
		int glyphCharEnd = glyphsCharStart[glyphPos + 1];
		while (glyphCharEnd > lastBoundary)
		{
			lastBoundary = breakIter.next();

			if (lastBoundary == BreakIterator.DONE)
			{
				lastBoundary = Integer.MAX_VALUE;
			}
			else
			{
				boundary[lastBoundary] = true;
			}
		}

		for (int i = glyphCharStart; i < glyphCharEnd; ++i)
		{
			if (boundary[i + 1] || text.charAt(i) <= ' ')
			{
				return true;
			}
		}
		return false;
	}
	
	private void loadChars(GlyphLine glyphLine)
	{
		lastGlyphLine = glyphLine;
		
		int glyphCount = glyphLine.size();
		glyphsCharStart = new int[glyphCount + 1];
		int charCount = 0;
		StringBuilder chars = new StringBuilder();
		for (int glyphIdx = 0; glyphIdx < glyphCount; ++glyphIdx)
		{
			glyphsCharStart[glyphIdx] = charCount;
			
			char[] glyphChars = glyphLine.get(glyphIdx).getChars();
			if (glyphChars != null)
			{
				charCount += glyphChars.length;
				chars.append(glyphChars);
			}
		}
		glyphsCharStart[glyphCount] = charCount;
		
		text = chars.toString();
		breakIter.setText(text);
		boundary = new boolean[charCount + 1];
	}

}
