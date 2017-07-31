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
@TableName("t_member_find")
public class MemberFind extends Model<MemberFind> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private String title;
	private String image;
	private String url;
	private String text;
	private Integer model;
	private Integer publicId;
	private Integer type;
	private String color;
	private Integer soure;
	private Integer integral;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberFind{" +
			"id=" + id +
			", title=" + title +
			", image=" + image +
			", url=" + url +
			", text=" + text +
			", model=" + model +
			", publicId=" + publicId +
			", type=" + type +
			", color=" + color +
			", soure=" + soure +
			", integral=" + integral +
			", busId=" + busId +
			"}";
	}
}
