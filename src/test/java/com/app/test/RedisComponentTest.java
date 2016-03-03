package com.app.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.app.redis.RedisComponent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:applicationSpringApp.xml"})
public class RedisComponentTest {
	
	@Autowired
	RedisComponent redisComponent;
	
	@Test
	public void testSet() throws Exception{
		String res = redisComponent.set("a", "c");
		Assert.assertArrayEquals(new Object[]{res}, new Object[]{"OK"});
		Assert.assertTrue("set方法返回不是OK", "OK".equals(res));
	}
}
