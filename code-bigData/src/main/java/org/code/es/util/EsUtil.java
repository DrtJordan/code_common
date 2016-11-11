package org.code.es.util;


import java.lang.reflect.Constructor;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
public class EsUtil {
	static Settings settings = ImmutableSettings.settingsBuilder()
			.put("cluster.name", "elasticsearch")
			.put("client.transport.sniff", true).build();
	private static TransportClient client;
	static {
		try {
			Class<?> clazz = Class.forName(TransportClient.class.getName());
			Constructor<?> constructor = clazz
			.getDeclaredConstructor(Settings.class);
			constructor.setAccessible(true);
			client = (TransportClient) constructor.newInstance(settings);
			client.addTransportAddress(new InetSocketTransportAddress(
					"192.168.127.6", 9300));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static synchronized TransportClient getTransportClient() {
		return client;
	}
}
