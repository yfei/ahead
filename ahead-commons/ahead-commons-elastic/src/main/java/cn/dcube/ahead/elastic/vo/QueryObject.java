package cn.dcube.ahead.elastic.vo;

import java.io.Serializable;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import lombok.Getter;
import lombok.Setter;

/**
 * ES查询条件相关属性对象
 */
@Setter
@Getter
public abstract class QueryObject implements Serializable {

	/**
	 * 当前页
	 */
	protected Integer currentPage = 1;

	/**
	 * 每页显示的数据大小
	 */
	protected Integer pageSize = 10000;

	/**
	 * 搜索关键字
	 */
	protected String keyword;

	/**
	 * 开始的记录数
	 * 
	 * @return
	 */
	public Integer getStart() {
		return (currentPage - 1) * pageSize;
	}

	/**
	 * 创建queryBuilder 子类按需重写此方法
	 * 
	 * @return
	 */
	public QueryBuilder createQueryBuilder() {
		return null;
	}

	/**
	 * 创建SearchSourceBuilder，并设置通用的属性 子类按需重写此方法
	 * 
	 * @param pageSize
	 *            每页数据条数
	 * @return
	 */
	public SearchSourceBuilder createSearchSourceBuilder(int pageSize) {
		// 这里可以给searchSourceBuilder设置一些通用的条件
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.from(getStart());
		searchSourceBuilder.size(pageSize);
		return searchSourceBuilder;
	}

	/**
	 * 创建SearchSourceBuilder，并设置通用的属性 子类按需重写此方法
	 * 
	 * @return
	 */
	public SearchSourceBuilder createSearchSourceBuilder() {
		return createSearchSourceBuilder(this.pageSize);
	}

	/**
	 * 创建HighlightBuilder对象，并设置一些通用的属性
	 * 
	 * @return
	 */
	public HighlightBuilder createHighlightBuilder() {
		// 这里可以给HighlightBuilder设置一些通用的高亮配置
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<b>");
		highlightBuilder.postTags("</b>");
		return highlightBuilder;
	}

	/**
	 * 给搜索出来的资源重新设置高亮字段的值，父类无需做任何操作，由子类操作
	 * 
	 * @param obj
	 * @param hit
	 */
	public void setHighlightFields(Object obj, SearchHit hit) {}
}
