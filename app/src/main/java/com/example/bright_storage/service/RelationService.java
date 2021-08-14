package com.example.bright_storage.service;

import com.example.bright_storage.model.dto.RelationDTO;
import com.example.bright_storage.model.entity.Relation;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.model.vo.UserVO;

import java.util.List;

public interface RelationService {

    /**
     * 创建关系<br/>
     * 请使用返回的DTO，其中包含创建关系的ID
     * @param relationDTO /
     * @return 包含ID的RelationDTO
     */
    RelationDTO createRelation(RelationDTO relationDTO);

    BaseResponse<Object> updateRelation(RelationDTO relationDTO);

    BaseResponse<Object> deleteRelation(Long id);

    /**
     * 获取关系邀请码uuid
     * @param id 关系id
     * @return uuid
     */
    String getInviteCode(Long id);

    BaseResponse<Object> joinRelation(String uuid);

    RelationDTO getRelationById(Long id);

    List<RelationDTO> listByCurrentUser();

    List<UserVO> listMembersByRelationId(Long id);


}
