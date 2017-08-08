package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 多粉卡卷审核情况
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_duofen_card_examine")
public class DuofenCardExamine extends Model<DuofenCardExamine> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * t_duofen_card 多粉卡券id
     */
	@TableField("duofen_card_id")
	private Integer duofenCardId;
    /**
     * 状态 0 通过 1不通过
     */
	@TableField("card_status")
	private Integer cardStatus;
    /**
     * 审核时间
     */
	private String ctime;
    /**
     * t_man_user 审核人
     */
	@TableField("man_user_id")
	private Integer manUserId;
    /**
     * 审核不通过的原因
     */
	private String remark;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "DuofenCardExamine{" +
			"id=" + id +
			", duofenCardId=" + duofenCardId +
			", cardStatus=" + cardStatus +
			", ctime=" + ctime +
			", manUserId=" + manUserId +
			", remark=" + remark +
			"}";
	}
}
