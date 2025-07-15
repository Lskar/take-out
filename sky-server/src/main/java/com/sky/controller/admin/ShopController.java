package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
@Api(tags = "店铺操作接口")
public class ShopController {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result<Void> setStatus(@PathVariable Integer status){
        log.info("设置店铺状态：{}",status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set("SHOP_STATUS",status.toString());
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getStatus(){
        log.info("查询店铺状态");
        String status = redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("店铺状态：{}",status);
        return Result.success(Integer.parseInt(status));
    }

}
