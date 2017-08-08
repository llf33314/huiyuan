package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 卡包领取记录
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_duofen_card_receivelog")
public class DuofenCardReceivelog extends Model<DuofenCardReceivelog> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 卡包id
     */
	private Integer crId;
    /**
     * 粉丝id
     */
	private Integer memberId;
    /**
     * 领取时间
     */
	private Date createDate;
    /**
     * 第三方平台id  0不是来源第三方平台
     */
	private Integer threeMemberId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofenCardReceivelog{" +
			"id=" + id +
			", crId=" + crId +
			", memberId=" + memberId +
			", createDate=" + createDate +
			", threeMemberId=" + threeMemberId +
			"}";
	}
}
