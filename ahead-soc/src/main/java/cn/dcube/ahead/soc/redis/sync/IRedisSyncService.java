package cn.dcube.ahead.soc.redis.sync;

/**
 * 同步redis数据服务
 * 
 * @author yangfei
 *
 */
public interface IRedisSyncService {
	/**
	 * 同步数据
	 * 
	 * @param <T>
	 * @param datas
	 */
	public <T> void sync();

	/**
	 * 同步的数据类型
	 * 
	 * @return
	 */
	public String getType();
}
