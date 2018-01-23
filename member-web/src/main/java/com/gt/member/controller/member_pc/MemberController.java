package com.gt.member.controller.member_pc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.enums.ResponseEnums;
import com.gt.api.util.SessionUtils;
import com.gt.member.dto.ServerResponse;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.service.bo.ErrorWorkbook;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.service.member.MemberNoticeService;
import com.gt.member.service.member.export.ExportExcel;
import com.gt.member.util.CommonUtil;
import com.gt.member.util.Page;
import com.gt.member.util.RedisCacheUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/18.
 */
@Api( value = "会员列表", description = "会员列表" )
@Controller
@CrossOrigin
@RequestMapping( "/memberPc/memberController" )
public class MemberController {

    private static final Logger LOG = LoggerFactory.getLogger( MemberController.class );

    @Autowired
    private MemberCardService memberCardService;

    @Autowired
    private MemberNoticeService memberNoticeService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private ExportExcel exportExcel;



    @ApiOperation( value = "判断当前登录人是否是主账户", notes = "判断当前登录人是否是主账户" )
    @ResponseBody
    @RequestMapping( value = "/findIsPid", method = RequestMethod.POST )
    public ServerResponse findIsPid(HttpServletRequest request, HttpServletResponse response){
        Integer pId=SessionUtils.getPidBusId( request );
        Integer busId=SessionUtils.getLoginUser( request ).getId();
        if(pId.equals( busId )){
	    return ServerResponse.createBySuccess( "",1 );
	}
	return ServerResponse.createBySuccess( "",0 );

    }

