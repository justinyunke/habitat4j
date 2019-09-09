package org.productivity.java.habitat4j.junit.examples.propertybean;

import java.math.BigDecimal;
import java.math.BigInteger;

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
 * @version $Id: EveryParameterTypeBean.java,v 1.10 2006/02/14 06:49:33 cvs Exp $
 */
public class EveryParameterTypeBean {
	private short shortValue = 0;
	private int intValue = 0;
	private boolean _has_intValue = false;
	private long longValue = 0;
	
	private double doubleValue = 0.00;
	private float floatValue = 0;
	
	private boolean booleanValue = false;
	private char charValue = 'x';

	private Short shortObjectValue = null;
	private Integer intObjectValue = null;
	private Boolean _has_intObjectValue = new Boolean(false);
	private BigInteger bigIntegerObjectValue = null;
	private Long longObjectValue = null;
	
	private Double doubleObjectValue = null;
	private Float floatObjectValue = null;
	private BigDecimal bigDecimalObjectValue = null;
	
	private Boolean booleanObjectValue = null;
	private Character charObjectValue = null;
	
	private String stringValue = null;
	
	private CloneableBeanParameter cloneableBeanParameter = new CloneableBeanParameter();
	
	/**
	 * @return Returns an instance of CloneableBeanParameter.
	 */
	public CloneableBeanParameter getCloneableBeanParameter() {
		return cloneableBeanParameter;
	}
	
	/**
	 * @param cloneableBeanParameter - the instance of CloneableBeanParameter to set
	 */
	public void setCloneableBeanParameter(
			CloneableBeanParameter cloneableBeanParameter) {
		this.cloneableBeanParameter = cloneableBeanParameter;
	}
	
	/**
	 * @return Returns the booleanObjectValue.
	 */
	public Boolean getBooleanObjectValue() {
		return booleanObjectValue;
	}
	
	/**
	 * @param booleanObjectValue The booleanObjectValue to set.
	 */
	public void setBooleanObjectValue(Boolean booleanObjectValue) {
		this.booleanObjectValue = booleanObjectValue;
	}
	
	/**
	 * @return Returns the booleanValue.
	 */
	public boolean isBooleanValue() {
		return booleanValue;
	}
	
	/**
	 * @param booleanValue The booleanValue to set.
	 */
	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
	
	/**
	 * @return Returns the charObjectValue.
	 */
	public Character getCharObjectValue() {
		return charObjectValue;
	}
	
	/**
	 * @param charObjectValue The charObjectValue to set.
	 */
	public void setCharObjectValue(Character charObjectValue) {
		this.charObjectValue = charObjectValue;
	}
	
	/**
	 * @return Returns the charValue.
	 */
	public char getCharValue() {
		return charValue;
	}
	
	/**
	 * @param charValue The charValue to set.
	 */
	public void setCharValue(char charValue) {
		this.charValue = charValue;
	}
	
	/**
	 * @return Returns the doubleObjectValue.
	 */
	public Double getDoubleObjectValue() {
		return doubleObjectValue;
	}
	
	/**
	 * @param doubleObjectValue The doubleObjectValue to set.
	 */
	public void setDoubleObjectValue(Double doubleObjectValue) {
		this.doubleObjectValue = doubleObjectValue;
	}
	
	/**
	 * @return Returns the doubleValue.
	 */
	public double getDoubleValue() {
		return doubleValue;
	}
	
	/**
	 * @param doubleValue The doubleValue to set.
	 */
	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}
	
	/**
	 * @return Returns the floatObjectValue.
	 */
	public Float getFloatObjectValue() {
		return floatObjectValue;
	}
	
	/**
	 * @param floatObjectValue The floatObjectValue to set.
	 */
	public void setFloatObjectValue(Float floatObjectValue) {
		this.floatObjectValue = floatObjectValue;
	}
	
	/**
	 * @return Returns the floatValue.
	 */
	public float getFloatValue() {
		return floatValue;
	}
	
	/**
	 * @param floatValue The floatValue to set.
	 */
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}
	
	/**
	 * @return Returns the intObjectValue.
	 */
	public Integer getIntObjectValue() {
		return intObjectValue;
	}
	
	/**
	 * @return Returns the intObjectValue.
	 */
	public Boolean hasIntObjectValue() {
		return _has_intObjectValue;
	}
	
	/**
	 * @param intObjectValue The intObjectValue to set.
	 */
	public void setIntObjectValue(Integer intObjectValue) {
		_has_intObjectValue = new Boolean(true);
		
		this.intObjectValue = intObjectValue;
	}
	
	/**
	 * @return Returns the intValue.
	 */
	public int getIntValue() {
		return intValue;
	}
	
	/**
	 * @return Returns the intValue.
	 */
	public boolean hasIntValue() {
		return _has_intValue;
	}
	
	/**
	 * @param intValue The intValue to set.
	 */
	public void setIntValue(int intValue) {
		_has_intValue = true;
		
		this.intValue = intValue;
	}
	
	/**
	 * @return Returns the longObjectValue.
	 */
	public Long getLongObjectValue() {
		return longObjectValue;
	}
	
	/**
	 * @param longObjectValue The longObjectValue to set.
	 */
	public void setLongObjectValue(Long longObjectValue) {
		this.longObjectValue = longObjectValue;
	}
	
	/**
	 * @return Returns the longValue.
	 */
	public long getLongValue() {
		return longValue;
	}
	
	/**
	 * @param longValue The longValue to set.
	 */
	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}
	
	/**
	 * @return Returns the shortObjectValue.
	 */
	public Short getShortObjectValue() {
		return shortObjectValue;
	}
	
	/**
	 * @param shortObjectValue The shortObjectValue to set.
	 */
	public void setShortObjectValue(Short shortObjectValue) {
		this.shortObjectValue = shortObjectValue;
	}
	
	/**
	 * @return Returns the shortValue.
	 */
	public short getShortValue() {
		return shortValue;
	}
	
	/**
	 * @param shortValue The shortValue to set.
	 */
	public void setShortValue(short shortValue) {
		this.shortValue = shortValue;
	}
	
	/**
	 * @return Returns the stringValue.
	 */
	public String getStringValue() {
		return stringValue;
	}
	
	/**
	 * @param stringValue The stringValue to set.
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	
	/**
	 * @return Returns an instance of BigDecimal.
	 */
	public BigDecimal getBigDecimalObjectValue() {
		return bigDecimalObjectValue;
	}
	
	/**
	 * @param bigDecimalObjectValue - the instance of BigDecimal to set
	 */
	public void setBigDecimalObjectValue(BigDecimal bigDecimalObjectValue) {
		this.bigDecimalObjectValue = bigDecimalObjectValue;
	}
	
	/**
	 * @return Returns an instance of BigInteger.
	 */
	public BigInteger getBigIntegerObjectValue() {
		return bigIntegerObjectValue;
	}
	
	/**
	 * @param bigIntegerObjectValue - the instance of BigInteger to set
	 */
	public void setBigIntegerObjectValue(BigInteger bigIntegerObjectValue) {
		this.bigIntegerObjectValue = bigIntegerObjectValue;
	}
	
	/**
	 * @return Returns a null; used to test PROPERTY_LIST_FEATURE_SUPPRESS_PROPERTY_BEAN_COPY_WARNINGS.
	 */
	public String getNoMatchingSetter() {
		return null;
	}
}
