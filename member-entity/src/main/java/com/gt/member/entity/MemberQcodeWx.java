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
@TableName("t_member_qcodeWx")
public class MemberQcodeWx extends Model<MemberQcodeWx> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 微信生成url地址
     */
	private String codeUrl;
    /**
     * 商家id
     */
	private Integer busId;
    /**
     * 商家类型 0商家id 1员工id
     */
	private Integer busType;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberQcodeWx{" +
			"id=" + id +
			", codeUrl=" + codeUrl +
			", busId=" + busId +
			", busType=" + busType +
			"}";
	}
}
