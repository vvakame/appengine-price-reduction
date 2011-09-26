package net.vvakame.appengine.reducepricing.controller;

import java.util.ArrayList;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Transaction;

/**
 * APIを適当に叩くコントローラ
 * @author vvakame
 */
public class UseApiController extends Controller {

	@Override
	protected Navigation run() throws Exception {

		Entity entity = new Entity("Test");

		Datastore.put(entity);
		Datastore.get(entity.getKey());

		Datastore.allocateId("Test");

		Transaction tx = Datastore.beginTransaction();
		tx.commit();

		Datastore.delete(entity.getKey());

		Datastore.getAsMap(entity.getKey());

		Datastore.query("entity").filter("test", FilterOperator.EQUAL, 1L).asList();

		List<Entity> list = new ArrayList<Entity>();
		for (int i = 0; i <= 100; i++) {
			list.add(new Entity("Test"));
		}
		Datastore.put(list);

		Datastore.query("Test").limit(10).asList();

		Datastore.query("Test").limit(10).asKeyList();

		Datastore.query("Test").count();

		return null;
	}
}
