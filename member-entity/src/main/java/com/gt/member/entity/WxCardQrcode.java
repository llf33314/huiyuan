package com.gt.member.entity;

import com.baomidou.mybatisplus.annotations.TableField;
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
 * 投放卡券——二维码
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_card_qrcode")
public class WxCardQrcode extends Model<WxCardQrcode> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 二维码类型
     */
	@TableField("action_name")
	private String actionName;
    /**
     * 有效时间
     */
	@TableField("expire_seconds")
	private String expireSeconds;
    /**
     * 明细(json格式) { "card": {"card_id": "pFS7Fjg8kV1IdDz01r4SQwMkuCKc",  "code": "198374613512",  "openid": "oFS7Fjl0WsZ9AMZqrI80nbIq8xrA", "is_unique_code": false , "outer_id" : 1 },{ "multiple_card": { "card_list": [ { "card_id": "p1Pj9jgj3BcomSgtuW8B1wl-wo88"
     */
	@TableField("action_info")
	private String actionInfo;
    /**
     * 获取的二维码ticket，凭借此ticket调用通过ticket换取二维码接口可以在有效时间内换取二维码
     */
	private String ticket;
    /**
     * 可根据该地址自行生成需要的二维码图片
     */
	private String url;
    /**
     * 二维码显示地址
     */
	@TableField("show_qrcode_url")
	private String showQrcodeUrl;
    /**
     * 状态
     */
	private Integer status;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "WxCardQrcode{" +
			"id=" + id +
			", actionName=" + actionName +
			", expireSeconds=" + expireSeconds +
			", actionInfo=" + actionInfo +
			", ticket=" + ticket +
			", url=" + url +
			", showQrcodeUrl=" + showQrcodeUrl +
			", status=" + status +
			"}";
	}
}
