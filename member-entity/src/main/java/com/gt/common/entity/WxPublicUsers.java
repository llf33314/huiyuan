package com.gt.common.entity;

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
 * 微信订阅号用户表

 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-26
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_public_users")
public class WxPublicUsers extends Model<WxPublicUsers> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 用户令牌 系统自动创建
     */
	@TableField("wx_user_token")
	private String wxUserToken;
    /**
     * 授权方的刷新令牌
     */
	@TableField("auth_refresh_token")
	private String authRefreshToken;
    /**
     * 多粉用户ID
     */
	@TableField("bus_user_id")
	private Integer busUserId;
    /**
     * 第三方公众号昵称
     */
	@TableField("authorizer_info")
	private String authorizerInfo;
    /**
     * 第三方公众号头像
     */
	@TableField("head_img")
	private String headImg;
    /**
     * 第三方公众号公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
     */
	@TableField("service_type_info")
	private Integer serviceTypeInfo;
    /**
     * 第三方公众号认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
     */
	@TableField("verify_type_info")
	private Integer verifyTypeInfo;
    /**
     * 第三方公众号的原始ID
     */
	@TableField("user_name")
	private String userName;
    /**
     * 第三方公众号所设置的微信号，可能为空
     */
	private String alias;
    /**
     * 二维码图片的URL，开发者最好自行也进行保存
     */
	@TableField("qrcode_url")
	private String qrcodeUrl;
    /**
     * 第三方公众号appid
     */
	private String appid;
    /**
     * 第三方公众号appsecret
     */
	private String appsecret;
    /**
     * 公众号授权给开发者的权限集列表（请注意，当出现用户已经将消息与菜单权限集授权给了某个第三方，再授权给另一个第三方时，由于该权限集是互斥的，后一个第三方的授权将去除此权限集，开发者可以在返回的func_info信息中验证这一点，避免信息遗漏），1到9分别代表：
     */
	@TableField("func_info")
	private String funcInfo;
    /**
     * 微信access_token
     */
	@TableField("access_token")
	private String accessToken;
    /**
     * 微信access_token最后生成时间
     */
	@TableField("token_last_time")
	private Date tokenLastTime;
    /**
     * 微信access_token有时长
     */
	@TableField("token_duration")
	private Integer tokenDuration;
    /**
     * 微信apiticket
     */
	private String apiticket;
    /**
     * apiticket 有效时长
     */
	@TableField("api_ticket_duration")
	private Integer apiTicketDuration;
    /**
     * 最后生成时间
     */
	@TableField("api_ticket_last_time")
	private Date apiTicketLastTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 修改时间
     */
	@TableField("modify_time")
	private Date modifyTime;
    /**
     * 语音总数量
     */
	@TableField("total_voice_count")
	private Integer totalVoiceCount;
    /**
     * 视频总数量
     */
	@TableField("total_video_count")
	private Integer totalVideoCount;
    /**
     * 图片总数量
     */
	@TableField("total_image_count")
	private Integer totalImageCount;
    /**
     * 图文总数量
     */
	@TableField("total_news_count")
	private Integer totalNewsCount;
    /**
     * 已用图文数量
     */
	@TableField("use_news_count")
	private Integer useNewsCount;
    /**
     * 已用语音数量
     */
	@TableField("use_voice_count")
	private Integer useVoiceCount;
    /**
     * 已用视频数量
     */
	@TableField("use_video_count")
	private Integer useVideoCount;
    /**
     * 已用图片数量
     */
	@TableField("use_image_count")
	private Integer useImageCount;
    /**
     * 关注后自动回复是否开启，0代表未开启，1代表开启
     */
	@TableField("is_friend_reply_open")
	private Integer isFriendReplyOpen;
    /**
     * 消息自动回复是否开启，0代表未开启，1代表开启
     */
	@TableField("is_autoreply_open")
	private Integer isAutoreplyOpen;
    /**
     * 微官网是否开启:0代表未开启, 1.代表开启
     */
	@TableField("is_template_open")
	private Integer isTemplateOpen;
    /**
     * 群发总数
     */
	@TableField("total_bulk")
	private Integer totalBulk;
    /**
     * 已经群发总数
     */
	@TableField("use_bulk")
	private Integer useBulk;
    /**
     * 微信支付分配的商户号
     */
	@TableField("mch_id")
	private String mchId;
    /**
     * 微信支付分配的商户密匙
     */
	@TableField("api_key")
	private String apiKey;
    /**
     * 是否授权 0已授权 1未授权
     */
	@TableField("is_power")
	private Integer isPower;
    /**
     * 用以了解以下功能的开通状况（0代表未开通，1代表已开通）：
     */
	@TableField("business_info")
	private String businessInfo;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "WxPublicUsers{" +
			"id=" + id +
			", wxUserToken=" + wxUserToken +
			", authRefreshToken=" + authRefreshToken +
			", busUserId=" + busUserId +
			", authorizerInfo=" + authorizerInfo +
			", headImg=" + headImg +
			", serviceTypeInfo=" + serviceTypeInfo +
			", verifyTypeInfo=" + verifyTypeInfo +
			", userName=" + userName +
			", alias=" + alias +
			", qrcodeUrl=" + qrcodeUrl +
			", appid=" + appid +
			", appsecret=" + appsecret +
			", funcInfo=" + funcInfo +
			", accessToken=" + accessToken +
			", tokenLastTime=" + tokenLastTime +
			", tokenDuration=" + tokenDuration +
			", apiticket=" + apiticket +
			", apiTicketDuration=" + apiTicketDuration +
			", apiTicketLastTime=" + apiTicketLastTime +
			", createTime=" + createTime +
			", modifyTime=" + modifyTime +
			", totalVoiceCount=" + totalVoiceCount +
			", totalVideoCount=" + totalVideoCount +
			", totalImageCount=" + totalImageCount +
			", totalNewsCount=" + totalNewsCount +
			", useNewsCount=" + useNewsCount +
			", useVoiceCount=" + useVoiceCount +
			", useVideoCount=" + useVideoCount +
			", useImageCount=" + useImageCount +
			", isFriendReplyOpen=" + isFriendReplyOpen +
			", isAutoreplyOpen=" + isAutoreplyOpen +
			", isTemplateOpen=" + isTemplateOpen +
			", totalBulk=" + totalBulk +
			", useBulk=" + useBulk +
			", mchId=" + mchId +
			", apiKey=" + apiKey +
			", isPower=" + isPower +
			", businessInfo=" + businessInfo +
			"}";
	}
}
