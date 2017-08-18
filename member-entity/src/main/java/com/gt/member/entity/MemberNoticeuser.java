package com.gt.member.entity;

import java.util.Date;
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
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_noticeuser")
public class MemberNoticeuser extends Model<MemberNoticeuser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
	private Integer busId;
    /**
     * 通知信息id
     */
	private Integer noticeId;
    /**
     * 0短信 1消息
     */
	private Integer msgType;
    /**
     * 信息状态 0未查看 1已查看
     */
	private Integer status;
	private Date sendDate;
	private Date createDate;


	@Override
	protected Serializable pkVal() {
		return this.busId;
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
			"}";
	}
}