    @ApiOperation( value = "查询商家发布的会员类型", notes = "查询商家发布的会员类型" )
    @ResponseBody
    @RequestMapping( value = "/findCardType", method = RequestMethod.GET )
    public ServerResponse findCardType(HttpServletRequest request, HttpServletResponse response){

	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    List<Map<String,Object>> listMap = memberCardService.findGradeTypeByBusId(busId );
	    return ServerResponse.createBySuccess( listMap );
	} catch ( Exception e ) {
	    LOG.error( "查询商家发布的会员类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员列表异常" );
	}
    }


    @ApiOperation( value = "查询商家会员卡等级类型", notes = "查询商家会员卡等级类型" )
    @ResponseBody
    @RequestMapping( value = "/findGradeType", method = RequestMethod.GET )
    public ServerResponse findGradeType(HttpServletRequest request, HttpServletResponse response,@RequestParam Integer ctId){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    List<Map<String,Object>> listMap = memberCardService.findGradeTypeByCtId(busId,ctId );
	    return ServerResponse.createBySuccess( listMap );
	} catch ( Exception e ) {
	    LOG.error( "查询商家发布的会员类型异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员列表异常" );
	}
    }


    @ApiOperation( value = "查询会员列表", notes = "查询会员列表" )
    @ResponseBody
    @RequestMapping( value = "/findMember", method = RequestMethod.GET )
    public ServerResponse findMember(HttpServletRequest request, HttpServletResponse response,@RequestParam String params ){

	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Page page = memberCardService.findMemberPage(busId, params );
	    return ServerResponse.createBySuccess( page );
	} catch ( Exception e ) {
	    LOG.error( "查询会员列表异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( ResponseEnums.ERROR.getCode(), "查询会员列表异常" );
	}
    }

    @ApiOperation( value = "会员批量审核", notes = "会员批量审核" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberIds", value = "会员卡memberId集合逗号隔开", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "ischecked", value = "审核状态 0不通过 1通过", paramType = "query", required = false, dataType = "int" )

    })
    @ResponseBody
    @RequestMapping( value = "/cardBatchApplyChecked", method = RequestMethod.POST )
    public ServerResponse cardBatchApplyChecked(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam String params ){
	try {
	    JSONObject obj= JSON.parseObject( params );
	    String memberIds= CommonUtil.toString(obj.get( "memberIds" ));
	    Integer ischecked=CommonUtil.toInteger(  obj.get( "ischecked" ));
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.cardBatchApplyChecked(busId, memberIds,ischecked );
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    LOG.error( "会员批量审核异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	}catch ( Exception e ){
	    LOG.error( "会员批量审核异常：", e );
	    return ServerResponse.createByError("错误");
	}
    }

    @ApiOperation( value = "会员卡审核", notes = "会员卡审核" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberId", value = "会员卡memberId", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "ischecked", value = "审核状态 0不通过 1通过", paramType = "query", required = false, dataType = "int" )

    })
    @ResponseBody
    @RequestMapping( value = "/cardApplyCheckedByOne", method = RequestMethod.POST )
    public ServerResponse cardApplyCheckedByOne(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam String params){
	try {
	    JSONObject obj= JSON.parseObject( params );
	    Integer memberId= CommonUtil.toInteger(obj.get( "memberId" ));
	    Integer ischecked=CommonUtil.toInteger(  obj.get( "ischecked" ));
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.cardApplyCheckedByOne(busId, memberId,ischecked );
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    LOG.error( "会员卡审核异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}catch ( Exception e ){
	    LOG.error( "会员批量审核异常：", e );
	    return ServerResponse.createByError("错误");
	}
    }

    @ApiOperation( value = "商家赠送积分、粉币", notes = "商家赠送积分、粉币" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberIds", value = "会员卡memberId集合逗号隔开", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "giveType", value = "赠送类型 2粉币 1积分", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "number", value = "数量", paramType = "query", required = false, dataType = "int" )

    })
    @ResponseBody
    @RequestMapping( value = "/addIntegralAndfenbi", method = RequestMethod.POST )
    public ServerResponse addIntegralAndfenbi(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam  String json){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.addIntegralAndfenbi(busId, json);
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    LOG.error( "商家赠送积分、粉币异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(),e.getMessage());
	}
    }

    @ApiOperation( value = "给会员发送通知", notes = "给会员发送通知" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberIds", value = "会员卡memberId集合逗号隔开", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "id", value = "短信模板id", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/sendNoticeToUser", method = RequestMethod.GET )
    public ServerResponse sendNoticeToUser(HttpServletRequest request,
		    HttpServletResponse response,Integer id,String memberIds){
	try {
	    memberNoticeService.sendNoticeToUser(id, memberIds);
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    LOG.error( "会员发送通知：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(),e.getMessage());
	}
    }



    @ApiOperation( value = "拉黑或恢复会员", notes = "拉黑或恢复会员" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "mcId", value = "会员卡id", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "cardStatus", value = "会员卡状态 状态 0正常 1禁用", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/updateDis", method = RequestMethod.GET )
    public ServerResponse updateDis(HttpServletRequest request,
		    HttpServletResponse response,Integer mcId,Integer cardStatus){
	try {
	    memberCardService.updateDis(mcId, cardStatus);
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    LOG.error( "拉黑或恢复会员异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(),e.getMessage());
	}
    }

    @ApiOperation( value = "删除会员", notes = "删除会员" )
    @ApiImplicitParam( name = "memberId", value = "会员id", paramType = "query", required = false, dataType = "int" )

    @ResponseBody
    @RequestMapping( value = "/deleteMember", method = RequestMethod.POST )
    public ServerResponse deleteMember(HttpServletRequest request,
		    HttpServletResponse response,Integer memberId){
	try {
	    memberCardService.deleteMemberCard(request,memberId);
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    LOG.error( "拉黑或恢复会员异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(),e.getMessage());
	}
    }




    @ApiOperation( value = "查询会员详情", notes = "查询会员详情" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberId", value = "会员id", paramType = "query", required = false, dataType = "int" )
		    })
    @ResponseBody
    @RequestMapping( value = "/findMemberDetails", method = RequestMethod.GET )
    public ServerResponse findMemberDetails(HttpServletRequest request,
		    HttpServletResponse response,Integer memberId){

	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	     Map<String,Object> map= memberCardService.findMemberDetails(busId,memberId);
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    LOG.error( "查询会员详情：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( "查询会员详情失败");
	}
    }

    @ApiOperation( value = "查询修改会员资料", notes = "修改会员资料" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberId", value = "会员id", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/findMemberByMemberId", method = RequestMethod.GET )
    public ServerResponse findMemberByMemberId(HttpServletRequest request,
		    HttpServletResponse response,Integer memberId){

	try {
	    Map<String,Object> map=memberCardService.findMemberByMemberId(memberId);
	    return ServerResponse.createBySuccess(map  );
	} catch ( Exception e ) {
	    LOG.error( "查询会员详情：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( "修改会员资料失败");
	}
    }


    @ApiOperation( value = "修改会员资料保存", notes = "修改会员资料" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberId", value = "会员id", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/updateMember", method = RequestMethod.POST )
    public ServerResponse updateMember(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam  String json){

	try {
	    memberCardService.updateMember(json);
	    return ServerResponse.createBySuccess(  );
	} catch ( Exception e ) {
	    LOG.error( "查询会员详情：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( "修改会员资料失败");
	}
    }


    @ApiOperation( value = "导入实体卡", notes = "导入实体卡" )
    @ResponseBody
    @RequestMapping( value = "/upLoadMember", method = RequestMethod.POST )
    public ServerResponse upLoadMember(HttpServletRequest request,
		    HttpServletResponse response){
	LOG.info("调用导入实体卡");
	try {
	    Integer danqianBusId=SessionUtils.getLoginUser( request ).getId();
	    Integer busId = SessionUtils.getPidBusId( request );

	    MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;

	    MultipartFile file = mulRequest.getFile("file");
	    String filename = file.getOriginalFilename();
	    if (filename != null && !"".equals(filename)) {
		InputStream input = file.getInputStream();
		List<ErrorWorkbook > wbs = memberCardService.upLoadMember(busId, input);
		if (wbs == null) {
		    return ServerResponse.createBySuccess(  );
		} else if (wbs.size() == 0) {
		    return ServerResponse.createByError( ResponseMemberEnums.NOT_MEMBER_COUNT.getCode(), ResponseMemberEnums.NOT_MEMBER_COUNT.getMsg());
		} else {
		    String redisStr="MEMBER_IMP_"+danqianBusId;
		    redisCacheUtil.set(  redisStr, JSONArray.toJSONString(wbs),5*60);
		    return ServerResponse.createDataByError( ResponseMemberEnums.IMP_ERROR.getCode(), ResponseMemberEnums.IMP_ERROR.getMsg(),redisStr);
		}
	    }
	    return ServerResponse.createByError("没有读取到文件");
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	}catch (Exception e) {
	    LOG.error( "导入会员卡异常",e );
	    e.printStackTrace();
	    return ServerResponse.createByError("导入会员卡异常");
	}
    }


    @ApiOperation( value = "会员卡导入失败  下载错误信息", notes = "会员卡导入失败  下载错误信息" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "redisStr", value = "redisStr 导入错误下载标示", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/downExecl", method = RequestMethod.GET )
    public void downExecl(HttpServletRequest request,
		    HttpServletResponse response,  String redisStr) {
	try {
	    String json=redisCacheUtil.get(redisStr);
	    List<ErrorWorkbook> wbs = (List<ErrorWorkbook>) JSONArray.parseArray( json,ErrorWorkbook.class );
	    SXSSFWorkbook wb = memberCardService.errorMember(wbs);
	    response.reset();
	    String saveFileName = "导入会员卡错误信息.xlsx"; // 保存文件名
	    // 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
	    response.addHeader("Content-Disposition",
			    "attachment;filename="
					    + new String(saveFileName.replaceAll(" ", "")
					    .getBytes("utf-8"), "iso8859-1"));
	    // response.addHeader("Content-Length", "" + file.length());
	    OutputStream os = new BufferedOutputStream(
			    response.getOutputStream());
	    response.setContentType("application/octet-stream");
	    wb.write(os);// 输出文件
	    os.flush();
	    os.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @ApiOperation( value = "会员卡导出", notes = "会员卡导出" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "ctId", value = "会员卡类型 -1实体卡  " , paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "gtId", value = "等级 0导出全部", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/exportMember", method = RequestMethod.GET )
    public void exportMember(HttpServletRequest request,
		    HttpServletResponse response, @RequestParam Integer ctId,@RequestParam Integer gtId){
	Integer busId = SessionUtils.getPidBusId( request );

	List<Map<String, Object>> listMap = memberCardService.findMember(busId,
			ctId,gtId);
	try {
	    SXSSFWorkbook wb = exportExcel.todo((byte) 1, listMap);
	    response.reset();
	    String saveFileName = "会员卡.xlsx"; // 保存文件名
	    // 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
	    response.addHeader("Content-Disposition",
			    "attachment;filename="
					    + new String(saveFileName.replaceAll(" ", "")
					    .getBytes("utf-8"), "iso8859-1"));
	    // response.addHeader("Content-Length", "" + file.length());
	    OutputStream os = new BufferedOutputStream(
			    response.getOutputStream());
	    response.setContentType("application/octet-stream");
	    wb.write(os);// 输出文件
	    os.flush();
	    os.close();
	} catch (Exception e) {
	    LOG.error("导出会员信息异常", e);
	}
    }




    @ApiOperation( value = "查询会员卡信息", notes = "查询会员卡信息" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "cardNo", value = "卡号" , paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/findMemberCardByCardNo", method = RequestMethod.GET )
    public ServerResponse findMemberCardByCardNo(HttpServletRequest request,
		    HttpServletResponse response,  String cardNo){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map<String,Object> map=memberCardService.findMemberCardByCardNo(busId,cardNo);
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    LOG.error( "查询会员卡信息异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(),e.getMessage());
	}

    }

    @ApiOperation( value = "积分兑换", notes = "积分兑换" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "cardNo", value = "卡号  " , paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "intergral", value = "兑换积分", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/intergralConsume", method = RequestMethod.GET )
    public ServerResponse intergralConsume(HttpServletRequest request,
		    HttpServletResponse response,String cardNo,Integer intergral){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.intergralConsume(busId,intergral,cardNo);
	    return ServerResponse.createBySuccess(  );
	}catch ( BusinessException e ){
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	}catch ( Exception e ) {
	    LOG.error( "积分兑换异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( "积分兑换失败");
	}
    }

    @ApiOperation( value = "工作台——会员卡统计", notes = "工作台 会员卡统计" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "ctId", value = "会员卡类型 第一次默认传0" , paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/memberTongJi", method = RequestMethod.GET )
    public ServerResponse memberTongJi(HttpServletRequest request,
		    HttpServletResponse response,Integer ctId){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map<String,Object> map= memberCardService.memberTongJi(busId,ctId);
	    return ServerResponse.createBySuccess(map  );
	} catch ( BusinessException e ) {
	    LOG.error( "积分兑换异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }


    @ApiOperation( value = "工作台——会员卡统计7天售卡和销售", notes = "工作台——会员卡统计7天售卡和销售" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "ctId", value = "会员卡类型 第一次默认传0" , paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/sum7DayOrder", method = RequestMethod.GET )
    public ServerResponse sum7DayOrder(HttpServletRequest request,
		    HttpServletResponse response,Integer ctId,String startdate,String enddate){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map<String,Object> map= memberCardService.sum7DayOrder(busId,ctId,startdate,enddate);
	    return ServerResponse.createBySuccess(map  );
	} catch ( BusinessException e ) {
	    LOG.error( "积分兑换异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }



    @ApiOperation( value = "交易记录——充值记录", notes = "充值记录" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "cardNo", value = "卡号、手机号" , paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "startTime", value = "查询日期" , paramType = "query", required = false, dataType = "String" )
    })
    @ResponseBody
    @RequestMapping( value = "/findChongZhiLog", method = RequestMethod.GET )
    public ServerResponse findChongZhiLog(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam String  params){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Page page= memberCardService.findChongZhiLog(busId,params);
	    return ServerResponse.createBySuccess( page  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "交易记录——充值记录详情", notes = "充值记录详情" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "ucId", value = "订单id" , paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/findChongZhiLogDetails", method = RequestMethod.GET )
    public ServerResponse findChongZhiLogDetails(HttpServletRequest request,
		    HttpServletResponse response,Integer ucId){
	try {
	    Map<String,Object> map= memberCardService.findChongZhiLogDetails(ucId);
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "交易记录——积分兑换记录", notes = "兑换记录" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "cardNo", value = "卡号、手机号" , paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "startTime", value = "查询日期" , paramType = "query", required = false, dataType = "String" )
    })
    @ResponseBody
    @RequestMapping( value = "/findDuiHuanLog", method = RequestMethod.GET )
    public ServerResponse findDuiHuanLog(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam String params){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Page page= memberCardService.findDuiHuanLog(busId,params);
	    return ServerResponse.createBySuccess( page  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "交易记录——积分兑换记录详情", notes = "兑换记录详情" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "ucId", value = "订单id" , paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/findDuiHuanLogDetails", method = RequestMethod.GET )
    public ServerResponse findDuiHuanLogDetails(HttpServletRequest request,
		    HttpServletResponse response,Integer ucId){
	try {
	    Map<String,Object> map= memberCardService.findDuiHuanLogDetails(ucId);
	    return ServerResponse.createBySuccess( map );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }


    @ApiOperation( value = "交易记录——次卡记录", notes = "次卡记录" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "cardNo", value = "卡号、手机号" , paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "startTime", value = "查询日期" , paramType = "query", required = false, dataType = "String" )
    })
    @ResponseBody
    @RequestMapping( value = "/findCiKaLog", method = RequestMethod.GET )
    public ServerResponse findCiKaLog(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam String params){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > param = JSON.toJavaObject( JSON.parseObject( params ), Map.class );
	    Page page= memberCardService.findCikaLog(busId,param);
	    return ServerResponse.createBySuccess( page  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }


    @ApiOperation( value = "交易记录——次卡记录详情", notes = "次卡记录详情" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "ucId", value = "订单id" , paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/findCiKaLogDetails", method = RequestMethod.GET )
    public ServerResponse findCiKaLogDetails(HttpServletRequest request,
		    HttpServletResponse response,Integer ucId){
	try {
	    Map<String,Object> map=  memberCardService.findCikaLogDetails(ucId);
	    return ServerResponse.createBySuccess( map  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }


    @ApiOperation( value = "交易记录——消费记录", notes = "消费记录" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "cardNo", value = "卡号、手机号" , paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "startTime", value = "查询日期" , paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "payStatus", value = "支付方式 0未支付 1已支付" , paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/findXiaoFeiLog", method = RequestMethod.GET )
    public ServerResponse findXiaoFeiLog(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam String params){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map< String,Object > param = JSON.toJavaObject( JSON.parseObject( params ), Map.class );
	    Page page= memberCardService.findXiaoFeiLog(busId,param);
	    return ServerResponse.createBySuccess( page  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }


    @ApiOperation( value = "交易记录——消费记录详情", notes = "消费记录详情" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "ucId", value = "订单id" , paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/findXiaoFeiLogDetails", method = RequestMethod.GET )
    public ServerResponse findXiaoFeiLogDetails(HttpServletRequest request,
		    HttpServletResponse response,Integer ucId){
	try {
	    Map<String,Object> map=  memberCardService.findXiaoFeiLogDetails(ucId);
	    return ServerResponse.createBySuccess( map  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "工作台--推荐--会员推荐", notes = "会员推荐" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "phone", value = "卡号、手机号" , paramType = "query", required = false, dataType = "String" )
    })
    @ResponseBody
    @RequestMapping( value = "/findMemberCommend", method = RequestMethod.GET )
    public ServerResponse findMemberCommend(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam  String  params){
	try {
	    Map<String,Object> map=JSON.parseObject( params,Map.class );
	    Integer busId = SessionUtils.getPidBusId( request );
	    map.put( "recommendType",0 );
	    Page page= memberCardService.findCommend(busId,map);
	    return ServerResponse.createBySuccess( page  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }


    @ApiOperation( value = "工作台--推荐--优惠券推荐", notes = "会员推荐" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "phone", value = "卡号、手机号" , paramType = "query", required = false, dataType = "String" )
    })
    @ResponseBody
    @RequestMapping( value = "/findCardCommend", method = RequestMethod.GET )
    public ServerResponse findCardCommend(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam  String params  ){
	try {
	    Map<String,Object> map=JSON.parseObject( params,Map.class );
	    Integer busId = SessionUtils.getPidBusId( request );
	    map.put( "recommendType",1 );
	    Page page= memberCardService.findCommend(busId,map);
	    return ServerResponse.createBySuccess( page  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "工作台--推荐--佣金提取", notes = "会员推荐" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "phone", value = "手机号" , paramType = "query", required = false, dataType = "String" )
    })
    @ResponseBody
    @RequestMapping( value = "/findPickLog", method = RequestMethod.GET )
    public ServerResponse findPickLog(HttpServletRequest request,
		    HttpServletResponse response,@RequestParam String params){
	try {
	    Map<String,Object> map=JSON.parseObject( params,Map.class );
	    Integer busId = SessionUtils.getPidBusId( request );
	    Page page= memberCardService.findPickLog(busId,map);
	    return ServerResponse.createBySuccess( page  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "会员卡充值信息查询", notes = "会员卡充值" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "phone", value = "手机号或卡号" , paramType = "query", required = false, dataType = "String" )
    })
    @ResponseBody
    @RequestMapping( value = "/findMemberCardByrecharge", method = RequestMethod.GET )
    public ServerResponse findMemberCardByrecharge(HttpServletRequest request,
		    HttpServletResponse response,  String phone){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Map<String,Object> map= memberCardService.findMemberCardByrecharge(busId,phone);
	    return ServerResponse.createBySuccess( map  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }


    @ApiOperation( value = "会员卡充值信息", notes = "会员卡充值" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "cardNo", value = "手机号或卡号" , paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "ctId", value = "选择充值卡类型" , paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "money", value = "充值金额" , paramType = "query", required = false, dataType = "double" )
    })
    @ResponseBody
    @RequestMapping( value = "/rechargeMemberCard", method = RequestMethod.POST )
    public ServerResponse rechargeMemberCard(HttpServletRequest request,
		    HttpServletResponse response,  String params){

	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    memberCardService.rechargeMemberCard(busId,params);
	    return ServerResponse.createBySuccess(   );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "查询商家管理的门店信息", notes = "查询商家管理的门店信息" )
    @ResponseBody
    @RequestMapping( value = "/findBusUserShop", method = RequestMethod.GET )
    public ServerResponse findBusUserShop(HttpServletRequest request,
		    HttpServletResponse response){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Integer dangqianbusId = SessionUtils.getLoginUser( request ).getId();
	    List<Map<String,Object>> list= memberCardService.findBusUserShop(busId,dangqianbusId);
	    return ServerResponse.createBySuccess(  list );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}catch ( Exception e ){
	    return ServerResponse.createByError(ResponseEnums.ERROR.getCode(),ResponseEnums.ERROR.getMsg());
	}

    }


    @ApiOperation( value = "会员卡消费查询信息", notes = "会员卡消费查询信息" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "cardNo", value = "手机号或卡号" , paramType = "query", required = false, dataType = "String" )
    })
    @ResponseBody
    @RequestMapping( value = "/consumefindMemberCard", method = RequestMethod.GET )
    public ServerResponse consumefindMemberCard(HttpServletRequest request,
		    HttpServletResponse response,  String cardNo,Integer shopId){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	   Map<String,Object> map= memberCardService.consumefindMemberCard(busId,cardNo,shopId);
	    return ServerResponse.createBySuccess( map  );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }

    @ApiOperation( value = "会员卡消费", notes = "会员卡消费" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "cardNo", value = "手机号或卡号" , paramType = "query", required = false, dataType = "String" )
    })
    @ResponseBody
    @RequestMapping( value = "/consumeMemberCard", method = RequestMethod.POST )
    public ServerResponse consumeMemberCard(HttpServletRequest request,
		    HttpServletResponse response,  String params){
	try {
	    Integer busId = SessionUtils.getPidBusId( request );
	    Integer dangqianbusId = SessionUtils.getLoginUser( request ).getId();
	    memberCardService.consumeMemberCard(busId,params,dangqianbusId);
	    return ServerResponse.createBySuccess(   );
	} catch ( BusinessException e ) {
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }



}
