package com.gt.member.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统通知类型
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("system_calltype")
public class SystemCalltype extends Model<SystemCalltype> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 名称
     */
	private String name;
    /**
     * 父类id
     */
	private Integer parentId;
	private Integer isOpenMsg;
	private Integer isOpenSms;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SystemCalltype{" +
			"id=" + id +
			", name=" + name +
			", parentId=" + parentId +
			", isOpenMsg=" + isOpenMsg +
			", isOpenSms=" + isOpenSms +
			"}";
	}
}
