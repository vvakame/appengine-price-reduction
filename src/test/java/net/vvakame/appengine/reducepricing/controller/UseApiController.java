package net.vvakame.appengine.reducepricing.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Transaction;

/**
 * APIを適当に叩くコントローラ
 * @author vvakame
 */
public class UseApiController extends Controller {

	final TestModelMeta META = TestModelMeta.get();


	@Override
	protected Navigation run() throws Exception {

		TestModel model = new TestModel();
		Datastore.put(model);
		Datastore.get(model.getKey());

		Datastore.allocateId(META);

		Transaction tx = Datastore.beginTransaction();
		tx.commit();

		Datastore.delete(model.getKey());

		Datastore.getAsMap(model.getKey());

		Datastore.query(META)
			.filter(META.longValue.equal(1L), META.dateValue.lessThanOrEqual(new Date())).asList();

		List<TestModel> list = new ArrayList<TestModel>();
		for (int i = 0; i <= 100; i++) {
			list.add(new TestModel());
		}
		Datastore.put(list);

		Datastore.query(META).limit(1).asSingle();

		Datastore.query(META).limit(10).asList();

		Datastore.query(META).limit(10).asKeyList();

		Datastore.query(META).count();

		return null;
	}
}
