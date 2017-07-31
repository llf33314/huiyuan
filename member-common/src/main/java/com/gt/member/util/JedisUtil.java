package com.gt.member.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * 
 * @author 李逢喜
 * @version 创建时间：2015年9月7日 下午7:14:20
 * 
 */
public class JedisUtil {
	private static JedisPool pool = null;

	public static JedisPool getPool() {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxTotal(100);
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(5);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(3000 * 100);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			if(CommonUtil.isNotEmpty(PropertiesUtil.getRedisPwd())){
				pool = new JedisPool(config,PropertiesUtil.getRedisIp(),CommonUtil.toInteger(PropertiesUtil.getRedisPort()),60000,PropertiesUtil.getRedisPwd());
			}else{				
				pool = new JedisPool(config,PropertiesUtil.getRedisIp(),CommonUtil.toInteger(PropertiesUtil.getRedisPort()),60000);
			}
		}
		return pool;
	}
	
	public static void main(String[] args) {
		Map<String, Integer > params=new HashMap<String, Integer>();
		params.put("total",36002 );
		params.put("daysCount",211 );
		set("busCount", JSONObject.fromObject(params).toString());
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public static void returnResource(JedisPool pool, Jedis redis) {
		if (redis != null) {
			pool.returnResource(redis);
		}
	}

	/**
	 * 设置缓存
	 * 
	 * @param key
	 * @param value
	 * @param time
	 */
	public static void set(String key, String value) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

	}

	/**
	 * 设置缓存
	 * 
	 * @param key
	 * @param value
	 * @param nxxx
	 *            NX XX
	 * @param expx
	 *            时间单位 ex:秒 px:毫秒
	 * @param time
	 */
	public static void set(String key, String value, int seconds) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.setex(key, seconds, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

	}

	/**
	 * 设置缓存
	 * 
	 * @param key
	 * @param value
	 * @param nxxx
	 *            NX XX
	 */
	public static void set(String key, String value, String nxxx) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.set(key, value, nxxx);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		String value = null;

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

		return value;

	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 * @return
	 */
	public static Long del(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return jedis.del(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
			return null;
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 存储成map对象，对应的key中添加对应的键值对值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public static void map(String key, String field, String value) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

	}

	/**
	 * 根据key，同时根据map当中的key删除数据
	 * 
	 * @param key
	 * @param field
	 */
	public static long mapdel(String key, String field) {
		long num = 0;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			num = jedis.hdel(key, field);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return num;
	}

	/**
	 * 查看map对象数据
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static String maoget(String key, String field) {
		String value = null;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return value;
	}

	/**
	 * 存储成list，对应的key中添加对应的键值对值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public static void rPush(String key, String value) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.rpush(key, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

	}

	/**
	 * 存储成list，对应的key中添加对应的键值对值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public static void lPush(String key, String value) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.lpush(key, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

	}

	/**
	 * 查看队列数据
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static List lpoplist(String key, long start, long end) {
		List list = new ArrayList();
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			list = jedis.lrange(key, start, end);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return list;
	}

	public static long listLen(String key) {
		long len = 0;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			len = jedis.llen(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return len;
	}

	/**
	 * 删除list
	 * 
	 * @param key
	 * @param count
	 *            当count>0时，从表头开始查找，移除count个；当count=0时，从表头开始查找，移除所有等于value的；
	 *            当count<0时，从表尾开始查找，移除|count| 个
	 */
	public static void listDel(String key, int count, String value) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			if (count == 10)
				jedis.del(key);
			jedis.lrem(key, count, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 检查给定 key 是否存在。
	 * 
	 * @param key
	 */
	public static boolean exists(String key) {

		boolean flag = false;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			flag = jedis.exists(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return flag;
	}

	/**
	 * 检查给定 key对应的field 是否存在。
	 * 
	 * @param key
	 */
	public static boolean hExists(String key, String field) {

		boolean flag = false;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			flag = jedis.hexists(key, field);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return flag;
	}

	/**
	 * 查看map所有对象数据。
	 * 
	 * @param key
	 */
	public static Map<String, String> mapGetAll(String key) {

		Map<String, String> map = null;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			map = jedis.hgetAll(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return map;
	}

	/**
	 * 存放一个Map<String, String>到redis中
	 * 
	 * @param key
	 * @param map
	 */
	public static Boolean hmset(String key, Map<String, String> map) {
		JedisPool pool = null;
		Jedis jedis = null;
		Boolean bool = false;
		try {
			pool = getPool();
			jedis = pool.getResource();
			String rsult = jedis.hmset(key, map);
			if ("OK".equals(rsult)) {
				bool = true;
			}
			// map key的个数
			// System.out.println("map的key的个数" + jedis.hlen(key));
			// map key
			// System.out.println("map的key" + jedis.hkeys(key));
			// map value
			// System.out.println("map的value" + jedis.hvals(key));
		} catch (Exception e) {
			// 释放redis对象
			pool.close();
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return bool;
	}

	/**
	 * 仅仅通过key获取一个Map集合
	 * 
	 * @param key
	 * @return
	 */
	public static Map<String, String> hmget(String key) {
		Map<String, String> map = new HashMap<>();
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			// 获取map中的key
			Set<String> keySet = jedis.hkeys(key);
			for (String string : keySet) {
				// 获取value，还可以通过 jedis.hvals(key)方式获取
				List<String> valueLs = jedis.hmget(key, string);
				if (valueLs != null && valueLs.size() > 0) {
					map.put(string, valueLs.get(0));
				}
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.close();
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return map;
	}

	/**
	 * 通过resdis中的key和map中的一个或者多个key获取map中对应的值
	 * 
	 * @param key
	 * @param keys
	 * @return
	 */
	public static List<String> hmgetByKeys(String key, String... fields) {
		JedisPool pool = null;
		Jedis jedis = null;
		List<String> valueLs = new ArrayList<>();
		try {
			pool = getPool();
			jedis = pool.getResource();
			valueLs = jedis.hmget(key, fields);
		} catch (Exception e) {
			// 释放redis对象
			pool.close();
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return valueLs;
	}

	/**
	 * 删除Map中某个或多个key
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	public static Boolean hdel(String key, String... fields) {
		JedisPool pool = null;
		Jedis jedis = null;
		Boolean bool = false;
		try {
			pool = getPool();
			jedis = pool.getResource();
			Long l = jedis.hdel(key, fields);
			if (l > 0) {
				bool = true;
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.close();
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return bool;
	}

	/**
	 * 存储任何对象到redis中(包括List,Map等)
	 * @param key
	 * @param obj
	 * @return
	 */
	public static Boolean setObject(String key, Object obj) {
		JedisPool pool = null;
		Jedis jedis = null;
		Boolean bool = false;
		try {
			pool = getPool();
			jedis = pool.getResource();
			String result = jedis.set(key.getBytes(),
					SerializationUtil.serialize(obj));
			if("OK".equals(result)){
				bool = true;
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.close();
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return bool;
	}
	
	/**
	 * 存储任何对象到redis中(包括List,Map等)
	 * @param key
	 * @param obj
	 * @return
	 */
	public static Boolean setObject(String key, Object obj, int seconds) {
		JedisPool pool = null;
		Jedis jedis = null;
		Boolean bool = false;
		try {
			pool = getPool();
			jedis = pool.getResource();
			System.out.println(SerializationUtil.serialize(obj));
			String result = jedis.setex(key.getBytes(),seconds,
					SerializationUtil.serialize(obj));
			if("OK".equals(result)){
				bool = true;
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.close();
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return bool;
	}

	/**
	 * 获取某个对象
	 * @param key
	 * @return
	 */
	public static Object getObject(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		Object obj = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			byte[] bs = jedis.get(key.getBytes());
			obj = SerializationUtil.deserialize(bs);
		} catch (Exception e) {
			// 释放redis对象
			pool.close();
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return obj;
	}

	/**
	 * 存储成set 对象，对应的key中添加对应的键值对值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public static void zAdd(String key, double score, String value) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.zadd(key, score, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 给sort set 排序
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public static Set<String> zSort(String key, int start, int end) {
		JedisPool pool = null;
		Jedis jedis = null;
		Set<String> set = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			set = jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return set;
	}

	/**
	 * 给sort set 排序
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public static Double zScore(String key, String value) {
		JedisPool pool = null;
		Jedis jedis = null;
		Double score = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			score = jedis.zscore(key, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return score;
	}

}
