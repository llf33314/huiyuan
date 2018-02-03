package com.gt.member.controller.member_pc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.member.dto.ServerResponse;
import com.gt.member.entity.*;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.service.member.MemberNoticeService;
import com.gt.member.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 卡片设置和消息设置
 * </p>
 *
 * @author pengjiangli
 * @since 2017-08-09
 */
@Api( value = "卡片设置和消息设置", description = "卡片设置和消息设置" )
@Controller
@CrossOrigin
@RequestMapping( "/memberPc/editCard" )
public class CardController {

    private static final Logger LOG = LoggerFactory.getLogger( CardController.class );

    @Autowired
    private MemberCardService memberCardService;

    @Autowired
    private MemberNoticeService memberNoticeService;

    @Autowired
    private RequestService requestService;

    @ApiOperation( value = "查询没有创建的会员卡类型", notes = "查询会员卡类型" )
    @ResponseBody
    @RequestMapping( value = "/findCardType", method = RequestMethod.GET )
    public ServerResponse findCardType( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    List< MemberCardtype > cardTypes = memberCardService.findCardType( busId );
	    return ServerResponse.createBySuccess( cardTypes );
	} catch ( Exception e ) {
	    LOG.error( "查询会员卡类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员卡类型异常" );
	}
    }

    @ApiOperation( value = "会员卡新增和修改第1步页面", notes = "会员卡新增和修改第1步页面" )
    @ApiImplicitParams( @ApiImplicitParam( name = "ctId", value = "会员卡类型id", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/editGradeTypeFrist", method = RequestMethod.GET )
    public ServerResponse editGradeTypeFrist( HttpServletRequest request, HttpServletResponse response, Integer ctId ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberCardService.editGradeTypeFrist( busId, ctId );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    LOG.error( "会员卡新增和修改第1步页面：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "会员卡新增和修改异常" );
	}
    }

    @ApiOperation( value = "会员卡新增和修改第2步页面", notes = "会员卡新增和修改第2步页面" )
    @ApiImplicitParams( @ApiImplicitParam( name = "ctId", value = "会员卡类型id", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/editGradeTypeSecond", method = RequestMethod.GET )
    public ServerResponse editGradeTypeSecond( HttpServletRequest request, HttpServletResponse response, Integer ctId ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberCardService.editGradeTypeSecond( busId, ctId );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    LOG.error( "会员卡新增和修改第2步页面：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "会员卡新增和修改异常" );
	}
    }

    @ApiOperation( value = "查询卡片背景模板", notes = "查询卡片背景模板" )
    @ResponseBody
    @RequestMapping( value = "/findCardModel", method = RequestMethod.GET )
    public ServerResponse findCardModel( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberCardService.findCardModelByBusId( busId );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    LOG.error( "查询卡片背景模板异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询卡片背景模板异常" );
	}
    }

    @ApiOperation( value = "保存卡片背景模板", notes = "保存卡片背景模板" )
    @ApiImplicitParams( @ApiImplicitParam( name = "param", value = "json字符传", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/saveCardModel", method = RequestMethod.GET )
    public ServerResponse saveCardModel( HttpServletRequest request, HttpServletResponse response, @RequestParam String param ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Integer cmId = memberCardService.saveCardModel( busId, param );
	    return ServerResponse.createBySuccess( cmId );
	} catch ( Exception e ) {
	    LOG.error( "查询卡片背景模板异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询卡片背景模板异常" );
	}
    }

    @ApiOperation( value = "会员卡新增和修改第3步页面", notes = "会员卡新增和修改第3步页面" )
    @ApiImplicitParams( @ApiImplicitParam( name = "ctId", value = "会员卡类型id", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/editGradeTypeThird", method = RequestMethod.GET )
    public ServerResponse editGradeTypeThird( HttpServletRequest request, HttpServletResponse response, Integer ctId ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberCardService.editGradeTypeThird( busId, ctId );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    LOG.error( "会员卡新增和修改第3步页面异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "会员卡新增和修改第3步页面异常" );
	}
    }


    @ApiOperation( value = "添加完成判断是否还有要添加的会员卡", notes = "添加完成判断是否还有要添加的会员卡" )
    @ResponseBody
    @RequestMapping( value = "/isSurplusMemberCard", method = RequestMethod.POST )
    public ServerResponse isSurplusMemberCard(HttpServletRequest request, HttpServletResponse response){
	Integer busId = SessionUtils.getPidBusId( request );

	try {
	   Map<String,Object> map=memberCardService.isSurplusMemberCard( busId );
	    return ServerResponse.createBySuccess(map);
	} catch ( Exception e ) {
	    LOG.error( "添加完成判断是否还有要添加的会员卡：", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(),ResponseEnums.ERROR.getMsg());
	}

    }


    @ApiOperation( value = "保存会员卡设置", notes = "保存会员卡设置" )
    @ApiImplicitParams( @ApiImplicitParam( name = "json", value = "会员卡json类型", paramType = "query", required = false, dataType = "String" ) )
    @ResponseBody
    @RequestMapping( value = "/saveOrUpdateGradeType", method = RequestMethod.POST )
    public ServerResponse saveOrUpdateGradeType( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	Integer busId = SessionUtils.getPidBusId( request );
	try {
	    LOG.error( "保存参数:"+json );
	    memberCardService.saveOrUpdateGradeType( json, busId );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    LOG.error( "保存会员卡设置异常：", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "会员卡资料设置", notes = "会员卡资料设置" )
    @ResponseBody
    @RequestMapping( value = "/findMemberOption", method = RequestMethod.GET )
    public ServerResponse findMemberOption( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    MemberOption memberOption = memberCardService.findOption( busId );
	    return ServerResponse.createBySuccess( memberOption );
	} catch ( BusinessException e ) {
	    LOG.error( "查询会员资料设置异常：", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}

    }

    @ApiOperation( value = "保存会员资料设置", notes = "保存会员资料设置" )
    @ResponseBody
    @RequestMapping( value = "/saveMemberOption", method = RequestMethod.POST )
    public ServerResponse saveMemberOption( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {

	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.saveOrUpdateOption( json, busId );
	    return ServerResponse.createBySuccess();
	} catch ( Exception e ) {
	    LOG.error( "保存会员资料设置异常：", e );
	    return ServerResponse.createByError( "保存会员资料设置失败" );
	}
    }

    @ApiOperation( value = "通用设置", notes = "通用设置" )
    @ResponseBody
    @RequestMapping( value = "/findtongYongSet", method = RequestMethod.GET )
    public ServerResponse findtongYongSet( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberCardService.findtongyongSet( busId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    LOG.error( "查询通用设置失败异常：", e );
	    return ServerResponse.createByError( "查询通用设置失败" );
	}
    }

    @ApiOperation( value = "保存通用设置", notes = "保存通用设置" )
    @ResponseBody
    @RequestMapping( value = "/saveTongyongSet", method = RequestMethod.POST )
    public ServerResponse saveTongyongSet( HttpServletRequest request, HttpServletResponse response, String json ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.saveTongyongSet( json, busId );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "编辑礼品", notes = "编辑礼品" )
    @ResponseBody
    @RequestMapping( value = "/editGift", method = RequestMethod.GET )
    public ServerResponse editGift( HttpServletRequest request, HttpServletResponse response, Integer id ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberCardService.editGift( id );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( "错误" );
	}
    }

    @ApiOperation( value = "删除礼品", notes = "删除礼品" )
    @ResponseBody
    @RequestMapping( value = "/deleteGift", method = RequestMethod.GET )
    public ServerResponse deleteGift( HttpServletRequest request, HttpServletResponse response, Integer id ) {
	try {
	    memberCardService.deleteGift( id );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "保存或修改礼品设置", notes = "保存或修改礼品设置" )
    @ResponseBody
    @RequestMapping( value = "/saveOrUpdateGift", method = RequestMethod.POST )
    public ServerResponse saveOrUpdateGift( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.saveOrUpdateGift( json, busId );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "查询系统通知", notes = "查询系统通知" )
    @ResponseBody
    @RequestMapping( value = "/findSystemNotice", method = RequestMethod.GET )
    public ServerResponse findSystemNotice( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberNoticeService.findSystemNotice( busId );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "保存系统通知", notes = "保存系统通知" )
    @ResponseBody
    @RequestMapping( value = "/saveSystemNotice", method = RequestMethod.POST )
    public ServerResponse saveSystemNotice( HttpServletRequest request, HttpServletResponse response, String json ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberNoticeService.saveSystemNotice( busId, json );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "分页查询会员消息", notes = "分页查询会员消息" )
    @ApiImplicitParams( @ApiImplicitParam( name = "param", value = "分页参数", paramType = "query", required = false, dataType = "Integer" ) )
    @ResponseBody
    @RequestMapping( value = "/findMemberNotice", method = RequestMethod.GET )
    public ServerResponse findMemberNotice( HttpServletRequest request, HttpServletResponse response, @RequestParam String param ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Page page = memberNoticeService.findMemberNotice( busId, param );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "编辑会员消息", notes = "编辑会员消息" )
    @ResponseBody
    @RequestMapping( value = "/editMemberNotice", method = RequestMethod.GET )
    public ServerResponse editMemberNotice( HttpServletRequest request, HttpServletResponse response, Integer id ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberNoticeService.editMemberNotice( busId, id );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "统计会员数量", notes = "统计会员数量" )
    @ResponseBody
    @RequestMapping( value = "/countMember", method = RequestMethod.GET )
    public ServerResponse countMember( HttpServletRequest request, HttpServletResponse response, @RequestParam String ctIds ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    int count = memberNoticeService.countMember( ctIds, busId );
	    return ServerResponse.createBySuccess( count );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( "错误" );
	}

    }

    @ApiOperation( value = "保存会员消息", notes = "保存会员消息" )
    @ApiImplicitParams( @ApiImplicitParam( name = "sendDate", value = "发送时间", paramType = "query", required = false, dataType = "String" ) )
    @ResponseBody
    @RequestMapping( value = "/saveMemberNotice", method = RequestMethod.POST )
    public ServerResponse saveMemberNotice( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > json ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberNoticeService.saveMemberNotice( busId, json );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "删除会员消息", notes = "删除会员消息" )
    @ApiImplicitParams( @ApiImplicitParam( name = "id", value = "id", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/deleteMemberNotice", method = RequestMethod.GET )
    public ServerResponse deleteMemberNotice( HttpServletRequest request, HttpServletResponse response, Integer id ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberNoticeService.deleteMemberNotice( id );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "删除消息模板", notes = "删除消息模板" )
    @ApiImplicitParams( @ApiImplicitParam( name = "id", value = "id", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/deleteMemberNoticeModel", method = RequestMethod.GET )
    public ServerResponse deleteMemberNoticeModel( HttpServletRequest request, HttpServletResponse response, Integer id ) {
	try {
	    memberNoticeService.deleteMemberNoticeModel( id );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "会员短信发送状态", notes = "会员短信发送状态" )
    @ResponseBody
    @RequestMapping( value = "/findNoticeUser", method = RequestMethod.GET )
    public ServerResponse findNoticeUser( HttpServletRequest request, HttpServletResponse response, @RequestParam String params ) {

	try {
	    Page page = memberNoticeService.findNoticeUser( params );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "查询数据异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "重新发送短信", notes = "重新发送短信" )
    @ApiImplicitParams( @ApiImplicitParam( name = "noticeId", value = "会员短信消息id", paramType = "query", required = false, dataType = "String" ) )
    @ResponseBody
    @RequestMapping( value = "/resendNoticeUser", method = RequestMethod.POST )
    public ServerResponse resendNoticeUser( HttpServletRequest request, HttpServletResponse response, @RequestParam String params ) {

	try {
	    memberNoticeService.resendNoticeUser( params );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    LOG.error( "重新发送短信异常", e );
	    return ServerResponse.createByError();
	}
    }

    @ApiOperation( value = "查询会员卡发布信息", notes = "查询会员卡发布信息" )
    @ResponseBody
    @RequestMapping( value = "/findCard", method = RequestMethod.GET )
    public ServerResponse findCard( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > map = memberCardService.findCard( busId );
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    LOG.error( "查询会员卡发布信息异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "发布会员卡", notes = "发布会员卡" )
    @ResponseBody
    @RequestMapping( value = "/publishCard", method = RequestMethod.POST )
    public ServerResponse publishCard( HttpServletRequest request, HttpServletResponse response, @RequestParam String params ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.publishCard( busId, params );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "删除会员卡", notes = "删除会员卡" )
    @ResponseBody
    @RequestMapping( value = "/deleteCard", method = RequestMethod.POST )
    public ServerResponse deleteCard( HttpServletRequest request, HttpServletResponse response, @RequestParam String params ) {
	try {
	    JSONObject json = JSON.parseObject( params );
	    Integer ctId = CommonUtil.toInteger( json.get( "ctId" ) );
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.deleteCard( busId, ctId );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "浏览链接", notes = "浏览链接" )
    @ResponseBody
    @RequestMapping( value = "/browseAddr", method = RequestMethod.POST )
    public ServerResponse browseAddr(HttpServletRequest request, HttpServletResponse response){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    String url = PropertiesUtil.getWebHome()+"/html/member/phone/index.html#/home/" + busId;
	    return ServerResponse.createBySuccess(url);
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }

    @ApiOperation( value = "浏览链接", notes = "浏览链接" )
    @ResponseBody
    @RequestMapping( value = "/browseImg", method = RequestMethod.GET )
    public void browseImg( HttpServletRequest request, HttpServletResponse response ) {
	Integer busId = SessionUtils.getPidBusId( request );
	String url = PropertiesUtil.getWebHome()+"/html/member/phone/index.html#/home/" + busId;
	QRcodeKit.buildQRcode( url, 500, 500, response );
    }

    @ApiOperation( value = "下载会员二维码", notes = "下载会员二维码" )
    @ResponseBody
    @RequestMapping( value = "/downPulishCardImage", method = RequestMethod.GET )
    public void downPulishCardImage( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	Integer busId = SessionUtils.getPidBusId( request );
	String url = PropertiesUtil.getWebHome()+"/html/member/phone/index.html#/home/" + busId;
	String filename = "会员卡.jpg";
	response.addHeader( "Content-Disposition", "attachment;filename=" + new String( filename.replaceAll( " ", "" ).getBytes( "utf-8" ), "iso8859-1" ) );
	response.setContentType( "application/octet-stream" );
	QRcodeKit.buildQRcode( url, 450, 450, response );
    }

    @ApiOperation( value = "视频", notes = "视频" )
    @ResponseBody
    @RequestMapping( value = "/findMemberVideoUrl", method = RequestMethod.POST )
    public ServerResponse findMemberVideoUrl(HttpServletRequest request, HttpServletResponse response,@RequestParam String params){
	try {
	    JSONObject json = JSON.parseObject( params );
	    Integer model = CommonUtil.toInteger( json.get( "model" ) );
	    String  videoUrl=requestService.getVideoUrl(model);
	    return ServerResponse.createBySuccess(videoUrl);
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}

    }

}
