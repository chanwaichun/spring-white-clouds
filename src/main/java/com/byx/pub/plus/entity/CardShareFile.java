package com.byx.pub.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;

/**
 * <p>
 * 名片分享表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CardShareFile对象", description="名片分享表")
public class CardShareFile extends Model<CardShareFile> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    private String cardId;

    /**
     * 文件名称(文件中文名)
     */
    @ApiModelProperty(value = "文件名称(文件中文名)")
    private String fileName;

    /**
     * 上传文件(图片)名称(文件名、图片名)、合集icon
     */
    @ApiModelProperty(value = "上传文件(图片)名称(文件名、图片名)、合集icon")
    private String fileUrl;

    /**
     * 分享类型(1：文档分享，2：微信文章，3：微信视频号，4：快手视频，5：抖音视频，6：小红书，7：B站)
     */
    @ApiModelProperty(value = "分享类型(1：文档分享，2：微信文章，3：微信视频号，4：快手视频，5：抖音视频，6：小红书，7：B站)")
    private Integer shareType;

    /**
     * 文件后缀名
     */
    @ApiModelProperty(value = "文件后缀名")
    private String suffixName;

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
     * 合集状态(true：是，false：否)
     */
    @ApiModelProperty(value = "合集状态(true：是，false：否)")
    private Boolean folderStatus;

    /**
     * 所属合集id
     */
    @ApiModelProperty(value = "所属合集id")
    private String folderId;

    /**
     * 分享名称
     */
    @ApiModelProperty(value = "分享名称")
    private String shareName;

    /**
     * 微信视频号id
     */
    @ApiModelProperty(value = "微信视频号id")
    private String wxVideoSn;

    /**
     * 视频id
     */
    @ApiModelProperty(value = "视频id")
    private String videoId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
