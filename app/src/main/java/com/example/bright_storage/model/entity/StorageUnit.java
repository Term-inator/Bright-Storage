package com.example.bright_storage.model.entity;

import org.jetbrains.annotations.NotNull;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "localId", callSuper = false)
@Table(name = "storage_unit")
public class StorageUnit extends OwnershipEntity {

    @Column(name = "storage_unit_id", isId = true, autoGen = true)
    private Long localId;

    @Column(name = "remote_id")
    private Long id;

    /**
     * 0: 物品
     * 1: 容器
     */
    @Column(name = "type")
    private Integer type;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "local_parent_id")
    private Long localParentId;

    @Column(name = "remote_parent_id")
    private Long parentId;

    /**
     * true: 向关系成员公开
     * false: 私有
     */
    @Column(name = "access")
    private Boolean access;

    @Column(name = "image")
    private String image;

    @Column(name = "is_deleted")
    private Boolean deleted;

    @Column(name = "expire_time")
    private Date expireTime;

    @Column(name = "note")
    private String note;

    @Column(name = "last_access_time")
    private Date lastAccessTime;

    private Set<Category> categories;

    public void setParent(@NotNull StorageUnit storageUnit){
        this.localParentId = storageUnit.getLocalId();
        this.parentId = storageUnit.getId();
    }

    @Override
    public void prePersist() {
        super.prePersist();
        if(type == null){
            type = 0;
        }
        if(name == null){
            name = "";
        }
        if(amount == null){
            amount = 1;
        }
        if(localParentId == null){
            localParentId = 0L;
        }
        if(access == null){
            access = false;
        }
        if(image == null){
            image = "";
        }
        if(deleted == null){
            deleted = false;
        }
        if(note == null){
            note = "";
        }
        if(lastAccessTime == null){
            lastAccessTime = new Date();
        }
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        lastAccessTime = new Date();
    }
}
