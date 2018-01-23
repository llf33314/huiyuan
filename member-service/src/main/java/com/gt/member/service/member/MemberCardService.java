/**
 * P 2016年3月8日
 */
package com.gt.member.service.member;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.gt.member.entity.*;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.bo.ErrorWorkbook;
import com.gt.member.util.Page;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员卡业务处理
 *
 * @author pengjiangli
 * @version 创建时间:2016年3月8日
 */
public interface MemberCardService {

    /**
     * 查询会员卡类型
     *
     * @param busId
     *
     * @return
     */
    List< MemberCardtype > findCardType( Integer busId );

    /**
     * 查询卡片信息
     *
     * @return
     */
    Map< String,Object > findCardModelByBusId( Integer busId ) throws BusinessException;

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
     *
     * @param busId
     * @param param
     */
    Integer saveCardModel( Integer busId, String param );

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
     *
     * @param busId
     * @param ctId
     */
    public Map< String,Object > editGradeTypeSecond( Integer busId, Integer ctId );

    /**
     * 新增会员第3步信息pc
     *
     * @param busId
     * @param ctId
     */
    public Map< String,Object > editGradeTypeThird( Integer busId, Integer ctId );

    /**
     * 会员资料设置
     *
     * @param busId
     *
     * @return
     */
    public MemberOption findOption( Integer busId ) throws BusinessException;

    public void saveOrUpdateOption( String json, Integer busId ) throws BusinessException;

    public Map<String,Object> isSurplusMemberCard(Integer busId);

    /**
     * 保存会员卡设置
     *
     * @param json
     */
    public void saveOrUpdateGradeType(String json, Integer busUserId ) throws BusinessException;

    /**
     * 查询通用设置
     *
     * @param busId
     *
     * @return
     * @throws BusinessException
     */
    public Map< String,Object > findtongyongSet( Integer busId ) throws BusinessException;

    /**
     * 保存通用设置
     *
     * @param json
     * @param busId
     *
     * @throws BusinessException
     */
    public void saveTongyongSet( String json, Integer busId ) throws BusinessException;

    /**
     * 编辑礼品设置
     *
     * @param id
     *
     * @return
     */
    public Map<String,Object> editGift( Integer id );

    /**
     * 删除礼品设置
     *
     * @param id
     *
     * @return
     */
    public void deleteGift( Integer id )throws BusinessException;

    /**
     * 保存或修改礼品设置
     *
     * @param json
     * @param busId
     *
     * @throws BusinessException
     */
    public void saveOrUpdateGift( String json, Integer busId ) throws BusinessException;

    /**
     * 分页查询会员信息
     *
     * @param busId
     * @param params
     *
     * @return
     */
    public Page findMemberPage( Integer busId, String params );

    /**
     * 批量审核
     *
     * @param memberIds
     * @param ischecked
     */
    public void cardBatchApplyChecked( Integer busId, String memberIds, Integer ischecked ) throws BusinessException;

    public void cardApplyCheckedByOne( Integer busId, Integer memberId, Integer ischecked ) throws BusinessException;

    /**
     * 赠送积分和粉币给用户
     *
     * @param busId
     * @param json
     *
     * @throws BusinessException
     */
    public void addIntegralAndfenbi( Integer busId, String json ) throws BusinessException;

    /**
     * 拉黑 或回复操作
     *
     * @param mcId
     * @param cardStatus
     */
    public void updateDis( Integer mcId, Integer cardStatus );

    /**
     * 查询会员卡详情
     *
     * @param memberId
     *
     * @return
     */
    public Map< String,Object > findMemberDetails(Integer busId, Integer memberId );

    public Map<String,Object> findMemberByMemberId(Integer memberId);

    /**
     * 修改会员资料
     */
    public void updateMember(String json);

    /**
     * 导入会员信息
     *
     * @param input
     */
    public List< ErrorWorkbook > upLoadMember( Integer busId, InputStream input );

    public SXSSFWorkbook errorMember( List< ErrorWorkbook > wbs );

    public List< Map< String,Object > > findMember( Integer busId,  Integer ctId, Integer gtId );

    /**
     * 查询会员卡信息
     *
     * @param busId
     * @param cardNo
     *
     * @return
     * @throws BusinessException
     */
    public Map< String,Object > findMemberCardByCardNo( Integer busId, String cardNo ) throws BusinessException;

    /**
     * 积分消费
     *
     * @param busId
     * @param intergral
     * @param cardNo
     */
    public void intergralConsume( Integer busId, Integer intergral, String cardNo ) throws BusinessException;

