package com.gt.member.entity;

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
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-11-22
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_user_consume_new")
public class UserConsumeNew extends Model<UserConsumeNew > {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 商户id
     */
	@TableField("busId")
	private Integer busId;
    /**
     * 会员id
     */
	@TableField("memberId")
	private Integer memberId;
    /**
     * 会员卡id
     */
	@TableField("mcId")
	private Integer mcId;
    /**
     * 会员卡类型
     */
	@TableField("ctId")
	private Integer ctId;
    /**
     * 卡片等级Id
     */
	@TableField("gtId")
	private Integer gtId;
    /**
     * 记录类型 0积分记录 1充值记录 2消费记录
     */
	@TableField("recordType")
	private Integer recordType;
    /**
     * 消费类型 字典1197  7会员卡充值 13购买会员卡 14线下核销 18优惠买单   20流量充值 21微预约
     */
	@TableField("ucType")
	private Integer ucType;
    /**
     * 订单总金额
     */
	@TableField("totalMoney")
	private Double totalMoney;
    /**
     * 消费积分数量
     */
	private Integer integral;
    /**
     * 消费粉币数量
     */
	private Double fenbi;
    /**
     * 次数
     */
	private Integer uccount;
    /**
     * 优惠金额
     */
	@TableField("discountMoney")
	private Double discountMoney;
    /**
     * 优惠后金额
     */
	@TableField("discountAfterMoney")
	private Double discountAfterMoney;
    /**
     * 流量兑换
     */
	@TableField("changeFlow")
	private Integer changeFlow;
    /**
     * 卡券模板id
     */
	@TableField("dvId")
	private Integer dvId;
    /**
     * 用户领取卡券code值
     */
	@TableField("disCountdepict")
	private String disCountdepict;
    /**
     * 卡券类型 -1未使用优惠券 0微信卡券 1多粉卡券
     */
	@TableField("cardType")
	private Integer cardType;
    /**
     * 余额
     */
	private Double balance;
    /**
     * 兑换流量 状态 默认1兑换成功 0兑换失败回滚
     */
	@TableField("flowState")
	private Integer flowState;
    /**
     * 数据来源 0:pc端 1:微信 2:uc端 3:小程序 4魔盒 5:ERP
     */
	@TableField("dataSource")
	private Integer dataSource;
    /**
     * 订单是否已终结（不能退单）0未终结 1终结
     */
	private Integer isend;
    /**
     * 是否充值 0否 1是 （会员充值或其他金额充值）
     */
	private Integer ischongzhi;
    /**
     * 订单终结时间
     */
	@TableField("isendDate")
	private Date isendDate;
    /**
     * 退款金额
     */
	@TableField("refundMoney")
	private Double refundMoney;
    /**
     * 订单号
     */
	@TableField("orderCode")
	private String orderCode;
    /**
     * 门店
     */
	@TableField("shopId")
	private Integer shopId;
    /**
     * 创建时间
     */
	@TableField("createDate")
	private Date createDate;
    /**
     * 支付状态 0未支付 1已支付 2支付失败 3退单 4部分退单
     */
	@TableField("payStatus")
	private Integer payStatus;
    /**
     * 次卡剩余次数
     */
	@TableField("balanceCount")
	private Integer balanceCount;

    /**
     * 剩余流量
     */
    	@TableField("flowbalance")
	private Integer flowbalance;

    /**
     * 退粉币
     */
	@TableField("refundFenbi")
	private Double refundFenbi;
    /**
     * 退积分
     */
	@TableField("refundJifen")
	private Integer refundJifen;
    /**
     * 退款时间
     */
	@TableField("refundDate")
	private Date refundDate;
    /**
     * 流量剩余
     */
	private Integer flowbalance;
    /**
     * 副卡id
     */
	@TableField("fukaCtId")
	private Integer fukaCtId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "UserConsumeNew{" +
			"id=" + id +
			", busId=" + busId +
			", memberId=" + memberId +
			", mcId=" + mcId +
			", ctId=" + ctId +
			", gtId=" + gtId +
			", recordType=" + recordType +
			", ucType=" + ucType +
			", totalMoney=" + totalMoney +
			", integral=" + integral +
			", fenbi=" + fenbi +
			", uccount=" + uccount +
			", discountMoney=" + discountMoney +
			", discountAfterMoney=" + discountAfterMoney +
			", changeFlow=" + changeFlow +
			", dvId=" + dvId +
			", disCountdepict=" + disCountdepict +
			", cardType=" + cardType +
			", balance=" + balance +
			", flowState=" + flowState +
			", dataSource=" + dataSource +
			", isend=" + isend +
			", ischongzhi=" + ischongzhi +
			", isendDate=" + isendDate +
			", refundMoney=" + refundMoney +
			", orderCode=" + orderCode +
			", shopId=" + shopId +
			", createDate=" + createDate +
			", payStatus=" + payStatus +
			", balanceCount=" + balanceCount +
			", refundFenbi=" + refundFenbi +
			", refundJifen=" + refundJifen +
			", refundDate=" + refundDate +
			", flowbalance=" + flowbalance +
			", fukaCtId=" + fukaCtId +
			"}";
	}
}
