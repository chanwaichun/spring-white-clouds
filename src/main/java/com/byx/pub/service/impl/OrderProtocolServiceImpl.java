package com.byx.pub.service.impl;

import com.byx.pub.enums.ResultCode;
import com.byx.pub.exception.ApiException;
import com.byx.pub.plus.dao.OrderDetailPlusDao;
import com.byx.pub.plus.entity.OrderDetail;
import com.byx.pub.service.OrderProtocolService;
import com.byx.pub.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 协议服务
 * @author Jump
 * @date 2023/5/30 16:17:14
 */
@Slf4j
@Service
public class OrderProtocolServiceImpl implements OrderProtocolService {

    @Resource
    OrderDetailPlusDao detailPlusDao;
    @Value("${spring.servlet.multipart.location}")
    String locationStr;


    /**
     * 上传协议
     * @param orderSn
     * @param pictures
     * @return
     */
    public String setProtocol(String orderSn, MultipartFile[] pictures){
        OrderDetail detail = this.detailPlusDao.lambdaQuery().eq(OrderDetail::getOrderSn, orderSn).last(" limit 1").one();
        if(Objects.isNull(detail)){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"未找到订单信息");
        }
        if(StringUtils.isNotEmpty(detail.getProtocol())){
            throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"该订单已上传协议");
        }
        //上传图片的路径集合
        List<String> imgUrls = new ArrayList<>();

        //循环上传
        for( MultipartFile img : pictures){
            try {
                //获取文件名 并 重命名
                String fileName = StringUtil.getFileReName(Objects.requireNonNull(img.getOriginalFilename()));
                //文件存储路径地址
                String destFileName = this.locationStr + File.separator + "protocolUploadImg" + File.separator + fileName;
                File destFile = new File(destFileName);
                destFile.getParentFile().mkdirs();
                img.transferTo(destFile);
                imgUrls.add(fileName);
            } catch (Exception e) {
                throw new ApiException(ResultCode.VALIDATE_FAILED.getCode(),"上传商品图片失败");
            }
        }
        //设置到订单详情
        String protocolImg = imgUrls.stream().collect(Collectors.joining(","));
        OrderDetail updateBean = new OrderDetail().setId(detail.getId()).setProtocol(protocolImg);
        this.detailPlusDao.updateById(updateBean);
        return protocolImg;
    }




    /**
     * 下载协议模板
     * @param response
     */
    public void downloadCheck(HttpServletResponse response) throws IOException {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("template/protocol.pdf");
             OutputStream fos = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(stream)) {
            //mime类型
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment;filename=protocol.pdf");

            byte[] buff = new byte[1024];
            int i = bis.read(buff);
            while (i != -1) {
                fos.write(buff, 0, buff.length);
                fos.flush();
                i = bis.read(buff);
            }
        }





        /*try {
            File file = new File(Thread.currentThread().getContextClassLoader().getResource("template/protocol.pdf").getPath());
            BufferedInputStream bis = null;
            OutputStream os = null;
            FileInputStream fileInputStream = null;
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=protocolPDF");
            try {
                fileInputStream = new FileInputStream(file);
                byte[] buff = new byte[1024];
                bis = new BufferedInputStream(fileInputStream);
                os = response.getOutputStream();

                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    i = bis.read(buff);
                    os.flush();
                }
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 预览协议
     * @param response
     * @throws IOException
     */
    public void previewCheck(HttpServletResponse response) throws IOException {
        FileInputStream is = new FileInputStream(new File(Thread.currentThread().getContextClassLoader().getResource("template/protocol.pdf").getPath()));
        // 清空response
        response.reset();
        //2、设置文件下载方式
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/pdf");
        OutputStream outputStream = response.getOutputStream();
        int count = 0;
        byte[] buffer = new byte[1024 * 1024];
        while ((count = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0, count);
        }
        outputStream.flush();
    }








}
