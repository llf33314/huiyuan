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
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-16
 */
@Data
@Accessors(chain = true)
@TableName("t_duofen_card_get_new")
public class DuofenCardGetNew extends Model<DuofenCardGetNew> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 卡券id
     */
	@TableField("cardId")
	private Integer cardId;
    /**
     * 获取方式取字典
     */
	@TableField("getType")
	private Integer getType;
    /**
     * 卡券核销编码
     */
	private String code;
    /**
     * 卡券状态 0未使用 1已使用 2过期
     */
	private Integer state;
    /**
     * 领取时间
     */
	@TableField("getDate")
	private Date getDate;
	@TableField("memberId")
	private Integer memberId;
    /**
     * 卡券有效开始时间
     */
	@TableField("startTime")
	private Date startTime;
    /**
     * 卡券有效结束时间
     */
	@TableField("endTime")
	private Date endTime;
    /**
     * 商户id
     */
	@TableField("busId")
	private Integer busId;
    /**
     * 核销门店id
     */
	@TableField("storeId")
	private Integer storeId;
    /**
     * 核销人员
     */
	@TableField("operateBusId")
	private Integer operateBusId;
    /**
     * 核销时间
     */
	@TableField("operateDate")
	private Date operateDate;
    /**
     * 推荐表中id
     */
	@TableField("recommendId")
	private Integer recommendId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofenCardGetNew{" +
			"id=" + id +
			", cardId=" + cardId +
			", getType=" + getType +
			", code=" + code +
			", state=" + state +
			", getDate=" + getDate +
			", memberId=" + memberId +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", busId=" + busId +
			", storeId=" + storeId +
			", operateBusId=" + operateBusId +
			", operateDate=" + operateDate +
			", recommendId=" + recommendId +
			"}";
	}
}
