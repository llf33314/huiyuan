package com.gt.member.service.core.ws.entitybo.returnBo;

import java.io.Serializable;
import java.util.List;

/**
 * 微信卡券集合Bo
 *
 * Created by Administrator on 2017/7/28 0028.
 */
public class WxCardListBo implements Serializable{


    private List<WxCardBo> wxCardBos;

    public List< WxCardBo > getWxCardBos() {
	return wxCardBos;
    }

    public void setWxCardBos( List< WxCardBo > wxCardBos ) {
	this.wxCardBos = wxCardBos;
    }
}
