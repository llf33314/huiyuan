package com.gt.duofencard.entity;

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
 * 多粉卡券
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-16
 */
@Data
@Accessors(chain = true)
@TableName("t_duofen_card_new")
public class DuofenCardNew extends Model<DuofenCardNew> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 卡券的商户logo
     */
	@TableField("loginUrl")
	private String loginUrl;
    /**
     * 商家id
     */
	@TableField("busId")
	private Integer busId;
    /**
     * 商户名字
     */
	@TableField("brandName")
	private String brandName;
    /**
     * 卡券使用类型 0通用券 1指定券
     */
	@TableField("useType")
	private Integer useType;
    /**
     * 卡券类型 0折扣券 1代价券 2礼品券 3优惠券 4礼券
     */
	@TableField("cardType")
	private Integer cardType;
    /**
     * 团购券专用，团购详情。
     */
	@TableField("dealDetail")
	private String dealDetail;
    /**
     * 表示起用金额(代金券)
     */
	@TableField("cashLeastCost")
	private Double cashLeastCost;
    /**
     * 减免金额(代金券)
     */
	@TableField("reduceCost")
	private Double reduceCost;
    /**
     * 折扣券专用，表示打折额度
     */
	private Double discount;
    /**
     * 兑换券专用，填写兑换内容的名称
     */
	private String gift;
    /**
     * 优惠券专用，填写优惠详情
     */
	@TableField("defaultDetail")
	private String defaultDetail;
    /**
     * 卡券名
     */
	private String title;
    /**
     * 券名
     */
	@TableField("subTitle")
	private String subTitle;
    /**
     * 券颜色
     */
	private String color;
    /**
     * 卡券使用提醒
     */
	private String notice;
    /**
     * 卡券使用说明
     */
	private String description;
    /**
     * 使用时间的类型 0时间范围 1领取后时间端
     */
	private Integer type;
    /**
     * 起用时间
     */
	@TableField("beginTimestamp")
	private String beginTimestamp;
    /**
     * 结束时间
     */
	@TableField("endTimestamp")
	private String endTimestamp;
    /**
     * 自领取后多少天内有效
     */
	@TableField("fixedTerm")
	private Integer fixedTerm;
    /**
     * 自领取后多少天开始生效
     */
	@TableField("fixedBeginTerm")
	private Integer fixedBeginTerm;

    	@TableField("useTime")
	private Integer useTime;
    /**
     * 客服电话
     */
	@TableField("servicePhone")
	private String servicePhone;
    /**
     * 卡券领取页面是否可分享
     */
	@TableField("canShare")
	private Integer canShare;
    /**
     * 卡券是否可转赠
     */
	@TableField("canGiveFriend")
	private Integer canGiveFriend;
    /**
     * 封面摘要简介
     */
	private String summary;
    /**
     * 封面图片列表
     */
	@TableField("iconUrlList")
	private String iconUrlList;
    /**
     * 图文列表
     */
	@TableField("textImageList")
	private String textImageList;
    /**
     * 状态 0未审核 1审核中  2通过 3不通过 4过期
     */
	@TableField("cardStatus")
	private Integer cardStatus;

    	@TableField("createBusId")
	private Integer createBusId;

    /**
     * 创建时间
     */
	@TableField("createDate")
	private Date createDate;
    /**
     * 审核通过时间
     */
	private Date passTime;
    /**
     * 是否短信通知 0不通知 1通知
     */
	@TableField("isSendMsg")
	private Integer isSendMsg;
    /**
     * 电话区号
     */
	@TableField("areaCode")
	private String areaCode;
    /**
     * 手机号码
     */
	private String phone;
    /**
     * 是否允许自助核销 0不允许 1允许
     */
	@TableField("isOwnUse")
	private Integer isOwnUse;
    /**
     * 图片
     */
	private String image;
    /**
     * 1全部时段2部分时段
     */
	@TableField("timeType")
	private Integer timeType;
    /**
     * t_duofen_card_examine中的id 0代表审核通过或者还未送审
     */
	private Integer examineId;
    /**
     * 是否允许叠加使用 0不允许 1允许
     */
	@TableField("addUser")
	private Integer addUser;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofenCardNew{" +
			"id=" + id +
			", loginUrl=" + loginUrl +
			", busId=" + busId +
			", brandName=" + brandName +
			", useType=" + useType +
			", cardType=" + cardType +
			", dealDetail=" + dealDetail +
			", cashLeastCost=" + cashLeastCost +
			", reduceCost=" + reduceCost +
			", discount=" + discount +
			", gift=" + gift +
			", defaultDetail=" + defaultDetail +
			", title=" + title +
			", subTitle=" + subTitle +
			", color=" + color +
			", notice=" + notice +
			", description=" + description +
			", type=" + type +
			", beginTimestamp=" + beginTimestamp +
			", endTimestamp=" + endTimestamp +
			", fixedTerm=" + fixedTerm +
			", fixedBeginTerm=" + fixedBeginTerm +
			", servicePhone=" + servicePhone +
			", canShare=" + canShare +
			", canGiveFriend=" + canGiveFriend +
			", summary=" + summary +
			", iconUrlList=" + iconUrlList +
			", textImageList=" + textImageList +
			", cardStatus=" + cardStatus +
			", createDate=" + createDate +
			", passTime=" + passTime +
			", isSendMsg=" + isSendMsg +
			", areaCode=" + areaCode +
			", phone=" + phone +
			", isOwnUse=" + isOwnUse +
			", image=" + image +
			", timeType=" + timeType +
			", examineId=" + examineId +
			", addUser=" + addUser +
			"}";
	}
}
