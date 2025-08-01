package com.sky.controller.user;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "C端-店铺操作接口")
@Slf4j
public class ShopController {





    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getStatus(){
        log.info("查询店铺状态");
        String status = redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("店铺状态：{}",status);
        return Result.success(Integer.parseInt(status));
    }
}
