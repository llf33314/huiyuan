package com.gt.duofencard.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 多粉优惠券VO
 * </p>
 *
 * @author zhanbing
 * @since 2018-01-16
 */
@Data
@Accessors( chain = true )
public class DuofenCardNewVO extends DuofenCardNew {

    private static final long serialVersionUID = 1L;

    /**
     * 卡券id
     */
    @TableField("cardId")
    private Integer cardId;
    /**
     * 发行数量
     */
    private Integer number;
    /**
     * 是否允许购买 0不购买 1购买
     */
    @TableField("isBuy")
    private Integer isBuy;
    /**
     * 购买金额
     */
    @TableField("buyMoney")
    private String buyMoney;
    /**
     * 领取数量限制设置 0不限制 1限制
     */
    @TableField("isReceiveNum")
    private Integer isReceiveNum;
    /**
     * 限制方式 0每人每天最多领取 1此券最多领取数量
     */
    @TableField("limitType")
    private Integer limitType;
    /**
     * 限制数量
     */
    @TableField("limitNum")
    private Integer limitNum;
    /**
     * 是否允许推荐
     */
    @TableField("isRecommend")
    private Integer isRecommend;
    /**
     * 赠送积分
     */
    private Integer jifen;
    /**
     * 赠送粉币
     */
    private Double fenbi;
    /**
     * 赠送流量
     */
    private Integer flow;
    /**
     * 赠送金额
     */
    private Double money;

    /**
     * 一周不可用时间
     */
    private String week;
    /**
     * 节假日不可用
     */
    private Integer holidays;
    /**
     * 其他时间端 0未设置 1已设置
     */
    @TableField("otherTime")
    private Integer otherTime;
    /**
     * 其他时间设置
     */
    @TableField("otherTimeSet")
    private String otherTimeSet;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }
}
