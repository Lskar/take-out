package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {


    @Autowired
    private SetmealService setmealService;


    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result<Void> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult<SetmealVO>> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult<SetmealVO> pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result<Void> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }


    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<Void> delete(@RequestParam List<Long> ids) {
        log.info("批量删除套餐：{}", ids);
        setmealService.delete(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result<Void> startOrStop(@PathVariable Integer status, @RequestParam Long id) {
        log.info("套餐起售、停售：{}", id);
        setmealService.startOrStop(status,id);
        return Result.success();
    }
}
