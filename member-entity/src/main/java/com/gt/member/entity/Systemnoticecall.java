package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统消息记录
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
public class Systemnoticecall extends Model<Systemnoticecall> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 描述
     */
	private String describes;
    /**
     * 用户id
     */
	private Integer memberId;
    /**
     * 消息时间
     */
	private Date createDate;
    /**
     * 状态 0未查看 1已查看
     */
	private Integer status;
	private Integer publicId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Systemnoticecall{" +
			"id=" + id +
			", describes=" + describes +
			", memberId=" + memberId +
			", createDate=" + createDate +
			", status=" + status +
			", publicId=" + publicId +
			"}";
	}
}
