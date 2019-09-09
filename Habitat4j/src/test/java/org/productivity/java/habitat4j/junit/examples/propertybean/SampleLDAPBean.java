package org.productivity.java.habitat4j.junit.examples.propertybean;


/**
 * This is a sample PropertyBean.
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
 * @version $Id: SampleLDAPBean.java,v 1.6 2006/07/08 21:21:59 cvs Exp $
 */
public class SampleLDAPBean extends SampleCommProtocolBean {
	private static final long serialVersionUID = 7956436184295731395L;
	
	private String baseDN = null;
	private String authUser = null;
	private String authPass = null;
	
	/**
	 * @return Returns the baseDN.
	 */
	public String getBaseDN() {
		return baseDN;
	}
	/**
	 * @param baseDN The baseDN to set.
	 */
	public void setBaseDN(String baseDN) {
		this.baseDN = baseDN;
	}
	
	/**
	 * @return Returns the authPass.
	 */
	public String getAuthPass() {
		return authPass;
	}
	/**
	 * @param authPass The authPass to set.
	 */
	public void setAuthPass(String authPass) {
		this.authPass = authPass;
	}
	/**
	 * @return Returns the authUser.
	 */
	public String getAuthUser() {
		return authUser;
	}
	/**
	 * @param authUser The authUser to set.
	 */
	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}
}
