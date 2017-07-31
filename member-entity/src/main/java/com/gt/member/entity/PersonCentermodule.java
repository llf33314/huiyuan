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
 * 模块设置
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_person_centermodule")
public class PersonCentermodule extends Model<PersonCentermodule> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 模块图片
     */
	private String moduleImage;
    /**
     * 模块名称
     */
	private String moduleName;
    /**
     * 模块地址
     */
	private String url;
	private Integer publicId;
    /**
     * 排序
     */
	private Integer sort;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PersonCentermodule{" +
			"id=" + id +
			", moduleImage=" + moduleImage +
			", moduleName=" + moduleName +
			", url=" + url +
			", publicId=" + publicId +
			", sort=" + sort +
			", busId=" + busId +
			"}";
	}
}
