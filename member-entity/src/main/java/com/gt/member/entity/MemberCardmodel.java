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
 * 卡片模板表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_cardmodel")
public class MemberCardmodel extends Model<MemberCardmodel> {

    private static final long serialVersionUID = 1L;

	@TableId(value="cm_id", type= IdType.AUTO)
	private Integer cmId;
    /**
     * 模板地址url
     */
	@TableField("cm_url")
	private String cmUrl;
    /**
     * 模板类型
     */
	@TableField("cm_type")
	private Integer cmType;
    /**
     * 排序值
     */
	@TableField("cm_sort")
	private Integer cmSort;
	private Integer publicId;
	@TableField("cm_name")
	private String cmName;
	private Integer creatpeople;
	private String creattime;
	@TableField("cm_state")
	private Integer cmState;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.cmId;
	}

	@Override
	public String toString() {
		return "MemberCardmodel{" +
			"cmId=" + cmId +
			", cmUrl=" + cmUrl +
			", cmType=" + cmType +
			", cmSort=" + cmSort +
			", publicId=" + publicId +
			", cmName=" + cmName +
			", creatpeople=" + creatpeople +
			", creattime=" + creattime +
			", cmState=" + cmState +
			", busId=" + busId +
			"}";
	}
}
