package com.gt.common.entity;

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
 * @since 2017-08-15
 */
@Data
@Accessors(chain = true)
@TableName("t_man_dict")
public class Dict extends Model<Dict> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private String dictType;
	private String dictDescribe;
	private Integer dictSort;
	private String dictRemark;
	private String dictCtime;
	private String opt;
    /**
     * 状态
     */
	private Integer dictStatus;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Dict{" +
			"id=" + id +
			", dictType=" + dictType +
			", dictDescribe=" + dictDescribe +
			", dictSort=" + dictSort +
			", dictRemark=" + dictRemark +
			", dictCtime=" + dictCtime +
			", opt=" + opt +
			", dictStatus=" + dictStatus +
			"}";
	}
}
