package com.gt.duofencard.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-16
 */
@Data
@Accessors(chain = true)
@TableName("t_duofen_card_publish")
public class DuofenCardPublish extends Model<DuofenCardPublish> {

    private static final long serialVersionUID = 1L;

	@TableId(value="publishId", type= IdType.AUTO)
	private Integer publishId;
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
	private Double buyMoney;

    	@TableField("giftBuyMoney")
	private String giftBuyMoney;

    	@TableField("isDiscount")
	private Integer isDiscount;

    	@TableField("isFenbi")
    	private Integer isFenbi;

    	@TableField("isJifen")
    	private Integer isJifen;

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


	@Override
	protected Serializable pkVal() {
		return this.publishId;
	}

	@Override
	public String toString() {
		return "DuofenCardPublish{" +
			"publishId=" + publishId +
			", cardId=" + cardId +
			", number=" + number +
			", isBuy=" + isBuy +
			", buyMoney=" + buyMoney +
			", isReceiveNum=" + isReceiveNum +
			", limitType=" + limitType +
			", limitNum=" + limitNum +
			", isRecommend=" + isRecommend +
			", jifen=" + jifen +
			", fenbi=" + fenbi +
			", flow=" + flow +
			", money=" + money +
			"}";
	}
}
