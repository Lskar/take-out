package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;


    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        // 查询当前菜品或套餐是否在购物车中

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list != null && !list.isEmpty()) {
            // 已经存在，则更新数量
            ShoppingCart cartService = list.get(0);
            cartService.setNumber(cartService.getNumber() + 1);
            shoppingCartMapper.update(cartService);
        } else {

            Long dishId = shoppingCart.getDishId();
            Long setmealId = shoppingCart.getSetmealId();
            if (dishId != null) {
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                Setmeal setmeal = setmealMapper.getByIdWithDish(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }
    @Override
    public List<ShoppingCart> showShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO){
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list != null && !list.isEmpty()) {
            ShoppingCart cartService = list.get(0);
            if (cartService.getNumber() == 1) {
                shoppingCartMapper.delete(cartService.getId());
            }
            else {
                cartService.setNumber(cartService.getNumber() - 1);
                shoppingCartMapper.update(cartService);
            }
        }
        else {
            throw new RuntimeException("购物车为空");
        }
    }

    @Override
    public void cleanShppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
        log.info("清空购物车成功");
    }
}
