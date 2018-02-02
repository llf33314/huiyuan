package com.gt.duofencard.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author pengjiangli
 * @since 2018-01-23
 */
@Data
@Accessors(chain = true)
@TableName("t_duofen_card_share")
public class DuofenCardShare extends Model<DuofenCardShare> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * t_duofen_card_get_new 中id
     */
	@TableField("getId")
	private Integer getId;

   	 @TableField("memberId")
	private Integer memberId;

    	@TableField("shareDate")
   	 private Date shareDate;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofenCardShare{" +
			"id=" + id +
			", getId=" + getId +
			"}";
	}
}
