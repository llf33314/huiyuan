package com.gt.member.controller.member_pc;

import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.bean.vo.DuofenCardNewVO;
import com.gt.duofencard.entity.DuofenCardNew;
import com.gt.member.dto.ServerResponse;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.common.membercard.MemberCommonService;
import com.gt.member.service.common.membercard.RequestService;
import com.gt.member.service.member.DuofenCardNewService;
import com.gt.member.service.member.DuofencardAuthorizationService;
import com.gt.member.service.member.export.ExportExcel;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.Page;
import com.gt.member.util.PropertiesUtil;
import com.gt.member.util.QRcodeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhanbing
 * @since 2018年1月17日 16:03:23
 */
@Api( value = "DuofenCardController", description = "多粉优惠券" )
@Controller
@CrossOrigin
@RequestMapping( "/memberPc/duofenCoupon" )
public class DuofenCardController {
    private static final Logger log = LoggerFactory.getLogger( DuofenCardController.class );

    @Autowired
    private DuofenCardNewService duofenCardNewService;

    @Autowired
    private MemberCommonService memberCommonService;

    @Autowired
    private DuofencardAuthorizationService authorizationService;

    @Autowired
    private ExportExcel    exportExcel;
    @Autowired
    private RequestService requestService;

    @ApiOperation( value = "新增优惠券", notes = "新增优惠券" )
    @ResponseBody
    @RequestMapping( value = "/addCoupon", method = RequestMethod.POST )
    //@PostMapping(value="add",produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse addCoupon( HttpServletRequest request, HttpServletResponse response, @RequestBody @ModelAttribute DuofenCardNewVO coupon ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Integer createBusId = SessionUtils.getLoginUser( request ).getId();

	    coupon.setBusId( busId );
	    coupon.setCreateBusId( createBusId );

	    // 自动审核
	    boolean auditFlag = memberCommonService.getAutoAuditFlag( busId );
	    if ( auditFlag ) {
		coupon.setCardStatus( 2 );
	    }

