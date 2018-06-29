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

import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.property.TextAlignment;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.AbstractTextRenderer;
import net.sf.jasperreports.engine.type.RunDirectionEnum;
import net.sf.jasperreports.engine.util.JRStyledText;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 */
public abstract class AbstractPdfTextRenderer extends AbstractTextRenderer
{
	/**
	 * 
	 */
	protected JRPdfExporter pdfExporter;
	protected PdfCanvas pdfCanvas;
	protected TextAlignment horizontalAlignment;
	protected float leftOffsetFactor;
	protected float rightOffsetFactor;

	
	/**
	 * 
	 */
	public AbstractPdfTextRenderer(JasperReportsContext jasperReportsContext, boolean ignoreMissingFont)
	{
		super(jasperReportsContext, false, ignoreMissingFont);
	}
	
	
	/**
	 * 
	 */
	public void initialize(
		JRPdfExporter pdfExporter, 
		PdfCanvas pdfCanvas,
		JRPrintText text, JRStyledText styledText, 
		int offsetX,
		int offsetY
		)
	{
		this.pdfExporter = pdfExporter;
		this.pdfCanvas = pdfCanvas;
		
		horizontalAlignment = TextAlignment.LEFT;
		leftOffsetFactor = 0f;
		rightOffsetFactor = 0f;
		
		//FIXMETAB 0.2f was a fair approximation
		switch (text.getHorizontalTextAlign())
		{
			case JUSTIFIED :
			{
				horizontalAlignment = TextAlignment.JUSTIFIED_ALL;
				leftOffsetFactor = 0f;
				rightOffsetFactor = 0f;
				break;
			}
			case RIGHT :
			{
				if (text.getRunDirectionValue() == RunDirectionEnum.LTR)
				{
					horizontalAlignment = TextAlignment.RIGHT;
				}
				else
				{
					horizontalAlignment = TextAlignment.LEFT;
				}
				leftOffsetFactor = -0.2f;
				rightOffsetFactor = 0f;
				break;
			}
			case CENTER :
			{
				horizontalAlignment = TextAlignment.CENTER;
				leftOffsetFactor = -0.1f;
				rightOffsetFactor = 0.1f;
				break;
			}
			case LEFT :
			default :
			{
				if (text.getRunDirectionValue() == RunDirectionEnum.LTR)
				{
					horizontalAlignment = TextAlignment.LEFT;
				}
				else
				{
					horizontalAlignment = TextAlignment.RIGHT;
				}
				leftOffsetFactor = 0f;
				rightOffsetFactor = 0.2f;
				break;
			}
		}
 
		super.initialize(text, styledText, offsetX, offsetY);
	}
}
