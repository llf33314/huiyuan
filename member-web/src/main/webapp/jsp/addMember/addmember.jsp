<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>新增会员</title>
    <!-- 引入样式 -->

    <link rel="stylesheet" href="/js/elementui/elementui.css">
    <link rel="stylesheet" href="/css/member/common.css">
    <link rel="stylesheet" href="/css/member/member.css">
    <link rel="stylesheet" href="/css/iconfont/iconfont.css">

    <style>
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
    <div id="member">
        <input type="hidden" value="${shopId}" id="shopId"/>
        <input type="hidden" value="${busId}" id="busId"/>
        <input type="hidden" value="" id="memberId"/>
        <input type="hidden" value="${memberUser}" id="memberUser"/>


       <%-- <el-breadcrumb separator="/" class="member-brand">
            <el-breadcrumb-item :to="{ path: '/' }">工作台</el-breadcrumb-item>
            <el-breadcrumb-item>新增会员</el-breadcrumb-item>
        </el-breadcrumb>--%>
        <div class="member-search">
            <label>会员查询</label>
            <el-input placeholder="会员卡号/手机号" icon="search" size="small" v-model="search" :on-icon-click="handleIconClick">

            </el-input>
            <span class="findMemberMsg colorfe5"></span>
        </div>

        <div class="add-member-box pl60 clearfix">
            <label class="fl el-form-item__label" style="font-weight: bold">新增会员</label>
            <div class="add-main">
                <el-form :model="ruleForm" :rules="rules" :label-position="labelPosition" ref="ruleForm" label-width="130px" class="demo-ruleForm">
                    <c:if test="${gongzhong==1}">
                        <el-form-item label="关注公众号办理：" prop="follow">
                            <el-switch on-text="" off-text="" v-model="ruleForm.follow" @change="fnGuanZhu"></el-switch>
                        </el-form-item>
                    </c:if>
                    <el-form-item label="会员卡类型：" prop="cardType">
                        <el-select v-model="cardType"  placeholder="" size="small" @change="fnGradeType">
                               <el-option :label="option.ctName" :value="option.ctId" v-for="option in options"></el-option>
                          </el-select>
                    </el-form-item>
                    <el-form-item label="会员卡等级：" prop="cardRank">
                        <el-select v-model="cardRank"   placeholder="请选择会员卡等级" size="small" @change="fnGradeTypeBuy">
                                <el-option :label="option.gt_name"   :value="option"    v-for="option in gradeTypesOption"></el-option>
                        </el-select>
                        <span class="grey-warning"></span>
                    </el-form-item>

                    <el-form-item label="会员卡等级：" prop="cardPrice" v-if="cardPriceVisible">
                        <div class="colorfe5 font18">￥<span class="buyMoneyHtml">${gradeTypes[0].buyMoney}</span></div>
                    </el-form-item>

                    <el-form-item label="手机号码：" prop="phone">
                        <el-input v-model="ruleForm.phone" size="small" class="widthAuto"></el-input>
                        <el-button type="primary" size="small" @click="getCode(ruleForm)" :disabled="!show">
                            <span v-show="show">获取验证码</span>
                            <span v-show="!show">{{count}}s后重发</span>
                        </el-button>
                    </el-form-item>
                    <el-form-item label="短信验证码：" prop="verification">
                        <el-input v-model="ruleForm.verification" size="small"></el-input>
                    </el-form-item>
                    <el-form-item class="pt50">
                        <el-button type="primary" @click="submitForm('ruleForm')">保存</el-button>
                       <%-- <el-button @click="resetForm('ruleForm')">取消</el-button>--%>
                    </el-form-item>
                </el-form>
            </div>

            <!-- 右侧出现二维码 -->
            <transition name="el-fade-in-linear">
                <div class="qrCode right-box guanzhuShow" v-show="codeShow"  >
                    <h2> 扫描二维码关注办理新增会员</h2>
                    <div class="code-detail textcenter">
                        <div class="code-pic">
                            <img :src="guanZhuqrcode" alt="" title="二维码" width="240" height="240">
                            <div class="martop20">
                                <el-button :plain="true" type="info" class="border-btn" @click="downQcode">下载该二维码</el-button>
                            </div>
                        </div>
                        <div class="textleft color999 code-txt">
                            <p>提示：</p>
                            <p>1.请确认用户扫码关注成功后，才可出现粉丝列表进行下一步操作；</p>
                            <p>2.已关注过公众号的粉丝，扫码成功后将不在新增粉丝列表中呈现，粉丝可在公众号的会员中心，进行会员注册的相关操作。</p>
                        </div>
                    </div>
                </div>
            </transition>

            <!-- 右侧粉丝列表 -->
            <transition name="el-fade-in-linear" >
                <div class="right-box member-list memberShow" style="display:none">
                    <h2>今日新增粉丝信息，点击对应头像可与会员手机号绑定</h2>
                    <ul class="scrollBar">

                    </ul>
                </div>
            </transition>
        </div>

        <!-- 付款二维码 -->
        <el-dialog  title="付款" :visible.sync="dialogVisible" size="small" class="payCode" :modal="false" v-cloak top="5%">
            <div class="paycode-pic"><img :src="qrcode" alt="付款二维码"></div>
            <div class="textcenter martop20">请使用微信或者支付宝扫描该二维码付款</div>
        </el-dialog>

        <!-- 会员新增成功提示 -->
        <el-dialog  title="提示" :visible.sync="dialogVisible2" size="small" class="success" :modal="false" v-cloak top="5%">
            <div class="textcenter font34"><i class="el-icon-circle-check"></i></div>
            <div class="textcenter success-txt">会员新增成功</div>
            <div class="textcenter color999"></div>
        </el-dialog>


        <el-dialog  title="提示" :visible.sync="dialogVisible3" size="tiny" class="success" :modal="false" v-cloak top="5%">
            <div class="textcenter font34"><i class="el-icon-circle-close"></i></div>
            <div class="textcenter error-txt success-txt"></div>
        </el-dialog>

        <!-- 会员信息弹出层 -->
        <el-dialog title="会员信息" :visible.sync="dialogTableVisible4" size="tiny"  :modal="false" v-cloak top="5%" >
            <div class="memberHtml"></div>
        </el-dialog>

    </div>

    <script src="/js/vue.min.js"></script>
    <!-- 引入组件库 -->
    <script src="/js/elementui/elementui.js"></script>
    <script src="/js/jquery-2.2.2.js"></script>
    <script type="text/javascript" src="/js/socket.io/socket.io.js"></script>

    <script>
        const TIME_COUNT = 120;

        var vm = new Vue({
            el: '#member',
            data(){
                var validateCardPhone = (rule, value, callback) => {
                    if (value === '') {
                        callback(new Error('不能为空！'));
                    }
                    //var phone = value.replace(/(^\s*)|(\s*$)/g, "");//去空格
                    var phone = value;//去空格
                    var regPhone = /^0?(13[0-9]|15[012356789]|17[0678]|18[0123456789]|14[57])[0-9]{8}$/;
                     if (!regPhone.test(phone)) {
                        callback(new Error('手机号码不正确！'));
                    }else{
                         callback();
                    }
                    callback()
                };
                return{
                    search: '',
                    codeShow: false,
                    qrcode:'',
                    guanZhuqrcode:'',
                    validCode: true ,
                    labelPosition: 'right',
                    show: true,
                    count: '',
                    timer: null,
                    dialogVisible: false,
                    dialogVisible2: false,
                    dialogVisible3:false,
                    dialogTableVisible4:false,
                    cardPriceVisible:false,
                    options:'',
                    cardType:'',
                    gradeTypesOption:'',
                    cardRank:'',
                    carID:'',
                    ruleForm: {
                        follow:false ,
                        phone:'',
                        verification:''
                    },
                    rules:{
                        verification: [
                            { required: true, message: '请输入短信验证码', trigger: 'blur' }

                        ],
                        phone:[
                            {validator: validateCardPhone, trigger: 'blur'}
                        ]
                    }
                }
            },
            mounted(){

                setTimeout(function () {
                    vm.dialogVisible2 = false
                },3000);

                var gradeTypes=${gradeTypes};
                var mapList=${mapList};
                if(mapList!=""){
                    this.options=mapList;
                    this.cardType=mapList[0].ctId;
                }

                if(gradeTypes!=""){
                    if(gradeTypes[0].applyType==3) {
                        this.cardPriceVisible =true;
                    }
                    this.gradeTypesOption=gradeTypes;
                    this.cardRank=gradeTypes[0].gt_name;
                    this.carID=gradeTypes[0].gt_id;
                }



            },
            methods: {
                handleIconClick: function (ev) {
                    var cardno=vm.search;
                    if(cardno==null || cardno==""){
                        return;
                    }
                    $.ajax({
                        url:"/addMember/findMemberByCardNo.do",
                        data:{"cardNo":cardno,"busId":$("#busId").val()},
                        type:"POST",
                        dataType:"JSON",
                        success:function (data) {
                            $(".findMemberMsg").html("");
                            if(data.result==true){
                                vm.dialogTableVisible4=true;
                                var html="<div class='flex dialog-item'>";
                                if(data.headimgUrl==null || data.headimgUrl==""){
                                    html+="<div class='item-left'><img src='/images/addmember/headImage.png' alt='' class='item-pic'></div>";
                                }else{
                                    html+="<div class='item-left'><img src='"+data.headimgUrl+"' alt='' class='item-pic'></div>";
                                }
                                html+=" <div class='flex-1 item-right lineH'>"+data.nickName+"</div>";
                                html+="   </div>";
                                html+=" <div class='flex dialog-item'>";
                                html+="  <div class='item-left'>会员卡号：</div>";
                                html+="<div class='flex-1 item-right cardno'>"+data.cardNo+"</div>";
                                html+="     </div>";
                                html+="    <div class='flex dialog-item'>";
                                html+="    <div class='item-left'>性别：</div>";

                                html+="<div class='flex-1 item-right sex'>";
                                if(data.sex==0){
                                    html+="未知";
                                }else if(data.sex==1){
                                    html+="男";
                                }else{
                                    html+="女";
                                }
                                html+="</div>";
                                html+="     </div>";
                                html+="    <div class='flex dialog-item'>";
                                html+="    <div class='item-left'>领卡时间：</div>";
                                html+="<div class='flex-1 item-right '>"+data.receiveDate+"</div>";
                                html+="    </div>";


                                html+="   <div class='flex dialog-item'>";
                                html+="   <div class='item-left'>会员卡名称：</div>";
                                html+=" <div class='flex-1 item-right'>"+data.ctName+"</div>";
                                html+="    </div>";

                                html+="   <div class='flex dialog-item'>";
                                html+="   <div class='item-left'>会员卡等级：</div>";
                                html+=" <div class='flex-1 item-right'>"+data.gradeName+"</div>";
                                html+="    </div>";

                                var ctId=data.ctId;
                                if(ctId==2){
                                    html+="   <div class='flex dialog-item'>";
                                    html+="   <div class='item-left'>会员卡折扣：</div>";
                                    html+=" <div class='flex-1 item-right'>"+data.discount+"</div>";
                                    html+="    </div>";
                                }else if(ctId==3){
                                    html+="   <div class='flex dialog-item'>";
                                    html+="   <div class='item-left'>会员卡余额：</div>";
                                    html+=" <div class='flex-1 item-right'>"+data.money+"</div>";
                                    html+="    </div>";
                                }

                                html+="   <div class='flex dialog-item'>";
                                html+="   <div class='item-left'>积分：</div>";
                                html+=" <div class='flex-1 item-right'>"+data.integral+"</div>";
                                html+="    </div>";
                                html+="   <div class='flex dialog-item'>";
                                html+="   <div class='item-left'>粉币：</div>";
                                html+=" <div class='flex-1 item-right'>"+data.fans_currency+"</div>";
                                html+="    </div>";

                                vm.$nextTick(function () {
                                    $(".memberHtml").html(html);
                                })

                            }else{
                                $(".findMemberMsg").html(data.message);
                            }
                        }
                    })
                    console.log(ev);
                } ,

                fnGuanZhu: function (e) {
                    if(e == true){
                        vm.codeShow = true;
                        $(".memberShow").hide();
                        $.ajax({
                            url:"/addMember/guanzhuiQcode.do",
                            type:"POST",
                            dataType:"JSON",
                            success:function(data){
                                this.codeShow = true;
                                vm.$nextTick(function () {
                                    vm.guanZhuqrcode=data.imgUrl;
                                })
                            }
                        })
                    }
                    if(e == false){
                        vm.codeShow = false;
                        $(".memberShow").hide();
                        $("#memberId").val("");
                    }
                },

                validphone: function (str){//手机验证
                    var phone = str.toString().replace(/(^\s*)|(\s*$)/g, "");//去空格
                    var regPhone = /^0?(13[0-9]|15[012356789]|17[0678]|18[0123456789]|14[57])[0-9]{8}$/;
                    if (phone == '') {
                        return false;
                    } else if (!regPhone.test(phone)) {
                        return false;
                    }else{
                        return true;
                    }
                  },

                getCode: function (ruleForm) {
                        var phone = vm.ruleForm.phone;
                        if(phone==null || phone==""){
                            return null;
                        }
                        var validphone= vm.validphone(phone);
                        if(validphone==true) {
                            $.get("/addMember/sendMsgerp.do", {
                                telNo: phone,
                                busId:$("#busId").val(),
                            }, function (data) {
                                if (!data.result) {
                                    vm.dialogVisible3 = true;
                                    vm.$nextTick(function () {
                                        $(".error-txt").html(data.msg);
                                    })
                                    vm.timer = null;
                                    vm.show = true;
                                } else {

                                }
                            }, "json");

                            if(!this.timer){
                                this.count = TIME_COUNT;
                                this.show = false;
                                this.timer = setInterval(()=>{
                                        if(this.count > 0 && this.count<=TIME_COUNT){
                                    this.count--
                                }else{
                                    this.show = true;
                                    clearInterval(this.timer);
                                    this.timer = null
                                }
                            },1000)
                            }

                        }
                },

                fnGradeTypeBuy:function(){
                    var applyType=vm.cardRank.applyType;
                    if(applyType==3){
                        vm.cardPriceVisible=true;
                        vm.$nextTick(function () {
                           $(".buyMoneyHtml").html(vm.cardRank.buyMoney);
                        })
                    }else{
                        vm.cardPriceVisible=false;
                    }
                    var ctId=vm.cardType;
                    if(ctId==2){
                        $(".grey-warning").html("该卡享受"+vm.cardRank.gr_discount/10+"折");
                    }else{
                        $(".grey-warning").html("");
                    }

                },

                submitForm: function (formName) {
                    this.$refs[formName].validate((valid) => {
                        console.log(valid);
                        if (valid) {
                            var phone=vm.ruleForm.phone;
                            var vcode=vm.ruleForm.verification;
                            if(vcode==null || vcode==""){
                                return;
                            }
                            var ctId=vm.cardType;
                            if(ctId==null || ctId==""){
                                return;
                            }

                            var gtId=vm.cardRank.gt_id;
                            if(gtId==null || gtId==""){
                                gtId=vm.carID;
                                if(gtId==null || gtId==""){
                                    return;
                                }
                            }

                            var params={};
                            params["shopId"]=$("#shopId").val();
                            params["phone"]=phone;
                            params["ctId"]=ctId;
                            params["gtId"]=gtId;
                            params["vcode"]=vcode;
                            params["busId"]=$("#busId").val();
                            params["memberId"]=$("#memberId").val();
                            params["memberUser"]=$("#memberUser").val();
                            $.ajax({
                                url:"/addMember/liquMemberCard.do",
                                data:params,
                                dataType:"JSON",
                                type:"POST",
                                success:function(data){
                                    if(data.code==-1){
                                        vm.dialogVisible3 = true;
                                        vm.$nextTick(function () {
                                            $(".error-txt").html(data.message);
                                        })
                                    }else if(data.code==1){
                                        vm.dialogVisible2=true;
                                        $("#memberId").val("");
                                        $(".cur-list").remove();
                                        setTimeout();
                                    }else if(data.code==2){
                                        userId1="addMember_"+data.orderCode;
                                        //跳转支付页面
                                        vm.dialogVisible=true;
                                        vm.$nextTick(function () {
                                            vm.qrcode=data.url;
                                        })
                                    }
                                }
                            })
                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });

                },

                downQcode:function () {
                  //下载二维码
                    window.location.href="/addMember/downQcode.do?url="+vm.guanZhuqrcode;
                },
                fnGradeType:function(){
                    var ctId = vm.cardType;
                    if(ctId==null || ctId==""){
                        return;
                    }
                    $.ajax({
                        url:"/addMember/findCardType.do",
                        data:{'cardType':ctId},
                        dataType:"JSON",
                        type:"POST",
                        success:function(data){
                            if(data.code==0){
                                if(ctId==2){
                                    $(".grey-warning").html("该卡享受"+data.gradeTypes[0].gr_discount/10+"折");
                                }else{
                                    $(".grey-warning").html("");
                                }
                                vm.$set(vm,"gradeTypesOption",data.gradeTypes);
                                vm.cardRank=data.gradeTypes[0];
                            }else{
                                $(".error-txt").html(data.message);
                                this.dialogVisible3=true;
                            }
                        }
                    });
                }
            }
        })


        //推送
        var userId = '${memberUser}';
        var socket =  io.connect('${host}');

        socket.on('connect', function() {
            var jsonObject = {userId: userId,
                message: "0"};
            socket.emit('auth', jsonObject);
        });

        socket.on('chatevent', function(data) {
            sendMessage(data.message,data.style);
        });

        socket.on('disconnect', function() {

        });

        function sendMessage(data,style) {
            if(style==2){
                data=eval('(' + data + ')');
                var newDate = new Date();
                var html="";
                html+="<li class='list-item' onclick='selli(this,"+data.id+","+data.phone+")'>";
                html+="    <img src='"+data.headimgurl+"' alt='粉丝头像' title='粉丝头像'>";
                html+="    <div class='list-item-name'>";
                html+="   <p>"+data.nickname+"</p>";
                html+="    <p class='color999 martop20'>时间： "+newDate.toLocaleTimeString()+"</p>";
                html+="</div>";
                html+=" <label  class='list-item-status'>";
                html+="   <i class='list-item-check iconfont disnone icon-wancheng'></i>";
                html+="</label>";
                html+=" </li>";
                $(".scrollBar").prepend(html);
                $(".guanzhuShow").hide();
                $(".memberShow").show();

            }else{
                vm.dialogVisible=false;
                vm.dialogVisible2=true;
                $("#memberId").val("");
                $(".cur-list").remove();
                setTimeout();
            }
        }

        function selli(obj,memberId,phone) {
            if(!$(obj).hasClass("cur-list")){
                $(obj).addClass("cur-list").siblings().removeClass('cur-list');
                $("#memberId").val(memberId);
                vm.ruleForm.phone=phone;
            }else{
                $(obj).removeClass('cur-list');
                $("#memberId").val("");
                vm.ruleForm.phone="";
            }

        }


    </script>
</body>

</html>