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
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_card_receive")
public class WxCardReceive extends Model<WxCardReceive> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 公众号表ID
     */
	@TableField("public_id")
	private Integer publicId;
    /**
     * 原始ID
     */
	private String username;
    /**
     * 领取人openid
     */
	private String openid;
    /**
     * 赠送方账号"IsGiveByFriend”为1时填写该参数
     */
	@TableField("friend_user_name")
	private String friendUserName;
    /**
     * 领取时间
     */
	private Date ctime;
    /**
     * 卡券ID
     */
	@TableField("card_id")
	private String cardId;
    /**
     * 是否为转赠，1代表是，0代表否
     */
	@TableField("is_give_by_friend")
	private Integer isGiveByFriend;
    /**
     * code序列号
     */
	@TableField("user_card_code")
	private String userCardCode;
    /**
     * 转赠前的code序列号
     */
	@TableField("old_user_card_code")
	private String oldUserCardCode;
    /**
     * 领取场景值
     */
	@TableField("outer_id")
	private Integer outerId;
    /**
     * 状态 0正常 1删除 4已用
     */
	private Integer status;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "WxCardReceive{" +
			"id=" + id +
			", publicId=" + publicId +
			", username=" + username +
			", openid=" + openid +
			", friendUserName=" + friendUserName +
			", ctime=" + ctime +
			", cardId=" + cardId +
			", isGiveByFriend=" + isGiveByFriend +
			", userCardCode=" + userCardCode +
			", oldUserCardCode=" + oldUserCardCode +
			", outerId=" + outerId +
			", status=" + status +
			"}";
	}
}
