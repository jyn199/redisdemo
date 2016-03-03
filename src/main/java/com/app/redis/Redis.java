package com.app.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class Redis {

	@Autowired
    public JedisPool jedisPool;
	
    private final static Logger logger = LoggerFactory.getLogger(Redis.class);

    public static abstract class JedisTask {
        protected abstract void doExecution(Jedis jedis);
    }

    public static abstract class JedisResultTask<T> {
        protected abstract T doExecution(Jedis jedis);
    }

    protected void execution(JedisTask jedisTask) {
    	
        Jedis jedis = null;
        try {
        	jedis=jedisPool.getResource();
            jedisTask.doExecution(jedis);
        } finally {
        	if(jedis!=null){
        		 jedis.close();
        	}
        }
    }

    protected <T> T execution(JedisResultTask<T> task) {
        Jedis jedis = null;
        try {
        	jedis=jedisPool.getResource();
            return task.doExecution(jedis);
        } finally {
        	if(jedis!=null){
        		jedis.close();
        	}
        }
    }


}