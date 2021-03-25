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
@EqualsAndHashCode(of = "localId", callSuper = false)
@Table(name = "category")
public class Category extends OwnershipEntity {

    @Column(name = "category_id", isId = true, autoGen = true)
    private Long localId;

    @Column(name = "remote_id")
    private Long id;

    @Column(name = "name")
    private String name;
}