    /**
     * 会员卡统计
     *
     * @param busId
     * @param ctId
     *
     * @return
     */
    public Map< String,Object > memberTongJi( Integer busId, Integer ctId) throws BusinessException;

    public Map<String,Object> sum7DayOrder(Integer busId, Integer ctId, String startTime,String enddate)throws BusinessException;

    /**
     * 分页查询充值记录
     *
     * @param busId
     * @param params
     *
     * @return
     */
    public Page findChongZhiLog( Integer busId, String params ) throws BusinessException;

    /**
     * 充值记录详情
     *
     * @param ucId
     *
     * @return
     */
    public Map< String,Object > findChongZhiLogDetails( Integer ucId ) throws BusinessException;

    /**
     * 分页查询积分兑换记录
     *
     * @param busId
     * @param params
     *
     * @return
     */
    public Page findDuiHuanLog( Integer busId, String params ) throws BusinessException;

    /**
     * 兑换详情
     *
     * @param ucId
     *
     * @return
     */
    public Map< String,Object > findDuiHuanLogDetails( Integer ucId ) throws BusinessException;

    /**
     * 次卡消费次数
     *
     * @param busId
     * @param params
     *
     * @return
     * @throws BusinessException
     */
    public Page findCikaLog( Integer busId, Map< String,Object > params ) throws BusinessException;

    /**
     * 次卡消费次数详情
     *
     * @param ucId
     *
     * @return
     */
    public Map< String,Object > findCikaLogDetails( Integer ucId ) throws BusinessException;

    /**
     * 消费记录
     *
     * @param busId
     * @param params
     *
     * @return
     * @throws BusinessException
     */
    public Page findXiaoFeiLog( Integer busId, Map< String,Object > params ) throws BusinessException;

    /**
     * 消费记录详情
     *
     * @param ucId
     *
     * @return
     * @throws BusinessException
     */
    public Map< String,Object > findXiaoFeiLogDetails( Integer ucId ) throws BusinessException;

    /**
     * 会员推荐
     *
     * @param busId
     * @param params
     *
     * @return
     * @throws BusinessException
     */
    public Page findCommend( Integer busId, Map< String,Object > params ) throws BusinessException;

    /**
     * 推荐佣金提取记录
     *
     * @param busId
     * @param params
     *
     * @return
     */
    public Page findPickLog( Integer busId, Map< String,Object > params ) throws BusinessException;

    /**
     * 删除会员卡信息
     *
     * @param memberId
     *
     * @throws BusinessException
     */
    public void deleteMemberCard( HttpServletRequest request, Integer memberId ) throws BusinessException;

    /**
     * 查询会员卡
     * @param busId
     * @return
     */
    public Map<String,Object> findCard( Integer busId );

    /**
     * 发布会员卡
     * @param busId
     * @param params
     */
    public void publishCard( Integer busId, String params )throws BusinessException;

    /**
     * 删除会员卡信息
     * @param busId
     * @param ctId
     * @throws BusinessException
     */
    public void deleteCard( Integer busId, Integer ctId ) throws BusinessException;

    /**
     * 查询会员卡充值信息
     * @param busId
     * @throws BusinessException
     */
    public Map<String,Object> findMemberCardByrecharge( Integer busId, String phone ) throws BusinessException;

    /**
     * 会员卡充值
     * @param busId
     */
    public void rechargeMemberCard( Integer busId, String jsonObj )throws BusinessException;

    /**
     * 查询商家发布的会员卡类型
     * @param busId
     * @return
     */
    public List<Map<String,Object>> findGradeTypeByBusId(Integer busId);


    /**
     * 查询商家发布的会员卡类型等级
     * @param busId
     * @return
     */
    public List<Map<String,Object>> findGradeTypeByCtId(Integer busId,Integer ctId);

    /**
     * 查询当前商家管理的门店信息
     * @param pbusId
     * @param dangqianUserId
     * @return
     */
    public List<Map<String,Object>> findBusUserShop(Integer pbusId,Integer dangqianUserId);

    /**
     * 会员卡消费查询信息
     * @param busId
     * @param cardNo
     * @return
     */
    public Map<String,Object>  consumefindMemberCard(Integer busId,String cardNo,Integer shopId);

    /**
     * 会员卡消费
     * @param busId
     * @param params
     * @param dangqianBusId
     */
    public void consumeMemberCard(Integer busId,String params,Integer dangqianBusId)throws BusinessException;
}
