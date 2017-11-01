package com.gt.common.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付宝用户表
 * </p>
 *
 * @author pengjiangli
 * @since 2017-10-31
 */
@Data
@Accessors(chain = true)
@TableName("t_alipay_user")
public class AlipayUser extends Model<AlipayUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 商家ID
     */
	private Integer busId;
    /**
     * 合作身份者ID
     */
	private String partner;
    /**
     * 收款支付宝账号
     */
	private String sellerId;
    /**
     * MD5密钥
     */
	private String payKey;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 0:正常
     */
	private Integer status;
    /**
     * 卖家支付宝账号
     */
	private String sellerEmail;
    /**
     * RSA2私钥
     */
	private String privateKey;
    /**
     * RSA2公钥
     */
	private String publicKey;
    /**
     * 支付宝公钥
     */
	private String uPublicKey;
    /**
     * 应用appid
     */
	private String appid;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "AlipayUser{" +
			"id=" + id +
			", busId=" + busId +
			", partner=" + partner +
			", sellerId=" + sellerId +
			", payKey=" + payKey +
			", ctime=" + ctime +
			", status=" + status +
			", sellerEmail=" + sellerEmail +
			", privateKey=" + privateKey +
			", publicKey=" + publicKey +
			", uPublicKey=" + uPublicKey +
			", appid=" + appid +
			"}";
	}
}
