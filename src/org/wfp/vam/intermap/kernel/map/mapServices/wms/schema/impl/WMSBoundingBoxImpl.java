//=============================================================================
//===	Copyright (C) 2001-2007 Food and Agriculture Organization of the
//===	United Nations (FAO-UN), United Nations World Food Programme (WFP)
//===	and United Nations Environment Programme (UNEP)
//===
//===	This program is free software; you can redistribute it and/or modify
//===	it under the terms of the GNU General Public License as published by
//===	the Free Software Foundation; either version 2 of the License, or (at
//===	your option) any later version.
//===
//===	This program is distributed in the hope that it will be useful, but
//===	WITHOUT ANY WARRANTY; without even the implied warranty of
//===	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
//===	General Public License for more details.
//===
//===	You should have received a copy of the GNU General Public License
//===	along with this program; if not, write to the Free Software
//===	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
//===
//===	Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
//===	Rome - Italy. email: geonetwork@osgeo.org
//==============================================================================

package org.wfp.vam.intermap.kernel.map.mapServices.wms.schema.impl;


import org.jdom.Element;
import org.wfp.vam.intermap.kernel.map.mapServices.wms.schema.type.WMSBoundingBox;


/**
 * @author ETj
 */
public class WMSBoundingBoxImpl implements WMSBoundingBox
{
	private String _srs = null;
	private double _minx = Float.NaN;
	private double _miny = Float.NaN;
	private double _maxx = Float.NaN;
	private double _maxy = Float.NaN;
	private float _resx = Float.NaN; // opt
	private float _resy = Float.NaN; // opt

	private WMSBoundingBoxImpl()
	{
	}

	public static WMSBoundingBox newInstance()
	{
		return new WMSBoundingBoxImpl();
	}

	public static WMSBoundingBox parse(Element ebb)
	{
		WMSBoundingBoxImpl bb = new WMSBoundingBoxImpl();

		bb.setSRS(ebb.getAttributeValue("srs"));
		bb.setMinx(Float.parseFloat(ebb.getAttributeValue("minx")));
		bb.setMiny(Float.parseFloat(ebb.getAttributeValue("miny")));
		bb.setMaxx(Float.parseFloat(ebb.getAttributeValue("maxx")));
		bb.setMaxy(Float.parseFloat(ebb.getAttributeValue("maxy")));

		String sresx = ebb.getAttributeValue("resx");
		if(sresx != null)
			bb.setResx(Float.parseFloat(sresx));

		String sresy = ebb.getAttributeValue("resy");
		if(sresy != null)
			bb.setResy(Float.parseFloat(sresy));

		return bb;
	}

	/**
	 * Sets Srs
	 */
	public void setSRS(String srs)
	{
		_srs = srs;
	}

	/**
	 * Returns Srs
	 */
	public String getSRS()
	{
		return _srs;
	}

	/**
	 * Sets Minx
	 */
	public void setMinx(double minx)
	{
		_minx = minx;
	}

	/**
	 * Returns Minx
	 */
	public double getMinx()
	{
		return _minx;
	}

	/**
	 * Sets Miny
	 */
	public void setMiny(double miny)
	{
		_miny = miny;
	}

	/**
	 * Returns Miny
	 */
	public double getMiny()
	{
		return _miny;
	}

	/**
	 * Sets Maxx
	 */
	public void setMaxx(double maxx)
	{
		_maxx = maxx;
	}

	/**
	 * Returns Maxx
	 */
	public double getMaxx()
	{
		return _maxx;
	}

	/**
	 * Sets Maxy
	 */
	public void setMaxy(double maxy)
	{
		_maxy = maxy;
	}

	/**
	 * Returns Maxy
	 */
	public double getMaxy()
	{
		return _maxy;
	}

	/**
	 * Sets Resx
	 */
	public void setResx(float resx)
	{
		_resx = resx;
	}

	/**
	 * Returns Resx
	 */
	public float getResx()
	{
		return _resx;
	}

	/**
	 * Sets Resy
	 */
	public void setResy(float resy)
	{
		_resy = resy;
	}

	/**
	 * Returns Resy
	 */
	public float getResy()
	{
		return _resy;
	}


	/**
	 * Method toElement
	 */
	public Element toElement(String name)
	{
		if(_srs == null)
			throw new IllegalStateException(name + "/SRS is missing");
		if(_minx == Float.NaN)
			throw new IllegalStateException(name + "/minx is missing");
		if(_miny == Float.NaN)
			throw new IllegalStateException(name + "/miny is missing");
		if(_maxx == Float.NaN)
			throw new IllegalStateException(name + "/maxx is missing");
		if(_maxy == Float.NaN)
			throw new IllegalStateException(name + "/maxy is missing");

		Element ret = new Element(name)
			.setAttribute("SRS", _srs)
			.setAttribute("minx", ""+_minx)
			.setAttribute("miny", ""+_miny)
			.setAttribute("maxx", ""+_maxx)
			.setAttribute("maxy", ""+_maxy);

		if(_resx != Float.NaN)
			ret.setAttribute("resx", ""+_resx);
		if(_resy != Float.NaN)
			ret.setAttribute("resy", ""+_resy);

		return ret;
	}

}

