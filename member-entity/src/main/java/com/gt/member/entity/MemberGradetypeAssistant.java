package com.gt.member.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 会员卡副卡和主卡关系表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-16
 */
@Data
@Accessors(chain = true)
@TableName("t_member_gradetype_assistant")
public class MemberGradetypeAssistant extends Model<MemberGradetypeAssistant> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 会员卡模板id
     */
	@TableField("gtId")
	private Integer gtId;
    /**
     * 副卡会员卡类型
     */
	@TableField("ctId")
	private Integer ctId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberGradetypeAssistant{" +
			"id=" + id +
			", gtId=" + gtId +
			", ctId=" + ctId +
			"}";
	}
}
