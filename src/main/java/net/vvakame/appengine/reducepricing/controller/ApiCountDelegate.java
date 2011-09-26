package net.vvakame.appengine.reducepricing.controller;

import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Entity;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.ApiProxy.ApiProxyException;
import com.google.apphosting.api.ApiProxy.Delegate;
import com.google.apphosting.api.ApiProxy.Environment;
import com.google.apphosting.api.ApiProxy.LogRecord;

/**
 * Datastoreを叩く時、GetとRunQueryのみ受け付け、それ以外は弾く{@link com.google.apphosting.api.ApiProxy.Delegate}.
 * @author vvakame
 */
public class ApiCountDelegate implements ApiProxy.Delegate<Environment> {

	static final Logger logger = Logger.getLogger(ApiCountDelegate.class.getName());

	final ApiProxy.Delegate<Environment> parent;

	Entity entity = ApiCountService.createEntity();


	/**
	 * {@link ApiCountDelegate}を{@link ApiProxy}に設定する。
	 * <p>現在{@link ApiProxy}に設定されている{@link com.google.apphosting.api.ApiProxy.Delegate}が
	 * {@link ApiCountDelegate}だった場合は何もしない。</p>
	 * @return 新たに作成した{@link ApiCountDelegate}か、
	 *   既に適用済みだった場合は元々設定されていた{@link ApiCountDelegate}
	 */
	public static ApiCountDelegate install() {
		@SuppressWarnings("unchecked")
		Delegate<Environment> originalDelegate = ApiProxy.getDelegate();
		if (originalDelegate instanceof ApiCountDelegate == false) {
			ApiCountDelegate newDelegate = new ApiCountDelegate(originalDelegate);
			ApiProxy.setDelegate(newDelegate);
			return newDelegate;
		} else {
			return (ApiCountDelegate) originalDelegate;
		}
	}

	/**
	 * {@link ApiCountDelegate}を{@link ApiProxy}からはずす。
	 * @param originalDelegate 元々設定されていた{@link com.google.apphosting.api.ApiProxy.Delegate}.
	 *   {@link ApiCountDelegate#getParent()}を使用すると良い。
	 */
	public static void uninstall(Delegate<Environment> originalDelegate) {
		ApiProxy.setDelegate(originalDelegate);
	}

	/**
	 * {@link ApiCountDelegate}を{@link ApiProxy}からはずす。
	 */
	public void uninstall() {
		ApiProxy.setDelegate(parent);
	}

	@Override
	public Future<byte[]> makeAsyncCall(Environment env, String service, String method,
			byte[] requestBytes, ApiConfig config) {

		analysis(service, method, requestBytes);

		return getParent().makeAsyncCall(env, service, method, requestBytes, config);
	}

	@Override
	public byte[] makeSyncCall(Environment env, String service, String method, byte[] requestBytes)
			throws ApiProxyException {
		return getParent().makeSyncCall(env, service, method, requestBytes);
	}

	void analysis(String service, String method, byte[] requestBytes) {
		logger.log(Level.INFO, "service=" + service + ", method=" + method);

		ApiCountService.countUp(entity, service, method, requestBytes);

	}

	/**
	 * the constructor.
	 * @param delegate
	 * @category constructor
	 */
	ApiCountDelegate(Delegate<Environment> delegate) {
		this.parent = delegate;
	}

	@Override
	public void log(Environment env, LogRecord logRecord) {
		getParent().log(env, logRecord);
	}

	@Override
	public void flushLogs(Environment env) {
		getParent().flushLogs(env);
	}

	@Override
	public List<Thread> getRequestThreads(Environment env) {
		return getParent().getRequestThreads(env);
	}

	/**
	 * @return the parent
	 * @category accessor
	 */
	public ApiProxy.Delegate<Environment> getParent() {
		return parent;
	}
}
