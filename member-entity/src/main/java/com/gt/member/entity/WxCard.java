package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
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
 * 微信卡券
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_card")
public class WxCard extends Model<WxCard> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 卡券的商户logo
     */
	@TableField("logo_url")
	private String logoUrl;
    /**
     * 商家id
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 公众号表id
     */
	@TableField("public_id")
	private Integer publicId;
    /**
     * 微信卡券id
     */
	@TableField("card_id")
	private String cardId;
    /**
     * 卡券码型
     */
	@TableField("code_type")
	private String codeType;
    /**
     * 商户名字
     */
	@TableField("brand_name")
	private String brandName;
    /**
     * 卡券类型
     */
	@TableField("card_type")
	private String cardType;
    /**
     * 团购券专用，团购详情。
     */
	@TableField("deal_detail")
	private String dealDetail;
    /**
     * 表示起用金额(代金券)
     */
	@TableField("cash_least_cost")
	private Double cashLeastCost;
    /**
     * 减免金额(代金券)
     */
	@TableField("reduce_cost")
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
	@TableField("default_detail")
	private String defaultDetail;
    /**
     * 卡券名
     */
	private String title;
    /**
     * 券名
     */
	@TableField("sub_title")
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
     * 卡券库存的数量
     */
	private Integer quantity;
    /**
     * 使用时间的类型
     */
	private String type;
    /**
     * 起用时间
     */
	@TableField("begin_timestamp")
	private Date beginTimestamp;
    /**
     * 结束时间
     */
	@TableField("end_timestamp")
	private Date endTimestamp;
    /**
     * 自领取后多少天内有效
     */
	@TableField("fixed_term")
	private Integer fixedTerm;
    /**
     * 自领取后多少天开始生效
     */
	@TableField("fixed_begin_term")
	private Integer fixedBeginTerm;
    /**
     * (默认值即可)是否自定义Code码
     */
	@TableField("use_custom_code")
	private Integer useCustomCode;
    /**
     * (默认值即可)是否指定用户领取
     */
	@TableField("bind_openid")
	private Integer bindOpenid;
    /**
     * 客服电话
     */
	@TableField("service_phone")
	private String servicePhone;
    /**
     * 使用门店ID，多个用逗号隔开
     */
	@TableField("location_id_list")
	private String locationIdList;
    /**
     * (暂时不用)第三方来源名
     */
	private String source;
    /**
     * 自定义跳转外链的入口名字
     */
	@TableField("custom_url_name")
	private String customUrlName;
    /**
     * 卡券顶部居中的按钮
     */
	@TableField("center_title")
	private String centerTitle;
    /**
     * 显示在入口下方的提示语
     */
	@TableField("center_sub_title")
	private String centerSubTitle;
    /**
     * 顶部居中的url
     */
	@TableField("center_url")
	private String centerUrl;
    /**
     * 自定义跳转的URL
     */
	@TableField("custom_url")
	private String customUrl;
    /**
     * 显示在入口右侧的提示语
     */
	@TableField("custom_url_sub_title")
	private String customUrlSubTitle;
    /**
     * 营销场景的自定义入口名称
     */
	@TableField("promotion_url_name")
	private String promotionUrlName;
    /**
     * 显示在营销入口右侧的提示语
     */
	@TableField("promotion_url")
	private String promotionUrl;
    /**
     * 显示在营销入口右侧的提示语
     */
	@TableField("promotion_url_sub_title")
	private String promotionUrlSubTitle;
    /**
     * 每人可领券的数量限制,不填写默认为50
     */
	@TableField("get_limit")
	private Integer getLimit;
    /**
     * 卡券领取页面是否可分享
     */
	@TableField("can_share")
	private Integer canShare;
    /**
     * 卡券是否可转赠
     */
	@TableField("can_give_friend")
	private Integer canGiveFriend;
    /**
     * 指定可用的商品类目，仅用于代金券类型 ，填入后将在券面拼写指定xx可用
     */
	@TableField("accept_category")
	private String acceptCategory;
    /**
     * 指定可用的商品类目，仅用于代金券类型 ，填入后将在券面拼写指定xx不可用
     */
	@TableField("reject_category")
	private String rejectCategory;
    /**
     * 满减门槛字段，可用于兑换券和代金券 ，填入后将在全面拼写消费满xx元可用
     */
	@TableField("least_cost")
	private Double leastCost;
    /**
     * 购买xx可用类型门槛，仅用于兑换 ，填入后自动拼写购买xxx可用。
     */
	@TableField("object_use_for")
	private String objectUseFor;
    /**
     * (默认值即可)不可以与其他类型共享门槛，填写false时系统将在使用须知里 拼写不可与其他优惠共享
     */
	@TableField("can_use_with_other_discount")
	private Integer canUseWithOtherDiscount;
    /**
     * 封面摘要简介
     */
	private String summary;
    /**
     * 封面图片列表
     */
	@TableField("icon_url_list")
	private String iconUrlList;
    /**
     * 图文列表
     */
	@TableField("text_image_list")
	private String textImageList;
    /**
     * (暂时不用)商家服务类型
     */
	@TableField("business_service")
	private String businessService;
    /**
     * 使用时段限制
     */
	@TableField("time_limit")
	private String timeLimit;
    /**
     * 状态
     */
	@TableField("card_status")
	private Integer cardStatus;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 审核通过时间
     */
	@TableField("pass_time")
	private Integer passTime;
	private Integer isCallSMS;
	private String phone;
	private String image;
	private Integer isDelivery;
    /**
     * 1全部时段2部分时段
     */
	private Integer timeType;
    /**
     * 是否删除 0未删除 1已删除
     */
	private Integer iddelete;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "WxCard{" +
			"id=" + id +
			", logoUrl=" + logoUrl +
			", userId=" + userId +
			", publicId=" + publicId +
			", cardId=" + cardId +
			", codeType=" + codeType +
			", brandName=" + brandName +
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
			", quantity=" + quantity +
			", type=" + type +
			", beginTimestamp=" + beginTimestamp +
			", endTimestamp=" + endTimestamp +
			", fixedTerm=" + fixedTerm +
			", fixedBeginTerm=" + fixedBeginTerm +
			", useCustomCode=" + useCustomCode +
			", bindOpenid=" + bindOpenid +
			", servicePhone=" + servicePhone +
			", locationIdList=" + locationIdList +
			", source=" + source +
			", customUrlName=" + customUrlName +
			", centerTitle=" + centerTitle +
			", centerSubTitle=" + centerSubTitle +
			", centerUrl=" + centerUrl +
			", customUrl=" + customUrl +
			", customUrlSubTitle=" + customUrlSubTitle +
			", promotionUrlName=" + promotionUrlName +
			", promotionUrl=" + promotionUrl +
			", promotionUrlSubTitle=" + promotionUrlSubTitle +
			", getLimit=" + getLimit +
			", canShare=" + canShare +
			", canGiveFriend=" + canGiveFriend +
			", acceptCategory=" + acceptCategory +
			", rejectCategory=" + rejectCategory +
			", leastCost=" + leastCost +
			", objectUseFor=" + objectUseFor +
			", canUseWithOtherDiscount=" + canUseWithOtherDiscount +
			", summary=" + summary +
			", iconUrlList=" + iconUrlList +
			", textImageList=" + textImageList +
			", businessService=" + businessService +
			", timeLimit=" + timeLimit +
			", cardStatus=" + cardStatus +
			", ctime=" + ctime +
			", passTime=" + passTime +
			", isCallSMS=" + isCallSMS +
			", phone=" + phone +
			", image=" + image +
			", isDelivery=" + isDelivery +
			", timeType=" + timeType +
			", iddelete=" + iddelete +
			"}";
	}
}
