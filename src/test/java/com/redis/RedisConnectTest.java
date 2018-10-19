package com.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: ZiJie.Yip
 * @Description:testJedisSingle
 * @date: 2018/10/19 14:19
 */
public class RedisConnectTest {

    private JedisCluster jedisCluster = null;

    private static final String REDIS_CLUSTER_PASSWORD = "";

    @Before
    public void before(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10000);
        jedisPoolConfig.setMaxTotal(50000);


        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("127.0.0.1",7001));
        nodes.add(new HostAndPort("127.0.0.1",7002));
        nodes.add(new HostAndPort("127.0.0.1",7003));
        nodes.add(new HostAndPort("127.0.0.1",7004));
        nodes.add(new HostAndPort("127.0.0.1",7005));
        nodes.add(new HostAndPort("127.0.0.1",7006));
        jedisCluster = new JedisCluster(nodes,20000,600,100,REDIS_CLUSTER_PASSWORD,jedisPoolConfig);
    }

    @Test
    public void testJedisSingle(){
        //创建Jedis对象
        Jedis jedis = new Jedis("127.0.0.1",7001);
        jedis.set("name","ZiJie-Yip");
        String name = jedis.get("name");
        System.out.println("name:" + name);

        //关闭Jedis
        jedis.close();
    }

    @Test
    public void testJedisCluster(){

    }

    @Test
    public void testJedisClusterListMakeQueue(){
        jedisCluster.del("books");
        jedisCluster.rpush("books","Think in Java");
        jedisCluster.rpush("books","Java Concurrent");
        jedisCluster.rpush("books","C++");
        System.out.println(jedisCluster.llen("books"));
        System.out.println(jedisCluster.lrange("books",0,-1));

        System.out.println(jedisCluster.lpop("books"));
        System.out.println(jedisCluster.lpop("books"));
        System.out.println(jedisCluster.lpop("books"));
        System.out.println(jedisCluster.llen("books"));
        System.out.println(jedisCluster.lrange("books",0,-1));
    }

    @Test
    public void testJedisClusterListMakeStack(){
        jedisCluster.del("books");
        jedisCluster.rpush("books","Think in Java");
        jedisCluster.rpush("books","Java Concurrent");
        jedisCluster.rpush("books","C++");
        System.out.println(jedisCluster.llen("books"));
        System.out.println(jedisCluster.lrange("books",0,-1));

        System.out.println(jedisCluster.rpop("books"));
        System.out.println(jedisCluster.rpop("books"));
        System.out.println(jedisCluster.rpop("books"));
        System.out.println(jedisCluster.llen("books"));
        System.out.println(jedisCluster.lrange("books",0,-1));
    }

    @Test
    public void testJedisClusterHash(){
        jedisCluster.del("books");
        jedisCluster.hset("books","java","Think in Java");
        jedisCluster.hset("books","golang","go");
        jedisCluster.hset("books","python","python cookbook");

        System.out.println(jedisCluster.hget("books","java"));
        System.out.println(jedisCluster.hget("books","golang"));
        System.out.println(jedisCluster.hget("books","python"));

        System.out.println(jedisCluster.hgetAll("books"));
    }

    @Test
    public void testJedisClusterSet(){
        jedisCluster.del("books");
        jedisCluster.sadd("books","java");
        jedisCluster.sadd("books","golang");
        jedisCluster.sadd("books","python");
        jedisCluster.sadd("books","java");

        System.out.println(jedisCluster.smembers("books"));

        System.out.println(jedisCluster.sismember("books","java"));
        System.out.println(jedisCluster.sismember("books","javaa"));
        System.out.println(jedisCluster.scard("books"));
        System.out.println(jedisCluster.spop("books"));
    }

    @Test
    public void testJedisClusterZSet(){
        jedisCluster.del("books");
        jedisCluster.zadd("books",10d,"Java");
        jedisCluster.zadd("books",8d,"GoLang");
        jedisCluster.zadd("books",9d,"Python");

        System.out.println(jedisCluster.zrange("books",0,-1));
        System.out.println(jedisCluster.zrevrange("books",0,-1));
        System.out.println(jedisCluster.zcard("books"));
        System.out.println(jedisCluster.zscore("books","Java"));
        System.out.println(jedisCluster.zrevrank("books","Java"));

        System.out.println(jedisCluster.zrangeByScore("books",9.1,10));
        System.out.println(jedisCluster.zrem("books","Python"));
        System.out.println(jedisCluster.zrange("books",0,-1));
    }

    @After
    public void closeCluster() throws IOException {
        System.out.println("关闭JedisCluster连接");
        jedisCluster.close();
    }

}
