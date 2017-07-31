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
 * 卡券货架
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Data
@Accessors(chain = true)
@TableName("t_wx_card_shelves")
public class WxCardShelves extends Model<WxCardShelves> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 页面的banner图片链接，须调用，建议尺寸为640*300
     */
	private String banner;
    /**
     * 页面标题
     */
	@TableField("page_title")
	private String pageTitle;
    /**
     * 页面是否可以分享,填入true/false
     */
	@TableField("can_share")
	private Integer canShare;
    /**
     * 投放页面的场景值； SCENE_NEAR_BY 附近 SCENE_MENU	自定义菜单 SCENE_QRCODE	二维码 SCENE_ARTICLE	公众号文章 SCENE_H5	h5页面 SCENE_IVR	自动回复 SCENE_CARD_CUSTOM_CELL	卡券自定义cell
     */
	private String scene;
    /**
     * 卡券列表json格式[{card_id:'',thumb_url:''}]
     */
	@TableField("card_list")
	private String cardList;
    /**
     * 货架链接
     */
	private String url;
    /**
     * page_id
     */
	@TableField("page_id")
	private Integer pageId;
    /**
     * status
     */
	private Integer status;
    /**
     * 公众号id
     */
	private Integer publicId;
	private Date endDate;
    /**
     * 卡券名称
     */
	private String cardName;
    /**
     * 状态 0未发布 1已发布
     */
	private Integer state;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "WxCardShelves{" +
			"id=" + id +
			", banner=" + banner +
			", pageTitle=" + pageTitle +
			", canShare=" + canShare +
			", scene=" + scene +
			", cardList=" + cardList +
			", url=" + url +
			", pageId=" + pageId +
			", status=" + status +
			", publicId=" + publicId +
			", endDate=" + endDate +
			", cardName=" + cardName +
			", state=" + state +
			"}";
	}
}
