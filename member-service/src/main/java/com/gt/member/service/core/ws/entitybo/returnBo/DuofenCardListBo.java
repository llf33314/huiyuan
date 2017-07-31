package com.gt.member.service.core.ws.entitybo.returnBo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31 0031.
 */
public class DuofenCardListBo implements Serializable{

    private List<DuofenCardBo>  duofenCardBos;

    public List< DuofenCardBo > getDuofenCardBos() {
	return duofenCardBos;
    }

    public void setDuofenCardBos( List< DuofenCardBo > duofenCardBos ) {
	this.duofenCardBos = duofenCardBos;
    }
}
