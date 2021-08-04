package com.example.bright_storage.model.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 存储单位的访问记录，可用推荐
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "access_log")
@EqualsAndHashCode(of = "id", callSuper = false)
public class AccessLog extends BaseEntity{

    /**
     * 物品信息被修改(包括被创建以及移动等等)
     */
    public static final int MODIFIED = 0;

    /**
     * 1 查看详细信息
     */
    public static final int VISITED = 1;

    /**
     * 2 作为容器被放入物品
     */
    public static final int MOVED_IN = 2;

    @Column(name = "access_log_id", isId = true, autoGen = true)
    private Long id;

    @Column(name = "storage_unit_id")
    private Long storageUnitId;

    @Column(name = "access_type")
    private Integer accessType;

    public static AccessLog storageUnitModified(Long storageUnitId){
        return new AccessLog(null, storageUnitId, MODIFIED);
    }

    public static AccessLog storageUnitVisited(Long storageUnitId){
        return new AccessLog(null, storageUnitId, VISITED);
    }

    public static AccessLog storageUnitMovedIn(Long storageUnitId){
        return new AccessLog(null, storageUnitId, MOVED_IN);
    }
}
