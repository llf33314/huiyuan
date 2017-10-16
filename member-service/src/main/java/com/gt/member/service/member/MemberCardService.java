/**
 * P 2016年3月8日
 */
package com.gt.member.service.member;

import java.util.List;
import java.util.Map;

import com.gt.member.entity.MemberCardmodel;
import com.gt.member.entity.MemberCardrecord;
import com.gt.member.entity.MemberCardtype;

/**
 * 会员卡业务处理
 *
 * @author pengjiangli
 * @version 创建时间:2016年3月8日
 */
public interface MemberCardService {

    /**
     * 查询会员卡类型
     * @param busId
     * @return
     */
    List<MemberCardtype> findCardType(Integer busId);

    /**
     * 分组查询卡片信息
     *
     * @return
     */
    List< MemberCardmodel > findGroupByType( Integer busId );




    /**
     * 根据类型查询模板
     *
     * @param cmType
     *
     * @return
     */
    List< MemberCardmodel > findByType( Integer cmType );

    /**
     * 根据商户id和模板id 查询同一类型模板
     *
     * @param publicId
     * @param ctId
     *
     * @return
     */
    List< MemberCardmodel > findCmType( Integer publicId, Integer ctId );

    /**
     * 保存卡片背景模板
     * @param busId
     * @param param
     */
    void saveCardModel( Integer busId, String param );



    /**
     * 新增会员第1步信息pc
     *
     * @param busId
     * @param ctId
     *
     * @return
     */
    public Map< String,Object > editGradeTypeFrist( Integer busId, Integer ctId );

    /**
     * 新增会员第2步信息pc
     * @param busId
     * @param ctId
     */
    public Map< String,Object > editGradeTypeSecond(Integer busId,Integer ctId);



    /**
     * 新增会员第3步信息pc
     * @param busId
     * @param ctId
     */
    public Map< String,Object > editGradeTypeThird(Integer busId,Integer ctId);

    /**
     * 商家卡片背景模板
     * @param busId
     * @return
     */
    public List< MemberCardmodel >  findCardModel(Integer busId);


    /**
     * 积分清0
     *
     * @param busIds
     */
    void clearJifen( String busIds );

    /**
     * 添加积分记录
     *
     * @param str
     */
    void jifenLog( String str );
}
