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
@TableName("t_member_parameter")
public class MemberParameter extends Model<MemberParameter> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private Integer memberId;
	private Date signDate;
    /**
     * 省
     */
	private String provinceCode;
    /**
     * 市
     */
	private String cityCode;
    /**
     * 区
     */
	private String countyCode;
    /**
     * 地址
     */
	private String address;
    /**
     * 收入
     */
	private Double getMoney;
	private String headImg;
	private Integer arerId;
	private String arerCode;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberParameter{" +
			"id=" + id +
			", memberId=" + memberId +
			", signDate=" + signDate +
			", provinceCode=" + provinceCode +
			", cityCode=" + cityCode +
			", countyCode=" + countyCode +
			", address=" + address +
			", getMoney=" + getMoney +
			", headImg=" + headImg +
			"}";
	}
}
