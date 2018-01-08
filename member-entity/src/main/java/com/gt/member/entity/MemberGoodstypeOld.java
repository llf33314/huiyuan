package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 赠送物品信息表
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-08
 */
@Data
@Accessors(chain = true)
@TableName("t_member_goodstype_old")
public class MemberGoodstypeOld extends Model<MemberGoodstypeOld> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Integer gtId;
    /**
     * 物品名称
     */
	private String gtName;
    /**
     * 是否赠送流量 0:是 1:否 0是查询流量字典表1054
     */
	private Integer gtFlow;
    /**
     * 商户id 0:代表平台
     */
	private Integer publicId;
    /**
     * 单位
     */
	private String gtUnit;
    /**
     * 等级类型主键
     */
	@TableField("gType_id")
	private Integer gTypeId;
	@TableField("busId")
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.gtId;
	}

	@Override
	public String toString() {
		return "MemberGoodstypeOld{" +
			"gtId=" + gtId +
			", gtName=" + gtName +
			", gtFlow=" + gtFlow +
			", publicId=" + publicId +
			", gtUnit=" + gtUnit +
			", gTypeId=" + gTypeId +
			", busId=" + busId +
			"}";
	}
}
