package net.vvakame.appengine.reducepricing.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * APIの利用量を計測する.
 * @author vvakame
 */
public abstract class ApiCountFilter implements Filter {

	static final Logger logger = Logger.getLogger(ApiCountFilter.class.getSimpleName());


	/**
	 * API名を計算し返すメソッド.
	 * @param request
	 * @return API名
	 * @author vvakame
	 */
	public abstract String getApiName(HttpServletRequest request);

	/**
	 * ログを保存するか否かチェックするメソッド.
	 * @param request
	 * @param response
	 * @return 保存をするかしないか
	 * @author vvakame
	 */
	public abstract boolean checkDoSave(HttpServletRequest request, HttpServletResponse response);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ApiCountDelegate delegate = null;
		try {
			delegate = ApiCountDelegate.install();
		} catch (Throwable th) {
			logger.log(Level.INFO, "failed to create api call log.");
		} finally {
			try {
				chain.doFilter(request, response);
			} catch (Throwable th) {
				logger.log(Level.INFO, "failed to save accesslog.", th);
				doThrow(th);
			} finally {
				if (delegate != null) {
					delegate.uninstall();
				}
				try {
					Entity entity = delegate.entity;
					ApiCountService.addApiName(entity, getApiName(request));
					DatastoreServiceFactory.getDatastoreService().put(entity);
				} catch (Throwable th) {
					logger.log(Level.INFO, "failed to save accesslog.", th);
					doThrow(th);
				}
			}
		}
	}

	void doThrow(Throwable th) throws IOException, ServletException {
		if (th instanceof ServletException) {
			throw (ServletException) th;
		}
		if (th instanceof IOException) {
			throw (IOException) th;
		}
		throw new ServletException(th);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
}
