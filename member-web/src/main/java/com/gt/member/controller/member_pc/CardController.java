package com.gt.member.controller.member_pc;

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
public class CardController {

    private static final Logger LOG = LoggerFactory.getLogger( CardController.class );



    /**
     * 查询积分基础设置
     */
    public void findJifenGradeType(HttpServletRequest request, HttpServletResponse response,Integer ctId){
	Integer busId=SessionUtil.getPidBusId( request );
	if(CommonUtil.isNotEmpty( ctId )){
	    //修改

	}else{
	    //新增
	}
    }
}
