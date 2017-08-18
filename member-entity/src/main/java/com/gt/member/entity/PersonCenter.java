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
 * 个人中心设置
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_person_center")
public class PersonCenter extends Model<PersonCenter> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer publicId;
    /**
     * 背景图片
     */
	private String backimage;
    /**
     * 格式 0竖状 1九宫格 2十六宫格
     */
	private Integer format;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PersonCenter{" +
			"id=" + id +
			", publicId=" + publicId +
			", backimage=" + backimage +
			", format=" + format +
			", busId=" + busId +
			"}";
	}
}
