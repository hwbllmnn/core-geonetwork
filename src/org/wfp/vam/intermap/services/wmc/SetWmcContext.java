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

package org.wfp.vam.intermap.services.wmc;

import java.net.URLDecoder;
import jeeves.interfaces.Service;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Xml;
import org.jdom.Element;
import org.wfp.vam.intermap.Constants;
import org.wfp.vam.intermap.kernel.map.MapMerger;
import org.wfp.vam.intermap.kernel.map.mapServices.wmc.GeoRSSCodec;
import org.wfp.vam.intermap.kernel.map.mapServices.wmc.om.WMCExtension;
import org.wfp.vam.intermap.kernel.map.mapServices.wmc.om.WMCViewContext;
import org.wfp.vam.intermap.kernel.map.mapServices.wmc.om.WMCWindow;
import org.wfp.vam.intermap.kernel.map.mapServices.wmc.util.WMC2jdom;
import org.wfp.vam.intermap.kernel.map.mapServices.wmc.util.WMCParser;
import org.wfp.vam.intermap.kernel.map.mapServices.wmc.util.WMCUtil;
import org.wfp.vam.intermap.kernel.map.mapServices.wms.schema.impl.Utils;
import org.wfp.vam.intermap.kernel.marker.MarkerSet;
import org.wfp.vam.intermap.services.map.MapUtil;

/**
 * Set the WMC from an URL-encoded parameter.
 *
 * @author ETj
 */
public class SetWmcContext implements Service
{
	public void init(String appPath, ServiceConfig config) throws Exception {}

	//--------------------------------------------------------------------------
	//---
	//--- Service
	//---
	//--------------------------------------------------------------------------

	public Element exec(Element params, ServiceContext context) throws Exception
	{
		String wmc = params.getChildText("wmc");
		String dec = URLDecoder.decode(wmc, "UTF-8");

//		System.out.println("DECODED\n" + dec);

		Element mapContext = Xml.loadString(dec, false);

//		XMLOutputter xo = new XMLOutputter(Format.getPrettyFormat());
//		System.out.println(" ============= request wmc is:\n\n" +xo.outputString(mapContext));

		// Create a new MapMerger object
		String sreplace  = params.getChildText("clearLayers"); // TODO: let's set the same param in all services supporting it. "clear" is simpler.
		boolean breplace = Utils.getBooleanAttrib(sreplace, true);

		MapMerger mm = breplace?
							new MapMerger():
							MapUtil.getMapMerger(context);

		WMCParser parser = new WMCParser();
		parser.setLenientParsing(true); // will accept also documents without namespace
		WMCViewContext vc = parser.parseViewContext(mapContext);
		WMCWindow win = vc.getGeneral().getWindow();

		String url = MapUtil.setContext(mm, vc);

		// Update the user session
		context.getUserSession().setProperty(Constants.SESSION_MAP, mm);

		// load markerset, if any, from context
		WMCExtension ext = vc.getGeneral().getExtension();
		if(ext != null)
		{
			Element georss = WMCUtil.getExtensionChild(ext, "georss");
			if(georss != null)
			{
				Element feed = (Element)georss.getChildren().get(0);
				MarkerSet ms = GeoRSSCodec.parseGeoRSS(feed);
				context.getUserSession().setProperty(Constants.SESSION_MARKERSET, ms);
			}
		}

		// Prepare response
		Element response = new Element("response")
			.addContent(new Element("imgUrl").setText(url))
			.addContent(new Element("scale").setText(mm.getDistScale()))
			.addContent(mm.getBoundingBox().toElement())
			.addContent(new Element("width").setText("" + win.getWidth()))
			.addContent(new Element("height").setText("" + win.getHeight()));

		MarkerSet ms = (MarkerSet)context.getUserSession().getProperty(Constants.SESSION_MARKERSET);
		if(ms != null)
			response.addContent(ms.select(mm.getBoundingBox()).toElement());

		return response;
	}
}

//=============================================================================
