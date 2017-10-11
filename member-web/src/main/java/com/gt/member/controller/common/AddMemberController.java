package com.gt.member.controller.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.HttpClienUtils;
import com.gt.api.util.RequestUtils;
import com.gt.common.entity.BusUserEntity;
import com.gt.common.entity.TCommonStaffEntity;
import com.gt.common.entity.WxPublicUsersEntity;
import com.gt.member.dao.MemberEntityDAO;
import com.gt.member.dao.MemberGradetypeDAO;
import com.gt.member.dao.MemberQcodeWxDAO;
import com.gt.member.dao.common.BusUserDAO;
import com.gt.member.dao.common.WxPublicUsersDAO;
import com.gt.member.dao.common.WxShopDAO;
import com.gt.member.entity.MemberEntity;
import com.gt.member.entity.MemberQcodeWx;
import com.gt.member.service.common.dict.DictService;
import com.gt.member.service.member.CardERPService;
import com.gt.member.util.*;
import com.gt.util.entity.param.sms.OldApiSms;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private MemberEntityDAO memberMapper;

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
    private WxShopDAO wxShopDAO;

    @ApiOperation( value = "新增会员统一页面", notes = "新增会员统一页面" )
    @ApiImplicitParam( name = "shopId", value = "门店id(没有门店请传主门店id)", paramType = "query", required = true, dataType = "int" )
    @RequestMapping( "/erpAddMember" )
    public String erpAddMember( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
//       SessionUtil.setLoginStyle( request,1 );
//       BusUserEntity busUserEntity=busUserMapper.selectById( 36 );
//       SessionUtil.setLoginUser( request,busUserEntity );
//       SessionUtil.setPidBusId( request,36 );

        Integer shopId = CommonUtil.toInteger( params.get( "shopId" ) );
	Integer loginStyle = SessionUtil.getLoginStyle( request );
	Integer userId = 0;
	if ( "0".equals( loginStyle ) ) {
	    TCommonStaffEntity tc = SessionUtil.getCommonStaff( request );
	    userId = tc.getId();
	} else {
	    userId= SessionUtil.getLoginUser(request).getId();
	}
	Integer busId = SessionUtil.getLoginUser( request ).getId();
	if(CommonUtil.isEmpty( shopId )){
	    shopId= wxShopDAO.selectMainShopByBusId(busId  ).getId();
	}
	WxPublicUsersEntity wxPublicUsersEntity = wxPublicUsersMapper.selectByUserId( busId );

	if ( ( CommonUtil.isEmpty( wxPublicUsersEntity ) ) || wxPublicUsersEntity.getServiceTypeInfo() != 2 || wxPublicUsersEntity.getVerifyTypeInfo() != 0 ) {
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
	        if("3".equals( CommonUtil.toString( gradeTypes.get( 0 ).get( "applyType" ) ) )){
		    request.setAttribute( "gradeTypes", JSON.toJSON( gradeTypes ) );
		}else{
		    List< Map< String,Object > > gts=new ArrayList<>(  );
		    gts.add( gradeTypes.get( 0 ) );
		    request.setAttribute( "gradeTypes", JSON.toJSON( gts) );
		}
	    }
	}
	request.setAttribute( "busId",busId );
	request.setAttribute( "shopId", shopId );
	request.setAttribute( "memberUser", "member_" +loginStyle+"_"+ userId );
	request.setAttribute( "addmember", "addmember_" +busId+"_"+shopId );
	request.setAttribute( "host", PropertiesUtil.getSocket_url() );

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

	    Integer busId = SessionUtil.getPidBusId( request );
	    List< Map< String,Object > > gradeTypes = gradeTypeMapper.findGradeTyeBybusIdAndctId( busId, cardType );
	    if(gradeTypes.size()>0 ) {
		if("3".equals( CommonUtil.toString(  gradeTypes.get( 0 ).get( "applyType" ) ))){
		    map.put( "gradeTypes",gradeTypes );
		}else{
		    List< Map< String,Object > > gts=new ArrayList<>(  );
		    gts.add( gradeTypes.get( 0 ) );
		    map.put( "gradeTypes",gts );
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
	Map< String,Object > map = new HashMap< String,Object >();
        try {
	    if ( LOG.isDebugEnabled() ) {
		// 验证类型
		LOG.debug( "进入短信发送,手机号:" + telNo );
	    }
	    String url = PropertiesUtil.getWxmp_home() + "/8A5DA52E/smsapi/6F6D9AD2/79B4DE7C/sendSmsOld.do";
	    RequestUtils<OldApiSms> requestUtils=new RequestUtils<OldApiSms>(  );
	    String no = CommonUtil.getPhoneCode();
	    redisCacheUtil.set( telNo+"_"+no, no, 5 * 60 );
	    LOG.debug( "进入短信发送,手机号:" + no );

	    OldApiSms oldApiSms=new OldApiSms();
	    oldApiSms.setMobiles(telNo);
	    oldApiSms.setContent( "会员短信校验码:"+no);
	    oldApiSms.setCompany(PropertiesUtil.getSms_name());
	    oldApiSms.setBusId(busId);
	    oldApiSms.setModel(9);

	    requestUtils.setReqdata( oldApiSms );
	    try {
		String smsStr = HttpClienUtils.reqPostUTF8(JSONObject.toJSONString( requestUtils ), url,String.class, PropertiesUtil.getWxmpsignKey() );
		JSONObject json=JSONObject.parseObject( smsStr );
		if ( "0".equals( CommonUtil.toString(json.get( "code" )  ) ) ) {
		    map.put( "result", true );
		    map.put( "msg", "发送成功" );
		} else {
		    map.put( "result", false );
		    map.put( "msg", "发送失败:"+json.get( "msg" ) );
		}

	    } catch ( Exception e ) {
		LOG.error( "短信发送失败", e );
		map.put( "result", false );
		map.put( "msg", "获取短信验证码失败" );
	    }
	    CommonUtil.write( response, map );
	}catch ( Exception e ){
	    e.printStackTrace();
	    LOG.error( "短信发送失败", e );
	    map.put( "result", false );
	    map.put( "msg", "获取短信验证码失败" );
	}
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
	String phone = CommonUtil.toString( params.get( "phone" ) );
	String vcode1 = redisCacheUtil.get( phone+"_"+vcode );
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
	    redisCacheUtil.del( phone+"_"+vcode );
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
    public void successPayBuyCard( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) throws IOException {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    map = cardERPService.buyMemberCard(params );
	} catch ( Exception e ) {
	    map.put( "code", -1 );
	    map.put( "msg", "领取异常" );
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
	BusUserEntity busUserEntity = CommonUtil.getLoginUser( request );
	Integer busId = busUserEntity.getId();
	if ( busUserEntity.getPid() > 0 ) {
	    busId = dictService.pidUserId( busUserEntity.getId() );
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
	Integer loginStyle = SessionUtil.getLoginStyle( request );
	String scene_id = "";
	Integer pIduserId = 0;
	Integer userId=0;
	if (loginStyle==0 ) {
	    TCommonStaffEntity tc = SessionUtil.getCommonStaff( request );
	    userId = tc.getId();
	    scene_id = userId + "_" + System.currentTimeMillis() + "_5";//员工
	} else {
	    userId = SessionUtil.getLoginUser( request ).getId();
	    scene_id = userId + "_" + System.currentTimeMillis() + "_6";//管理员
	}

	if(loginStyle==0){
	    loginStyle=1;
	}else{
	    loginStyle=0;
	}

	MemberQcodeWx mqw = memberQcodeWxMapper.findByBusId( userId, loginStyle);
	String imgUrl = null;
	if ( CommonUtil.isEmpty( mqw ) ) {
	    Map< String,Object > querymap = new HashMap<>();
	    pIduserId=SessionUtil.getPidBusId( request );
	    WxPublicUsersEntity wxPublicUsersEntity =wxPublicUsersMapper.selectByUserId( pIduserId );

	    RequestUtils requestUtils=new RequestUtils<>(  );
	      querymap.put( "scene_id", scene_id );
	    querymap.put( "publicId", wxPublicUsersEntity.getId() );
	    requestUtils.setReqdata( querymap );
	    String url = PropertiesUtil.getWxmp_home() + "/8A5DA52E/wxpublicapi/6F6D9AD2/79B4DE7C/qrcodeCreateFinal.do";
	    Map<String,Object> jsonMap= HttpClienUtils.reqPostUTF8( JSON.toJSONString( requestUtils ),url,Map.class, PropertiesUtil.getWxmpsignKey() );

	    if ( "0".equals( CommonUtil.toString( jsonMap.get( "code" ) ) )  ) {
		imgUrl=CommonUtil.toString( jsonMap.get( "data" ) );
		mqw = new MemberQcodeWx();
		mqw.setBusId( userId );
		mqw.setBusType( loginStyle );
		mqw.setCodeUrl( imgUrl );
		if(CommonUtil.isNotEmpty( imgUrl )) {
		    memberQcodeWxMapper.insert( mqw );
		}
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
    public void downPulishCardImage( HttpServletRequest request, HttpServletResponse response, @RequestParam String url ) throws Exception {
        String headPath = URLConnectionDownloader.downloadRqcode(url,PropertiesUtil.getRes_image_path() + "/images", 300, 300);
	String path = headPath;
	String filename = "关注公众号.jpg";
	response.addHeader("Content-Disposition", "attachment;filename="
			+ new String(filename.replaceAll(" ", "").getBytes("utf-8"),
			"iso8859-1"));
	response.setContentType("application/octet-stream");
	InputStream in = null;
	OutputStream out = null;
	try {
	    in = new FileInputStream(path);
	    int len = 0;
	    byte[] buffer = new byte[1024];
	    out = response.getOutputStream();
	    while ((len = in.read(buffer)) > 0) {
		out.write(buffer, 0, len);
	    }
	} catch (Exception e) {
	    throw new RuntimeException(e);
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
	    }
	}

    }

}
