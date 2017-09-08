package com.gt.member.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.common.entity.BusUser;
import com.gt.common.entity.TCommonStaff;
import com.gt.common.entity.WxPublicUsers;
import com.gt.member.base.BaseController;
import com.gt.member.dao.MemberDAO;
import com.gt.member.dao.MemberGradetypeDAO;
import com.gt.member.dao.MemberQcodeWxDAO;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.entity.Member;
import com.gt.member.entity.MemberGradetype;
import com.gt.member.entity.MemberQcodeWx;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.count.ERPCountService;
import com.gt.member.service.entityBo.queryBo.MallAllEntityQuery;
import com.gt.member.service.entityBo.queryBo.MallEntityQuery;
import com.gt.member.service.member.CardERPService;
import com.gt.member.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 统一新增会员卡页面
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-09
 */
@Api(value = "统一新增会员卡页面",description = "统一新增会员卡页面")
@Controller
@RequestMapping( "/addMember" )
public class AddMemberController {

    private static final Logger LOG = LoggerFactory.getLogger( AddMemberController.class );

    @Autowired
    private MemberQcodeWxDAO memberQcodeWxMapper;

    @Autowired
    private MemberDAO memberMapper;

    @Autowired
    private WxPublicUsersDAO wxPublicUsersMapper;

    @Autowired
    private MemberGradetypeDAO gradeTypeMapper;

    @Autowired
    private BusUserDAO busUserMapper;

    @Autowired
    private CardERPService cardERPService;

    @Autowired
    private DictService dictService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private MemberConfig memberConfig;

    @ApiOperation( value = "新增会员统一页面", notes = "新增会员统一页面" )
    @ApiImplicitParam( name = "shopId", value = "门店id(没有门店请传主门店id)", paramType = "query", required = true, dataType = "int" )
    @RequestMapping( "/erpAddMember" )
    public String erpAddMember( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Integer shopId = CommonUtil.toInteger( params.get( "shopId" ) );

	//测试
	BusUser busUser = busUserMapper.selectById( 42 );
	Integer busId = busUser.getId();
	Integer loginStyle = SessionUtil.getLoginStyle( request );
	Integer userId = 0;
	if ( "0".equals( loginStyle ) ) {
	    TCommonStaff tc = SessionUtil.getTCommonStaff( request );
	    userId = tc.getId();
	} else {
	    //	userId= SessionUtil.getBusUser(request).getId();
	    busId = dictService.pidUserId( busId );
	}

	WxPublicUsers wxPublicUsers = wxPublicUsersMapper.selectByUserId( busId );

	if ( ( CommonUtil.isEmpty( wxPublicUsers ) ) || wxPublicUsers.getServiceTypeInfo() != 2 || wxPublicUsers.getVerifyTypeInfo() != 0 ) {
	    request.setAttribute( "gongzhong", 0 );
	} else {
	    request.setAttribute( "gongzhong", 1 );
	}

	request.setAttribute( "shopId", shopId );
	List< Map< String,Object > > mapList = gradeTypeMapper.findBybusId1( busId );//查询该商户下的卡片类型
	request.setAttribute( "mapList", JSON.toJSON( mapList ) );
	if ( mapList.size() > 0 ) {
	    List< Map< String,Object > > gradeTypes = gradeTypeMapper.findGradeTyeBybusIdAndctId( busId, CommonUtil.toInteger( mapList.get( 0 ).get( "ctId" ) ) );
	    if(gradeTypes.size()>0 ) {
	        if("3".equals( gradeTypes.get( 0 ).get( "applyType" ) )){
		    request.setAttribute( "gradeTypes", JSON.toJSON( gradeTypes ) );
		}else{
		    request.setAttribute( "gradeTypes", JSON.toJSON( gradeTypes.get( 0 ) ) );
		}
	    }
	}
	request.setAttribute( "busId",busId );
	request.setAttribute( "shopId", shopId );
	request.setAttribute( "memberUser", "member_" + userId );
	request.setAttribute( "host", memberConfig.getSocket_url() );

	return "addMember/addmember";
    }

