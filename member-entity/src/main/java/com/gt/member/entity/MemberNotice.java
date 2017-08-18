package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 通知信息表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_notice")
public class MemberNotice extends Model<MemberNotice> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 标题
     */
	private String title;
    /**
     * 通知图片路径
     */
	@TableField("img_url")
	private String imgUrl;
    /**
     *  -1:没有人群 0所有 逗号隔开
     */
	@TableField("notice_user")
	private String noticeUser;
    /**
     *  0立即方式 1定时发送 2保存
     */
	private Integer sendType;
    /**
     * 发送时间
     */
	private Date sendDate;
    /**
     * 短信发送方式 0不发送 1发送
     */
	private Integer sendSms;
    /**
     * 会员消息发送方式 0不发送 1发送
     */
	private Integer sendMsg;
    /**
     * 内容
     */
	private String content;
    /**
     * 创建时间
     */
	private Date createDate;
    /**
     * 创建人
     */
	private Integer createUser;
    /**
     * 公众号id
     */
	private Integer publicId;
    /**
     * 发送状态 0未发送 1、已发送 2 发送中
     */
	private Integer sendStuts;
    /**
     * 0:不通知 1通知某个人群
     */
	private Integer noticeMember;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberNotice{" +
			"id=" + id +
			", title=" + title +
			", imgUrl=" + imgUrl +
			", noticeUser=" + noticeUser +
			", sendType=" + sendType +
			", sendDate=" + sendDate +
			", sendSms=" + sendSms +
			", sendMsg=" + sendMsg +
			", content=" + content +
			", createDate=" + createDate +
			", createUser=" + createUser +
			", publicId=" + publicId +
			", sendStuts=" + sendStuts +
			", noticeMember=" + noticeMember +
			", busId=" + busId +
			"}";
	}
}
