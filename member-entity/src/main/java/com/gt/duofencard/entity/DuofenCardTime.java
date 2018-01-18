package com.gt.duofencard.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
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
 * @since 2018-01-16
 */
@Data
@Accessors(chain = true)
@TableName("t_duofen_card_time")
public class DuofenCardTime extends Model<DuofenCardTime> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;

    	@TableField("cardId")
	private Integer cardId;
    /**
     * 一周不可用时间
     */
	private String week;
    /**
     * 节假日不可用
     */
	private Integer holidays;
    /**
     * 其他时间端 0未设置 1已设置
     */
	@TableField("otherTime")
	private Integer otherTime;
    /**
     * 其他时间设置
     */
	@TableField("otherTimeSet")
	private String otherTimeSet;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofenCardTime{" +
			"id=" + id +
			", week=" + week +
			", holidays=" + holidays +
			", otherTime=" + otherTime +
			", otherTimeSet=" + otherTimeSet +
			"}";
	}
}