	    duofenCardNewService.addCoupon( coupon );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    log.error( "新增优惠券异常：", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "修改优惠券", notes = "修改优惠券" )
    @ResponseBody
    @RequestMapping( value = "/updateCouponById", method = RequestMethod.POST )
    public ServerResponse updateCouponById( HttpServletRequest request, HttpServletResponse response, @RequestBody @ModelAttribute DuofenCardNewVO coupon ) {
	try {
	    if ( coupon.getId() == null ) {
		return ServerResponse.createByError( "修改优惠券不存在" );
	    }
	    duofenCardNewService.updateCouponById( coupon );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    log.error( "优惠券修改异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "修改优惠券基础设置", notes = "修改优惠券基础设置" )
    @ResponseBody
    @RequestMapping( value = "/updateCouponNewById", method = RequestMethod.POST )
    public ServerResponse updateCouponNewById( HttpServletRequest request, HttpServletResponse response, @RequestBody @ModelAttribute DuofenCardNewVO coupon ) {
	try {
	    if ( coupon.getId() == null ) {
		return ServerResponse.createByError( "修改优惠券不存在" );
	    }
	    duofenCardNewService.updateCouponNewById( coupon );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    log.error( "优惠券修改异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }
    @ApiOperation( value = "修改优惠券发布设置", notes = "修改优惠券发布设置" )
    @ResponseBody
    @RequestMapping( value = "/updateCouponPublishById", method = RequestMethod.POST )
    public ServerResponse updateCouponPublishById( HttpServletRequest request, HttpServletResponse response, @RequestBody @ModelAttribute DuofenCardNewVO coupon ) {
	try {
	    if ( coupon.getId() == null ) {
		return ServerResponse.createByError( "修改优惠券不存在" );
	    }
	    duofenCardNewService.updateCouponPublishById( coupon );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    log.error( "优惠券修改异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }
    @ApiOperation( value = "修改优惠券时间设置", notes = "修改优惠券时间设置" )
    @ResponseBody
    @RequestMapping( value = "/updateCouponTimeById", method = RequestMethod.POST )
    public ServerResponse updateCouponTimeById( HttpServletRequest request, HttpServletResponse response, @RequestBody @ModelAttribute DuofenCardNewVO coupon ) {
	try {
	    if ( coupon.getId() == null ) {
		return ServerResponse.createByError( "修改优惠券不存在" );
	    }
	    duofenCardNewService.updateCouponTimeById( coupon );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    log.error( "优惠券修改异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "删除优惠券", notes = "删除优惠券" )
    @ResponseBody
    @RequestMapping( value = "/deleteByCouponId", method = RequestMethod.POST )
    public ServerResponse deleteByCouponId( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer couponId ) {
	try {
	    duofenCardNewService.deleteByCouponId( couponId );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    log.error( "优惠券删除异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

	@ApiOperation( value = "优惠券信息", notes = "优惠券信息" )
	@ResponseBody
	@RequestMapping( value = "/findCouponById", method = { RequestMethod.POST, RequestMethod.GET } )
	public ServerResponse findCouponById( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	    try {
		Map< String,Object > coupon = duofenCardNewService.findCouponById( id );
		return ServerResponse.createBySuccess( coupon );
	    } catch ( Exception e ) {
		log.error( "优惠券查询异常", e );
		return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "优惠券查询异常" );
	    }
	}

    @ApiOperation( value = "优惠券详情", notes = "优惠券详情" )
    @ResponseBody
    @RequestMapping( value = "/findCouponDetail", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse findCouponDetail( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	try {
	    Map< String,Object > coupon = duofenCardNewService.findCouponDetail( id );
	    return ServerResponse.createBySuccess( coupon );
	} catch ( Exception e ) {
	    log.error( "优惠券查询异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "优惠券查询异常" );
	}
    }

  /* 获取会员已领取优惠券数量 receiveRule 领取规则 0=每人每天最多领取 1=每人最多领取
    @ResponseBody
    @RequestMapping( value = "/getReceiveQuantity", method = RequestMethod.GET )
    public ServerResponse getReceiveQuantity( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer userId, @RequestParam Integer couponId,
		    @RequestParam Integer receiveRule ) {
	Integer receiveQuantity = duofenCardNewService.getReceiveQuantity( userId, couponId, receiveRule );
	return ServerResponse.createBySuccess( receiveQuantity );
    }*/

    @ApiOperation( value = "优惠券已领取数量  ", notes = "优惠券已领取数量  " )
    @ResponseBody
    @RequestMapping( value = "/getCouponReceiveQuantity", method = RequestMethod.GET )
    public ServerResponse getReceiveQuantity( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	Integer receiveQuantity = duofenCardNewService.getCouponReceiveQuantity( id );
	return ServerResponse.createBySuccess( receiveQuantity );
    }

    @ApiOperation( value = "获取优惠券列表", notes = "获取优惠券列表" )
    @ResponseBody
    @RequestMapping( value = "/getCouponListByBusId", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse getCouponListByBusId( HttpServletRequest request, HttpServletResponse response, @RequestParam( defaultValue = "1" ) Integer curPage,
		    @RequestParam( defaultValue = "10" ) Integer pageSize, String cardStatus, String couponName, Integer useType, Integer busId ) {
	try {
	    if ( busId == null ) {
		busId = SessionUtils.getPidBusId( request );
	    }
	    Page page = duofenCardNewService.getCouponListByBusId( curPage, pageSize, busId, cardStatus, couponName, useType );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    log.error( "获取优惠券异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }
    @ApiOperation( value = "商家拥有优惠券数量", notes = "商家拥有优惠券数量" )
    @ResponseBody
    @RequestMapping( value = "/getCouponQuantity", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse getCouponQuantity( HttpServletRequest request, HttpServletResponse response) {
        try{
	    Integer  busId = SessionUtils.getPidBusId( request );
	    Map<String,Object>  couponQuantity = duofenCardNewService.getCouponQuantity(busId);
	    return ServerResponse.createBySuccess( couponQuantity );
	} catch ( BusinessException e ) {
	    log.error( "获取优惠券数量异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "优惠券领取列表", notes = "优惠券领取列表" )
    @ResponseBody
    @RequestMapping( value = "/getReceiveCouponListById", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse getReceiveCouponListById( HttpServletRequest request, HttpServletResponse response, @RequestParam( defaultValue = "1" ) Integer curPage,
		    @RequestParam( defaultValue = "10" ) Integer pageSize, @RequestParam Integer couponId, String searchContent ) {
	try {
	    Page page = duofenCardNewService.getReceiveCouponListById( curPage, pageSize, couponId, searchContent );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    log.error( "获取领取列表异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "优惠券购买详情", notes = "优惠券购买详情" )
    @ResponseBody
    @RequestMapping( value = "/getPaymentDetailById", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse getPaymentDetailById( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id,@RequestParam String code ) {
	try {
	    Map< String,Object > map = duofenCardNewService.getPaymentDetailById( id,code );
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    log.error( "查询购买详情异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }
    @ApiOperation( value = "核销优惠券信息", notes = "核销优惠券信息" )
    @ResponseBody
    @RequestMapping( value = "/findCouponInfoByCode", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse findCouponInfoByCode( HttpServletRequest request, HttpServletResponse response, @RequestParam String code ) {
	try {
	    Map< String,Object > coupon = duofenCardNewService.findCouponInfoByCode( code );
	    return ServerResponse.createBySuccess( coupon );
	} catch ( Exception e ) {
	    log.error( "优惠券查询异常", e );
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "优惠券查询异常" );
	}
    }

    @ApiOperation( value = "核销人员列表", notes = "核销人员列表" )
    @ResponseBody
    @RequestMapping( value = "/authorization/getAuthorizationUser", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse getAuthorizationUser( HttpServletRequest request, HttpServletResponse response, @RequestParam( defaultValue = "1" ) Integer curPage,
		    @RequestParam( defaultValue = "10" ) Integer pageSize, String searchContent ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Page page = authorizationService.getAuthorizationUser( curPage, pageSize, busId, searchContent );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    log.error( "查询优惠券核销列表异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );

	}
    }

    @ApiOperation( value = "新增核销员二维码URL", notes = "新增核销员二维码URL" )
    @ResponseBody
    @RequestMapping( value = "/authorization/getAddAuthUserUrl", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse getAddAuthUserUrl( HttpServletRequest request, HttpServletResponse response ,@RequestParam  Integer shopId ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    String url =PropertiesUtil.getWebHome()+"/html/duofencard/phone/#/accredit/"+busId+"/"+shopId;
	   	HashMap<String,Object> map = new HashMap<String,Object>();
	   	map.put( "url",url );
	    return ServerResponse.createBySuccess(map);
	} catch ( BusinessException e ) {
	    log.error( "路径获取异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );

	}
    }

    @ApiOperation( value = "删除核销人员", notes = "删除核销人员" )
    @ResponseBody
    @RequestMapping( value = "/authorization/deleteById", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse deleteById( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id ) {
	authorizationService.deleteById( id );
	return ServerResponse.createBySuccess();
    }

    @ApiOperation( value = "门店优惠券使用情况", notes = "门店优惠券使用情况" )
    @ResponseBody
    @RequestMapping( value = "/authorization/usageStatistics", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse usageStatistics( HttpServletRequest request, HttpServletResponse response ) {
	Integer busId = SessionUtils.getPidBusId( request );
	List< Map< String,Object > > usageStatistics = duofenCardNewService.usageStatistics( busId );
	return ServerResponse.createBySuccess( usageStatistics );
    }

    @ApiOperation( value = "优惠券核销列表", notes = "优惠券核销列表" )
    @ResponseBody
    @RequestMapping( value = "/authorization/usageList", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse usageList( HttpServletRequest request, HttpServletResponse response, @RequestParam( defaultValue = "1" ) Integer curPage,
		    @RequestParam( defaultValue = "10" ) Integer pageSize, String searchContent ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Page page = duofenCardNewService.usageList(curPage ,pageSize , busId,searchContent );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    log.error( "查询优惠券核销列表异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );

	}
    }

  /*  @ApiOperation( value = "增加核销用户", notes = "增加核销用户" )
    @ResponseBody
    @RequestMapping( value = "/authorization/authorizationUser", method = RequestMethod.POST )
    public ServerResponse authorizationUser( HttpServletRequest request, HttpServletResponse response, @RequestParam String json ) {
	try {
	    Map< String,Object > params = JSON.toJavaObject( JSON.parseObject( json ), Map.class );
	    Integer busId = CommonUtil.toInteger( params.get( "busId" ) );
	    Member member = SessionUtils.getLoginMember( request, busId );
	    if ( CommonUtil.isEmpty( member ) ) {
		String url = authorizeMember( request, response, params );
		if ( CommonUtil.isNotEmpty( url ) ) {
		    return ServerResponse.createByError( ResponseMemberEnums.USERGRANT.getCode(), ResponseMemberEnums.USERGRANT.getMsg(), url );
		}
	    }
	    DuofencardAuthorization authorizationInfo = new DuofencardAuthorization();
	    authorizationInfo.setOpenId( member.getOpenid() );
	    authorizationInfo.setStatus( 0 );
	    authorizationInfo.setShopId( CommonUtil.toInteger( params.get( "shopId" ) ) );
	    authorizationInfo.setBusId( busId );
	    authorizationInfo.setMemberId( member.getId() );
	    authorizationService.insert( authorizationInfo );
	    return ServerResponse.createBySuccess();
	} catch ( BusinessException e ) {
	    LOG.error( "增加核销用户异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getMsg() );
	}
    }*/

    @ApiOperation( value = "优惠券推荐列表", notes = "优惠券推荐列表" )
    @ResponseBody
    @RequestMapping( value = "/recommend/recommendList", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse recommendList( HttpServletRequest request, HttpServletResponse response, @RequestParam( defaultValue = "1" ) Integer curPage,
		    @RequestParam( defaultValue = "10" ) Integer pageSize, String searchContent ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Page page = duofenCardNewService.recommendList(curPage ,pageSize , busId,searchContent );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    log.error( "查询优惠券推荐列表异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );

	}
    }
    @ApiOperation( value = "优惠券推荐领取列表", notes = "优惠券推荐领取列表" )
    @ResponseBody
    @RequestMapping( value = "/recommend/recommendReceiveList", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse recommendReceiveList( HttpServletRequest request, HttpServletResponse response, @RequestParam( defaultValue = "1" ) Integer curPage,
		    @RequestParam( defaultValue = "10" ) Integer pageSize,@RequestParam Integer recommendId ,  String searchContent ) {
	try {
	    Page page = duofenCardNewService.recommendReceiveList(curPage ,pageSize ,recommendId,searchContent );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    log.error( "查询优惠券推荐领取列表异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );

	}
    }
    @ApiOperation( value = "推荐提现列表", notes = "推荐提现列表" )
    @ResponseBody
    @RequestMapping( value = "/recommend/withdrawList", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse withdrawList( HttpServletRequest request, HttpServletResponse response, @RequestParam( defaultValue = "1" ) Integer curPage,
		    @RequestParam( defaultValue = "10" ) Integer pageSize,  String searchContent ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Page page = duofenCardNewService.withdrawList(curPage ,pageSize ,busId,searchContent );
	    return ServerResponse.createBySuccess( page );
	} catch ( BusinessException e ) {
	    log.error( "查询推荐提现列表异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );

	}
    }

    @ApiOperation( value = "推荐提现金额设置", notes = "推荐提现金额设置" )
    @ResponseBody
    @RequestMapping( value = "/recommend/withdrawMoneySet", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse withdrawMoneySet( HttpServletRequest request, HttpServletResponse response,  Integer money ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Integer num = duofenCardNewService.withdrawMoneySet(busId,money );
	    return ServerResponse.createBySuccess( );
	} catch ( BusinessException e ) {
	    log.error( "提现金额设置异常", e );
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );

	}
    }

    @ApiOperation( value = "导出excel", notes = "导出excel" )
    @RequestMapping( value = "/exportExcel", method = { RequestMethod.POST, RequestMethod.GET } )
    public void exportExcel( HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> params) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Integer type = CommonUtil.toInteger(params.get( "type" ));
	    List<Map<String,Object>> listItem=null;
	    //type  5 核销列表  6 优惠券（购买类型优惠券）领取列表  7 优惠券（免费）领取列表

	    String saveFileName=null;// 保存文件名
	    switch ( type ){
		case 5:
		    saveFileName = "核销优惠券列表.xlsx";
		    listItem = duofenCardNewService.getUsageListByBusId( busId );
		    break;
		case 6:
		    saveFileName = "优惠券（购买类型优惠券）领取列表.xlsx";
		    listItem = duofenCardNewService.getReceiveCouponListByCouponId( CommonUtil.toInteger(  params.get( "couponId" )) );
		    break;
		case 7:
		    saveFileName = "优惠券（免费）领取列表.xlsx";
		    listItem = duofenCardNewService.getReceiveCouponListByCouponId( CommonUtil.toInteger(  params.get( "couponId" )) );
		    break;
	    }
	    SXSSFWorkbook excel = exportExcel.todo( (byte) type.intValue(), listItem );

	    response.reset();

	    // 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
	    response.addHeader("Content-Disposition",
			    "attachment;filename="
					    + new String(saveFileName.replaceAll(" ", "")
					    .getBytes("utf-8"), "ISO8859-1"));
	    // response.addHeader("Content-Length", "" + file.length());
	    OutputStream os = new BufferedOutputStream(
			    response.getOutputStream());
	    response.setContentType("application/msexcel");
	    excel.write(os);// 输出文件
	    os.flush();
	    os.close();
	} catch ( Exception e ) {
	    e.printStackTrace();
	    log.error( "导出excel异常", e );
	}
    }


    @ApiOperation( value = "获取节假日", notes = "获取节假日" )
    @ResponseBody
    @RequestMapping( value = "/getLegalHolidays", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse getLegalHolidays( HttpServletRequest request, HttpServletResponse response ) {
	try {
	    List<Map> map =requestService.findHoliList();
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    log.error( "获取节假日异常", e );
	    return ServerResponse.createByError( "获取节假日" );
	}
    }

    @ApiOperation( value = "获取所有门店", notes = "获取所有门店" )
    @ResponseBody
    @RequestMapping( value = "/getWxshops", method = { RequestMethod.POST, RequestMethod.GET } )
    public ServerResponse getWxshops( HttpServletRequest request, HttpServletResponse response, String content, Integer width, Integer height ) {
	Integer busId = SessionUtils.getPidBusId( request );
	List< Map > wxShops = requestService.findShopAllByBusId( busId );
	return ServerResponse.createBySuccess( wxShops );
    }

    @ApiOperation( value = "生成二维码", notes = "生成二维码" )
    @RequestMapping( value = "/qrCode", method = { RequestMethod.POST, RequestMethod.GET } )
    public void qrCode( HttpServletRequest request, HttpServletResponse response, String content ) {
	QRcodeKit.buildQRcode( content, 500, 500, response );
    }

    @ApiOperation( value = "下载二维码", notes = "下载二维码" )
    @RequestMapping( value = "/downLoadQrCode", method = { RequestMethod.POST, RequestMethod.GET } )
    public void downLoadQrCode( HttpServletRequest request, HttpServletResponse response, String content ) {
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    String url = content;
	    String filename = "领取优惠券二维码.jpg";
	    response.addHeader( "Content-Disposition", "attachment;filename=" + new String( filename.replaceAll( " ", "" ).getBytes( "utf-8" ), "iso8859-1" ) );
	    response.setContentType( "application/octet-stream" );
	    QRcodeKit.buildQRcode( url, 500, 500, response );
	} catch ( UnsupportedEncodingException e ) {
	    log.error( "下载二维码异常" );
	    e.printStackTrace();
	}
    }
}
