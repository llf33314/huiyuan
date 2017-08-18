package com.gt.common.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商户表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-26
 */
@Data
@Accessors(chain = true)
@TableName("bus_user")
public class BusUser extends Model<BusUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户标识
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 用户名
     */
	private String name;
    /**
     * 邮箱
     */
	private String email;
    /**
     * 密码(密文)
     */
	private String password;
    /**
     * 手机号
     */
	private String phone;
    /**
     * 性别(0表示男性, 1表示女性)
     */
	private String gender;
    /**
     * 注册ip
     */
	@TableField("register_ip")
	private String registerIp;
    /**
     * 最近登录ip
     */
	@TableField("recent_ip")
	private String recentIp;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 最近修改时间
     */
	private Date mtime;
    /**
     * 状态(0 正常用户, -1 冻结用户)
     */
	private String status;
    /**
     * 粉币
     */
	@TableField("fans_currency")
	private BigDecimal fansCurrency;
    /**
     * 流量
     */
	private Float flow;
    /**
     * 关联城市ID
     */
	@TableField("city_id")
	private Integer cityId;
    /**
     * 会员级别:0:试用期1:普通;2:高级;3:皇冠;4:钻石
     */
	private Integer level;
    /**
     * 会员开始时间
     */
	private Date startTime;
    /**
     * 会员结束时间
     */
	private Date endTime;
    /**
     * 续费年限(1:12个月、2:18个月、3:24个月)
     */
	private Integer years;
	private Date statTime;
    /**
     * 行业id（对应字典表里面的19）
     */
	private Integer industryid;
    /**
     * 子账号父类的账号id，0代表不是子账号
     */
	private Integer pid;
    /**
     * 短信条数
     */
	@TableField("sms_count")
	private Integer smsCount;
    /**
     * 刮刮乐里面的技术支持，0，显示，1不显示
     */
	private Integer istechnique;
    /**
     * 多粉平台下面的广告，是否显示，0 显示，1不显示
     */
	private Integer advert;
    /**
     * 针对：代理的商家支付总部等级，  取自字典1112 （旧版代理用）
     */
	@TableField("busmoney_level")
	private String busmoneyLevel;
    /**
     * 需要支付所属代理的账号id，T_man_user，id之间要用逗号区分,级别越高越在后面旧版代理用）
     */
	private String regionids;
    /**
     * 是否代理，0，是代理，1不是代理 （新旧都用）,2新版城市代理，3是省会代理
     */
	private Integer isagent;
    /**
     * 商家所属代理的账号id，T_man_user，（旧版代理用）
     */
	private Integer agentid;
    /**
     * 商家真实姓名
     */
	private String realname;
    /**
     * 登录来源，0 代表来自与网站，1来自与电信那边
     */
	@TableField("login_source")
	private Integer loginSource;
    /**
     * 是否解绑公众号 0：否 1:解绑
     */
	@TableField("is_binding")
	private Integer isBinding;
    /**
     * 解绑时间
     */
	@TableField("unbundling_time")
	private Date unbundlingTime;
    /**
     * 固话
     */
	@TableField("fixed_phone")
	private String fixedPhone;
    /**
     * 客户ID(电信账号独有)
     */
	@TableField("customer_id")
	private String customerId;
    /**
     * 商户名（针对无公众号的短信发送和展现）
     */
	@TableField("merchant_name")
	private String merchantName;
	@TableField("add_source")
	private Integer addSource;
    /**
     * 微站字段:0表示未认证,1:已认证
     */
	@TableField("wz_auth")
	private Integer wzAuth;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BusUser{" +
			"id=" + id +
			", name=" + name +
			", email=" + email +
			", password=" + password +
			", phone=" + phone +
			", gender=" + gender +
			", registerIp=" + registerIp +
			", recentIp=" + recentIp +
			", ctime=" + ctime +
			", mtime=" + mtime +
			", status=" + status +
			", fansCurrency=" + fansCurrency +
			", flow=" + flow +
			", cityId=" + cityId +
			", level=" + level +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", years=" + years +
			", statTime=" + statTime +
			", industryid=" + industryid +
			", pid=" + pid +
			", smsCount=" + smsCount +
			", istechnique=" + istechnique +
			", advert=" + advert +
			", busmoneyLevel=" + busmoneyLevel +
			", regionids=" + regionids +
			", isagent=" + isagent +
			", agentid=" + agentid +
			", realname=" + realname +
			", loginSource=" + loginSource +
			", isBinding=" + isBinding +
			", unbundlingTime=" + unbundlingTime +
			", fixedPhone=" + fixedPhone +
			", customerId=" + customerId +
			", merchantName=" + merchantName +
			", addSource=" + addSource +
			", wzAuth=" + wzAuth +
			"}";
	}
}
