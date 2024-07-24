package com.byx.pub.controller;

import com.byx.pub.service.CropWeChatCallBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * [管理后台]-[企业微信]-[回调]管理Api
 * @author Jump
 * @date 2023/8/7 14:38:50
 */
@Slf4j
@RestController
@Api(value = "[管理后台]-[企业微信]-[回调]管理Api",tags = "[管理后台]-[企业微信]-[回调管理Api")
public class CropWeChatCallBackController {

    @Autowired
    CropWeChatCallBackService callBackService;

    /**
     * 数据回调URL(POST)
     * @param request
     * @return
     */
    @ApiOperation("企业微信服务器配置地址，用于验证启用 应用管理-回调配置-数据回调URL")
    @PostMapping(value = "/white/clouds/manage/v1/crop/callback/msg")
    @ResponseBody
    public String weComMsg(HttpServletRequest request) {
        log.info("----crop callback msg-----start");
        // 微信加密签名
        String signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        log.info("----crop callback msg-----parameter:signature-"+signature+",timestamp-"+timestamp+",nonce-"+nonce);
        return "success";
    }

    /**
     * 数据回调URL(GET)
     * @param request
     * @return
     */
    @ApiOperation("企业微信服务器配置地址，用于验证启用 应用管理-回调配置-数据回调URL")
    @GetMapping(value = "/white/clouds/manage/v1/crop/callback/msg")
    @ResponseBody
    public String weComMsgVerify(HttpServletRequest request) {
        log.info("----crop callback msg-----start");
        // 微信加密签名
        String signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        log.info("----crop callback msg-----parameter:signature-"+signature+",timestamp-"+timestamp+",nonce-"+nonce+",echostr-"+echostr);
        // 确认此次GET请求来自企业微信服务器，原样返回echostr参数内容
        return callBackService.weComVerify(signature, timestamp, nonce, echostr);
    }

    /**
     * 指令回调URL(GET)
     * @param request
     * @return
     */
    @ApiOperation("企业微信服务器配置地址，用于验证启用 应用管理-回调配置-指令回调URL")
    @GetMapping(value = "/white/clouds/manage/v1/crop/callback/command")
    @ResponseBody
    public String weComCommandVerify(HttpServletRequest request) {
        log.info("----crop callback get command-----start");
        // 微信加密签名
        String signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        // 确认此次GET请求来自企业微信服务器，原样返回echostr参数内容
        log.info("----crop callback get command-----parameter:signature-"+signature+",timestamp-"+timestamp+",nonce-"+nonce+",echostr-"+echostr);
        return callBackService.weComVerify(signature, timestamp, nonce, echostr);
    }

    /**
     * 刷新ticket(POST)
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation("企业微信，指令回调（刷新ticket）")
    @PostMapping(value = "/white/clouds/manage/v1/crop/callback/command")
    @ResponseBody
    public String weComCommand(HttpServletRequest request) throws Exception {
        log.info("----crop callback post command-----start");
        // 微信加密签名
        String signature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        ServletInputStream in = request.getInputStream();
        String success = callBackService.weComCommand(signature, timestamp, nonce, in);
        log.info("----crop callback post command-----parameter:signature-"+signature+",timestamp-"+timestamp+",nonce-"+nonce+",in-"+in);
        return success;
    }

    /**
     * 用于验证企业微信域名校验
     * @return
     */
    @RequestMapping(value = "/WW_verify_jZGe5zcuOYBj3Hex.txt", method = RequestMethod.GET)
    public String checkDomain () {
        return "jZGe5zcuOYBj3Hex";
    }


    /**
     * 平台-内部校验域名
     * @return
     */
    @RequestMapping(value = "/WW_verify_YoJSgfVFgIffzTpE.txt", method = RequestMethod.GET)
    public String platformCheckDomain () {
        return "YoJSgfVFgIffzTpE";
    }


}
