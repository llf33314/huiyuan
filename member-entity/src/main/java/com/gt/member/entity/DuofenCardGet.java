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
@TableName("t_duofen_card_get")
public class DuofenCardGet extends Model<DuofenCardGet> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 卡券id
     */
	private Integer cardId;
    /**
     * 获取方式0商场 1会员卡 2普通领取 3第三方商场 4ERP 5游戏 6二维码推荐等
     */
	private Integer getType;
    /**
     * 卡券编码
     */
	private String code;
    /**
     * 卡券状态 0未使用 1已使用 2过期
     */
	private Integer state;
    /**
     * 领取时间
     */
	private Date getDate;
    /**
     * 卡包id
     */
	private Integer cardReceiveId;
	private Integer memberId;
	private Integer publicId;
    /**
     * 卡券有效开始时间
     */
	private Date startTime;
    /**
     * 卡券有效结束时间
     */
	private Date endTime;
    /**
     * 朋友粉丝id逗号隔开
     */
	private String friendMemberId;
    /**
     * 商户id
     */
	private Integer busId;
    /**
     * 核销门店id
     */
	private Integer storeId;
    /**
     * 推荐表中id
     */
	private Integer recommendId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofenCardGet{" +
			"id=" + id +
			", cardId=" + cardId +
			", getType=" + getType +
			", code=" + code +
			", state=" + state +
			", getDate=" + getDate +
			", cardReceiveId=" + cardReceiveId +
			", memberId=" + memberId +
			", publicId=" + publicId +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", friendMemberId=" + friendMemberId +
			", busId=" + busId +
			", storeId=" + storeId +
			", recommendId=" + recommendId +
			"}";
	}
}
