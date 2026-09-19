/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.auditagent.controller;import cn.zhuatech.auditagent.common.ApiResponse;import cn.zhuatech.auditagent.service.ControlCoverageService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/auditagent/insights/control-coverage") public class ControlCoverageController{private final ControlCoverageService service;/**
                                                                                                                                                                  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                  */
public ControlCoverageController(ControlCoverageService service){this.service=service;}/**
                                                                                                                                                                                                                                                         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                         */
@PostMapping ApiResponse<ControlCoverageService.Result> evaluate(@Valid @RequestBody ControlCoverageService.Request r){return ApiResponse.ok(service.evaluate(r));}}
