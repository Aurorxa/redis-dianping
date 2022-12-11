package com.github.redis.web;

import com.github.redis.rest.Result;
import com.github.redis.service.ShopService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-12-10 22:07:16
 */
@Slf4j
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    @NonNull
    private ShopService shopService;

    @GetMapping("/{id}")
    public Result view(@PathVariable("id") Long id) {
        return this.shopService.view(id);
    }
    
}
