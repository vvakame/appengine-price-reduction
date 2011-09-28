package net.vvakame.appengine.reducepricing.controller;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.repackaged.com.google.io.protocol.ProtocolSource;
import com.google.apphosting.api.DatastorePb;
import com.google.apphosting.api.DatastorePb.GetRequest;
import com.google.apphosting.api.DatastorePb.Query;

class ApiCountService {

	static final Logger logger = Logger.getLogger(ApiCountService.class.getName());

	public static final String KIND = "vv_pricing_count";

	public static final String SEPARATOR = "#";


	public static Entity createEntity() {
		Entity entity = new Entity(KIND);
		entity.setProperty("api", "unknown");
		entity.setProperty("at", new Date());

		return entity;
	}

	public static void addApiName(Entity entity, String apiName) {
		entity.setProperty("api", apiName);
	}

	public static void countUp(Entity entity, String service, String method, byte[] data) {

		if ("datastore_v3".equals(service)) {
			analysisDatastore_v3(entity, method, data);
		}

		String propertyName = service + "#" + method;
		if (entity.hasProperty(propertyName)) {
			long count = (Long) entity.getProperty(propertyName);
			count++;
			entity.setProperty(propertyName, count);
		} else {
			entity.setProperty(propertyName, 1L);
		}
	}

	static void analysisDatastore_v3(Entity entity, String method, byte[] data) {
		// AllocateIds, Put, Get, BeginTransaction, Commit, Delete, 
		if ("RunQuery".equals(method)) {
			analysisDatastore_v3_RunQuery(entity, data);
		} else if ("AllocateIds".equals(method)) {
		} else if ("Put".equals(method)) {
		} else if ("Get".equals(method)) {
			analysisDatastore_v3_Get(entity, data);
		} else if ("BeginTransaction".equals(method)) {
		} else if ("Commit".equals(method)) {
		} else if ("Delete".equals(method)) {
		} else {
			logger.log(Level.INFO, "unknown datastore_v3 method=" + method);
		}
	}

	static void analysisDatastore_v3_Get(Entity entity, byte[] data) {
		ProtocolSource source = new ProtocolSource(data);
		GetRequest requestPb = new DatastorePb.GetRequest();
		requestPb.mergeFrom(source);
	}

	static void analysisDatastore_v3_RunQuery(Entity entity, byte[] data) {
		ProtocolSource source = new ProtocolSource(data);
		Query requestPb = new DatastorePb.Query();
		requestPb.mergeFrom(source);

		String kind = requestPb.getKind();
		String query = requestPb.getSearchQuery();

		logger.log(Level.INFO, "RunQuery, kind=" + kind + ", query=" + query);
	}
}
