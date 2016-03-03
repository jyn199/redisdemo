package com.app.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONObject;

@Component
public class RedisComponent extends Redis {

	/**
	 * 保存对象，转为json存储，永不过期
	 * 
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public String set(final String key, final Object value) throws Exception {
		return execution(new JedisResultTask<String>() {
			@Override
			protected String doExecution(Jedis jedis) {
				return jedis.set(key, JSONObject.toJSONString(value));
			}
		});
	}

	/**
	 * 保存字符串，永不过期
	 * 
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public String set(final String key, final String value) throws Exception {
		return execution(new JedisResultTask<String>() {
			@Override
			protected String doExecution(Jedis jedis) {
				return jedis.set(key, value);
			}
		});
	}
	
	/**
	 * 将 key 的值设为 value ，当且仅当 key 不存在。
	 * @param key
	 * @param value
	 * @return
	 */
	public int setnx(final String key, final String value) {
		return execution(new JedisResultTask<Integer>() {
			@Override
			protected Integer doExecution(Jedis jedis) {
				return jedis.setnx(key, value).intValue();
			}
		});
	}

	/**
	 * 保存带过期时间的字符串
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 *            过期时间（默认单位为秒）
	 * @throws Exception
	 */
	public String set(final String key, final String value, final int expireTime)
			throws Exception {
		return set(key, value, expireTime, TimeUnit.SECONDS);
	}
	
	//保存对象（转为json），带过期时间
	public String set(final String key, final Object value, final int expireTime)
			throws Exception {
		return set(key, JSONObject.toJSONString(value), expireTime);
	}

	public String set(final String key, final String value,
			final int expireTime, final TimeUnit timeUnit) throws Exception {
		return execution(new JedisResultTask<String>() {
			@Override
			protected String doExecution(Jedis jedis) {
				return jedis.setex(key, getExpireTime(expireTime, timeUnit),
						value);
			}
		});
	}

	public String set(final String key, final Object value,
			final int expireTime, final TimeUnit timeUnit) throws Exception {
		return set(key, JSONObject.toJSONString(value), expireTime,
				timeUnit);
	}

	private int getExpireTime(int expireTime, TimeUnit timeUnit) {
		int eTime = expireTime;
		if (timeUnit == TimeUnit.DAYS) {
			eTime = expireTime * 24 * 3600;
		} else if (timeUnit == TimeUnit.HOURS) {
			eTime = expireTime * 3600;
		} else if (timeUnit == TimeUnit.MINUTES) {
			eTime = expireTime * 60;
		}
		return eTime;
	}


	/**
	 * 根据key clazz 返回对象
	 * 
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public <T> T get(final String key, final Class<T> clazz) throws Exception {
		return execution(new JedisResultTask<T>() {
			@Override
			protected T doExecution(Jedis jedis) {
				String json = jedis.get(key);
				if (StringUtils.isNotBlank(json)) {
					return JSONObject.parseObject(json, clazz);
				} else {
					return null;
				}
			}
		});
	}

	/**
	 * 根据key clazz isList=true 返回Lit<对象>
	 * 
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> get(final String key, final Class<T> clazz,
			final boolean isList) throws Exception {
		return execution(new JedisResultTask<List<T>>() {
			@Override
			protected List<T> doExecution(Jedis jedis) {
				String json = jedis.get(key);
				if (StringUtils.isNotBlank(json)) {
					if (isList) {
						return JSONObject.parseArray(json, clazz);
					}
				}
				return null;
			}
		});
	}

	public String get(final String key) throws Exception {
		return execution(new JedisResultTask<String>() {
			@Override
			protected String doExecution(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}

	/**
	 * 根据key删除数据
	 * 
	 * @param key
	 * @throws Exception
	 */
	public long delete(final String... keys) throws Exception {
		return execution(new JedisResultTask<Long>() {
			@Override
			protected Long doExecution(Jedis jedis) {
				return jedis.del(keys);
			}
		});
	}

