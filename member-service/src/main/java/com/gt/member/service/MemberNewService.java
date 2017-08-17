package com.gt.member.service;

import org.springframework.stereotype.Service;

/**
 * 会员新的数据接口
 *
 * Created by Administrator on 2017/7/28 0028.
 */
public interface MemberNewService {
    /**
     * 添加会员卡记录((新数据接口))
     * @param cardId
     * @param recordType
     * @param number
     * @param itemName
     * @param busId
     * @param balance
     * @param ctId
     * @param amount
     */
    public void saveCardRecordNew(Integer cardId, Integer recordType,
		    String number, String itemName, Integer busId, String balance,
		    Integer ctId, double amount);
}
