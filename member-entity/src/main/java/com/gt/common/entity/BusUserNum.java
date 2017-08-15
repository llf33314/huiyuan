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
@TableName("bus_user_num")
public class BusUserNum extends Model<BusUserNum> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 商家bus_user的id
     */
	private Integer busUserId;
    /**
     * 属性：1：微场景，2：微名片；3：微预约；4：微会员；5：微商城；6：微酒店；7：微相册；8：微邀请
     */
	private Integer style;
    /**
     * 数量，特殊值得话，用，区分
     */
	private String usernum;
    /**
     * 创建人
     */
	private Integer createperson;
    /**
     * 创建时间
     */
	private String createtime;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BusUserNum{" +
			"id=" + id +
			", busUserId=" + busUserId +
			", style=" + style +
			", usernum=" + usernum +
			", createperson=" + createperson +
			", createtime=" + createtime +
			"}";
	}
}
