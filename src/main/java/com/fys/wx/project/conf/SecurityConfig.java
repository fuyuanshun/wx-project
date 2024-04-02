package com.fys.wx.project.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fys.wx.project.filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author fys
 * @date 2024/4/1
 * @description
 */
@Configuration
public class SecurityConfig {

    /**
     * security的过滤器链
     * @param http
     * @param redisTemplate
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RedisTemplate<String, Object> redisTemplate) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement((session)->
                session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/user/login").anonymous()
                    .anyRequest()
                    .authenticated()
            );

        //自定义未认证请求响应
//        http.exceptionHandling((e)-> e.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        //在验证之前先执行过滤器校验token
        http.addFilterBefore(new TokenFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * redis配置类
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //json序列化配置
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        //String序列化配置
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //key采用String的序列化配置、value采用json的序列化配置
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(serializer);

        //key采用String的序列化配置、value采用json的序列化配置
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * security基于数据库认证
     * @return
     */
    @Bean
    public UserDetailsService dbUserDetailsManager(){
        return new DBUserDetailsManager();
    }

    /**
     *
     * @param userDetailsService
     * @param passwordEncoder
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    /**
     * security密码加密配置类
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
