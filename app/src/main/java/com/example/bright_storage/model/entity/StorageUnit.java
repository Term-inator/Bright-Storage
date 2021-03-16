package com.example.bright_storage.model.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "storage_unit")
public class StorageUnit extends BaseEntity {

    @Column(name = "storage_unit_id", isId = true, autoGen = true)
    private Long id;

    @Column(name = "remote_id")
    private Long remoteId;

    @Column(name = "name")
    private String name;
}
