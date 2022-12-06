package com.bluewind.base.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author liuxingyu01
 * @date 2021-09-13-12:46
 * @description redis配置类
 **/
@Configuration
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    private static final String REDIS_URL_SEPARATOR = ";";

    private static final String HOST_PORT_SEPARATOR = ":";

    @Value("${redis.type}") // 连接类型，standalone单机，cluster集群，sentinel哨兵
    private String redisType;
    @Value("${redis.url}")
    private String redisUrl;
    @Value("${redis.redisDbIndex}")
    private int redisDbIndex;
    @Value("${redis.password}")
    private String redisPassword;
    @Value("${redis.timeout}")
    private long timeout;
    @Value("${lettuce.pool.max-active}") // 最大连接数
    private int maxActive;
    @Value("${lettuce.pool.max-idle}") // 最大空闲数
    private int maxIdle;
    @Value("${lettuce.pool.min-idle}") // 最小空闲数
    private int minIdle;
    @Value("${lettuce.pool.max-wait}") // 连接池等待时间
    private long maxWait;
    @Value("${lettuce.pool.test-on-borrow}") // 连接池等待时间
    private boolean testOnBorrow;
    @Value("${lettuce.pool.test-while-idle}") // 连接池等待时间
    private boolean testWhileIdle;
    @Value("${lettuce.pool.shutdown-timeout}") // 关闭超时
    private long shutdownTimeout;


    /**
     * 单机模式（测试好用）
     * 适用：普通测试，少量数据缓存
     *
     * @return LettuceConnectionFactory
     */
    public LettuceConnectionFactory lettuceConnectionFactory() {
        if (logger.isInfoEnabled()) {
            logger.info("lettuceConnectionFactory -- the redisUrl is {}", redisUrl);
        }
        if (StringUtils.isBlank(redisUrl)) {
            throw new IllegalStateException("the urls of redis is not configured");
        }

        String[] urls = StringUtils.split(redisUrl, HOST_PORT_SEPARATOR);
        String hostName = urls[0];
        int port = Integer.parseInt(urls[1]);

        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(hostName, port);
        redisConfiguration.setPassword(redisPassword);
        redisConfiguration.setDatabase(redisDbIndex);

        // redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        builder.commandTimeout(Duration.ofMillis(timeout));
        builder.shutdownTimeout(Duration.ofMillis(shutdownTimeout));
        // 配置连接池
        builder.poolConfig(poolConfig());

        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        // 根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();

        return lettuceConnectionFactory;
    }


    /**
     * Cluster集群模式（测试好用）
     * 适用：针对海量数据+高并发+高可用的场景，解决单机Redis容量有限的问题，将Redis的数据根据一定的规则分配到多台机器
     * 一般集群建议搭建三主三从架构，三主提供服务，三从提供备份功能
     *
     * @return LettuceConnectionFactory
     */
    public LettuceConnectionFactory lettuceClusterConnectionFactory() {
        if (logger.isInfoEnabled()) {
            logger.info("lettuceClusterConnectionFactory -- the redisUrl is {}", redisUrl);
        }
        if (StringUtils.isBlank(redisUrl)) {
            throw new IllegalStateException("the urls of redis is not configured");
        }

        RedisClusterConfiguration redisConfiguration = new RedisClusterConfiguration();
        redisConfiguration.setPassword(redisPassword);

        if (StringUtils.split(redisUrl, REDIS_URL_SEPARATOR).length > 1) {
            for (String item : StringUtils.split(redisUrl, REDIS_URL_SEPARATOR)) {
                String[] urls = StringUtils.split(item, HOST_PORT_SEPARATOR);
                String host = urls[0];
                int port = Integer.parseInt(urls[1]);

                RedisNode redisNode = new RedisNode(host, port);
                redisConfiguration.addClusterNode(redisNode);
            }
        }

        // redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        builder.commandTimeout(Duration.ofMillis(timeout));
        builder.shutdownTimeout(Duration.ofMillis(shutdownTimeout));
        // 配置连接池
        builder.poolConfig(poolConfig());

        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        // 根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);
        lettuceConnectionFactory.setDatabase(redisDbIndex);

        lettuceConnectionFactory.afterPropertiesSet();

        return lettuceConnectionFactory;
    }


    /**
     * Sentinel哨兵模式（暂未测试）
     * 适用：Redis的高可用性解决方案，主从架构，选举模式
     * 当sentinel发现master节点挂了以后，sentinel就会从slave中重新选举一个master
     *
     * @return LettuceConnectionFactory
     */
    public LettuceConnectionFactory lettuceSentinelConnectionFactory() {
        if (logger.isInfoEnabled()) {
            logger.info("lettuceSentinelConnectionFactory -- the redisUrl is {}", redisUrl);
        }
        if (StringUtils.isBlank(redisUrl)) {
            throw new IllegalStateException("the urls of redis is not configured");
        }

        RedisSentinelConfiguration redisConfiguration = new RedisSentinelConfiguration();
        // 此处暂时写死，也可从配置文件中取
        redisConfiguration.setMaster("master-1");
        redisConfiguration.setPassword(redisPassword);
        redisConfiguration.setDatabase(redisDbIndex);

        if (StringUtils.split(redisUrl, REDIS_URL_SEPARATOR).length > 1) {
            for (String item : StringUtils.split(redisUrl, REDIS_URL_SEPARATOR)) {
                String[] urls = StringUtils.split(item, HOST_PORT_SEPARATOR);
                String host = urls[0];
                int port = Integer.parseInt(urls[1]);

                RedisNode redisNode = new RedisNode(host, port);
                redisConfiguration.addSentinel(redisNode);
            }
        }

        // redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        builder.commandTimeout(Duration.ofMillis(timeout));
        builder.shutdownTimeout(Duration.ofMillis(shutdownTimeout));
        // 配置连接池
        builder.poolConfig(poolConfig());

        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        // 根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);

        lettuceConnectionFactory.afterPropertiesSet();

        return lettuceConnectionFactory;
    }


    /**
     * 配置连接池参数
     *
     * @return
     */
    private GenericObjectPoolConfig poolConfig() {
        // 连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);
        genericObjectPoolConfig.setTestOnBorrow(testOnBorrow);
        genericObjectPoolConfig.setTestWhileIdle(testWhileIdle);
        return genericObjectPoolConfig;
    }


    /**
     * RedisTemplate配置
     */
    @Bean
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public RedisTemplate<String, Object> redisTemplate() {
        // 设置序列化
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer valueSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();

        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        valueSerializer.setObjectMapper(om);

        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();

        // 根据配置，加载补不同的ConnectionFactory
        if (StringUtils.isNotBlank(redisType) && "cluster".equals(redisType)) {
            redisTemplate.setConnectionFactory(lettuceClusterConnectionFactory());
        } else if (StringUtils.isNotBlank(redisType) && "sentinel".equals(redisType)) {
            redisTemplate.setConnectionFactory(lettuceSentinelConnectionFactory());
        } else {
            redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        }

        redisTemplate.setKeySerializer(stringSerializer);// key序列化
        redisTemplate.setValueSerializer(valueSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(valueSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}
