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

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.properties.TextAlignment;

import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.export.pdf.PdfTextAlignment;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernPdfUtils
{

	public ModernPdfUtils()
	{
	}
	
	public static Color toPdfColor(java.awt.Color color)
	{
		return new DeviceRgb(color);
	}
	
	public static TextAlignment toITextAlignment(PdfTextAlignment alignment)
	{
		TextAlignment iTextAlign;
		switch (alignment)
		{
		case LEFT:
			iTextAlign = TextAlignment.LEFT;
			break;
		case RIGHT:
			iTextAlign = TextAlignment.RIGHT;
			break;
		case CENTER:
			iTextAlign = TextAlignment.CENTER;
			break;
		case JUSTIFIED:
			iTextAlign = TextAlignment.JUSTIFIED;
			break;
		case JUSTIFIED_ALL:
			iTextAlign = TextAlignment.JUSTIFIED_ALL;
			break;
		default:
			throw new JRRuntimeException("Unknown paragraph alignment " + alignment);
		}
		return iTextAlign;
	}

}
