package com.aeye.mifss.ipt.controller;

import cn.hsa.hsaf.core.framework.web.WrapperResponse;
import cn.hsa.ims.common.contoller.AeyeAbstractController;
import cn.hsa.ims.common.utils.AeyeFSManager;
import cn.hsa.ims.common.utils.AeyeIdGeneratorUtil;
import cn.hsa.ims.common.utils.AeyePageInfo;
import cn.hsa.ims.common.utils.AeyePageResult;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aeye.mifss.bas.dto.CrtfDTO;
import com.aeye.mifss.bas.service.RpcCrtfService;
import com.aeye.mifss.bio.dto.FaceImageReq;
import com.aeye.mifss.bio.service.RpcFaceRecognitionService;
import com.aeye.mifss.common.dto.RpcMergeDTO;
import com.aeye.mifss.common.mybatis.wrapper.RpcQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Api(tags = "认证记录管理")
@RestController
@RequestMapping("/crtf")
public class CrtfController extends AeyeAbstractController {

    @Autowired(required = false)
    private RpcCrtfService crtfService;

    @Autowired(required = false)
    private RpcFaceRecognitionService faceRecognitionService;

    @ApiOperation("查询列表")
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "int", paramType = "header", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条目", dataType = "int", paramType = "header", example = "10"),
            @ApiImplicitParam(name = "orderField", value = "排序字段", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderType", value = "排序类型", dataType = "string", paramType = "header", example = "asc或者desc")
    })
    public WrapperResponse<List<CrtfDTO>> list(@RequestBody CrtfDTO crtfDTO) throws Exception {
        AeyePageInfo pageInfo = buildPageInfo();
        RpcMergeDTO<CrtfDTO> rpcMergeDTO = new RpcMergeDTO(pageInfo, new RpcQueryWrapper<CrtfDTO>()
                .eq(StrUtil.isNotBlank(crtfDTO.getPsnName()), CrtfDTO::getPsnName, crtfDTO.getPsnName())
                .ge(crtfDTO.getStartTime() != null, CrtfDTO::getCrtfTime, crtfDTO.getStartTime())
                .le(crtfDTO.getEndTime() != null, CrtfDTO::getCrtfTime, crtfDTO.getEndTime())
                .like(StrUtil.isNotBlank(crtfDTO.getMedinsName()), CrtfDTO::getMedinsName, crtfDTO.getMedinsName()));
        AeyePageResult<CrtfDTO> result = crtfService.pageRpc(rpcMergeDTO);
        return (WrapperResponse) WrapperResponse.success(result);
    }

    @ApiOperation("人员认证")
    @PostMapping("/certify")
    public WrapperResponse<Boolean> certify(@RequestBody CrtfDTO crtfDTO) {
        // 1. 校验必填参数
        if (StrUtil.isBlank(crtfDTO.getImageData())) {
            return WrapperResponse.fail("认证照不能为空！", false);
        }

        String pic = Base64.encodeBase64String(AeyeFSManager.getObjectEntity(crtfDTO.getCrtfUrl()).getData());
        System.out.println(pic);

        // 2. 构建人脸识别请求，调用 biode 服务进行人脸认证

        FaceImageReq faceImageReq = new FaceImageReq();
        faceImageReq.setImageData(crtfDTO.getImageData());
        WrapperResponse<Boolean> authResult = faceRecognitionService.faceAuthenticate(faceImageReq);

        if (authResult == null) {
            return WrapperResponse.fail("认证服务调用失败", false);
        }

        // 3. 保存认证记录
        try {
            crtfDTO.setCrtfId(AeyeIdGeneratorUtil.next());
            // 认证结果：1-成功，0-失败
            Boolean authenticated = authResult.getData();
            crtfDTO.setCrtfRslt(Boolean.TRUE.equals(authenticated) ? "1" : "0");
            crtfService.saveRpc(crtfDTO);
        } catch (Exception e) {
            // 记录保存失败不影响认证结果返回
            e.printStackTrace();
        }

        return authResult;
    }

}
