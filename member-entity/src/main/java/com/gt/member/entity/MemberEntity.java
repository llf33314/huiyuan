package com.gt.member.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商家会员表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-02
 */
@Data
@Accessors( chain = true )
@TableName( "t_wx_bus_member" )
public class MemberEntity extends Model< MemberEntity > {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 多粉会员ID
     */
    @TableField( "df_member_id" )
    private Integer dfMemberId;
    /**
     * 公众号表ID
     */
    @TableField( "public_id" )
    private Integer publicId;
    /**
     * 粉币
     */
    @TableField( "fans_currency" )
    private Double  fansCurrency;
    /**
     * 流量
     */
    private Integer flow;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 会员等级
     */
    private Integer level;
    private String  openid;
    /**
     * 手机号码
     */
    private String  phone;
    /**
     * 昵称
     */

    private String  nickname;
    /**
     * 性别 0未知 1男 2女
     */
    private Integer sex;
    /**
     * 省份
     */
    private String  province;
    /**
     * 城市
     */
    private String  city;
    /**
     * 国家
     */
    private String  country;
    /**
     * 头像
     */
    private String  headimgurl;
    /**
     * 3：电信  1 移动  2 联通 0 未知号码
     */
    @TableField( "operate_type" )
    private Integer operateType;
    /**
     * 卡片id
     */
    @TableField( "mc_id" )
    private Integer mcId;
    /**
     * 历史消费金额
     */
    private Double  totalMoney;
    /**
     * 历史累计积分
     */
    private Integer totalIntegral;
    /**
     * 详细地址
     */
    private String  address;
    /**
     * 邮箱
     */
    private String  email;
    /**
     * 身份证
     */
    private String  cardId;
    /**
     * 身份证上传地址2
     */
    private String  cardImgback;
    /**
     * 身份证上传地址1
     */
    private String  cardImg;
    /**
     * 出生年月
     */
    private Date    birth;
    /**
     * 粉笔更新时间
     */
    private Date    currencyDate;
    /**
     * 流量更新时间
     */
    private Date    flowDate;
    /**
     * 积分更新时间
     */
    private Date    integralDate;
    /**
     * 分享次数
     */
    private Integer shareCount;
    /**
     * 分享更新时间
     */
    private Date    shareDate;
    /**
     * 备注
     */
    private String  remark;
    /**
     * 身份证审核 0未上传 1待审核 2审核 3审核未通过
     */
    private Integer cardChecked;
    private String  name;
    /**
     * 是否够买
     */
    private Integer isBuy;
    /**
     * 是否已关注 0未关注 1已关注 null为已关注(兼容以前数据)
     */
    private Integer isSubscribe;
    @TableField( "scence_code" )
    private String  scenceCode;
    private Integer busId;
    private String  pwd;
    private Integer loginMode;
    private String  oldId;
    private Date    subscribeDate;
    /**
     * 小程序openid
     */
    private String  openid2;

    private Double rechargeMoney;

    @TableField("recommendMoney")
    private Double recommendMoney;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MemberEntity{" + "id=" + id + ", dfMemberId=" + dfMemberId + ", publicId=" + publicId + ", fansCurrency=" + fansCurrency + ", flow=" + flow + ", integral=" + integral
			+ ", level=" + level + ", openid=" + openid + ", phone=" + phone + ", nickname=" + nickname + ", sex=" + sex + ", province=" + province + ", city=" + city
			+ ", country=" + country + ", headimgurl=" + headimgurl + ", operateType=" + operateType + ", mcId=" + mcId + ", totalMoney=" + totalMoney
			+ ", totalIntegral=" + totalIntegral + ", address=" + address + ", email=" + email + ", cardId=" + cardId + ", cardImgback=" + cardImgback + ", cardImg="
			+ cardImg + ", birth=" + birth + ", currencyDate=" + currencyDate + ", flowDate=" + flowDate + ", integralDate=" + integralDate + ", shareCount="
			+ shareCount + ", shareDate=" + shareDate + ", remark=" + remark + ", cardChecked=" + cardChecked + ", name=" + name + ", isBuy=" + isBuy + ", isSubscribe="
			+ isSubscribe + ", scenceCode=" + scenceCode + ", busId=" + busId + ", pwd=" + pwd + ", loginMode=" + loginMode + ", oldId=" + oldId + ", subscribeDate="
			+ subscribeDate + ", openid2=" + openid2 + "}";
    }
}
