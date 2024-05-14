package com.w2m.spacecraft.infrastructure.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachingConfig {

    public static final String NAME_SPACECRAFTS_CACHE = "spacecrafts-cache";

    public static final String KEY_ID = "#id";
    public static final String KEY_NAME_PAGE_SIZE = "{#name,#page,#size}";
    public static final String KEY_PAGE_SIZE = "{#page,#size}";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(NAME_SPACECRAFTS_CACHE);
    }
}
