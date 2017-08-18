package com.gt.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import java.math.BigDecimal;
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
 * @since 2017-08-18
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_fenbi_flow_record")
public class FenbiFlowRecord extends Model<FenbiFlowRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键表
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 用户ID
     */
	private Integer busUserId;
    /**
     * 1：粉币 2：流量
     */
	private Integer recType;
    /**
     * 总数量
     */
	private BigDecimal recCount;
    /**
     * 已使用数量
     */
	private BigDecimal recUseCount;
    /**
     * 创建时间
     */
	@TableField("rec_createTime")
	private Date recCreateTime;
    /**
     * 描述
     */
	private String recDesc;
    /**
     * 冻结类型1-转盘活动2、签到活动 3刮刮乐 4页面红包
     */
	private Integer recFreezeType;
    /**
     * 外键ID(如大转盘，刮刮乐，红包等具体数据ID)
     */
	private Integer recFkId;
    /**
     * 是否回滚 1：否 2：是
     */
	private Integer rollStatus;
    /**
     * 流量类型
     */
	private Integer flowType;
    /**
     * 流量表ID
     */
	private Integer flowId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "FenbiFlowRecord{" +
			"id=" + id +
			", busUserId=" + busUserId +
			", recType=" + recType +
			", recCount=" + recCount +
			", recUseCount=" + recUseCount +
			", recCreateTime=" + recCreateTime +
			", recDesc=" + recDesc +
			", recFreezeType=" + recFreezeType +
			", recFkId=" + recFkId +
			", rollStatus=" + rollStatus +
			", flowType=" + flowType +
			", flowId=" + flowId +
			"}";
	}
}
