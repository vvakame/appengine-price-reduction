package net.vvakame.appengine.reducepricing.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.*;

public class IndexServletTest extends ControllerTestCase {

	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException,
			ServletException {
		tester.start("/");
		assertThat(tester.response.getStatus(), is(equalTo(HttpServletResponse.SC_OK)));
	}
}
