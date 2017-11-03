package com.gt.common.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商家购买流量表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-11-02
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_bus_flow")
public class BusFlow extends Model<BusFlow> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 商家主键
     */
	private Integer busId;
    /**
     * 运营商 1：电信  2 移动  3 联通
     */
	private Integer operateType;
    /**
     * 规格：10M 30M 100M 200M 500M
     */
	private Integer type;
    /**
     * 数量
     */
	private Integer count;
    /**
     * 覆盖区域 省内
     */
	private Integer scope;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BusFlow{" +
			"id=" + id +
			", busId=" + busId +
			", operateType=" + operateType +
			", type=" + type +
			", count=" + count +
			", scope=" + scope +
			"}";
	}
}
