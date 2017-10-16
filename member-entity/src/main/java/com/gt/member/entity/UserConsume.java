package com.gt.member.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
 * 会员消费记录总表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-16
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_user_consume")
public class UserConsume extends Model<UserConsume> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 公众号id（已无效）
     */
	private Integer publicId;
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
	private Integer gtId;
    /**
     * 记录类型 0积分记录 1充值记录 2消费记录
     */
	@TableField("recordType")
	private Integer recordType;
    /**
     * 消费类型 字典1197 0积分卡消费 1储值卡消费 2折扣卡消费 3次卡消费 4积分兑换粉笔 5积分兑换商品 6游客消费  7会员卡充值 13购买会员卡 14线下核销 18优惠买单  19粉币兑换物品 20流量充值 21微预约
     */
	@TableField("ucType")
	private Integer ucType;
    /**
     * 消费金额
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
     * 次数(次卡)
     */
	private Integer uccount;
    /**
     * 折扣数
     */
	private Integer discount;
    /**
     * 折扣后金额
     */
	@TableField("discountMoney")
	private Double discountMoney;
    /**
     * 订单id （已无效）
     */
	@TableField("orderId")
	private Integer orderId;
    /**
     * 赠送积分
     */
	@TableField("giveIntegral")
	private Integer giveIntegral;
    /**
     * 赠送流量(兑换流量负数)
     */
	@TableField("giveFlow")
	private Integer giveFlow;
    /**
     * 赠送粉币
     */
	@TableField("giveFenbi")
	private Double giveFenbi;
    /**
     * 详情表名 （已无效）
     */
	@TableField("ucTable")
	private String ucTable;
    /**
     * 创建时间
     */
	@TableField("createDate")
	private Date createDate;
    /**
     * 支付方式 1198
     */
	@TableField("paymentType")
	private Integer paymentType;
    /**
     * 优惠劵id 不用了
     */
	@TableField("dvId")
	private Integer dvId;
    /**
     * 优惠描述(保存卡劵code)
     */
	@TableField("disCountdepict")
	private String disCountdepict;
    /**
     * 余额
     */
	private Double balance;
    /**
     * 支付状态 0未支付 1已支付 2支付失败 3、退单
     */
	@TableField("payStatus")
	private Integer payStatus;
    /**
     * 赠送物品名
     */
	@TableField("giveGift")
	private String giveGift;
    /**
     * 赠送数量
     */
	@TableField("giftCount")
	private Integer giftCount;
    /**
     * 订单号
     */
	@TableField("orderCode")
	private String orderCode;
    /**
     * 不用了 模块类型 0商城 1酒店 2微餐饮 3其他
     */
	@TableField("moduleType")
	private Integer moduleType;
    /**
     * 店铺id
     */
	@TableField("storeId")
	private Integer storeId;
    /**
     * （已无效）0主店铺 1子店铺 不用了
     */
	@TableField("isParent")
	private Integer isParent;
    /**
     * 运费金额
     */
	@TableField("freightMoney")
	private Double freightMoney;
    /**
     * 线下抵消方式 不用了
     */
	@TableField("offlinePayType")
	private Integer offlinePayType;
    /**
     * 卡券类型 -1未使用优惠券 0微信卡券 1多粉卡券
     */
	@TableField("cardType")
	private Integer cardType;
    /**
     * 兑换流量 状态 默认1兑换成功 0兑换失败回滚
     */
	@TableField("flowState")
	private Integer flowState;
    /**
     * 数据来源 0:pc端 1:微信 2:uc端 3:小程序 4魔盒
     */
	@TableField("dataSource")
	private Integer dataSource;
    /**
     * 不需要 行业业务场景类型 1.拼团订单 2积分兑换商品订单 3.秒杀订单 4.拍卖订单 5 粉币订单 6预售订单 7批发商品订单
     */
	@TableField("moduleOrderType")
	private Integer moduleOrderType;
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
     * 优惠金额（new）
     */
	@TableField("youhuiMoney")
	private Double youhuiMoney;
    /**
     * 退款粉币
     */
	@TableField("refundFenbi")
	private Double refundFenbi;
    /**
     * 退款积分
     */
	@TableField("refundJifen")
	private Integer refundJifen;
    /**
     * 退款金额
     */
	@TableField("refundMoney")
	private Double refundMoney;
    /**
     * 退款时间
     */
	@TableField("refundDate")
	private Date refundDate;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "UserConsume{" +
			"id=" + id +
			", publicId=" + publicId +
			", busUserId=" + busUserId +
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
			", discount=" + discount +
			", discountMoney=" + discountMoney +
			", orderId=" + orderId +
			", giveIntegral=" + giveIntegral +
			", giveFlow=" + giveFlow +
			", giveFenbi=" + giveFenbi +
			", ucTable=" + ucTable +
			", createDate=" + createDate +
			", paymentType=" + paymentType +
			", dvId=" + dvId +
			", disCountdepict=" + disCountdepict +
			", balance=" + balance +
			", payStatus=" + payStatus +
			", giveGift=" + giveGift +
			", giftCount=" + giftCount +
			", orderCode=" + orderCode +
			", moduleType=" + moduleType +
			", storeId=" + storeId +
			", isParent=" + isParent +
			", freightMoney=" + freightMoney +
			", offlinePayType=" + offlinePayType +
			", cardType=" + cardType +
			", flowState=" + flowState +
			", dataSource=" + dataSource +
			", moduleOrderType=" + moduleOrderType +
			", isend=" + isend +
			", ischongzhi=" + ischongzhi +
			", isendDate=" + isendDate +
			", youhuiMoney=" + youhuiMoney +
			", refundFenbi=" + refundFenbi +
			", refundJifen=" + refundJifen +
			", refundMoney=" + refundMoney +
			", refundDate=" + refundDate +
			"}";
	}
}
