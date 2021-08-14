package com.example.bright_storage.model.dto;

import com.example.bright_storage.model.vo.UserVO;

import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class StorageUnitDTO {

    private Long id;

    private UserVO owner;

    private Integer type;

    private String name;

    private Integer amount;

    private Long parentId;

    private Boolean access;

    private String image;

    private Boolean deleted;

    private Date expireTime;

    private String note;

    private Long childCount;

    private Set<CategoryDTO> categories;

    private List<StorageUnitDTO> children;
}
