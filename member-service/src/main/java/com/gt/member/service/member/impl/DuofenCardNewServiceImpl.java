package com.gt.member.service.member.impl;

import com.gt.duofencard.entity.DuofenCardNew;
import com.gt.duofencard.entity.DuofenCardNewVO;
import com.gt.duofencard.entity.DuofenCardPublish;
import com.gt.member.dao.duofencard.DuofenCardNewDAO;
import com.gt.member.dao.duofencard.DuofenCardPublishDAO;
import com.gt.member.dao.duofencard.DuofenCardTimeDAO;
import com.gt.member.entity.DuofenCard;
import com.gt.member.service.member.DuofenCardNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 多粉优惠券pc端
 *
 * Created by Administrator on 2018/1/16.
 */
@Service
public class DuofenCardNewServiceImpl implements DuofenCardNewService{
    @Autowired
    private DuofenCardNewDAO     cardMapper;
    @Autowired
    private DuofenCardTimeDAO    cardTimeMapper;
    @Autowired
    private DuofenCardPublishDAO cardPublishMapper;


    public Integer addCoupon( DuofenCardNewVO coupon ) {
        Integer cardId =cardMapper.insert( coupon );
	coupon.setCardId( cardId );
	cardTimeMapper.insert( coupon );
	return -1;
    }
}
