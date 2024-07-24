package com.byx.pub.bean.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jump
 * @date 2023/8/4 16:59:35
 */
@Data
public class SaveShareFileBo {
    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    @NotEmpty(message = "名片id不能为空")
    private String cardId;

    /**
     * 分享主键(修改)
     */
    @ApiModelProperty(value = "分享主键(修改)")
    private String id;

    /**
     * 所属合集id(有就传)
     */
    @ApiModelProperty(value = "所属合集id(有就传)")
    private String folderId;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    @Size(max = 100,message = "文件名称最大100个字符")
    private String fileName;

    /**
     * 分享名称
     */
    @ApiModelProperty(value = "分享名称")
    @NotEmpty(message = "请填写分享名称")
    @Length(max = 60,message = "分享名称最大60个字符")
    private String shareName;

    /**
     * 上传文件(图片)名称(文件名、图片名)、合集icon
     */
    @ApiModelProperty(value = "上传文件(图片)名称(文件名、图片名)、合集icon")
    @Length(max = 100,message = "上传文件最大100个字符")
    private String fileUrl;

    /**
     * 文件后缀名(前端匹配icon)
     */
    @ApiModelProperty(value = "文件后缀名(前端匹配icon)")
    private String suffixName;

    /**
     * 分享类型(1：文档分享，2：微信文章，3：微信视频号，4：快手视频，5：抖音视频，6：小红书，7：B站)
     */
    @ApiModelProperty(value = "分享类型(1：文档分享，2：微信文章，3：微信视频号，4：快手视频，5：抖音视频，6：小红书，7：B站)")
    private Integer shareType;

    /**
     * 搜索名
     */
    @ApiModelProperty(value = "搜索名")
    private String searchName;

    /**
     * 跳转连接(文章连接、视频连接、视频号)、合集简介
     */
    @ApiModelProperty(value = "跳转连接(文章连接、视频连接、视频号)、合集简介")
    private String jumpUrl;

    /**
     * 微信视频号id
     */
    @ApiModelProperty(value = "微信视频号id")
    @Length(max = 255,message = "微信视频号ID最大255字符")
    private String wxVideoSn;

    /**
     * 视频id
     */
    @ApiModelProperty(value = "视频id")
    @Length(max = 255,message = "视频ID最大255字符")
    private String videoId;

    /**
     * 合集状态(true：是，false：否)
     */
    @ApiModelProperty(value = "合集状态(true：是，false：否)")
    @NotNull(message = "请传入合集状态")
    private Boolean folderStatus;

    /**
     * 集合下文件数量(展示用)
     */
    @ApiModelProperty(value = "集合下文件数量(展示用)")
    private Integer fileNum;

    /**
     * 集合下文件类型列表(展示用)
     */
    @ApiModelProperty(value = "集合下文件类型列表(展示用)")
    private List<Integer> shareTypeList = new ArrayList<>(10);

}
