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
 * @Description: ES操作工具类
 */
@Slf4j
@Component
@ConditionalOnBean(value = RestHighLevelClient.class)
public class ElasticService {
	
	private RestHighLevelClient restHighLevelClient;

	private BulkProcessor myBulkProcessor;

	/** 缓存所有ES索引名称 */
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
	 * 获取ES所有的索引名称
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
			log.error("获取ES索引列表时发生异常：", e);
		}
		return null;
	}

	/**
	 * 处理ES翻页数据接口
	 */
	public interface DealElasticSearchScrollData {
		void dealData(SearchHit[] searchHits, int curPage);
	}

	/**
	 * 获取总页码
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
			log.error("ES查询异常：", e);
		}
		return 0;
	}

	/**
	 * ES查询所有数据
	 * 
	 * @param esIndex
	 * @param queryObject
	 * @param dealElasticSearchScrollData
	 */
	public void scrollSearchAll(String esIndex, QueryObject queryObject,
	        DealElasticSearchScrollData dealElasticSearchScrollData) {
		// 数据结果缓存时长2分钟
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
		// 处理ES返回数据
		dealElasticSearchScrollData.dealData(searchHits, page);

		String scrollId = response.getScrollId();
		while (null != searchHits && searchHits.length > 0) {
			SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
			scrollRequest.scroll(scroll);
			try {
				response = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
			} catch (IOException e) {
				log.error("查询索引为:{} 的深分页数据时发生异常：", esIndex, e);
			}
			scrollId = response.getScrollId();
			searchHits = response.getHits().getHits();
			if (null != searchHits && searchHits.length > 0) {
				page++;
				// 处理ES返回数据
				dealElasticSearchScrollData.dealData(searchHits, page);
			}
		}

		// 清除滚屏
		ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
		clearScrollRequest.addScrollId(scrollId);
		try {
			ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest,
			        RequestOptions.DEFAULT);
			boolean flag = clearScrollResponse.isSucceeded();
			if (!flag) {
				log.warn("ES查询完成后，滚屏清除失败！");
			}
		} catch (IOException e) {
			log.error("ES清除滚屏异常：", e);
		}
	}

	/**
	 * @param indexName
	 *            索引名称
	 * @param indexSQL
	 *            索引mapping json sql
	 */
	public void createIndex(String indexName, String indexSQL) throws Exception {
		if (indexSet.contains(indexName) || indexExist(indexName)) {
			log.warn("索引:{} 已经存在，无需重复创建", indexName);
			return;
		}
		CreateIndexRequest request = new CreateIndexRequest(indexName);

		// 设置分片
		// buildSetting(request);
		request.source(indexSQL, XContentType.JSON);

		// 设置mapping
		// request.mapping(indexSQL, XContentType.JSON);

		CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		if (!res.isAcknowledged()) {
			log.error("ES创建索引失败");
		} else {
			indexSet.add(indexName);
		}
	}

	/**
	 * @param indexName
	 *            索引名称
	 * @param builder
	 *            索引mapping
	 */
	public void createIndex(String indexName, XContentBuilder builder) throws Exception {
		if (indexSet.contains(indexName) || indexExist(indexName)) {
			log.warn("索引:{} 已经存在", indexName);
			return;
		}
		CreateIndexRequest request = new CreateIndexRequest(indexName);

		// 设置分片
		buildSetting(request);

		// 设置mapping
		request.mapping(builder);

		CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		if (!res.isAcknowledged()) {
			log.error("ES创建索引失败");
		} else {
			indexSet.add(indexName);
		}
	}

	/**
	 * 删除索引
	 * 
	 * @param indexName
	 *            索引名称
	 */
	public void deleteIndex(String indexName) {
		try {
			if (!indexExist(indexName)) {
				log.error("名为:{} 的索引不存在", indexName);
				return;
			}
			restHighLevelClient.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
			indexSet.remove(indexName);
		} catch (Exception e) {
			log.error("删除索引:{} 时发生异常：", indexName, e);
		}
	}

	/**
	 * 创建文档
	 * 
	 * @param indexName
	 *            索引名称
	 * @param obj
	 *            文档数据实体
	 * @throws IOException
	 */
	public <T> boolean insert(String indexName, T obj) {
		IndexRequest request = getIndexRequest(indexName, obj);

		// 使用client对象添加文档
		try {
			IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
			return BooleanUtils.toBoolean(response.getResult().toString());
		} catch (IOException e) {
			log.error("名为:{}的索引，数据插入异常：", indexName, e);
			return false;
		}
	}

	/**
	 * 组织 indexRequest 对象
	 * 
	 * @param indexName
	 * @param obj
	 * @param <T>
	 * @return
	 */
	private <T> IndexRequest getIndexRequest(String indexName, T obj) {
		// 准备json数据
		String jsonData;
		if (obj instanceof MessageLite) {
			JsonFormat jsonFormat = new JsonFormat();
			jsonData = jsonFormat.printToString((Message) obj);
		} else {
			jsonData = JSONObject.toJSONString(obj);
		}

		// 准备request对象
		IndexRequest request = new IndexRequest(indexName);
		Object idVal = CommonUtils.getObjectId(obj);
		Optional.ofNullable(idVal).ifPresent(o -> request.id(String.valueOf(o)));

		request.source(jsonData, XContentType.JSON);
		return request;
	}

	/**
	 * 修改文档
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
				// 准备json数据
				String jsonData = JSONObject.toJSONString(data);
				UpdateRequest request = new UpdateRequest(indexName, o.toString());
				request.doc(jsonData, XContentType.JSON);
				UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
				result.set(BooleanUtils.toBoolean(response.getResult().toString()));
			} catch (Exception e) {
				log.error("修改索引为:{}，ID为:{}的文档时发生异常：", indexName, o, e);
			}
		});
		return result.get();
	}

	/**
	 * 删除文档
	 * 
	 * @param indexName
	 *            索引名称
	 * @param docId
	 *            文档ID（_id）
	 * @return
	 * @throws IOException
	 */
	public boolean delete(String indexName, String docId) throws IOException {
		DeleteRequest request = new DeleteRequest(indexName, docId);
		DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
		return BooleanUtils.toBoolean(response.getResult().toString());
	}

	/**
	 * 批量插入数据
	 * 
	 * @param indexName
	 *            index
	 * @param list
	 *            带插入列表
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
			log.error("ES索引名称为：{}的数据入库发生异常：", e);
		}
		return false;
	}

	/**
	 * 批量更新
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
				// 准备json数据
				String jsonData = JSONObject.toJSONString(obj);
				UpdateRequest updateRequest = new UpdateRequest(indexName, o.toString());
				updateRequest.doc(jsonData, XContentType.JSON);
				request.add(updateRequest);
			});
		});

		try {
			BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
			if (response.hasFailures()) {
				log.error("名为:{}的索引在执行批量更新时发生异常：", indexName, response.buildFailureMessage());
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("名为:{}的索引，批量更新异常：", indexName, e);
		}
		return false;
	}

	/**
	 * 批量删除
	 * 
	 * @param indexName
	 *            index
	 * @param idList
	 *            待删除列表
	 */
	public boolean deleteBatch(String indexName, Collection<String> idList) {
		BulkRequest request = new BulkRequest();
		idList.forEach(item -> request.add(new DeleteRequest(indexName, item)));
		try {
			BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
			if (response.hasFailures()) {
				log.error("名为:{}的索引在执行批量删除时发生异常：", indexName, response.buildFailureMessage());
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("名为:{}的索引，批量删除异常：", indexName, e);
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
		// 设置批量操作数量,最大为10000
		request.setBatchSize(10000);
		request.setConflicts("proceed");
		try {
			restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
		} catch (Exception e) {
			log.error("ES按条件删除异常：", e);
		}
	}

	/**
	 * 设置分片
	 * 
	 * @param request
	 */
	public void buildSetting(CreateIndexRequest request) {
		request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 1));
	}

	public void createMappingTest() throws IOException {
		Settings.Builder builder = Settings.builder();
		builder.put("index.number_of_shards", 3).put("index.number_of_replicas", 1);

		XContentBuilder mapping = JsonXContent.contentBuilder().startObject().startObject("properties")
		        .startObject("name").field("type", "text").field("analyzer", "ik_max_word").endObject()
		        .startObject("birthday").field("type", "date").field("format", "yyyy-MM-dd").endObject().endObject()
		        .endObject();

		CreateIndexRequest request = new CreateIndexRequest("wenlong_test");
		request.settings(builder);
		request.mapping(mapping);
		restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
	}

	/**
	 * 判断某个index是否存在
	 * 
	 * @param idxName
	 *            index名
	 * @return boolean
	 */
	public boolean indexExist(String idxName) throws Exception {
		return restHighLevelClient.indices().exists(new GetIndexRequest(idxName), RequestOptions.DEFAULT);
	}

	/**
	 * 查询
	 * 
	 * @param esIndex
	 *            ES索引名
	 * @param queryObject
	 *            查询参数实体
	 * @param c
	 *            结果类对象
	 * @return java.util.List<T>
	 */
	public <T> List<T> search(String esIndex, QueryObject queryObject, Class<T> c) {
		return search(esIndex, queryObject.createSearchSourceBuilder(), c);
	}

	/**
	 * 查询
	 * 
	 * @param esIndex
	 *            ES索引名
	 * @param searchSourceBuilder
	 *            查询参数构建者
	 * @param c
	 *            结果类对象
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
			log.error("ES查询异常：", e);
		}
		return null;
	}

}
