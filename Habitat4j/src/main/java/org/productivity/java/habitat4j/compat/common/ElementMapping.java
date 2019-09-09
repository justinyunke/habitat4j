package org.productivity.java.habitat4j.compat.common;

/**
 * ElementMapping is an object that defines the "target" information
 * relating to XML elements used by AbstractXMLMap.
 * 
 * <p>## LICENSE INFORMATION ##</p>
 * 
 * <p>This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.</p>
 *
 * <p>This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.</p>
 *
 * <p>You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 
 * USA</p>
 * 
 * @author Justin Yunke &lt;habitat4j@productivity.org&gt;
 * @version $Id: ElementMapping.java,v 1.3 2004/11/10 08:00:51 cvs Exp $
 */
public class ElementMapping {
	private String elementName = null;
	private String elementValue = null;
	/**
	 * @return Returns the elementName.
	 */
	public String getElementName() {
		return elementName;
	}
	/**
	 * @param elementName The elementName to set.
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	/**
	 * @return Returns the elementValue.
	 */
	public String getElementValue() {
		return elementValue;
	}
	/**
	 * @param elementValue The elementValue to set.
	 */
	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}
}
