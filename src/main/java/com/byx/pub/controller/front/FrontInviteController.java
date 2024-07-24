package com.byx.pub.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byx.pub.bean.qo.PageInviteRecordQo;
import com.byx.pub.bean.vo.InviteRecordVo;
import com.byx.pub.plus.entity.Invite;
import com.byx.pub.service.InviteService;
import com.byx.pub.service.WeChatApiService;
import com.byx.pub.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Objects;


/**
 * [客户端]-[邀请函]服务Api
 * @Author Jump
 * @Date 2023/8/14 22:19
 */
@RestController
@RequestMapping("/white/clouds/front/business/v1/invite")
@Api(value = "[客户端]-[邀请函]服务Api",tags = "[客户端]-[邀请函]服务Api")
public class FrontInviteController {

    @Resource
    WeChatApiService weChatApiService;
    @Resource
    InviteService inviteService;

    /**
     * 1.获取邀请函
     * @param inviteUserId
     * @return
     */
    @GetMapping("/get")
    @ApiOperation(value = "1.获取邀请函")
    public CommonResult<Invite> getInvite(
        @ApiParam(value = "邀请人id")@RequestHeader(value = "user-id",required = false) String inviteUserId
    ){
        return CommonResult.success(inviteService.getInviteByUserId(inviteUserId));
    }

    /**
     * 2.生成邀请函二维码(返回流)
     * @param response
     * @param inviteUserId
     * @throws IOException
     */
    @GetMapping("/create/qrcode")
    @ApiOperation(value = "2.生成邀请函二维码")
    public void createCardQrCode(
        HttpServletResponse response,
        @ApiParam(value = "邀请人id")@RequestHeader(value = "user-id",required = false) String inviteUserId
    ) throws IOException {
        //设置响应类型
        String result = weChatApiService.createQrCode(inviteUserId);
        // 以流的形式返回
        ServletOutputStream out = null;
        ByteArrayOutputStream baos = null;
        try {
            InputStream inStream = new ByteArrayInputStream(result.getBytes());
            byte[] buffer = new byte[1024];
            int len;
            baos = new ByteArrayOutputStream();
            while ((len = inStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            out = response.getOutputStream();
            out.write(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Objects.requireNonNull(baos).flush();
            baos.close();
            Objects.requireNonNull(out).flush();
            out.close();
        }
    }


    /**
     * 3.保存邀请函二维码
     * @param inviteUserId
     * @param qrCode
     * @return
     */
    @GetMapping("/save/qrcode")
    @ApiOperation(value = "3.保存邀请函二维码")
    public CommonResult<Invite> saveCardQrCode(
        @ApiParam(value = "邀请人id")@RequestHeader(value = "user-id",required = false) String inviteUserId,
        @ApiParam(required = true, value = "二维码") @RequestParam("qrCode") String qrCode
    ){
        return CommonResult.success(inviteService.saveQrCode(inviteUserId,qrCode));
    }

    /**
     * 保存邀请记录
     * @param inviteUserId
     * @param newUserId
     * @return
     */
    @GetMapping("/save/invite/record")
    @ApiOperation(value = "保存邀请记录")
    public CommonResult<Void> saveInviteRecode(
        @ApiParam(value = "邀请人id")@RequestHeader(value = "user-id",required = false) String inviteUserId,
        @ApiParam(required = true, value = "新用户id") @RequestParam("newUserId") String newUserId
    ){
        inviteService.saveInviteRecord(inviteUserId,newUserId);
        return CommonResult.success();
    }

    /**
     * 分页查询邀请记录
     * @param qo 查询条件
     * @return
     */
    @PostMapping("/page/record/list")
    @ApiOperation(value = "分页查询邀请记录")
    public CommonResult<Page<InviteRecordVo>> pageRecordList(
        @ApiParam(value = "查询条件")@Validated @RequestBody PageInviteRecordQo qo
    ){
        return CommonResult.success(inviteService.pageInviteRecordList(qo));
    }



    /*@GetMapping("/get/qrcode")
    @ApiOperation(value = "获取二维码")
    public CommonResult<String> getCardQrCode(
            @ApiParam(value = "咨询师id")@RequestHeader(value = "admin-id",required = false) String adminId,
            @ApiParam(required = true, value = "名片id") @RequestParam("cardId") String cardId
    ) throws IOException {
        return CommonResult.success(pullNewUserService.getOrCreateQrCode(adminId,cardId));
    }
*/

    /**
     * 生成拉新二维码(字节)
     * @param response
     * @param inviteUserId
     * @return
     * @throws IOException
     */
    @GetMapping("/create/qrcode/byte")
    @ApiOperation(value = "生成拉新二维码(字节)")
    public CommonResult<Void> createQrCodeByte(
            HttpServletResponse response,
            @ApiParam(value = "邀请人id")@RequestHeader(value = "user-id",required = false) String inviteUserId
    ) throws IOException {
        //设置响应类型
        response.setContentType("image/png");
        byte[] result = weChatApiService.createQrCodeByByte(inviteUserId);
        response.setContentType("image/jpg");
        OutputStream stream = response.getOutputStream();
        stream.write(result);
        stream.flush();
        stream.close();
        return CommonResult.success();
    }

}
