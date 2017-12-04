/**
 * P 2016年3月24日
 */
package com.gt.member.service.member;

import com.gt.member.exception.BusinessException;
import com.gt.member.util.Page;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * @author pengjiangli
 * @version
 * 创建时间:2016年3月24日
 *
 */
public interface MemberNoticeService {
    /**
     * 系统通知设置
     * @param busId
     * @return
     * @throws BusinessException
     */
    public Map<String,Object> findSystemNotice( Integer busId )throws BusinessException;

    /**
     * 保存系统通知设置
     * @param busId
     * @param json
     * @throws BusinessException
     */
    public void saveSystemNotice( Integer busId, String json )throws BusinessException;

    /**
     * 编辑会员消息
     * @param busId
     * @return
     * @throws BusinessException
     */
    public Map<String,Object> editMemberNotice( Integer busId, Integer id ) throws  BusinessException;

    /**
     * 统计会员卡数量
     * @param ctIds
     * @param busId
     * @return
     */
    public Integer countMember(String ctIds,Integer busId);
    /**
     * 保存会员发送通知
     * @param busId
     * @param json
     * @param sendDate
     * @throws BusinessException
     */
    public void saveMemberNotice( Integer busId, String json ) throws  BusinessException;

    /**
     * 删除会员消息
     * @param id
     */
    public void deleteMemberNotice(Integer id)throws BusinessException;

    /**
     * 分页查询
     * @param busId
     * @param params
     * @return
     */
    public Page findMemberNotice( Integer busId, String params );


    /**
     * 给会员发送通知
     * @param id
     * @param memberIds
     * @throws BusinessException
     */
    public void sendNoticeToUser( Integer id, String memberIds ) throws BusinessException;

    /**
     * 查询会员发送信息状态
     * @param params
     * @return
     */
    public Page findNoticeUser(String params)throws BusinessException;

    /**
     *  重新发送短信
     * @param params
     * @param busId
     * @throws BusinessException
     */
    public void resendNoticeUser(String params) throws BusinessException;


}