package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2017-07-25
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
	@TableField("gt_grade_name")
	private String gtGradeName;
    /**
     * 会员卡名称
     */
	@TableField("gt_name")
	private String gtName;
    /**
     * 会员卡名称颜色
     */
	@TableField("gt_name_color")
	private String gtNameColor;
    /**
     * 会员卡login图片
     */
	@TableField("gt_login_url")
	private String gtLoginUrl;
    /**
     * 模板id
     */
	@TableField("cm_id")
	private Integer cmId;
    /**
     * 文字字体颜色
     */
	@TableField("gt_text_color")
	private String gtTextColor;
    /**
     * 卡片类型(积分卡、储值卡、时效卡、折扣卡、次卡)
     */
	@TableField("ct_id")
	private Integer ctId;
    /**
     * 商户id
     */
	@TableField("public_id")
	private Integer publicId;
    /**
     * 是否已不发布 0未发布 1已发布
     */
	@TableField("is_publish")
	private Integer isPublish;
	private Integer isCheck;
	private Integer applyType;
	private Double buyMoney;
	private Integer buyModel;
	private Double costMoney;
	private String balance;
	private Integer giveflow;
	private Integer givefenbi;
	private Integer giveIntegral;
	private String url;
	private Integer isView;
	private Integer ismemberDate;
	private Integer isUploadImg;
	private String imgExplain;
	private Integer isleft;
	private Integer iseasy;
	private Integer easyApply;
	private Integer busId;
//	private Integer assistantCard;
    /**
     * 推荐送钱
     */
	private Double giveMoney;
    /**
     * 金额最低值才可提取
     */
	private Double pickMoney;


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
			"}";
	}
}
