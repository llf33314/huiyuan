package com.gt.member.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 会员日志 用来只做异常查询问题
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-14
 */
@Data
@Accessors(chain = true)
@TableName("t_member_log")
public class MemberLog extends Model<MemberLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 日志描述记录
     */
	private String logtxt;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberLog{" +
			"id=" + id +
			", logtxt=" + logtxt +
			"}";
	}
}
