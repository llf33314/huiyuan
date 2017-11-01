package com.gt.member.entity;

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
 * 卡片模板信息表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-28
 */
@Data
@Accessors(chain = true)
@TableName("t_member_gradetype")
public class MemberGradetype extends Model<MemberGradetype> {

    private static final long serialVersionUID = 1L;

	@TableId(value="gt_id", type= IdType.AUTO)
	private Integer gtId;
    /**
     * 会员卡等级
     */
	private String gtGradeName;
    /**
     * 会员卡名称
     */
	private String gtName;
    /**
     * 会员卡名称颜色
     */
	private String gtNameColor;
    /**
     * 会员卡login图片
     */
	private String gtLoginUrl;
    /**
     * 模板id
     */
	private Integer cmId;
    /**
     * 文字字体颜色
     */
	private String gtTextColor;
    /**
     * 卡片类型(积分卡、储值卡、时效卡、折扣卡、次卡)
     */
	private Integer ctId;
    /**
     * 商户id
     */
	private Integer publicId;
    /**
     * 是否已不发布 0未发布 1已发布
     */
	private Integer isPublish;
    /**
     * 是否审核 1不审核 0审核
     */
	@TableField("isCheck")
	private Integer isCheck;
    /**
     * 0会员免费领取 1、老会员邀请码领取 2老会员信息领取 3通过购买申请
     */
	@TableField("applyType")
	private Integer applyType;
    /**
     * 购买金额
     */
	@TableField("buyMoney")
	private Double buyMoney;
    /**
     * 配置方式 0未设置 1购买方式 2非购买
     */
	@TableField("buyModel")
	private Integer buyModel;
    /**
     * 工本费
     */
	@TableField("costMoney")
	private Double costMoney;
    /**
     * 卡内余额或次数
     */
	private String balance;
    /**
     * 送流量 老会员推荐
     */
	private Integer giveflow;
    /**
     * 送积分 老会员推荐
     */
	private Integer givefenbi;
    /**
     * 赠送粉币
     */
	@TableField("giveIntegral")
	private Integer giveIntegral;
    /**
     * 游戏跳转地址
     */
	private String url;
    /**
     * 是否开启完善资料页面 0开启 1未开启
     */
	@TableField("isView")
	private Integer isView;
    /**
     * 是否开启会员日 0开启 1不开启
     */
	@TableField("ismemberDate")
	private Integer ismemberDate;
    /**
     * 是否上传图片 0否 1 是
     */
	@TableField("isUploadImg")
	private Integer isUploadImg;
    /**
     * 图片说明
     */
	@TableField("imgExplain")
	private String imgExplain;
    /**
     * 0左 1右
     */
	private Integer isleft;
    /**
     * 是否是简易会员卡 0否 1是
     */
	private Integer iseasy;
    /**
     * 简易会员卡领取 0不开启 1开启
     */
	@TableField("easyApply")
	private Integer easyApply;
	@TableField("busId")
	private Integer busId;
    /**
     * 推荐送钱
     */
	@TableField("giveMoney")
	private Double giveMoney;
    /**
     * 金额最低值才可提取
     */
	@TableField("pickMoney")
	private Double pickMoney;
    /**
     * 是否开通副卡 0没有 1开通
     */
	@TableField("assistantCard")
	private Integer assistantCard;
    /**
     * 是否卡开通推荐  0没有 1开通
     */
	private Integer isrecommend;


	@Override
	protected Serializable pkVal() {
		return this.gtId;
	}

	@Override
	public String toString() {
		return "MemberGradetype{" +
			"gtId=" + gtId +
			", gtGradeName=" + gtGradeName +
			", gtName=" + gtName +
			", gtNameColor=" + gtNameColor +
			", gtLoginUrl=" + gtLoginUrl +
			", cmId=" + cmId +
			", gtTextColor=" + gtTextColor +
			", ctId=" + ctId +
			", publicId=" + publicId +
			", isPublish=" + isPublish +
			", isCheck=" + isCheck +
			", applyType=" + applyType +
			", buyMoney=" + buyMoney +
			", buyModel=" + buyModel +
			", costMoney=" + costMoney +
			", balance=" + balance +
			", giveflow=" + giveflow +
			", givefenbi=" + givefenbi +
			", giveIntegral=" + giveIntegral +
			", url=" + url +
			", isView=" + isView +
			", ismemberDate=" + ismemberDate +
			", isUploadImg=" + isUploadImg +
			", imgExplain=" + imgExplain +
			", isleft=" + isleft +
			", iseasy=" + iseasy +
			", easyApply=" + easyApply +
			", busId=" + busId +
			", giveMoney=" + giveMoney +
			", pickMoney=" + pickMoney +
			", assistantCard=" + assistantCard +
			", isrecommend=" + isrecommend +
			"}";
	}
}
