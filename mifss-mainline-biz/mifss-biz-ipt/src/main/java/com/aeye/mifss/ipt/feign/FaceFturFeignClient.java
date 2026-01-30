//package com.aeye.mifss.ipt.feign;
//
//import com.aeye.mifss.bio.dto.FaceFturDTO;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.util.List;
//
//@FeignClient(name = "bio-service", url = "${mifss.bio.service.url:http://127.0.0.1:8802/mifss/bio}")
//public interface FaceFturFeignClient {
//
//    @GetMapping("/faceFtur/{id}")
//    FaceFturDTO getById(@PathVariable("id") String id);
//
//    @PostMapping("/faceFtur/queryList")
//    List<FaceFturDTO> queryList(@RequestBody FaceFturDTO dto);
//
//}
