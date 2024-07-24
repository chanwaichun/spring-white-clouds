package com.byx.pub.controller.front;

import com.byx.pub.service.WxPayService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * [客户端]-[用户]-[微信支付回调]接口Api
 * @author: Jump
 * @create: 2023-05-26
 */
@RestController
@RequestMapping("/white/clouds/front/user/v1/wx")
@Api(value = "[客户端]-[用户]-[微信支付回调]接口Api",tags = "[客户端]-[用户]-[微信支付回调]接口Api")
public class WxNotifyController {
    @Resource
    private WxPayService wxPayService;

    /**
     * 微信通知
     * @param requestBodyString
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pay/notify", method = RequestMethod.POST)
    public Map<String, String> notify(@RequestBody String requestBodyString) throws Exception {
        return wxPayService.notify(requestBodyString);
    }

    /**
     * 微信退款通知
     * @param requestBodyString
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refund/notify", method = RequestMethod.POST)
    public Map<String, String> refundNotify(@RequestBody String requestBodyString) throws Exception {
        return wxPayService.refundNotify(requestBodyString);
    }
}
