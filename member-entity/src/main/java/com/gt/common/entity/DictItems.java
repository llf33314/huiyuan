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
@TableName("t_man_dict_items")
public class DictItems extends Model<DictItems> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 主表ID
     */
	private Integer dictId;
    /**
     * 实际值
     */
	private String itemKey;
    /**
     * 显示值
     */
	private String itemValue;
    /**
     * 排序号
     */
	private Integer itemSort;
    /**
     * 备注
     */
	private String itemRemark;
    /**
     * 创建时间
     */
	private String itemCtime;
    /**
     * 状态
     */
	private Integer itemStatus;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DictItems{" +
			"id=" + id +
			", dictId=" + dictId +
			", itemKey=" + itemKey +
			", itemValue=" + itemValue +
			", itemSort=" + itemSort +
			", itemRemark=" + itemRemark +
			", itemCtime=" + itemCtime +
			", itemStatus=" + itemStatus +
			"}";
	}
}