	/**
	 * 通过key获取过期时间
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public long getExpireTime(final String key) throws Exception {
		return execution(new JedisResultTask<Long>() {
			@Override
			protected Long doExecution(Jedis jedis) {
				return jedis.ttl(key);
			}
		});
	}

	/**
	 * 根据key自增value（默认+1）
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public long increment(final String key) throws Exception {
		return execution(new JedisResultTask<Long>() {
			@Override
			protected Long doExecution(Jedis jedis) {
				return jedis.incr(key);
			}
		});
	}

	/**
	 * 根据key自增value
	 * 
	 * @param key
	 * @param inc
	 * @return
	 * @throws Exception
	 */
	public long increment(final String key, final Long increment)
			throws Exception {
		return execution(new JedisResultTask<Long>() {
			@Override
			protected Long doExecution(Jedis jedis) {
				return jedis.incrBy(key, increment);
			}
		});
	}

	/**
	 * 设置过期时间
	 * 
	 * @param key
	 * @param expireTime
	 * @param unit
	 * @throws Exception
	 */
	public Long setExpire(final String key, final int seconds) throws Exception {
		return execution(new JedisResultTask<Long>() {
			@Override
			protected Long doExecution(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
	}
    
	/**
	 * 将一个或多个值 value 插入到列表 key 的表头
	 * @param key
	 * @param value
	 */
	public void lpush(final String key, final String value) {
		execution(new JedisTask() {
			@Override
			protected void doExecution(Jedis jedis) {
				jedis.lpush(key, value);
			}
		});
	}
	
    /**
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     * @param key
     * @param value
     * @return
     */
	public Long rpush(final String key, final String... value) {
		return execution(new JedisResultTask<Long>() {
			@Override
			protected Long doExecution(Jedis jedis) {
				return jedis.rpush(key, value);
			}
		});
	}

	/**
	 * 移除并返回列表 key 的尾元素。
	 * 
	 * @param key
	 * @return
	 */
	public String rpop(final String key) {
		return execution(new JedisResultTask<String>() {
			@Override
			protected String doExecution(Jedis jedis) {
				return jedis.rpop(key);
			}
		});
	}

	

	public Set<String> keys(final String pattern) {
		return execution(new JedisResultTask<Set<String>>() {
			@Override
			protected Set<String> doExecution(Jedis jedis) {
				return jedis.keys(pattern);
			}
		});
	}

	public Long zadd(final String key, final double score, final String member) {
		return execution(new JedisResultTask<Long>() {
			@Override
			protected Long doExecution(Jedis jedis) {
				return jedis.zadd(key, score, member);
			}
		});
	}

	/**
	 * start从0开始
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrange(final String key, final long start, final long end) {
		return execution(new JedisResultTask<Set<String>>() {
			@Override
			protected Set<String> doExecution(Jedis jedis) {
				return jedis.zrange(key, start, end);
			}
		});
	}

	/**
	 * 从map中取指定字段的值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(final String key, final String field) {
		return execution(new JedisResultTask<String>() {
			@Override
			protected String doExecution(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});
	}

	/**
	 * 批量取值：从map中取 多个字段的值
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<String> hmget(final String key, final String... fields) {
		return execution(new JedisResultTask<List<String>>() {
			@Override
			protected List<String> doExecution(Jedis jedis) {
				return jedis.hmget(key, fields);
			}
		});
	}

	/**
	 * 向map中存值
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hset(final String key, final String field, final String value) {
		return execution(new JedisResultTask<Long>() {
			@Override
			protected Long doExecution(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		});
	}

	/**
	 * 批量向map中存值
	 * 
	 * @param key
	 * @param hash
	 * @return
	 */
	public String hmset(final String key, final Map<String, String> hash) {
		return execution(new JedisResultTask<String>() {
			@Override
			protected String doExecution(Jedis jedis) {
				return jedis.hmset(key, hash);
			}
		});
	}

	/**
	 * map中是否存在某个字段
	 * 
	 * @param key
	 * @param hash
	 * @return
	 */
	public Boolean hmset(final String key, final String field) {
		return execution(new JedisResultTask<Boolean>() {
			@Override
			protected Boolean doExecution(Jedis jedis) {
				return jedis.hexists(key, field);
			}
		});
	}

	/**
	 * 取出map所有值
	 * 
	 * @param key
	 * @param fields
	 * @return
	 */
	public Map<String, String> hgetAll(final String key, final String... fields) {
		return execution(new JedisResultTask<Map<String, String>>() {
			@Override
			protected Map<String, String> doExecution(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		});
	}

}
