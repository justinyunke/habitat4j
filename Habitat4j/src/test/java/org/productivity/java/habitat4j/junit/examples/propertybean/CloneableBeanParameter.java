package org.productivity.java.habitat4j.junit.examples.propertybean;


/**
 * This is a sample PropertyBean that tests parameters implementing the
 * Cloneable interface.
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
 * @version $Id: CloneableBeanParameter.java,v 1.2 2005/09/21 07:13:37 cvs Exp $
 */
public class CloneableBeanParameter implements Cloneable {
	protected StringBuffer buffer = new StringBuffer("Cloneable Test");
	
	int primitiveInt = -1;
	
	public int getPrimitiveInt() {
		return primitiveInt;
	}

	public void setPrimitiveInt(int primitiveInt) {
		this.primitiveInt = primitiveInt;
	}

	public Object clone() throws CloneNotSupportedException {
		CloneableBeanParameter baseClone = (CloneableBeanParameter) super.clone();
		
		baseClone.buffer = new StringBuffer(buffer.toString());
		
		return baseClone;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuffer buffer) {
		this.buffer = buffer;
	}
}
