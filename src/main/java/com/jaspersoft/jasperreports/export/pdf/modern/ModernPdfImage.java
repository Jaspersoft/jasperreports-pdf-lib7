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


import com.itextpdf.layout.element.Image;

import net.sf.jasperreports.export.pdf.PdfImage;

/**
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 */
public class ModernPdfImage implements PdfImage
{

	private Image image;

	public ModernPdfImage(Image image)
	{
		this.image = image;
	}
	
	public Image getImage()
	{
		return image;
	}

	@Override
	public float getPlainWidth()
	{
		return image.getImageScaledWidth();
	}

	@Override
	public float getPlainHeight()
	{
		return image.getImageScaledHeight();
	}

	@Override
	public float getScaledWidth()
	{
		return image.getImageScaledWidth();//TODO lucian check
	}

	@Override
	public float getScaledHeight()
	{
		return image.getImageScaledHeight();
	}

	@Override
	public void scaleAbsolute(int width, int height)
	{
		image.scaleAbsolute(width, height);
	}

	@Override
	public void scaleToFit(int width, int height)
	{
		image.scaleToFit(width, height);
	}

	@Override
	public void setRotationDegrees(int degrees)
	{
		double angle = degrees / 180 * Math.PI;
		image.setRotationAngle(angle);
	}

}
