package com.gt.common.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
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
 * @since 2017-10-31
 */
@Data
@Accessors(chain = true)
public class BasisCity extends Model<BasisCity> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private String cityCode;
	private String cityName;
	private Integer cityParent;
	private Integer cityLevel;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BasisCity{" +
			"id=" + id +
			", cityCode=" + cityCode +
			", cityName=" + cityName +
			", cityParent=" + cityParent +
			", cityLevel=" + cityLevel +
			"}";
	}
}
