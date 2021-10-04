package com.example.bright_storage.service;

import com.example.bright_storage.model.dto.RelationDTO;
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

    /**
     * 更新关系
     * @param relationDTO
     * @return /
     */
    BaseResponse<Object> updateRelation(RelationDTO relationDTO);

    /**
     * 删除关系
     * @param id 关系ID
     * @return /
     */
    BaseResponse<Object> deleteRelation(Long id);

    /**
     * 获取关系邀请码uuid
     * @param id 关系id
     * @return uuid
     */
    String getInviteCode(Long id);

    /**
     * 获取新的邀请码uuid
     * @param id 关系id
     * @return uuid
     */
    String getNewInviteCode(Long id);

    /**
     * 通过邀请码加入关系
     * @param uuid 邀请码
     * @return /
     */
    BaseResponse<Object> joinRelation(String uuid);

    /**
     * 获得关系详细信息
     * @param id 关系id
     * @return
     */
    RelationDTO getRelationById(Long id);

    /**
     * 列出当前用户加入的关系
     * @return 关系
     */
    List<RelationDTO> listByCurrentUser();

    /**
     * 获取关系下的成员
     * @param id 关系id
     * @return
     */
    List<UserVO> listMembersByRelationId(Long id);


}
