package com.gt.member.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-12-08
 */
@Data
@Accessors(chain = true)
@TableName("t_member_old_id")
public class MemberOldId extends Model<MemberOldId > {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 最新粉丝id
     */
	@TableField("memberId")
	private Integer memberId;
    /**
     * 旧的粉丝id
     */
	@TableField("oldId")
	private Integer oldId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberOldId{" +
			"id=" + id +
			", memberId=" + memberId +
			", oldId=" + oldId +
			"}";
	}
}
