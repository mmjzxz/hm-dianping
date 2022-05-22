package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeList() {
        String shopTypeList = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_TYPE_KEY + "list");
        if (StrUtil.isNotBlank(shopTypeList)) {
            List<ShopType> shopTypes = JSONUtil.toList(shopTypeList, ShopType.class);
            return Result.ok(shopTypes);
        }

        List<ShopType> shopTypes = this.list();
        if (shopTypes == null || shopTypes.size() == 0) {
            return Result.fail("查询失败");
        }
        stringRedisTemplate.opsForValue().set(RedisConstants.CACHE_SHOP_TYPE_KEY + "list", JSONUtil.toJsonStr(shopTypes));
        return Result.ok(shopTypes);
    }
}
