package com.gt.common.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
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
 * @since 2017-10-19
 */
@Data
@Accessors(chain = true)
public class BusUserBranchRelation extends Model<BusUserBranchRelation> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * bus_user用户id
     */
	private Integer userid;
    /**
     * T_wx_shop 表的id
     */
	private Integer branchid;
    /**
     * 创建时间
     */
	@TableField("startTime")
	private String startTime;
    /**
     * 创建人
     */
	private Integer createpeople;
    /**
     * 是否管理员，0是，1不是
     */
	private Integer isadmin;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BusUserBranchRelation{" +
			"id=" + id +
			", userid=" + userid +
			", branchid=" + branchid +
			", startTime=" + startTime +
			", createpeople=" + createpeople +
			", isadmin=" + isadmin +
			"}";
	}
}
