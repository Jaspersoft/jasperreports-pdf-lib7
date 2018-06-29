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
package com.jaspersoft.jasperreports.export.pdf;

import java.io.IOException;
import java.text.AttributedString;

import com.itextpdf.layout.property.BaseDirection;
import com.itextpdf.layout.property.TextAlignment;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public class SimplePdfTextRenderer extends AbstractPdfTextRenderer
{
	/**
	 * 
	 */
	public SimplePdfTextRenderer(JasperReportsContext jasperReportsContext, boolean ignoreMissingFont)
	{
		super(jasperReportsContext, ignoreMissingFont);
	}
	
	
	/**
	 *
	 */
	protected Phrase getPhrase(JRStyledText styledText, JRPrintText textElement)
	{
		String text = styledText.getText();

		AttributedString as = styledText.getAttributedString();

		return pdfExporter.getPhrase(as, text, textElement);
	}

	
	@Override
	public void render()
	{
		ColumnText colText = new ColumnText(pdfCanvas);
		colText.setSimpleColumn(
			getPhrase(styledText, text),
			x + leftPadding,
			pdfExporter.getCurrentPageFormat().getPageHeight()
				- y
				- topPadding
				- verticalAlignOffset
				- text.getLeadingOffset(),
				//+ text.getLineSpacingFactor() * text.getFont().getSize(),
			x + width - rightPadding,
			pdfExporter.getCurrentPageFormat().getPageHeight()
				- y
				- height
				+ bottomPadding,
			0,//text.getLineSpacingFactor(),// * text.getFont().getSize(),
			horizontalAlignment == TextAlignment.JUSTIFIED_ALL ? TextAlignment.JUSTIFIED : horizontalAlignment
			);

		colText.setLeading(0, text.getLineSpacingFactor());// * text.getFont().getSize());
		colText.setRunDirection(
			text.getRunDirectionValue() == RunDirectionEnum.LTR
			? BaseDirection.LEFT_TO_RIGHT : BaseDirection.RIGHT_TO_LEFT
			);

		try
		{
			colText.go();
		}
		catch (IOException e)
		{
			throw new JRRuntimeException(e);
		}
	}


	@Override
	public void draw()
	{
		//nothing to do
	}
}
