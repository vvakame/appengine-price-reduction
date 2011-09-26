package net.vvakame.appengine.reducepricing.controller;

import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * テスト用データ!
 * @author vvakame
 */
@Model
public class TestModel {

	@Attribute(primaryKey = true)
	Key key;

	long longValue;

	Date dateValue;


	/**
	 * @return the key
	 * @category accessor
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 * @category accessor
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return the longValue
	 * @category accessor
	 */
	public long getLongValue() {
		return longValue;
	}

	/**
	 * @param longValue the longValue to set
	 * @category accessor
	 */
	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	/**
	 * @return the dateValue
	 * @category accessor
	 */
	public Date getDateValue() {
		return dateValue;
	}

	/**
	 * @param dateValue the dateValue to set
	 * @category accessor
	 */
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}
}
