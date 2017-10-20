package com.gt.member.controller.member_pc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gt.api.enums.ResponseEnums;
import com.gt.member.dto.ServerResponse;
import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import com.gt.member.export.ExportExcel;
import com.gt.member.service.bo.ErrorWorkbook;
import com.gt.member.service.member.MemberCardService;
import com.gt.member.service.member.MemberNoticeService;
import com.gt.member.util.RedisCacheUtil;
import com.gt.member.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @ApiOperation( value = "查询会员列表", notes = "查询会员列表" )
    @ResponseBody
    @RequestMapping( value = "/findMember", method = RequestMethod.GET )
    public ServerResponse findMember(HttpServletRequest request, HttpServletResponse response,Map<String,Object> params ){

	try {
	    Integer busId = SessionUtil.getPidBusId( request );
	    Map<String,Object> memberS = memberCardService.findMember(busId, params );
	    return ServerResponse.createBySuccess( memberS );
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
    @RequestMapping( value = "/cardBatchApplyChecked", method = RequestMethod.GET )
    public ServerResponse cardBatchApplyChecked(HttpServletRequest request,
		    HttpServletResponse response,String memberIds,Integer ischecked){
	try {
	    Integer busId = SessionUtil.getPidBusId( request );
	    memberCardService.cardBatchApplyChecked(busId, memberIds,ischecked );
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    LOG.error( "会员批量审核异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError(e.getCode(),e.getMessage());
	}
    }

    @ApiOperation( value = "会员卡审核", notes = "会员卡审核" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberId", value = "会员卡memberId集合逗号隔开", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "ischecked", value = "审核状态 0不通过 1通过", paramType = "query", required = false, dataType = "int" )

    })
    @ResponseBody
    @RequestMapping( value = "/cardApplyCheckedByOne", method = RequestMethod.GET )
    public ServerResponse cardApplyCheckedByOne(HttpServletRequest request,
		    HttpServletResponse response,Integer memberId,Integer ischecked){
	try {
	    Integer busId = SessionUtil.getPidBusId( request );
	    memberCardService.cardApplyCheckedByOne(busId, memberId,ischecked );
	    return ServerResponse.createBySuccess(  );
	} catch ( BusinessException e ) {
	    LOG.error( "会员卡审核异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(), e.getMessage() );
	}
    }





    @ApiOperation( value = "商家赠送积分、粉币", notes = "商家赠送积分、粉币" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberIds", value = "会员卡memberId集合逗号隔开", paramType = "query", required = false, dataType = "string" ),
		    @ApiImplicitParam( name = "giveType", value = "赠送类型 2粉币 1积分", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "number", value = "数量", paramType = "query", required = false, dataType = "int" )

    })
    @ResponseBody
    @RequestMapping( value = "/addIntegralAndfenbi", method = RequestMethod.GET )
    public ServerResponse addIntegralAndfenbi(HttpServletRequest request,
		    HttpServletResponse response,String json){
	try {
	    Integer busId = SessionUtil.getPidBusId( request );
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
	    Integer busId = SessionUtil.getPidBusId( request );
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

    @ApiOperation( value = "拉黑或恢复会员", notes = "拉黑或恢复会员" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberId", value = "会员id", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "name", value = "用户登录名称", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "pwd", value = "用户登录密码", paramType = "query", required = false, dataType = "int" )

    })
    @ResponseBody
    @RequestMapping( value = "/deleteMember", method = RequestMethod.GET )
    public ServerResponse deleteMember(HttpServletRequest request,
		    HttpServletResponse response,String json){
	//缺接口
	return null;
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
	     Map<String,Object> map= memberCardService.findMemberDetails(memberId);
	    return ServerResponse.createBySuccess( map );
	} catch ( Exception e ) {
	    LOG.error( "查询会员详情：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( "查询会员详情失败");
	}
    }

    @ApiOperation( value = "查询会员详情", notes = "查询会员详情" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "memberId", value = "会员id", paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/upLoadMember", method = RequestMethod.GET )
    public ServerResponse upLoadMember(HttpServletRequest request,
		    HttpServletResponse response){
	LOG.info("调用导入实体卡");
	try {
	    Integer danqianBusId=SessionUtil.getLoginUser( request ).getId();
	    Integer busId = SessionUtil.getPidBusId( request );

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
		    request.getSession().setAttribute("imp" + busId, wbs);
		    return ServerResponse.createDataByError( ResponseMemberEnums.IMP_ERROR.getCode(), ResponseMemberEnums.IMP_ERROR.getMsg(),redisStr);
		}
	    }
	    return ServerResponse.createByError("没有读取到文件");
	} catch (Exception e) {
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
		    HttpServletResponse response, String json){
	Integer busId = SessionUtil.getPidBusId( request );

	List<Map<String, Object>> listMap = memberCardService.findMember(busId,
			json);
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
	    Integer busId = SessionUtil.getPidBusId( request );
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
	    Integer busId = SessionUtil.getPidBusId( request );
	    memberCardService.intergralConsume(busId,intergral,cardNo);
	    return ServerResponse.createBySuccess(  );
	} catch ( Exception e ) {
	    LOG.error( "积分兑换异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( "积分兑换失败");
	}
    }

    @ApiOperation( value = "工作台 会员卡统计", notes = "工作台 会员卡统计" )
    @ApiImplicitParams({
		    @ApiImplicitParam( name = "ctId", value = "会员卡类型 第一次默认传0" , paramType = "query", required = false, dataType = "int" )
    })
    @ResponseBody
    @RequestMapping( value = "/memberTongJi", method = RequestMethod.GET )
    public ServerResponse memberTongJi(HttpServletRequest request,
		    HttpServletResponse response,Integer ctId,String startdate){
	try {
	    Integer busId = SessionUtil.getPidBusId( request );
	    Map<String,Object> map= memberCardService.memberTongJi(busId,ctId,startdate);
	    return ServerResponse.createBySuccess(map  );
	} catch ( BusinessException e ) {
	    LOG.error( "积分兑换异常：", e );
	    e.printStackTrace();
	    return ServerResponse.createByError( e.getCode(),e.getMessage());
	}

    }




}
