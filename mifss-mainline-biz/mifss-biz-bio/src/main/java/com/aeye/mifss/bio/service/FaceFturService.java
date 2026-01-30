package com.aeye.mifss.bio.service;

import com.aeye.mifss.bio.dto.FaceFturDTO;

import java.util.List;

public interface FaceFturService {

    FaceFturDTO getById(String id);

    List<FaceFturDTO> queryList(FaceFturDTO dto);

}
