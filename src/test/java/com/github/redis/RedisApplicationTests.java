package com.github.redis;

import com.github.redis.service.impl.ShopServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisApplicationTests {

    @Autowired
    private ShopServiceImpl shopServiceImpl;

    @Test
    public void test() {
        this.shopServiceImpl.saveShop2Redis(1L, 50L);
    }

}
