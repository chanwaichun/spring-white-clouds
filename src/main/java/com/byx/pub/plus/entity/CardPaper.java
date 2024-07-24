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
 * 名片证书表
 * </p>
 *
 * @author ZhuoYue
 * @since 2024-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CardPaper对象", description="名片证书表")
public class CardPaper extends Model<CardPaper> {

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
     * 证书类型(1：官方证明，2：公司证明，3：其他证明)
     */
    @ApiModelProperty(value = "证书类型(1：官方证明，2：公司证明，3：其他证明)")
    private Integer paperType;

    /**
     * 证书名称
     */
    @ApiModelProperty(value = "证书名称")
    private String paperName;

    /**
     * 证书文件名
     */
    @ApiModelProperty(value = "证书文件名")
    private String imgName;

    /**
     * 证书文件路径
     */
    @ApiModelProperty(value = "证书文件路径")
    private String imgUrl;

    /**
     * 证书审核状态(1：待审核，2：审核不通过，3：已审核)
     */
    @ApiModelProperty(value = "证书审核状态(1：待审核，2：审核不通过，3：已审核)")
    private Integer paperStatus;

    /**
     * 拒审理由
     */
    @ApiModelProperty(value = "拒审理由")
    private String auditReason;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
