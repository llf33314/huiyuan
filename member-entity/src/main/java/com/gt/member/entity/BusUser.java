package com.gt.member.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author zhangmz
 * @since 2017-07-09
 */
@Data
@Accessors( chain = true )
@TableName( "bus_user" )
public class BusUser extends Model< BusUser > {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String  name;
    private String  email;
    private String  password;
    private Long    phone;
    private Integer gender;
    @TableField( "register_ip" )
    private String  registerIp;
    @TableField( "recent_ip" )
    private String  recentIp;
    private String  ctime;
    private String  mtime;
    private Integer status;
    @TableField( "fans_currency" )
    private Double  fansCurrency;
    private Double  flow;
    @TableField( "city_id" )
    private Integer cityId;
    private Integer level;
    private String  startTime;
    private String  endTime;
    private Integer years;
    private String  statTime;
    private Integer industryid;
    private Integer pid;
    @TableField( "sms_count" )
    private Integer smsCount;
    private Integer istechnique;
    private Integer advert;
    @TableField( "busmoney_level" )
    private Integer busmoneyLevel;
    private Integer regionids;
    private Integer isagent;
    private Integer agentid;
    private String  realname;
    @TableField( "login_source" )
    private Integer loginSource;
    @TableField( "is_binding" )
    private Integer isBinding;
    @TableField( "unbundling_time" )
    private String  unbundlingTime;
    @TableField( "fixed_phone" )
    private String  fixedPhone;
    @TableField( "customer_id" )
    private String  customerId;
    @TableField( "merchant_name" )
    private String  merchantName;
    @TableField( "add_source" )
    private Integer addSource;
    @TableField( "wz_auth" )
    private Integer wzAuth;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "BusUser{" + "id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", phone=" + phone + ", gender=" + gender + ", registerIp=" + registerIp
			+ ", recentIp=" + recentIp + ", ctime=" + ctime + ", mtime=" + mtime + ", status=" + status + ", fansCurrency=" + fansCurrency + ", flow=" + flow
			+ ", cityId=" + cityId + ", level=" + level + ", startTime=" + startTime + ", endTime=" + endTime + ", years=" + years + ", statTime=" + statTime
			+ ", industryid=" + industryid + ", pid=" + pid + ", smsCount=" + smsCount + ", istechnique=" + istechnique + ", advert=" + advert + ", busmoneyLevel="
			+ busmoneyLevel + ", regionids=" + regionids + ", isagent=" + isagent + ", agentid=" + agentid + ", realname=" + realname + ", loginSource=" + loginSource
			+ ", isBinding=" + isBinding + ", unbundlingTime=" + unbundlingTime + ", fixedPhone=" + fixedPhone + ", customerId=" + customerId + ", merchantName="
			+ merchantName + ", addSource=" + addSource + ", wzAuth=" + wzAuth + "}";
    }
}
