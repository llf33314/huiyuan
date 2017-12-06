package com.gt.member.entity;

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
 * 商户系统通知表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("system_notice")
public class SystemNotice extends Model<SystemNotice> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 提醒类型
     */
	private Integer callType;
    /**
     * 消息提醒 0不提醒 1提醒
     */
	private Integer msgStatus;
	private Integer publicId;
    /**
     * 短信提醒 0不提醒 1提醒
     */
	private Integer smsStatus;
	private Integer publicMsg;
	private Integer publicIdMsgId;
	private Integer busId;
    /**
     * 消息内容
     */
	private String msgContent;
    /**
     * 短信内容
     */
	private String smsContent;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SystemNotice{" +
			"id=" + id +
			", callType=" + callType +
			", msgStatus=" + msgStatus +
			", publicId=" + publicId +
			", smsStatus=" + smsStatus +
			", publicMsg=" + publicMsg +
			", publicIdMsgId=" + publicIdMsgId +
			", busId=" + busId +
			", msgContent=" + msgContent +
			", smsContent=" + smsContent +
			"}";
	}
}
