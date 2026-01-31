package com.aeye.mifss.bio.service;

import com.aeye.mifss.bio.dto.FaceFturDTO;

import java.util.List;

public interface FaceFturService {

    /**
     * 根据ID查询人脸特征
     *
     * @param id 人脸特征ID
     * @return 人脸特征DTO
     */
    FaceFturDTO getById(String id);

    /**
     * 查询人脸特征列表
     *
     * @param dto 查询条件
     * @return 人脸特征列表
     */
    List<FaceFturDTO> queryList(FaceFturDTO dto);

}
