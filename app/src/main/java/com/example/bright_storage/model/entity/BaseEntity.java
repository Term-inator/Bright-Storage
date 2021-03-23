package com.example.bright_storage.model.entity;

import org.xutils.db.annotation.Column;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    public void prePersist(){
        Date now = new Date();
        createTime = now;
        updateTime = now;
    }

    public void preUpdate(){
        updateTime = new Date();
    }
}
