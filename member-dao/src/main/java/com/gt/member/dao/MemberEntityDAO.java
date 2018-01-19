package com.gt.member.dao;

import com.gt.member.entity.MemberEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 商家会员表 Mapper 接口
 * </p>
 *
 * @author pengjiangli
 * @since 2017-07-25
 */
@Component
public interface MemberEntityDAO extends BaseMapper<MemberEntity > {

    MemberEntity selectById(Integer id);


    MemberEntity selectByOpenid(@Param("openid")String openid,@Param("public_id")Integer public_id);

    MemberEntity selectByPublicIdAndPhone(@Param("public_id")Integer public_id,@Param("phone")String phone);

    /**
     * 查询该商户下的会员信息 条件是手机号或者卡号cz
     */
    List<Map<String,Object>> findCardPhoneMember(@Param("search")String search, @Param("busid")Integer busid);

    /**
     * 查询该商户下今日新增发粉丝信息cz
     */
    List<Map<String,Object>> findTodayAddFans(@Param("busid")Integer busid);


    /**
     * 分页查询
     */
    List<Map<String, Object>> findMemberBybusId(@Param("fristpage")Integer fristpage,
                                                @Param("pagesize")Integer pagesize,@Param("busId")Integer busId,
                                                @Param("search")String search,@Param("ctId")Integer ctId,
                                                @Param("gtId")Integer gtId,@Param("source")Byte source,@Param("changeCardType")Byte changeCardType,
                                                @Param("startDate")String startDate,@Param("endDate")String endDate);

    int countMember(@Param("busId")Integer busId,@Param("search")String search,@Param("phone")String phone,@Param("ctId")Integer ctId,
                    @Param("gtId")Integer gtId,@Param("source")Byte source,@Param("changeCardType")Byte changeCardType,
                    @Param("startDate")String startDate,@Param("endDate")String endDate);

    List<Map<String, Object>> findMember(@Param("busId")Integer busId,
                                         @Param("search")String search,@Param("ctId")Integer ctId,
                                         @Param("gtId")Integer gtId);

    MemberEntity findByMcId(@Param("busId")Integer busId,@Param("phone")String phone,@Param("mcId") Integer mcId);

    MemberEntity findByMcIdAndbusId(@Param("busId")Integer busId,@Param("mcId") Integer mcId);

    /**
     * 根据id集合查询
     */
    List<Map<String, Object>> findByMemberIds(@Param("busId")Integer busId,@Param("memberIds")List<Integer> memberIds);

    /**
     * 查询所有会员
     */
    List<Map<String, Object>> findBybusIdAndNotMcId(@Param("busId")Integer busId);

    List<Map<String,Object>> findBybusIdAndctId(@Param("busId")Integer busId,@Param("ctIds")List<Integer> ctIds);


    List<Map<String,Object>> findBymcIds(@Param("busId")Integer busId,@Param("mcIds")List<Integer> mcIds);



    Integer findByNameAndtelPhone(@Param("publicId")Integer publicId,@Param("name")String name,@Param("phone")String phone);

    List<Map<String, Object>> findById(@Param("memberId")Integer memberId);

    /**
     * 根据会员卡号查询会员信息
     */
    List<Map<String, Object>> findCardNo(@Param("cardNo")String cardNo);

    /**
     * 查询所有粉丝的数量
     */
    Integer queryFansNum(@Param("wxUserId")Integer wxUserId);

    /**
     * 查询随机的中奖人
     */
    List<MemberEntity > randNumUser(@Param("wxUserId")Integer wxUserId);

    /**
     * 查询对应的用户信息
     */
    MemberEntity selectOpenid(@Param("ids")String ids,@Param("wxUserId")Integer wxUserId);


    /**
     * 根据指定的openid去查询用户信息
     */
    MemberEntity queryOpenid(@Param("string")String string);



    /**
     * 查询大屏摇一摇用户
     */
    List<MemberEntity > queryShakeFans(@Param("wxUserId")Integer wxUserId);

    MemberEntity queryMax(@Param("openid")String openid);

    /**
     * 查询签到的用户信息
     */
    List<MemberEntity > queryWxUserId(@Param("userId")Integer userId);


    /**
     * 查询用户所属公众号Id
     */
    Integer selectByPublicId(@Param("openid")String openid);

    /**
     * 根据Id查询摇一摇用户信息
     */
    MemberEntity selectByKey(@Param("id")Integer id);


    int countCommend(@Param("busId")Integer busId,@Param("search")String search,
                     @Param("recommendType")Integer recommendType);

