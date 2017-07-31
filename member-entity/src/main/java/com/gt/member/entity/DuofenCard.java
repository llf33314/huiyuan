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
 * 多粉卡券
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_duofen_card")
public class DuofenCard extends Model<DuofenCard> {

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
     * 多粉卡券id
     */
	@TableField("card_id")
	private String cardId;
    /**
     * 商户名字
     */
	@TableField("brand_name")
	private String brandName;
    /**
     * 卡券类型 0折扣券 1代价券 2礼品券 3优惠券 4礼券
     */
	@TableField("card_type")
	private Integer cardType;
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
     * 满减门槛字段，可用于兑换券和代金券 ，填入后将在全面拼写消费满xx元可用
     */
	@TableField("least_cost")
	private Double leastCost;
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
     * 使用时段限制
     */
	@TableField("time_limit")
	private String timeLimit;
    /**
     * 状态 0未审核 1审核中  2通过 3不通过
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
	private Date passTime;
    /**
     * 卡券领取短信通知
     */
	private Integer isCallSMS;
    /**
     * 手机号码
     */
	private String phone;
    /**
     * 图片
     */
	private String image;
    /**
     * 1全部时段2部分时段
     */
	private Integer timeType;
    /**
     * 是否删除 0未删除 1已删除
     */
	private Integer iddelete;
    /**
     * 礼券 时间规则设置
     */
	private String dateTimeSet;
    /**
     * t_duofen_card_examine中的id 0代表审核通过或者还未送审
     */
	@TableField("examine_id")
	private Integer examineId;
    /**
     * 是否允许叠加使用 0不允许 1允许
     */
	private Integer addUser;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofenCard{" +
			"id=" + id +
			", logoUrl=" + logoUrl +
			", userId=" + userId +
			", publicId=" + publicId +
			", cardId=" + cardId +
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
			", servicePhone=" + servicePhone +
			", locationIdList=" + locationIdList +
			", canShare=" + canShare +
			", canGiveFriend=" + canGiveFriend +
			", leastCost=" + leastCost +
			", summary=" + summary +
			", iconUrlList=" + iconUrlList +
			", textImageList=" + textImageList +
			", timeLimit=" + timeLimit +
			", cardStatus=" + cardStatus +
			", ctime=" + ctime +
			", passTime=" + passTime +
			", isCallSMS=" + isCallSMS +
			", phone=" + phone +
			", image=" + image +
			", timeType=" + timeType +
			", iddelete=" + iddelete +
			", dateTimeSet=" + dateTimeSet +
			", examineId=" + examineId +
			", addUser=" + addUser +
			"}";
	}
}
