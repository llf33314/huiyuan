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
@TableName("t_offlinepay_detail")
public class OfflinepayDetail extends Model<OfflinepayDetail> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 父类id
     */
	private Integer offId;
    /**
     * 周 星期1 2 3 4 5 6 7 月例如20 年：02/05表示 2月5号
     */
	private String dayTime;
    /**
     * 开始时间
     */
	private Integer beginTime;
    /**
     * 结束时间
     */
	private Integer endTime;
    /**
     * 每消费
     */
	private Double eachMoney;
    /**
     * 减多少
     */
	private Double reduceMoney;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "OfflinepayDetail{" +
			"id=" + id +
			", offId=" + offId +
			", dayTime=" + dayTime +
			", beginTime=" + beginTime +
			", endTime=" + endTime +
			", eachMoney=" + eachMoney +
			", reduceMoney=" + reduceMoney +
			"}";
	}
}
