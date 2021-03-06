package cn.dcube.ahead.elastic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.googlecode.protobuf.format.JsonFormat;

import cn.dcube.ahead.elastic.util.CommonUtils;
import cn.dcube.ahead.elastic.vo.QueryObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: wenlong
 * @CreateTime: 2020-06-09 14:09
 * @Description: ES???????????????
 */
@Slf4j
@Component
@ConditionalOnBean(value = RestHighLevelClient.class)
public class ElasticService {
	
	private RestHighLevelClient restHighLevelClient;

	private BulkProcessor myBulkProcessor;

	/** ????????????ES???????????? */
	private Set<String> indexSet = Collections.synchronizedSet(new HashSet<>());

	@Autowired
	private void setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}

	@Resource(name = "myBulkProcessor")
	private void setRestHighLevelClient(BulkProcessor bulkProcessor) {
		this.myBulkProcessor = bulkProcessor;
	}

	@PostConstruct
	public void init() {
		Set<String> indices = getIndices();
		if (CollectionUtils.isEmpty(indices))
			return;
		indexSet.addAll(indices);
	}

	/**
	 * ??????ES?????????????????????
	 * 
	 * @return
	 */
	public Set<String> getIndices() {
		GetAliasesRequest request = new GetAliasesRequest();
		try {
			GetAliasesResponse getAliasesResponse = restHighLevelClient.indices().getAlias(request,
			        RequestOptions.DEFAULT);
			Map<String, Set<AliasMetaData>> map = getAliasesResponse.getAliases();
			Set<String> indices = map.keySet();
			return indices;
		} catch (IOException e) {
			log.error("??????ES??????????????????????????????", e);
		}
		return null;
	}

	/**
	 * ??????ES??????????????????
	 */
	public interface DealElasticSearchScrollData {
		void dealData(SearchHit[] searchHits, int curPage);
	}

	/**
	 * ???????????????
	 * 
	 * @param esIndex
	 * @param queryObject
	 * @return
	 */
	public long getTotalPage(String esIndex, QueryObject queryObject) {
		CountRequest countRequest = new CountRequest(esIndex);
		countRequest.query(queryObject.createQueryBuilder());
		try {
			CountResponse response = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
			Long totalCount = response.getCount();
			return CommonUtils.getTotalPages(totalCount.intValue(), queryObject.getPageSize());
		} catch (Exception e) {
			log.error("ES???????????????", e);
		}
		return 0;
	}

	/**
	 * ES??????????????????
	 * 
	 * @param esIndex
	 * @param queryObject
	 * @param dealElasticSearchScrollData
	 */
	public void scrollSearchAll(String esIndex, QueryObject queryObject,
	        DealElasticSearchScrollData dealElasticSearchScrollData) {
		// ????????????????????????2??????
		final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(2L));
		SearchRequest request = new SearchRequest(esIndex);
		request.scroll(scroll);

		SearchSourceBuilder searchSourceBuilder = queryObject.createSearchSourceBuilder();
		request.source(searchSourceBuilder);
		SearchResponse response = null;
		try {
			response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int page = 1;
		SearchHit[] searchHits = response.getHits().getHits();
		// ??????ES????????????
		dealElasticSearchScrollData.dealData(searchHits, page);

		String scrollId = response.getScrollId();
		while (null != searchHits && searchHits.length > 0) {
			SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
			scrollRequest.scroll(scroll);
			try {
				response = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
			} catch (IOException e) {
				log.error("???????????????:{} ????????????????????????????????????", esIndex, e);
			}
			scrollId = response.getScrollId();
			searchHits = response.getHits().getHits();
			if (null != searchHits && searchHits.length > 0) {
				page++;
				// ??????ES????????????
				dealElasticSearchScrollData.dealData(searchHits, page);
			}
		}

		// ????????????
		ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
		clearScrollRequest.addScrollId(scrollId);
		try {
			ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest,
			        RequestOptions.DEFAULT);
			boolean flag = clearScrollResponse.isSucceeded();
			if (!flag) {
				log.warn("ES???????????????????????????????????????");
			}
		} catch (IOException e) {
			log.error("ES?????????????????????", e);
		}
	}

	/**
	 * @param indexName
	 *            ????????????
	 * @param indexSQL
	 *            ??????mapping json sql
	 */
	public void createIndex(String indexName, String indexSQL) throws Exception {
		if (indexSet.contains(indexName) || indexExist(indexName)) {
			log.warn("??????:{} ?????????????????????????????????", indexName);
			return;
		}
		CreateIndexRequest request = new CreateIndexRequest(indexName);

		request.source(indexSQL, XContentType.JSON);

		CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		if (!res.isAcknowledged()) {
			log.error("ES??????????????????");
		} else {
			indexSet.add(indexName);
		}
	}

	/**
	 * @param indexName
	 *            ????????????
	 * @param builder
	 *            ??????mapping
	 */
	public void createIndex(String indexName, XContentBuilder builder) throws Exception {
		if (indexSet.contains(indexName) || indexExist(indexName)) {
			log.warn("??????:{} ????????????", indexName);
			return;
		}
		CreateIndexRequest request = new CreateIndexRequest(indexName);

		// ????????????
		buildSetting(request);

		// ??????mapping
		request.mapping(builder);

		CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		if (!res.isAcknowledged()) {
			log.error("ES??????????????????");
		} else {
			indexSet.add(indexName);
		}
	}

	/**
	 * ????????????
	 * 
	 * @param indexName
	 *            ????????????
	 */
	public void deleteIndex(String indexName) {
		try {
			if (!indexExist(indexName)) {
				log.error("??????:{} ??????????????????", indexName);
				return;
			}
			restHighLevelClient.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
			indexSet.remove(indexName);
		} catch (Exception e) {
			log.error("????????????:{} ??????????????????", indexName, e);
		}
	}

	/**
	 * ????????????
	 * 
	 * @param indexName
	 *            ????????????
	 * @param obj
	 *            ??????????????????
	 * @throws IOException
	 */
	public <T> boolean insert(String indexName, T obj) {
		IndexRequest request = getIndexRequest(indexName, obj);

		// ??????client??????????????????
		try {
			IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
			return BooleanUtils.toBoolean(response.getResult().toString());
		} catch (IOException e) {
			log.error("??????:{}?????????????????????????????????", indexName, e);
			return false;
		}
	}

	/**
	 * ?????? indexRequest ??????
	 * 
	 * @param indexName
	 * @param obj
	 * @param <T>
	 * @return
	 */
	private <T> IndexRequest getIndexRequest(String indexName, T obj) {
		// ??????json??????
		String jsonData;
		if (obj instanceof MessageLite) {
			JsonFormat jsonFormat = new JsonFormat();
			jsonData = jsonFormat.printToString((Message) obj);
		} else {
			jsonData = JSONObject.toJSONString(obj);
		}

		// ??????request??????
		IndexRequest request = new IndexRequest(indexName);
		Object idVal = CommonUtils.getObjectId(obj);
		Optional.ofNullable(idVal).ifPresent(o -> request.id(String.valueOf(o)));

		request.source(jsonData, XContentType.JSON);
		return request;
	}

	/**
	 * ????????????
	 * 
	 * @param indexName
	 * @param data
	 * @return
	 */
	public <T> boolean update(String indexName, T data) {
		AtomicBoolean result = new AtomicBoolean(false);
		Object idVal = CommonUtils.getObjectId(data);
		Optional.ofNullable(idVal).ifPresent(o -> {
			try {
				// ??????json??????
				String jsonData = JSONObject.toJSONString(data);
				UpdateRequest request = new UpdateRequest(indexName, o.toString());
				request.doc(jsonData, XContentType.JSON);
				UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
				result.set(BooleanUtils.toBoolean(response.getResult().toString()));
			} catch (Exception e) {
				log.error("???????????????:{}???ID???:{}???????????????????????????", indexName, o, e);
			}
		});
		return result.get();
	}

	/**
	 * ????????????
	 * 
	 * @param indexName
	 *            ????????????
	 * @param docId
	 *            ??????ID???_id???
	 * @return
	 * @throws IOException
	 */
	public boolean delete(String indexName, String docId) throws IOException {
		DeleteRequest request = new DeleteRequest(indexName, docId);
		DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
		return BooleanUtils.toBoolean(response.getResult().toString());
	}

	/**
	 * ??????????????????
	 * 
	 * @param indexName
	 *            index
	 * @param list
	 *            ???????????????
	 */
	public <T> boolean insertBatch(String indexName, List<T> list) {
		try {
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				IndexRequest indexRequest = getIndexRequest(indexName, obj);
				myBulkProcessor.add(indexRequest);
			}
			return true;
		} catch (Exception e) {
			log.error("ES??????????????????{}??????????????????????????????", e);
		}
		return false;
	}

	/**
	 * ????????????
	 * 
	 * @param indexName
	 * @param list
	 * @param <T>
	 */
	public <T> boolean updateBatch(String indexName, List<T> list) {
		BulkRequest request = new BulkRequest();

		list.forEach(obj -> {
			Object idVal = CommonUtils.getObjectId(obj);
			Optional.ofNullable(idVal).ifPresent(o -> {
				// ??????json??????
				String jsonData = JSONObject.toJSONString(obj);
				UpdateRequest updateRequest = new UpdateRequest(indexName, o.toString());
				updateRequest.doc(jsonData, XContentType.JSON);
				request.add(updateRequest);
			});
		});

		try {
			BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
			if (response.hasFailures()) {
				log.error("??????:{}????????????????????????????????????????????????", indexName, response.buildFailureMessage());
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("??????:{}?????????????????????????????????", indexName, e);
		}
		return false;
	}

	/**
	 * ????????????
	 * 
	 * @param indexName
	 *            index
	 * @param idList
	 *            ???????????????
	 */
	public boolean deleteBatch(String indexName, Collection<String> idList) {
		BulkRequest request = new BulkRequest();
		idList.forEach(item -> request.add(new DeleteRequest(indexName, item)));
		try {
			BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
			if (response.hasFailures()) {
				log.error("??????:{}????????????????????????????????????????????????", indexName, response.buildFailureMessage());
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("??????:{}?????????????????????????????????", indexName, e);
		}
		return false;
	}

	/**
	 * @param idxName
	 * @param builder
	 */
	public void deleteByQuery(String idxName, QueryBuilder builder) {
		DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
		request.setQuery(builder);
		// ????????????????????????,?????????10000
		request.setBatchSize(10000);
		request.setConflicts("proceed");
		try {
			restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
		} catch (Exception e) {
			log.error("ES????????????????????????", e);
		}
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 */
	public void buildSetting(CreateIndexRequest request) {
		request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1));
	}

	/**
	 * ????????????index????????????
	 * 
	 * @param idxName
	 *            index???
	 * @return boolean
	 */
	public boolean indexExist(String idxName) throws Exception {
		return restHighLevelClient.indices().exists(new GetIndexRequest(idxName), RequestOptions.DEFAULT);
	}

	/**
	 * ??????
	 * 
	 * @param esIndex
	 *            ES?????????
	 * @param queryObject
	 *            ??????????????????
	 * @param c
	 *            ???????????????
	 * @return java.util.List<T>
	 */
	public <T> List<T> search(String esIndex, QueryObject queryObject, Class<T> c) {
		return search(esIndex, queryObject.createSearchSourceBuilder(), c);
	}

	/**
	 * ??????
	 * 
	 * @param esIndex
	 *            ES?????????
	 * @param searchSourceBuilder
	 *            ?????????????????????
	 * @param c
	 *            ???????????????
	 * @return java.util.List<T>
	 */
	public <T> List<T> search(String esIndex, SearchSourceBuilder searchSourceBuilder, Class<T> c) {
		SearchRequest request = new SearchRequest(esIndex);
		request.source(searchSourceBuilder);
		try {
			SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
			SearchHit[] hits = response.getHits().getHits();
			List<T> res = new ArrayList<>(hits.length);
			for (SearchHit hit : hits) {
				res.add(JSON.parseObject(hit.getSourceAsString(), c));
			}
			return res;
		} catch (Exception e) {
			log.error("ES???????????????", e);
		}
		return null;
	}

}
