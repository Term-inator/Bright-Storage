package com.example.bright_storage.model.query;

import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.util.StringUtil;
import com.google.zxing.common.StringUtils;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.annotation.Column;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StorageUnitQuery extends BaseQuery<StorageUnit>{

    /**
     * 0 物品
     * 1 容器
     */
    private Integer type;

    private Long parentId;

    private String name;

    private Integer amount;

    /**
     * true: 向关系成员公开
     * false: 私有
     */
    private Boolean access;

    private Boolean deleted;

    /**
     * [0]：起始时间
     * [1]：截止时间
     * 只有截止时间时[0]传null
     */
    private Date[] expireTime;

    private String note;

    @Override
    public Selector<StorageUnit> toSelector(DbManager dbManager, Class<StorageUnit> c) {
        Selector<StorageUnit> selector = super.toSelector(dbManager, StorageUnit.class);
        if(type != null){
            selector.and("type", "=", type);
        }
        if(parentId != null){
            selector.and("parent_id", "=", parentId);
        }
        if(amount != null){
            selector.and("amount", "=", amount);
        }
        if(StringUtil.hasText(name)){
            selector.and("name", "like", "%"+name+"%");
        }
        if(access != null){
            selector.and("access","=", access);
        }
        if(deleted != null){
            selector.and("is_deleted","=", deleted);
        }
        between(selector, expireTime, "expire_time");
        if(StringUtil.hasText(note)){
            selector.and("note","like","%"+note+"%");
        }
        return selector;
    }
}
