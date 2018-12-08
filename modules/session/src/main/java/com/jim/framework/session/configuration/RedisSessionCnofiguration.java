package com.jim.framework.session.configuration;

import com.jim.framework.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.StringUtils.split;

/**
 * Created by celiang.hu on 2018-11-19.
 */
@EnableRedisHttpSession
public class RedisSessionCnofiguration {

    @Value("${jim.framework.session.redis.host:127.0.0.1}")
    private String host;

    @Value("${jim.framework.session.redis.port:6379}")
    private int port;

    @Value("${jim.framework.session.redis.password:}")
    private String password;

    @Value("${jim.framework.session.redis.database:0}")
    private int database;

    @Value("${jim.framework.session.redis.maxActive:8}")
    private int maxActive;

    @Value("${jim.framework.session.redis.maxIdle:8}")
    private int maxIdle;

    @Value("${jim.framework.session.redis.maxWait:-1}")
    private long maxWait;

    @Value("${jim.framework.session.redis.minIdle:0}")
    private int minIdle;

    @Value("${jim.framework.session.redis.hosts:}")
    private String hosts;

    @Value("${jim.framework.session.redis.hosts.type:sentinel}")
    private String hostsType;

    @Value("${jim.framework.session.redis.sentinel.master:masterName}")
    private String sentinelMaster;

    @Value("${jim.session.timeout:1800}")
    private int sessionTimeout;

    public static final String CLUSTER = "cluster";
    public static final String SENTINEL = "sentinel";
    private static final int DEFAULT_CLUSTER_REDIRECTS = 3;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxWaitMillis(maxWait);

        JedisConnectionFactory connectionFactory = null;
        // 优先使用集群的配置
        if (StringUtils.hasText(hosts)) {
            ArrayList<RedisNode> redisNodes = parseRedisNodes(hosts);
            Assert.notEmpty(redisNodes, "Invalid property configuration [jim.framework.session.redis.hosts]");

            if (SENTINEL.equalsIgnoreCase(hostsType)) {
                RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
                sentinelConfiguration.setSentinels(redisNodes);
                sentinelConfiguration.setMaster(sentinelMaster);
                connectionFactory = new JedisConnectionFactory(sentinelConfiguration);
            }
            if(CLUSTER.equalsIgnoreCase(hostsType)){
                RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
                clusterConfiguration.setClusterNodes(redisNodes);
                clusterConfiguration.setMaxRedirects(DEFAULT_CLUSTER_REDIRECTS);
                connectionFactory = new JedisConnectionFactory(clusterConfiguration);
            }
        }
        if(connectionFactory == null){
            connectionFactory = new JedisConnectionFactory();
            connectionFactory.setHostName(host);
            connectionFactory.setPort(port);
        }
        connectionFactory.setPassword(password);
        connectionFactory.setDatabase(database);
        connectionFactory.setPoolConfig(jedisPoolConfig);

        return connectionFactory;
    }



    private ArrayList<RedisNode> parseRedisNodes(String hosts) {
        ArrayList<RedisNode> ret = new ArrayList<RedisNode>();

        for (String part : hosts.split(";")) {
            ret.add(readHostAndPortFromString(part));
        }
        return ret;
    }

    private RedisNode readHostAndPortFromString(String hostAndPort) {

        String[] args = split(hostAndPort, ":");

        notNull(args, "HostAndPort need to be seperated by  ':'.");
        isTrue(args.length == 2, "Host and Port String needs to specified as host:port");
        return new RedisNode(args[0], Integer.valueOf(args[1]).intValue());
    }

    /**
     * 在Spring session已经注册了相关的Bean，使用注入一个新的configurer的方式是为了动态修改timeout的配置，
     * 有些绕有更好的方式吗？
     * @param redisOperationsSessionRepository
     * @return
     */
    @Bean
    public SessionRepositoryConfigurer sessionRepositoryConfigurer(RedisOperationsSessionRepository redisOperationsSessionRepository) {
        return new SessionRepositoryConfigurer(redisOperationsSessionRepository, sessionTimeout).config();
    }

    public static class SessionRepositoryConfigurer {
        private RedisOperationsSessionRepository redisOperationsSessionRepository;

        private int sessionTimeout;

        public SessionRepositoryConfigurer(RedisOperationsSessionRepository redisOperationsSessionRepository, int sessionTimeout) {
            this.redisOperationsSessionRepository = redisOperationsSessionRepository;
            this.sessionTimeout = sessionTimeout;
        }

        SessionRepositoryConfigurer config() {
            this.redisOperationsSessionRepository.setDefaultMaxInactiveInterval(this.sessionTimeout);
            return this;
        }
    }


}
