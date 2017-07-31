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
 * 卡片类型表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_cardtype")
public class MemberCardtype extends Model<MemberCardtype> {

    private static final long serialVersionUID = 1L;

	@TableId(value="ct_id", type= IdType.AUTO)
	private Integer ctId;
    /**
     * 卡片所属类型 0:充值卡 1:非充值卡 字典表1063
     */
	@TableField("ct_cardType")
	private Integer ctCardType;
    /**
     * 卡片名称
     */
	@TableField("ct_name")
	private String ctName;


	@Override
	protected Serializable pkVal() {
		return this.ctId;
	}

	@Override
	public String toString() {
		return "MemberCardtype{" +
			"ctId=" + ctId +
			", ctCardType=" + ctCardType +
			", ctName=" + ctName +
			"}";
	}
}
