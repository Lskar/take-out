package com.sky.service;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {


    SetmealVO getByIdWithDish(Long id);


    void save(SetmealDTO setmealDTO);

    PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void update(SetmealDTO setmealDTO);

    void delete(List<Long> ids);

    void startOrStop(Integer status, Long id);
}
