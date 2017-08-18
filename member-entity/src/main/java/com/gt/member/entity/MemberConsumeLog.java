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
 * 会员消费记录表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-18
 */
@Data
@Accessors(chain = true)
@TableName("t_member_consume_log")
public class MemberConsumeLog extends Model<MemberConsumeLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 商户id
     */
	@TableField("busUserId")
	private Integer busUserId;
    /**
     * 会员id
     */
	@TableField("memberId")
	private Integer memberId;
    /**
     * 卡片id
     */
	@TableField("mcId")
	private Integer mcId;
    /**
     * 卡片类型id 0代表非会员
     */
	@TableField("ctId")
	private Integer ctId;
    /**
     * 卡片等级Id
     */
	private Integer gtid;
    /**
     * 记录类型 0积分记录 1充值记录 2消费记录
     */
	@TableField("recordType")
	private Integer recordType;
    /**
     * 消费类型 1198
     */
	@TableField("ucType")
	private Integer ucType;
    /**
     * 订单金额
     */
	@TableField("totalMoney")
	private Double totalMoney;
    /**
     * 消费积分
     */
	private Integer integral;
    /**
     * 消费粉币数量
     */
	private Double fenbi;
    /**
     * 实际支付金额（扣除所有的优惠）
     */
	@TableField("discountMoney")
	private Double discountMoney;
	@TableField("createDate")
	private Date createDate;
    /**
     * 支付方式 1197
     */
	@TableField("paymentType")
	private Integer paymentType;
    /**
     * 卡劵code
     */
	@TableField("disCountdepict")
	private String disCountdepict;
    /**
     * 余额（储值卡）
     */
	private Double balance;
    /**
     * 支付状态 0未支付 1已支付 2支付失败 3、退单
     */
	@TableField("payStatus")
	private Integer payStatus;
    /**
     * 订单号微信或支付宝
     */
	@TableField("orderCode")
	private String orderCode;
    /**
     * 门店id
     */
	@TableField("storeId")
	private Integer storeId;
    /**
     * 运费金额
     */
	@TableField("freightMoney")
	private Double freightMoney;
    /**
     * 卡券类型 -1未使用优惠券 0微信卡券 1多粉卡券
     */
	@TableField("cardType")
	private Integer cardType;
    /**
     * 兑换流量 状态 默认1兑换
     */
	@TableField("flowState")
	private Integer flowState;
    /**
     * 数据来源 0:pc端 1:微信 2:uc端 3:小程序
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


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberConsumeLog{" +
			"id=" + id +
			", busUserId=" + busUserId +
			", memberId=" + memberId +
			", mcId=" + mcId +
			", ctId=" + ctId +
			", gtid=" + gtid +
			", recordType=" + recordType +
			", ucType=" + ucType +
			", totalMoney=" + totalMoney +
			", integral=" + integral +
			", fenbi=" + fenbi +
			", discountMoney=" + discountMoney +
			", createDate=" + createDate +
			", paymentType=" + paymentType +
			", disCountdepict=" + disCountdepict +
			", balance=" + balance +
			", payStatus=" + payStatus +
			", orderCode=" + orderCode +
			", storeId=" + storeId +
			", freightMoney=" + freightMoney +
			", cardType=" + cardType +
			", flowState=" + flowState +
			", dataSource=" + dataSource +
			", isend=" + isend +
			", ischongzhi=" + ischongzhi +
			", isendDate=" + isendDate +
			", refundMoney=" + refundMoney +
			"}";
	}
}
