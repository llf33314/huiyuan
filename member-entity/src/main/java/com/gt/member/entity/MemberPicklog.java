package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import java.util.Date;
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
@TableName("t_member_picklog")
public class MemberPicklog extends Model<MemberPicklog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer memberId;
	private Integer busId;
    /**
     * 提取金额
     */
	private Double pickMoney;
    /**
     * 提取时间
     */
	private Date pickDate;

	private Integer pickType;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberPicklog{" +
			"id=" + id +
			", memberId=" + memberId +
			", busId=" + busId +
			", pickMoney=" + pickMoney +
			", pickDate=" + pickDate +
			"}";
	}
}
