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
 * 会员资料设置信息表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_member_option")
public class MemberOption extends Model<MemberOption> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 姓名 0选填 1:必填
     */
	private Integer nameOption;
    /**
     * 性别 0选填 1:必填
     */
	private Integer sexOption;
    /**
     * 手机号 1必填 默认值1
     */
	private Integer phoneOption;
    /**
     * 地址 0选填 1:必填
     */
	private Integer addrOption;
    /**
     * 地址 0不显示 1显示
     */
	private Integer addrShow;
    /**
     * 详细地址 0选填 1:必填
     */
	private Integer addrDetailOption;
    /**
     * 详细地址 0不显示 1显示
     */
	private Integer addrDetailShow;
    /**
     * 邮箱  0选填 1:必填
     */
	private Integer mailOption;
    /**
     * 邮箱  0不显示 1显示
     */
	private Integer mailShow;
    /**
     * 出生日期 0选填 1:必填
     */
	private Integer birthOption;
    /**
     * 出生日期 0不显示 1显示
     */
	private Integer birthShow;
    /**
     * 身份证号码 0选填 1:必填
     */
	private Integer cardOption;
    /**
     * 身份证号码 0不显示 1显示
     */
	private Integer cardShow;
    /**
     * 上传身份证 0不显示 1显示
     */
	private Integer uploadCard;
    /**
     * 公众号id
     */
	private Integer publicId;
	private Integer nameShow1;
	private Integer sexShow1;
	private Integer addrShow1;
	private Integer addrDetailShow1;
	private Integer mailShow1;
	private Integer cardShow1;
	private Integer birthShow1;
	private Integer getMoneyOption;
	private Integer getMoneyShow;
	private Integer getMoneyShow1;
	private Integer busId;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberOption{" +
			"id=" + id +
			", nameOption=" + nameOption +
			", sexOption=" + sexOption +
			", phoneOption=" + phoneOption +
			", addrOption=" + addrOption +
			", addrShow=" + addrShow +
			", addrDetailOption=" + addrDetailOption +
			", addrDetailShow=" + addrDetailShow +
			", mailOption=" + mailOption +
			", mailShow=" + mailShow +
			", birthOption=" + birthOption +
			", birthShow=" + birthShow +
			", cardOption=" + cardOption +
			", cardShow=" + cardShow +
			", uploadCard=" + uploadCard +
			", publicId=" + publicId +
			", nameShow1=" + nameShow1 +
			", sexShow1=" + sexShow1 +
			", addrShow1=" + addrShow1 +
			", addrDetailShow1=" + addrDetailShow1 +
			", mailShow1=" + mailShow1 +
			", cardShow1=" + cardShow1 +
			", birthShow1=" + birthShow1 +
			", getMoneyOption=" + getMoneyOption +
			", getMoneyShow=" + getMoneyShow +
			", getMoneyShow1=" + getMoneyShow1 +
			", busId=" + busId +
			"}";
	}
}
