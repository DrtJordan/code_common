package org.code.solr;
//import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(basePackages = { "spring.data.solr.repository" }, multicoreSupport = true)
@PropertySource("classpath:solr/solr.properties")
public class SolrConfig {

	@Autowired
	private Environment env;

	@Bean
	public HttpSolrClient httpSolrClient() {
		return new HttpSolrClient(env.getProperty("solr.url"));
//		return new CloudSolrClient(env.getProperty("zkhost"));
	}

	@Bean
	public SolrTemplate solrTemplate() {
		return new SolrTemplate(httpSolrClient());
	}
}