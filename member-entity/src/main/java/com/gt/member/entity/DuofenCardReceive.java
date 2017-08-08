package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_duofen_card_receive")
public class DuofenCardReceive extends Model<DuofenCardReceive> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer publicId;
    /**
     * 卡包名称
     */
	private String cardsName;
    /**
     * 卡包图片
     */
	private String cardImage;
    /**
     * 领取结束时间
     */
	private Date receiveDate;
    /**
     * 投放地点 0本公众号 1第三方
     */
	private Integer deliveryAddr;
    /**
     * 本公众号投放方式 1普通 2会员领取  3商场购买  4二维码下载  5游戏
     */
	private Integer deliveryType;
    /**
     * 第三方 0免费领取 1商场购买
     */
	private Integer deliveryType1;
    /**
     * 领取数量限制 0不限制 1限制
     */
	private Integer numlimit;
    /**
     * 最多领取数量规则设置
     */
	private Integer maxNumType;
    /**
     * 最多领取数量
     */
	private Integer maxNum;
    /**
     * 商场购买券包设置
     */
	private String cardMessage;
    /**
     * 购买金额
     */
	private Double buyMoney;
    /**
     * 券号短信投放通知人群
     */
	private String ctIds;
    /**
     * 券号短信投放会员等级
     */
	private String gtIds;
    /**
     * 卡包审核后生成code
     */
	private String code;
    /**
     * 卡包状态 0未审核 1审核通过已启用 2已停用
     */
	private Integer state;
	private String titleCard;
	private String cardIds;
    /**
     * 卡包背景颜色
     */
	private String backColor;
	private Integer isCallSms;
	private String mobilePhone;
    /**
     * 卡包 0非礼券包 1礼券包（其他券为赠送）
     */
	private Integer cardType;
    /**
     * 礼券id
     */
	private Integer lqId;
    /**
     * 积分购买值
     */
	private Integer jifen;
    /**
     * 粉币购买值
     */
	private Integer fenbi;
    /**
     * 商家id
     */
	private Integer busId;
    /**
     * 第三方商场id
     */
	private Integer threeMallId;
    /**
     * 第三方商城投放数量
     */
	private Integer threeNum;
    /**
     * 投放类型
     */
	private Integer classifyId;
    /**
     * 是否推荐 0不允许 1允许
     */
	private Integer isrecommend;
    /**
     * 推荐赠送积分
     */
	private Integer giveJifen;
    /**
     * 推荐赠送粉币
     */
	private Integer giveFenbi;
    /**
     * 推荐赠送金额
     */
	private Double giveMoney;
    /**
     * 推荐赠送流量
     */
	private Integer giveFlow;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofenCardReceive{" +
			"id=" + id +
			", publicId=" + publicId +
			", cardsName=" + cardsName +
			", cardImage=" + cardImage +
			", receiveDate=" + receiveDate +
			", deliveryAddr=" + deliveryAddr +
			", deliveryType=" + deliveryType +
			", deliveryType1=" + deliveryType1 +
			", numlimit=" + numlimit +
			", maxNumType=" + maxNumType +
			", maxNum=" + maxNum +
			", cardMessage=" + cardMessage +
			", buyMoney=" + buyMoney +
			", ctIds=" + ctIds +
			", gtIds=" + gtIds +
			", code=" + code +
			", state=" + state +
			", titleCard=" + titleCard +
			", cardIds=" + cardIds +
			", backColor=" + backColor +
			", isCallSms=" + isCallSms +
			", mobilePhone=" + mobilePhone +
			", cardType=" + cardType +
			", lqId=" + lqId +
			", jifen=" + jifen +
			", fenbi=" + fenbi +
			", busId=" + busId +
			", threeMallId=" + threeMallId +
			", threeNum=" + threeNum +
			", classifyId=" + classifyId +
			", isrecommend=" + isrecommend +
			", giveJifen=" + giveJifen +
			", giveFenbi=" + giveFenbi +
			", giveMoney=" + giveMoney +
			", giveFlow=" + giveFlow +
			"}";
	}
}
