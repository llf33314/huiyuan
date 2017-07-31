package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
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
@TableName("t_member_date")
public class MemberDate extends Model<MemberDate> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer publicId;
	private Integer ctId;
	private Integer dateType;
	private String dateStr;
	private Integer discount;
	@TableField("fans_currency")
	private Integer fansCurrency;
	private Integer flow;
	private Integer integral;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberDate{" +
			"id=" + id +
			", publicId=" + publicId +
			", ctId=" + ctId +
			", dateType=" + dateType +
			", dateStr=" + dateStr +
			", discount=" + discount +
			", fansCurrency=" + fansCurrency +
			", flow=" + flow +
			", integral=" + integral +
			", busId=" + busId +
			"}";
	}
}
