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

	@TableId(value="receiveId", type= IdType.AUTO)
	private Integer receiveId;
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

    	@TableField("isbuy")
	private Integer isbuy;
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

	@TableField("verificationType")
	private Integer verificationType;
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
    /**
     * 使用来源 1=mobile 2=pc
     */
	@TableField("useLocation")
	private Integer useLocation;


	@Override
	protected Serializable pkVal() {
		return this.receiveId;
	}

	@Override
	public String toString() {
		return "DuofenCardGetNew{" +
			"id=" + receiveId +
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
