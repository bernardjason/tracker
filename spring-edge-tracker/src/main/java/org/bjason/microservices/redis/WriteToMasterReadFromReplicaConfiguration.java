package org.bjason.microservices.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.logging.Logger;


@Configuration
public class WriteToMasterReadFromReplicaConfiguration {


    protected Logger logger = Logger.getLogger(WriteToMasterReadFromReplicaConfiguration.class
            .getName());

    @Value("${redis.host}")
    private String redishost;
    @Value("${redis.port}")
    private String redisport;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory
                = new JedisConnectionFactory();

        logger.info("Redis host="+redishost+" port="+redisport);
        jedisConFactory.setHostName(redishost);
        jedisConFactory.setPort(Integer.parseInt(redisport));

        return jedisConFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }


}
