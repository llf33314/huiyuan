package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

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
@TableName("t_duofencard_authorization")
public class DuofencardAuthorization extends Model<DuofencardAuthorization> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 粉丝openId
     */
	private String openId;
    /**
     * 0正常 1禁用 2已删除
     */
	private Integer status;
	private Integer shopId;
	private Integer busId;
	private Integer memberId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofencardAuthorization{" +
			"id=" + id +
			", openId=" + openId +
			", status=" + status +
			", shopId=" + shopId +
			", busId=" + busId +
			", memberId=" + memberId +
			"}";
	}
}
