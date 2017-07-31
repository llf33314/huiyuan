package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
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
@TableName("t_wx_bus_member_old")
public class MemberOld extends Model<MemberOld> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	@TableField("df_member_id")
	private Integer dfMemberId;
	@TableField("public_id")
	private Integer publicId;
	@TableField("fans_currency")
	private Double fansCurrency;
	private Integer flow;
	private Integer integral;
	private Integer level;
	private String openid;
	private String phone;
	private String nickname;
	private Integer sex;
	private String province;
	private String city;
	private String country;
	private String headimgurl;
	@TableField("operate_type")
	private Integer operateType;
	@TableField("mc_id")
	private Integer mcId;
	private Double totalMoney;
	private Integer totalIntegral;
	private String address;
	private String email;
	private String cardId;
	private String cardImgback;
	private String cardImg;
	private Date birth;
	private Date currencyDate;
	private Date flowDate;
	private Date integralDate;
	private Integer shareCount;
	private Date shareDate;
	private String remark;
	private Integer cardChecked;
	private String name;
	private Integer isBuy;
	private Integer isSubscribe;
	@TableField("scence_code")
	private String scenceCode;
	private Integer busId;
	private String pwd;
	private Integer loginMode;
	private String oldId;
	private Date subscribeDate;
	private String openid2;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "MemberOld{" +
			"id=" + id +
			", dfMemberId=" + dfMemberId +
			", publicId=" + publicId +
			", fansCurrency=" + fansCurrency +
			", flow=" + flow +
			", integral=" + integral +
			", level=" + level +
			", openid=" + openid +
			", phone=" + phone +
			", nickname=" + nickname +
			", sex=" + sex +
			", province=" + province +
			", city=" + city +
			", country=" + country +
			", headimgurl=" + headimgurl +
			", operateType=" + operateType +
			", mcId=" + mcId +
			", totalMoney=" + totalMoney +
			", totalIntegral=" + totalIntegral +
			", address=" + address +
			", email=" + email +
			", cardId=" + cardId +
			", cardImgback=" + cardImgback +
			", cardImg=" + cardImg +
			", birth=" + birth +
			", currencyDate=" + currencyDate +
			", flowDate=" + flowDate +
			", integralDate=" + integralDate +
			", shareCount=" + shareCount +
			", shareDate=" + shareDate +
			", remark=" + remark +
			", cardChecked=" + cardChecked +
			", name=" + name +
			", isBuy=" + isBuy +
			", isSubscribe=" + isSubscribe +
			", scenceCode=" + scenceCode +
			", busId=" + busId +
			", pwd=" + pwd +
			", loginMode=" + loginMode +
			", oldId=" + oldId +
			", subscribeDate=" + subscribeDate +
			", openid2=" + openid2 +
			"}";
	}
}