    /**
     * 查詢卡片類型
     *
     * @param request
     * @param response
     * @param cardType
     *
     * @throws IOException
     */
    @RequestMapping( "/findCardType" )
    public void findCardType( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer cardType ) throws IOException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    BusUser busUser = busUserMapper.selectById( 42 );
	    //	BusUser busUser = CommonUtil.getLoginUser(request);
	    Integer busId = busUser.getId();
	    if ( busUser.getPid() > 0 ) {
		busId = dictService.pidUserId( busUser.getId() );
	    }
	    List< Map< String,Object > > gradeTypes = gradeTypeMapper.findGradeTyeBybusIdAndctId( busId, cardType );
	    if(gradeTypes.size()>0 ) {
		if("3".equals( gradeTypes.get( 0 ).get( "applyType" ) )){
		    request.setAttribute( "gradeTypes", JSON.toJSON( gradeTypes ) );
		}else{
		    request.setAttribute( "gradeTypes", JSON.toJSON( gradeTypes.get( 0 ) ) );
		}
	    }
	    map.put( "code", 0 );
	} catch ( Exception e ) {
	    map.put( "code", "-1" );//保存返回的结果code 100为正常返回
	    map.put( "data", "查询数据错误" );//保存数据源
	}
	CommonUtil.write( response, map );
    }

    /**
     * 发送短信
     *
     * @param telNo
     *
     * @throws IOException
     */
    @RequestMapping( value = "/sendMsgerp" )
    public void sendMsgerp( @RequestParam String telNo,@RequestParam Integer busId, HttpServletResponse response, HttpServletRequest request ) throws IOException {
	if ( LOG.isDebugEnabled() ) {
	    // 验证类型
	    LOG.debug( "进入短信发送,手机号:" + telNo );
	}

	//	    BusUser busUser = CommonUtil.getLoginUser(request);
	//	    Integer busId=busUser.getId();


	Map< String,Object > map = new HashMap< String,Object >();
	Member member = memberMapper.findByPhone( busId, telNo );
	if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( member.getMcId() ) ) {
	    map.put( "result", false );
	    map.put( "msg", "用户已是会员" );
	    CommonUtil.write( response, map );
	    return;
	}

	String url = memberConfig.getWxmp_home() + "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do";
	Map< String,Object > obj = new HashMap<>();
	String no = CommonUtil.getPhoneCode();
	redisCacheUtil.set( no, no, 5 * 60 );
	LOG.debug( "进入短信发送,手机号:" + no );
	obj.put( "busId", busId );
	obj.put( "telNo", telNo );
	obj.put( "content", "会员短信校验码：" + no );
	obj.put( "company", memberConfig.getSms_name() );
	obj.put( "model", 9 );
	try {
	    String smsStr = SignHttpUtils.WxmppostByHttp( url, obj, memberConfig.getWxmpsignKey() );
	    JSONObject smsJSON = JSONObject.parseObject( smsStr );
	    if ( "0".equals( smsJSON.get( "code" ) ) ) {
		map.put( "result", true );
		map.put( "msg", "发送成功" );
	    } else {
		map.put( "result", false );
		map.put( "msg", "发送失败" );
	    }

	} catch ( Exception e ) {
	    LOG.error( "短信发送失败", e );
	    map.put( "result", false );
	    map.put( "msg", "获取短信验证码失败" );
	}
	CommonUtil.write( response, map );

    }

    /**
     * 领取会员卡
     *
     * @param request
     * @param response
     *
     * @return
     * @throws Exception
     */
    @RequestMapping( "/liquMemberCard" )
    public void liquMemberCard( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws Exception {
	Map< String,Object > map = new HashMap< String,Object >();

	if ( CommonUtil.isEmpty( params.get( "vcode" ) ) ) {
	    map.put( "code", -1 );
	    map.put( "message", "请输入验证码" );
	    CommonUtil.write( response, map );
	    return;
	}
	String vcode = CommonUtil.toString( params.get( "vcode" ) );
	String vcode1 = redisCacheUtil.get( vcode );
	if ( CommonUtil.isEmpty( vcode1 ) ) {
	    map.put( "code", -1 );
	    map.put( "message", "验证码超时或错误" );
	    CommonUtil.write( response, map );
	    return;
	}
	if ( CommonUtil.isEmpty( params.get( "ctId" ) ) ) {
	    map.put( "code", -1 );
	    map.put( "message", "请选择会员卡" );
	    CommonUtil.write( response, map );
	    return;
	}
	if ( CommonUtil.isEmpty( params.get( "gtId" )  ) ) {
	    map.put( "code", -1 );
	    map.put( "message", "请选择会员卡等级" );
	    CommonUtil.write( response, map );
	    return;
	}
	if ( CommonUtil.isEmpty( params.get( "shopId" )  ) ) {
	    map.put( "code", -1 );
	    map.put( "message", "门店不能为空" );
	    CommonUtil.write( response, map );
	    return;
	}

	Integer busId =CommonUtil.toInteger( params.get( "busId" ) );

	busId = dictService.pidUserId( busId );

	try {
	    map = cardERPService.linquMemberCard( busId, params );
	} catch ( Exception e ) {
	    map.put( "code", -1 );
	    map.put( "message", "领取失败" );
	}
	CommonUtil.write( response, map );
    }

    /**
     * 查找会员信息
     *
     * @param request
     * @param response
     */
    @RequestMapping("/findMemberByCardNo")
    public void findMemberByCardNo( HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String,Object>  params ) throws Exception {
	Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	String cardNo=CommonUtil.toString( params.get( "cardNo" ) );
	Map< String,Object > map = cardERPService.findMemberCard( busId, cardNo );
	CommonUtil.write( response, map );
    }


    /**
     * 购买会员卡支付回调
     *
     * @param request
     * @param response
     * @param params
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/successPayBuyCard" )
    public void successPayBuyCard( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	Map< String,Object > map = new HashMap< String,Object >();

	try {
	    map = cardERPService.buyMemberCard(params );
	} catch ( Exception e ) {
	    map.put( "code", -1 );
	    map.put( "message", "领取成功" );
	}
	CommonUtil.write( response, map );
    }


    /**
     * 查询粉丝列表
     *
     * @param request
     * @param response
     * @param param
     *
     * @return
     */
    @RequestMapping( "/erpMember" )
    public String erpMember( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > param ) {
	BusUser busUser = CommonUtil.getLoginUser( request );
	Integer busId = busUser.getId();
	if ( busUser.getPid() > 0 ) {
	    busId = dictService.pidUserId( busUser.getId() );
	}
	List< Map< String,Object > > listMap = cardERPService.findMemberIsNotCard( busId, param );
	request.setAttribute( "memberList", listMap );
	request.setAttribute( "shopId", param.get( "shopId" ) );
	return "merchants/member/erp/chooseFans";
    }

    /**
     * 微信关注二维码
     *
     * @param request
     * @param response
     *
     * @return
     * @throws Exception
     */
    @RequestMapping( "/guanzhuiQcode" )
    public void guanzhuiQcode( HttpServletRequest request, HttpServletResponse response ) throws Exception {
	Map< String,Object > map = new HashMap<>();
	BusUser busUser = busUserMapper.selectById( 42 );

	//	    BusUser busUser = CommonUtil.getLoginUser(request);
	Integer busId = busUser.getId();
	if ( busUser.getPid() > 0 ) {
	    busId = dictService.pidUserId( busUser.getId() );
	}

	MemberQcodeWx mqw = memberQcodeWxMapper.findByBusId( busId, 0 );
	String imgUrl = null;

	Integer loginStyle = SessionUtil.getLoginStyle( request );
	String scene_id = "";
	Integer userId = 0;
	if ( "0".equals(loginStyle ) ) {
	    TCommonStaff tc = SessionUtil.getTCommonStaff( request );
	    userId = tc.getId();
	    scene_id = userId + "_" + System.currentTimeMillis() + "_4";//员工
	} else {
	//    userId = SessionUtil.getBusUser( request ).getId();
	    scene_id = userId + "_" + System.currentTimeMillis() + "_4";//管理员
	}
	if ( CommonUtil.isEmpty( mqw ) ) {
	    Map< String,Object > querymap = new HashMap<>();
	    querymap.put( "scene_id", scene_id );
	    querymap.put( "busId", busId );
	    String url = memberConfig.getWxmp_home() + "/memberERP/79B4DE7C/guanzhuiQcode.do";
	    String json = SignHttpUtils.WxmppostByHttp( url, querymap, memberConfig.getWxmpsignKey() );

	    JSONObject obj = JSONObject.parseObject( json );

	    imgUrl = obj.getString( "imgUrl" );
	    if ( CommonUtil.isNotEmpty( imgUrl ) ) {
		mqw = new MemberQcodeWx();
		mqw.setBusId( busId );
		mqw.setBusType( 0 );
		mqw.setCodeUrl( imgUrl );
		memberQcodeWxMapper.insert( mqw );
	    }
	} else {
	    imgUrl = mqw.getCodeUrl();
	}
	map.put( "imgUrl", "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + imgUrl );//保存数据源
	CommonUtil.write( response, map );
    }

    /**
     * 发行会员卡二维码下载
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "/downQcode" )
    public void downPulishCardImage( HttpServletRequest request, HttpServletResponse response, @RequestParam String url ) throws IOException {
	String filename = "关注公众号.jpg";
	response.addHeader( "Content-Disposition", "attachment;filename=" + new String( filename.replaceAll( " ", "" ).getBytes( "utf-8" ), "iso8859-1" ) );
	response.setContentType( "application/octet-stream" );
	QRcodeKit.buildQRcode( url, 450, 450, response );
    }

}
