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
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_bossqr")
public class MemberBossqr extends Model<MemberBossqr> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer publicId;
    /**
     * 姓名
     */
	private String name;
    /**
     * 签名
     */
	private String autograph;
    /**
     * 头像
     */
	private String headImage;
    /**
     * 二维码图片
     */
	private String qrcode;
    /**
     * 备注
     */
	private String remarks;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberBossqr{" +
			"id=" + id +
			", publicId=" + publicId +
			", name=" + name +
			", autograph=" + autograph +
			", headImage=" + headImage +
			", qrcode=" + qrcode +
			", remarks=" + remarks +
			", busId=" + busId +
			"}";
	}
}