    List<Map<String, Object>> findCommend(@Param("busId")Integer busId,
                                          @Param("search")String search,@Param("fristpage")Integer fristpage,
                                          @Param("pagesize")Integer pagesize,@Param("recommendType")Integer recommendType);

    /**
     * 会员总数
     */
    int totalCountMember(@Param("busId")Integer busId);



    /**
     * 根据电话号码查询粉丝信息
     * @param busId
     * @param phone
     * @return
     */
    List<MemberEntity > selectBybusIdAndPhone(@Param("busId")Integer busId,@Param("phone")String phone);

    /**
     *
     * @param busId
     * @param phone
     * @param pwd
     * @return
     */
    List<MemberEntity > selectByPhoneAndPwd(@Param("busId")Integer busId,@Param("phone")String phone,@Param("pwd")String pwd);


    /**
     * 根据手机号查询用户信息
     * @param busId
     * @param phone
     * @return
     */
    MemberEntity findByPhone(@Param("busId")Integer busId,@Param("phone")String phone);

    /**
     * 查询粉丝信息
     * @param busId
     * @param mcIds
     * @return
     */
    List<Map<String,Object>> findMemberBymcIds(@Param("busId")Integer busId,@Param("mcIds")List<Integer> mcIds,@Param("phone")String phone);

    /**
     * 今日新增用户
     * @param busId
     * @return
     */
    List<Map<String, Object>> findNewMemberByBusId(@Param("busId")Integer busId,@Param("date")Date date);

    /**
     * 通过小诚寻openid获取会员信息
     * @param openid2
     * @return
     */
    MemberEntity getByOpenid2(String openid2);



    MemberEntity findByMcId1(@Param("mcId") Integer mcId);

    List<Map<String, Object>> findMemberBybusIdAndPhone(@Param("busId")Integer busId,@Param("mcId") Integer mcId);


    /**
     * 查询粉丝信息
     */
    List<Map<String,Object>> findMemberByPhone(@Param("busId")Integer busId,@Param("phone")String phone);



    int updateJifen(@Param("busId")Integer busId,@Param("mcId") Integer mcId,@Param("jifen")Integer jifen);

    /**
     * 查询实体卡信息
     */
    MemberEntity findLoginMode2ByPhone(@Param("busId")Integer busId,@Param("phone")String phone);

    /**
     * 查询会员积分卡积分剩余
     */
    Integer countJifen(@Param("busId")Integer busId,@Param( "ctId" )Integer ctId);

    /**
     * 分类统计会员性别
     */
    List<Map<String, Object>> findSexGroupBySex(@Param("busId")Integer busId, @Param("ctId")Integer ctId);

    Integer updateMemberByMcId(@Param("busId")Integer busId,@Param("mcId")Integer mcId);

    Map<String,Object> selectIdByOpenid(String openid);

    /**
     * 查询微信非会员数量
     */
    Integer countMemberIsNotCard(@Param("busId")Integer busId);

    /**
     * 查询微信非会员数据
     */
    List<Map<String, Object>> findMemberIsNotCard(@Param("busId")Integer busId);

    MemberEntity selectByOpenidAndBusId(@Param("openid")String openid,@Param("busId")Integer busId);

    /**
     * 根据ids查询粉丝信息
     * @param busId
     * @param ids
     * @return
     */
    List<Map<String,Object>> findMemberByIds(@Param("busId")Integer busId,@Param("ids")List<Integer> ids);



    /**
     *  根据手机号查询粉丝信息 返回id 昵称 手机号 头像
     * @param busId
     * @return
     */
    List<Map<String,Object>> findMemberByPhoneAndBusId(@Param("busId")Integer busId,@Param("phone")String phone);

    /**
     * 查询粉丝信息 允许转换成member对象
     * @param memberId
     * @return
     */
    Map<String,Object> findMemberByMemberId(@Param("memberId")Integer memberId);





    List<MemberEntity> selectByPrimaryKeys(@Param("ids")String ids);


    void updateMemberJifen(@Param( "ids" )List<Integer> ids,@Param( "jifen" )Integer jifen);


    MemberEntity findMemberByOldId(@Param( "busId" )Integer busId,@Param( "memberId" )Integer memberId);

    /**
     * 解绑会员卡
     * @param memberId
     */
    void updateMcIdBymemberId(@Param( "memberId" )Integer memberId);

    /**
     * 查询会员今天会员生日
     * @return
     */
    List<Map<String,Object>> findMemberBir();

}