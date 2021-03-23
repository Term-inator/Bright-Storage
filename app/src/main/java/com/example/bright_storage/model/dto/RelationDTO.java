package com.example.bright_storage.model.dto;


import com.example.bright_storage.model.vo.UserVO;

import lombok.Data;

@Data
public class RelationDTO {

    private Long id;

    private String name;

    private String avatar;

    private UserVO owner;

}
