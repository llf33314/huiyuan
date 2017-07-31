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
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_member_applet_openid")
public class MemberAppletOpenid extends Model<MemberAppletOpenid> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 小程序openid
     */
	private String openid;
    /**
     * 会员表ID
     */
	@TableField("member_id")
	private Integer memberId;
    /**
     * 小程序类型，字典1191
     */
	private Integer style;
    /**
     * 商家ID
     */
	@TableField("bus_id")
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberAppletOpenid{" +
			"id=" + id +
			", openid=" + openid +
			", memberId=" + memberId +
			", style=" + style +
			", busId=" + busId +
			"}";
	}
}
