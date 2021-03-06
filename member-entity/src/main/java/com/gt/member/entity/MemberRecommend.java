package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_recommend")
public class MemberRecommend extends Model<MemberRecommend> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 推荐人
     */
	private Integer memberId;
    /**
     * 被推人姓名
     */
	private String name;
    /**
     * 手机号码
     */
	private String phone;
    /**
     * 是否审核 0未审核 1已审核 2审核不通过
     */
	private Integer ischeck;
    /**
     * 推荐码
     */
	private String code;
    /**
     * 卡片类型
     */
	private Integer ctId;
    /**
     * 图片
     */
	private String image;
    /**
     * 是否使用 0未使用 1使用
     */
	private Integer isUser;
    /**
     * 赠送积分
     */
	private Integer integral;
    /**
     * 赠送粉币
     */
	private Integer fenbi;
    /**
     * 赠送流量
     */
	private Integer flow;
    /**
     * 推荐时间
     */
	private Date datetime;
    /**
     * 理由
     */
	private String retext;
    /**
     * 赠送金额
     */
	private Double money;
    /**
     * 推荐方式  0推荐码  1二维码推荐 2链接推荐
     */
	private Integer reType;
    /**
     * 用户领取的卡券id（t_duofen_card_get）
     */
	private Integer cardId;
    /**
     * 推荐内容 0会员卡 1优惠券
     */
	private Integer recommendType;
    /**
     * 领取数量
     */
	private Integer lingquNum;
    /**
     * 使用数量
     */
	private Integer userNum;
    /**
     * 优惠券名称
     */
	private String cardName;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberRecommend{" +
			"id=" + id +
			", memberId=" + memberId +
			", name=" + name +
			", phone=" + phone +
			", ischeck=" + ischeck +
			", code=" + code +
			", ctId=" + ctId +
			", image=" + image +
			", isUser=" + isUser +
			", integral=" + integral +
			", fenbi=" + fenbi +
			", flow=" + flow +
			", datetime=" + datetime +
			", retext=" + retext +
			", money=" + money +
			", reType=" + reType +
			", cardId=" + cardId +
			", recommendType=" + recommendType +
			", lingquNum=" + lingquNum +
			", userNum=" + userNum +
			", cardName=" + cardName +
			"}";
	}
}
