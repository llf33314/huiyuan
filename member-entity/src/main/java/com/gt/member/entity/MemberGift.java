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
 * 会员赠送积分 粉币 流量 优惠券
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_gift")
public class MemberGift extends Model<MemberGift> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 赠送方式 0泛会员升级成为正式会员 1泛会员完善资料 2会员完善资料
     */
	private Integer modelCode;
    /**
     * 积分
     */
	private Double jifen;
    /**
     * 粉币
     */
	private Double fenbi;
    /**
     * 流量
     */
	private Integer flow;
    /**
     * 优惠券地址
     */
	private String cardUrl;
	private Integer publicId;
    /**
     * 描述
     */
	private String title;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberGift{" +
			"id=" + id +
			", modelCode=" + modelCode +
			", jifen=" + jifen +
			", fenbi=" + fenbi +
			", flow=" + flow +
			", cardUrl=" + cardUrl +
			", publicId=" + publicId +
			", title=" + title +
			", busId=" + busId +
			"}";
	}
}
