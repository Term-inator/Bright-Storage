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
@Table(name = "storage_unit_category")
public class StorageUnitCategory extends BaseEntity{

    @Column(name = "storage_unit_category_id", isId = true, autoGen = true)
    private Long id;

    @Column(name = "storage_unit_id")
    private Long storageUnitId;

    @Column(name = "category_id")
    private Long categoryId;
}
