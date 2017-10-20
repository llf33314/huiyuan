package com.gt.member.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
 * 通知用户关系表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-18
 */
@Data
@Accessors(chain = true)
@TableName("t_member_noticeuser")
public class MemberNoticeuser extends Model<MemberNoticeuser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
	@TableField("busId")
	private Integer busId;
    /**
     * 通知信息id
     */
	@TableField("noticeId")
	private Integer noticeId;
    /**
     * 0短信 1消息
     */
	@TableField("msgType")
	private Integer msgType;
    /**
     * 信息状态 0未查看 1已查看
     */
	private Integer status;
	@TableField("sendDate")
	private Date sendDate;
	@TableField("createDate")
	private Date createDate;
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberNoticeuser{" +
			"busId=" + busId +
			", noticeId=" + noticeId +
			", msgType=" + msgType +
			", status=" + status +
			", sendDate=" + sendDate +
			", createDate=" + createDate +
			", id=" + id +
			"}";
	}
}
