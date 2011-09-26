package net.vvakame.appengine.reducepricing.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import com.google.appengine.api.datastore.Entity;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

/**
 * {@link ApiCountDelegate} のテストケース.
 * @author vvakame
 */
public class ApiCountDelegateTest extends ControllerTestCase {

	/**
	 * 動作確認.
	 * @throws Exception 
	 * @author vvakame
	 */
	@Test
	public void test() throws Exception {
		ApiCountDelegate delegate = ApiCountDelegate.install();

		tester.start("/useApi");
		assertThat(tester.response.getStatus(), is(equalTo(HttpServletResponse.SC_OK)));

		delegate.uninstall();

		Entity entity = delegate.entity;
		Datastore.put(entity);
		Map<String, Object> properties = entity.getProperties();

		for (String key : properties.keySet()) {
			System.out.println(key);
		}
	}
}
