package cn.dcube.ahead.elastic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

@Service
public class ElasticService {

	@Autowired
	private ElasticsearchRestTemplate elasticTemplate;

	public ElasticsearchRestTemplate getElasticTemplate() {
		return elasticTemplate;
	}

	public void setElasticTemplate(ElasticsearchRestTemplate elasticTemplate) {
		this.elasticTemplate = elasticTemplate;
	}

}
