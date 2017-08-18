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
 * 赠送物品信息表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_goodstype")
public class MemberGoodstype extends Model<MemberGoodstype> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="gt_id", type= IdType.AUTO)
	private Integer gtId;
    /**
     * 物品名称
     */
	@TableField("gt_name")
	private String gtName;
    /**
     * 是否赠送流量 0:是 1:否 0是查询流量字典表1054
     */
	@TableField("gt_flow")
	private Integer gtFlow;
    /**
     * 商户id 0:代表平台
     */
	@TableField("public_id")
	private Integer publicId;
    /**
     * 单位
     */
	@TableField("gt_unit")
	private String gtUnit;
    /**
     * 等级类型主键
     */
	@TableField("gType_id")
	private Integer gTypeId;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.gtId;
	}

	@Override
	public String toString() {
		return "MemberGoodstype{" +
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
