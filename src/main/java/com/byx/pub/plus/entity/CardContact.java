package com.byx.pub.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 名片图片表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CardContact对象", description="名片图片表")
public class CardContact extends Model<CardContact> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 名片id
     */
    @ApiModelProperty(value = "名片id")
    private String cardId;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String icon;

    /**
     * 联系方式类型(1：抖音，2：手机，3：企业微信，4：微信，5：小红书，6：微信视频号，7：哔哩哔哩)
     */
    @ApiModelProperty(value = "联系方式类型(1：抖音，2：手机，3：企业微信，4：微信，5：小红书，6：微信视频号，7：哔哩哔哩)")
    private Integer contactType;

    /**
     * 主要联系(true：是，false：否)
     */
    @ApiModelProperty(value = "主要联系(true：是，false：否)")
    private Boolean mainStatus;

    /**
     * 文本(号码类)
     */
    @ApiModelProperty(value = "文本(号码类)")
    private String textRemark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
