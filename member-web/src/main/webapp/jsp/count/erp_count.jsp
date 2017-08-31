<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>订单结算</title>
    <link rel="stylesheet" href="/js/elementui/elementui.css">
    <link rel="stylesheet" href="/css/count/erp_billing.css">
    <link rel="stylesheet" href="/css/iconfont/iconfont.css">
    <script type="text/javascript" src="/js/socket.io/socket.io.js"></script>
</head>
<body>
<section id="erpBilling">
    <div class="erp-billing-title">
        <span class="erp-billing-color">未支付订单</span><span style="padding: 0 3px;">/</span><span>${mallAllEntityQuery.orderCode}/</span><span style="padding-left: 3px;">订单结算</span>
    </div>
    <div class="erp-billing-content">
        <input type="hidden" value="${mallAllEntityQuery.shopId}" id="shopId"/>
        <input type="hidden" value="${mallAllEntityQuery.busId}" id="busId"/>
        <input type="hidden" value="${mallAllEntityQuery.totalMoney}" id="totalMoney">
        <input type="hidden" value="${mallAllEntityQuery.jumpUrl}" id="jumpUrl"/>
        <input type="hidden" value="${mallAllEntityQuery.jumphttpPOST}" id="jumphttpPOST"/>
        <input type="hidden" value="${mallAllEntityQuery.derateMoney}" id="derateMoney"/>
        <input type="hidden" value="" id="memberId">
        <input type="hidden" value="" id="ctId">
        <input type="hidden" value="1" id="visitor">



        <!-- 查询会员信息 -->
        <div class="input-card error-state"> <!-- 错误状态添加 error-state ,否则去掉 -->
            <section class="title">会员卡号/联盟卡号/手机号：</section>
            <section class="input-box">
                <input type="text" class="input" id="cardNo" onkeydown="if(event.keyCode==13){fnMember()};"/>
            </section>
            <section class="btn-box">
                <button class="erp-billing-bg-color sure" onclick="fnMember()">确定</button>
            </section>
            <span class="tips erp-billing-red" style="padding-left: 20px;" id="errorMember"></span>
        </div>
        <div class="memberHtml"></div>
        <div class="youhuiHtml"></div>
        <!-- 会员信息 -->
        <%-- <div class="vip-msg">
             <section class="head" style="background-image: url(../imgs/floor2-3.jpg)"></section>
             <section style="overflow: hidden;">
                 <section class="name text">昵称：46464</section>
                 <section class="card-number text">联盟卡号：46464</section>
                 <section class="phone text">手机：46464</section>
                 <section class="integral text">联盟积分：46464</section>
                 <section class="type text">类型：次卡</section>
                 <section class="grade text">等级：VIP1</section>
                 <section class="balance text">余额：46464</section>
             </section>
         </div>--%>
        <!-- 选择抵扣优惠 -->
        <%--    <div class="select-discount-type erp-billing-border-margin">
                <div class="erp-billing-item-title">选择抵扣优惠</div>
                <div class="discount-box-nomsg">
                    <div class="icon"><i class="el-icon-warning"></i></div>
                    <p>您当前还未有会员优惠信息，请先领取优惠券、粉币，或直接消费以获得会员积分。</p>
                </div>
                <div class="discount-box">
                    <div class="coupon-box">
                        <ul>
                            <li class="coupon-item">
                                <i class="arrow erp-billing-border" style="left: -8px;"></i>
                                <i class="arrow erp-billing-border" style="right: -8px;"></i>
                                <section class="content erp-billing-bg-color">
                                    <p class="title">9折</p>
                                    <p class="dps">多粉折扣券<span style="margin-left: 20px;">x1</span></p>
                                </section>
                                <section class="erp-billing-color">已选择 ×5</section>
                            </li>
                            <li class="coupon-item">
                                <i class="arrow erp-billing-border" style="left: -8px;"></i>
                                <i class="arrow erp-billing-border" style="right: -8px;"></i>
                                <section class="content erp-billing-border erp-billing-color">
                                    <p class="title">9折</p>
                                    <p class="dps">多粉折扣券<span style="margin-left: 20px;">x1</span></p>
                                </section>
                            </li>
                        </ul>
                    </div>
                    <ul class="jf-and-fb">
                        <li class="erp-billing-color">
                            <div class="box erp-billing-bg-color">
                                <p class="title">31粉币</p>
                                <p>（2111元）</p>
                            </div>
                            <p>已抵扣2111元</p>
                        </li>
                        <li class=" erp-billing-color">
                            <div class="box  erp-billing-border">
                                <p class="title">31粉币</p>
                                <p>（2111元）</p>
                            </div>
                            <p>已抵扣21111元</p>
                        </li>
                    </ul>
                </div>
            </div>--%>
        <!-- 请选择联盟 -->
        <%-- <div class="select-union erp-billing-border-margin">
             <div class="erp-billing-item-title">请选择联盟</div>
             <div class="union-box">
                 <ul class="union-list">
                     <li class="union-title  erp-billing-border erp-billing-bg-color" title="联盟1">联盟1</li>
                     <li class="union-title erp-billing-border" title="联盟1">联盟2</li>
                 </ul>
                 <div class="union-msg-box">
                     <ul class="union-msg">
                         <li class="union-msg-item"><span class="union-msg-text">等级：</span>黑卡</li>
                         <li class="union-msg-item"><span class="union-msg-text">有效期：</span>30天</li>
                         <li class="union-msg-item"><span class="union-msg-text">该联盟的折扣为：</span>7折</li>
                         <li class="union-msg-item"><span class="union-msg-text">联盟折扣优惠：</span>￥-900</li>
                         <li class="union-msg-item"><span class="union-msg-text">联盟积分抵扣：</span>
                             <div class="gt-form-switch" style="display: inline-block;">
                                 <input class="switch small-switch" type="checkbox" value="未启用" />
                             </div>
                         </li>
                         <li class="union-msg-item"><span class="union-msg-text">联盟积分优惠：</span>
                             <span>￥-3.00（消耗联盟积分：110，最大抵扣比例：30%）</span>
                         </li>
                     </ul>
                 </div>
             </div>
         </div>--%>
        <!-- 结算 -->
        <div class="billing-msg erp-billing-border-margin">
            <div class="erp-billing-item-title">结算</div>
            <ul class="billing-msg-box">
                <li class="item">
                    <span class="title">消费金额：</span>
                    <span>￥${mallAllEntityQuery.totalMoney}</span>
                </li>
                <li class="item">
                    <span class="title">减免金额：</span>
                    <span>￥${mallAllEntityQuery.derateMoney}</span>
                </li>

                <li class="item youhuiItem">

                </li>
                <li class="item">
                    <span class="title">应收金额：</span>
                    <span class="erp-billing-red" style="font-size: 23px;" id="balanceMoney">￥<fmt:formatNumber value="${mallAllEntityQuery.totalMoney-mallAllEntityQuery.derateMoney}" pattern="0.00"/></span>
                    <%-- <botton @click="changePrice()" class="erp-billing-bg-color btn">改价处理</botton>--%>
                </li>
            </ul>
        </div>
        <!-- 选择支付方式 -->
        <div class="pay-type erp-billing-border-margin">
            <div class="erp-billing-item-title">选择支付方式</div>
            <div class="pay-box">
                <ul class="pay-icon">
                    <div class="chuzhiPayHtml">

                    </div>

                    <li @click="xinjinPayMent($event)" class="item erp-billing-bg-color erp-billing-border xianjinItem" payType="10">
                        <p class="icon-p"><i class="iconfont">&#xe62b;</i></p>
                        <p>现金支付</p>
                    </li>


                    <li @click="scanCodePayMent($event)" class="item erp-billing-border erp-billing-color">
                        <p class="icon-p"><i class="iconfont">&#xe630;</i></p>
                        <p>扫码支付</p>
                    </li>
                    <%-- <li class="item erp-billing-border erp-billing-color">
                         <p class="icon-p"><i class="iconfont">&#xe61d;</i></p>
                         <p>POS机支付</p>
                     </li>
                     <li @click="moBoxPay()" class="item erp-billing-border erp-billing-color">
                         <p class="icon-p"><i class="iconfont">&#xe682;</i></p>
                         <p>魔盒支付</p>
                     </li>
                     <li @click="billingOrder()" class="item erp-billing-border erp-billing-color">
                         <p class="icon-p"><i class="iconfont">&#xe737;</i></p>
                         <p>挂账</p>
                     </li>--%>
                </ul>
                <ul class="pay-tips">
                    <!--现金支付-->
                    <li class="cash-tips">
                        <%--<div>
                            <button class="btn erp-billing-bg-color" style="margin-right: 25px;">抹零</button>
                            <p style="display: inline">
                                <span>已抹零：</span>
                                <span class="erp-billing-red" style="font-size: 23px;vertical-align: bottom;">￥-900</span>
                            </p>
                        </div>--%>
                        <div class="item-title">
                            <span>输入收取金额：</span>
                            <input class="input" type="number" id="payMoney" min="0" onblur="fnchangePayMoney()" onkeyup="fnchangePayMoney()"/>
                            <p style="display: inline">
                                <span style="margin-left: 25px;">找零：</span>
                                <span class="erp-billing-red balaceHtml" style="font-size: 23px;vertical-align: baseline;">￥0</span>
                            </p>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="submit-content">
            <button @click="submitOrderMsg()" class="btn erp-billing-bg-color">确认</button>
            <button class="btn cancle">取消</button>
        </div>
        <%--<button @click="dialogDiscountTypeVisible = true">选择优惠方式</button>--%>
    </div>
    <el-dialog title="改价处理" :visible.sync="dialogChangePriceVisible" size="640px" :close-on-click-modal="false">
        <div class="smzf-dialogs-box">
            <el-form label-width="90px">
                <el-form-item label="原价格："> 266.00</el-form-item>
                <el-form-item label="改价金额：">
                    <el-input style="width: 200px;"></el-input>
                </el-form-item>
                <el-form-item label="改价备注：">
                    <el-input style="width: 200px;"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer" style="float: right;">
            <el-button type="primary" @click="changePriceSure()">确 定</el-button>
        </span>
        </div>
    </el-dialog>
    <el-dialog title="改价超额" :visible.sync="dialogChangePriceOverVisible" size="560px" :close-on-click-modal="false">
        <div class="smzf-dialogs-box" style="text-align: center;padding-bottom: 0">
            <img style="width: 180px;height: 180px;" src="../imgs/floor2-3.jpg"/>
            <p style="margin-top: 30px;">请授权人微信扫描授权</p>
            <button @click="changePriceSuccess()">授权改价成功提示框</button>
        </div>
    </el-dialog>
    <!-- 授权改价成功  -->
    <el-dialog :visible.sync="dialogChangePriceSuccessVisible" size="400px" top="30%" :close-on-click-modal="false" :show-close="false">
        <div class="smzf-dialogs-box" style="text-align: center;padding-bottom: 0">
            <div style="position: relative;padding-top: 20px;">
                <i class="el-icon-circle-check" style="font-size: 36px;color: #34d063;position: absolute; left: 0px;"></i>
                <p style="text-align: left; padding-left: 50px;">授权成功 ！</p>
                <p style="text-align: left; padding-left: 50px;color: #666">请继续完成结算</p>
            </div>
            <span slot="footer" class="dialog-footer" style="float: right;padding-top: 30px;">
            <el-button type="primary" @click="changePriceSuccessSure()">确 定</el-button>
        </span>
        </div>
    </el-dialog>

    <el-dialog :modal="false" title="扫描支付" :visible.sync="dialogScanCodePayMentVisible" size="590px" :close-on-click-modal="false">
         <div class="smzf-dialogs-box">
            <div class="left">
                <p class="title">方式1：顾客扫码支付</p>
                <img src="" id="saomaImage"/>
            </div>
            <div class="right">
                <p class="title">方式2：营业员输入</p>
                <p><input type="text" class="input" id="auth_code" placeholder="请使用扫码枪扫描或键盘输入付款码"/></p>
                <p style="text-align: right">
                    <%--<button class="btn erp-billing-bg-color" onclick="saomaQianBaoPay()">确认</button>--%>
                    <el-button type="primary" @click="saomaQianBaoPay()" style="margin-top: 50px;" >确 定</el-button>
                </p>
            </div>
        </div>
    </el-dialog>
    <!--魔盒支付弹框-->
    <el-dialog :modal="false" title="选择支付方式" :visible.sync="dialogmoBoxPayVisible" size="590px" top="30%" :close-on-click-modal="false">
        <el-radio-group v-model="moBoxPayType">
            <el-radio :label="1" style="margin-right: 60px;">微信</el-radio>
            <el-radio :label="2">支付宝</el-radio>
        </el-radio-group>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="moBoxPaySure()">确 定</el-button>
            <el-button @click="dialogmoBoxPayVisible = false">取 消</el-button>
        </div>
    </el-dialog>
    <!--选择优惠方式弹框-->
    <el-dialog title="选择优惠方式" :visible.sync="dialogDiscountTypeVisible" size="409px" top="30%" :close-on-click-modal="false">
        <el-radio-group v-model="discountType">
            <el-radio :label="1" style="margin-right: 60px;">会员卡</el-radio>
            <el-radio :label="2">联盟卡</el-radio>
        </el-radio-group>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="">确 定</el-button>
            <el-button @click="dialogDiscountTypeVisible = false">取 消</el-button>
        </div>
    </el-dialog>
    <el-dialog :modal="false" title="请选择挂账方式" :visible.sync="dialogBillingOrderVisible" size="590px" :close-on-click-modal="false">
        <div class="smzf-dialogs-box">
            <div class="left" style="height: 275px;">
                <p class="title">方式1：现场扫码授权</p>
                <img src="../imgs/floor2-3.jpg"/>
                <p style="margin-top: 20px;">请使用授权人微信扫码授权</p>
            </div>
            <div class="right">
                <p class="title">方式2：非现场授权</p>
                <p>
                    <el-select v-model="nameAndPhone" placeholder="请选择活动区域">
                        <el-option label="小强（1348740787）" value="xiaoqiang"></el-option>
                        <el-option label="小强22（1348740787）" value="beijing"></el-option>
                    </el-select>
                </p>
                <p style="margin-top: 39px;margin-right: 10px;">提示：授权通过，十分钟后有效</p>
                <p style="text-align: right">
                    <button class="btn erp-billing-bg-color">确认发送</button>
                </p>
            </div>
        </div>
    </el-dialog>

    <!--支付成功提示-->
    <el-dialog :modal="false" title="提示" :visible.sync="submitOrderMsgSuccess" size="530px" top="20%" :close-on-click-modal="false">
        <div class="smzf-dialogs-box" style="text-align: center;">
            <div style="position: relative;padding-top: 20px;">
                <i class="el-icon-circle-check" style="font-size: 36px;color: #34d063;position: absolute; left: 0px;"></i>
                <p style="text-align: left; padding-left: 50px;padding-top: 10px;">支付成功</p>
            </div>
        </div>
        <div slot="footer" class="dialog-footer" style="padding-bottom: 40px;padding-top: 60px;">
            <el-button type="primary" @click="returnFirstView()">返回首页</el-button>
            <el-button @click="zhifuQuxiao()">取 消</el-button>
        </div>
    </el-dialog>


    <!--错误提示-->
    <el-dialog :modal="false" title="提示" :visible.sync="errorsubmitOrderMsg" size="530px" top="20%" :close-on-click-modal="false">
        <div class="smzf-dialogs-box" style="text-align: center;">
            <div style="position: relative;padding-top: 20px;">
                <i class="el-icon-circle-close" style="font-size: 36px;position: absolute; left: 0px;"></i>
                <p style="text-align: left; padding-left: 50px;" class="errorHtml"></p>
            </div>
        </div>
        <div slot="footer" class="dialog-footer" style="padding-bottom: 40px;padding-top: 60px;">
            <el-button type="primary" @click="errorsubmitOrderMsg = false">确 定</el-button>
        </div>
    </el-dialog>


</section>
<script src="/js/vue.min.js"></script>
<script src="/js/elementui/elementui.js"></script>
<script src="/js/jquery-2.2.2.js"></script>
<script>
    var vue = new Vue({
        el: '#erpBilling',
        data: {
            dialogChangePriceVisible: false,
            dialogChangePriceOverVisible: false,
            dialogScanCodePayMentVisible: false,
            dialogChangePriceSuccessVisible: false,
            dialogBillingOrderVisible: false,
            nameAndPhone: 'xiaoqiang',
            dialogmoBoxPayVisible: false,
            moBoxPayType: 1,
            submitOrderMsgSuccess: false,
            errorsubmitOrderMsg: false,
            dialogDiscountTypeVisible: false,
            discountType: 1
        },
        methods: {
            xinjinPayMent: function (e) {
                console.log('现金支付')
                var _obj = $(e.target).parents('.item') || $(e.target);
                if ($(e.target).hasClass('item')) _obj = $(e.target);
                $(".item").removeClass("erp-billing-bg-color erp-billing-color");
                _obj.addClass("erp-billing-bg-color");
            },
            changePrice: function () {
                console.log('改价处理')
                this.dialogChangePriceVisible = true
            },
            changePriceSure: function () {
                console.log('确认改价')
                // this.dialogChangePriceVisible =false
                this.dialogChangePriceOverVisible = true
            },
            changePriceSuccess: function () {
                console.log('授权改价成功')
                this.dialogChangePriceSuccessVisible = true
            },
            changePriceSuccessSure: function () {
                console.log('授权改价成功确认')
                this.dialogChangePriceSuccessVisible = false
            },
            scanCodePayMent: function (e) {
                console.log('扫码支付')
                var _obj = $(e.target).parents('.item');
                if ($(e.target).hasClass('item')) _obj = $(e.target);
                $(".item").removeClass("erp-billing-bg-color erp-billing-color");
                _obj.addClass("erp-billing-bg-color");
                saomaPayMent(this);
            },
            moBoxPay: function () {
                console.log('魔盒支付')
                this.dialogmoBoxPayVisible = true
            },
            moBoxPaySure: function () {
                console.log('魔盒支付确认')
            },
            billingOrder: function () {
                console.log('挂账')
                this.dialogBillingOrderVisible = true
            },
            submitOrderMsg: function () {

                var payType = $(".pay-icon").find(".erp-billing-bg-color").attr("paytype");
                if (payType == 5 || payType == 10) {
                    if (payType == 10) {
                        var payMoney = $("#payMoney").val();
                    }
                    chuzhiPay(payType, this);

                }
                console.log('支付成功提示')
                //

            },
            returnFirstView: function () {
                window.location.href = $("#jumpUrl").val();
            },
            zhifuQuxiao: function () {
                this.submitOrderMsgSuccess = false;
                fnMember();
            },
            saomaQianBaoPay:function(){
                saomaQianBaoPay(this);
            }
        }
    })

    function fnchangePayMoney() {
        var payMoney = $("#payMoney").val();
        var totalMoney = $("#totalMoney").val();
        var derateMoney = $("#derateMoney").val();
        var balaceHtml = parseFloat(totalMoney)-parseFloat(derateMoney) - parseFloat(payMoney);
        if (balaceHtml < 0) {
            $(".balaceHtml").html("￥" + balaceHtml.toFixed(2));
        } else {
            $(".balaceHtml").html("￥0");
        }

    }

    function chuzhiPapMent(obj) {
        console.log('储值卡支付')
        $(".item").removeClass("erp-billing-bg-color erp-billing-color");
        $(obj).addClass("erp-billing-bg-color");
    }

    function fnMember() {
        var cardNo = $("#cardNo").val();
        if (cardNo == null || cardNo == "") {
            return;
        }
        $.ajax({
            url: "/erpCount/findMemberERP.do",
            data: {"busId": $("#busId").val(), "shopId": $("#shopId").val(), "cardNo": cardNo},
            type: "GET",
            dataType: "json",
            success: function (data) {
                if (data.result == false) {
                    $("#errorMember").html(data.msg);
                    $("#visitor").val(1);
                } else {
                    $(".memberHtml").html("");
                    $(".youhuiHtml").html("");
                    $(".chuzhiPayHtml").html("");
                    $("#visitor").val(0);

                    $("#memberId").val(data.memberId);
                    $("#ctId").val(data.ctId);
                    if (data.ctId == 2) {
                        fnErpCountMoney();
                    }
                    var memberHtml = "";
                    memberHtml += "<div class='vip-msg'>"
                    memberHtml += "<section class='head' style='background-image: url(" + data.headimg + ")'></section>";
                    memberHtml += " <section style='overflow: hidden;'>";
                    memberHtml += " <section class='name text'>昵称：" + data.nickName + "</section>";
                    memberHtml += "<section class='card-number text'>卡号：" + data.cardNo + "</section>";
                    memberHtml += "<section class='phone text'>手机：" + data.phone + "</section>";
                    memberHtml += "<section class='type text'>类型：" + data.ctName + "</section>";
                    memberHtml += "<section class='grade text'>等级：" + data.gradeName + "</section>";
                    if (data.ctId == 2) {
                        memberHtml += "<section class='balance text'>折扣：" + data.discount + "折</section>";
                    } else if (data.ctId == 3) {
                        memberHtml += "<section class='balance text'>余额：" + data.money + "元</section>";
                        $(".item").removeClass("erp-billing-bg-color");
                        var chuzhiPayHtml = " <li  onclick='chuzhiPapMent(this)' class='item erp-billing-bg-color erp-billing-border' payType='5'>";
                        chuzhiPayHtml += "<p class='icon-p'><i class='iconfont'>&#xe62b;</i></p>";
                        chuzhiPayHtml += "<p>储值卡</p>";
                        chuzhiPayHtml += "</li>";
                        $(".chuzhiPayHtml").html(chuzhiPayHtml);
                    }
                    memberHtml += "<section class='integral text'>积分：" + data.integral + "</section>";
                    memberHtml += "</section>";
                    memberHtml += "</div>";
                    $(".memberHtml").html(memberHtml);

                    var youhuiHtml = "<div class='select-discount-type erp-billing-border-margin'>";
                    if (data.duofenCards.length == 0 && (data.cardList == null || data.cardList == "")) {
                        youhuiHtml += " <div class='erp-billing-item-title'>选择抵扣优惠</div>";
                        youhuiHtml += "<div class='discount-box-nomsg'>";
                        youhuiHtml += " <div class='icon'><i class='el-icon-warning'></i></div>";
                        youhuiHtml += " <p>您当前还未有会员优惠信息，请先领取优惠券。</p>";
                        youhuiHtml += "</div>";
                    } else {
                        youhuiHtml += "<div class='discount-box'>";
                        youhuiHtml += "<div class='coupon-box'>";
                        youhuiHtml += "<ul>";
                        var duofenCard = data.duofenCards;
                        for (var i = 0; i < duofenCard.length; i++) {
                            youhuiHtml += "<li class='coupon-item' onclick='fnChange(this)'>";
                            youhuiHtml += "<i class='arrow erp-billing-border' style='left: -8px;'></i>";
                            youhuiHtml += " <i class='arrow erp-billing-border' style='right: -8px;'></i>";
                            youhuiHtml += "  <section class='content erp-billing-border erp-billing-color' couponType='1' coupondId='" + duofenCard[i].gId + "'>";
                            if (duofenCard[i].card_type == 0) {
                                youhuiHtml += "   <p class='title'>" + duofenCard[i].discount + "折</p>";
                            } else if (duofenCard[i].card_type == 1) {
                                if (duofenCard[i].cash_least_cost > 0) {
                                    youhuiHtml += "   <p class='title'>满" + duofenCard[i].cash_least_cost + "元减" + duofenCard[i].reduce_cost + "元</p>";
                                } else {
                                    youhuiHtml += "   <p class='title'>减" + duofenCard[i].reduce_cost + "元</p>";
                                }
                            }
                            youhuiHtml += " <p class='dps'>" + duofenCard[i].title + "<span style='margin-left: 20px;'>x" + duofenCard[i].countId + "</span></p>";
                            youhuiHtml += "  </section>";
                            youhuiHtml += "<div class='youhuiNum'></div>";
                            youhuiHtml += " </li>";

                        }
                        var wxcard = data.cardList;
                        if (wxcard != null && wxcard != "") {
                            for (var i = 0; i < wxcard.length; i++) {
                                youhuiHtml += "<li class='coupon-item' onclick='fnChange(this)'>";
                                youhuiHtml += "<i class='arrow erp-billing-border' style='left: -8px;'></i>";
                                youhuiHtml += " <i class='arrow erp-billing-border' style='right: -8px;'></i>";
                                youhuiHtml += "  <section class='content erp-billing-border erp-billing-color' couponType='0'  coupondId='" + wxcard[i].id + "'>";
                                if (wxcard[i].card_type == "DISCOUNT") {
                                    youhuiHtml += "   <p class='title'>" + wxcard[i].discount + "折</p>";
                                } else if (wxcard[i].card_type == "CASH") {
                                    if (wxcard[i].cash_least_cost > 0) {
                                        youhuiHtml += "   <p class='title'>满" + wxcard[i].cash_least_cost + "元减" + wxcard[i].reduce_cost + "元</p>";
                                    } else {
                                        youhuiHtml += "   <p class='title'>减" + wxcard[i].reduce_cost + "元</p>";
                                    }
                                }

                                youhuiHtml += " <p class='dps'>" + wxcard[i].title + "<span style='margin-left: 20px;'>x1</span></p>";
                                youhuiHtml += "  </section>";
                                youhuiHtml += "<div class='youhuiNum'></div>";
                                youhuiHtml += " </li>";

                            }
                        }

                        youhuiHtml += " </ul>";
                        youhuiHtml += "  </div>";
                    }
                    youhuiHtml += " <ul class='jf-and-fb'>";
                    if (data.fenbiMoeny > 0) {
                        youhuiHtml += "  <li class='erp-billing-color' onclick='fnFenbiChange(this)'>";
                        youhuiHtml += "     <div class='box boxfenbi erp-billing-border'>";
                        youhuiHtml += "    <p class='title'>" + data.fans_currency + "粉币</p>";
                        youhuiHtml += "  <p>（" + data.fenbiMoeny + "元）</p>";
                        youhuiHtml += "  </div>";
                        youhuiHtml += "<p class='fenbiUserHtml'></p>";
                        youhuiHtml += "  </li>";
                    }

                    if (data.jifenMoeny > 0) {
                        youhuiHtml += "  <li class='erp-billing-color' onclick='fnjifenChange(this)'>";
                        youhuiHtml += "     <div class='box boxjifen erp-billing-border'>";
                        youhuiHtml += "    <p class='title'>" + data.integral + "积分</p>";
                        youhuiHtml += "  <p>（" + data.jifenMoeny + "元）</p>";
                        youhuiHtml += "  </div>";
                        youhuiHtml += "<p class='jifenUserHtml'></p>";
                        youhuiHtml += "  </li>";
                    }
                    youhuiHtml += "  </ul>";
                    youhuiHtml += "  </div>";
                    youhuiHtml += "  </div>";
                    $(".youhuiHtml").html(youhuiHtml);
                }
            }
        });
    }

    function fnChange(obj) {
        $(".content").removeClass("erp-billing-color erp-billing-bg-color");
        $(obj).find(".content").addClass("erp-billing-bg-color");
        fnErpCountMoney();
    }

    function fnFenbiChange(obj) {
        $(obj).find(".boxfenbi").toggleClass("erp-billing-bg-color");
        fnErpCountMoney();
    }

    function fnjifenChange(obj) {
        $(obj).find(".boxjifen").toggleClass("erp-billing-bg-color");
        fnErpCountMoney();
    }

    var mallAllEntityQueryStr = '${mallAllEntityQueryStr}';

    function fnErpCountMoney() {
        var param = {};
        param["memberId"] = $("#memberId").val();
        if ($(".boxfenbi ").hasClass("erp-billing-bg-color")) {
            param["useFenbi"] = 1;
        } else {
            param["useFenbi"] = 0;
        }

        if ($(".boxjifen ").hasClass("erp-billing-bg-color")) {
            param["userJifen"] = 1;
        } else {
            param["userJifen"] = 0;
        }

        if ($(".content").hasClass("erp-billing-bg-color")) {
            param["useCoupon"] = 1;
            param["couponType"] = $(".coupon-item").find(".erp-billing-bg-color").attr("couponType");
            param["coupondId"] = $(".coupon-item").find(".erp-billing-bg-color").attr("coupondId");
        } else {
            param["useCoupon"] = 0;
        }
        $.ajax({
            url: "/erpCount/erpCountMoney",
            data: {"mallAllEntityQuery": mallAllEntityQueryStr, "param": JSON.stringify(param)},
            type: "GET",
            dataType: "json",
            success: function (data) {
                if (data.result == false) {

                } else {
                    $("#totalMoney").val(data.mallNotShopEntity.balanceMoney);
                    var balanceMoney = data.mallNotShopEntity.balanceMoney;
                    if (balanceMoney == 0) {
                        $(".item").removeClass("erp-billing-bg-color");
                        $(".xianjinItem").addClass("erp-billing-bg-color");
                    }
                    $("#balanceMoney").html(balanceMoney);
                    var canUseConpon = data.mallNotShopEntity.canUseConpon;
                    var couponNum = data.mallNotShopEntity.couponNum;
                    if (canUseConpon == 0) {
                        $(".youhuiNum").html("");
                        $(".coupon-item").find(".erp-billing-bg-color").parent().find(".youhuiNum").html("<section class='erp-billing-color'>不能使用</section>");
                        $(".content").removeClass("erp-billing-color erp-billing-bg-color");
                    } else {
                        $(".youhuiNum").html("");
                        $(".coupon-item").find(".erp-billing-bg-color").parent().find(".youhuiNum").html("<section class='erp-billing-color'>已选择 ×" + couponNum + "</section>");
                    }
                    var canUsefenbi = data.mallNotShopEntity.canUsefenbi;
                    if (canUsefenbi == 0) {
                        $(".fenbiUserHtml").html("");
                        $(".boxfenbi").removeClass("erp-billing-bg-color");
                    } else {
                        var discountfenbiMoney = data.mallNotShopEntity.discountfenbiMoney;
                        $(".fenbiUserHtml").html("已抵扣" + discountfenbiMoney + "元");
                    }
                    var canUseJifen = data.mallNotShopEntity.canUseJifen;
                    if (canUseJifen == 0) {
                        $(".jifenUserHtml").html("");
                        $(".boxjifen").removeClass("erp-billing-bg-color");
                    } else {
                        var discountjifenMoney = data.mallNotShopEntity.discountjifenMoney;
                        $(".jifenUserHtml").html("已抵扣" + discountjifenMoney + "元");
                    }
                    var totalMoney = data.mallNotShopEntity.totalMoney;
                    var balanceMoney = data.mallNotShopEntity.balanceMoney;
                    var youhuiMoney = parseFloat(totalMoney) - parseFloat(balanceMoney);

                    var youhuiHtml = "<span class='title'>优惠金额：</span>";
                    youhuiHtml += "<span>￥-" + youhuiMoney + "元</span>";
                    if (canUseConpon != 0 || canUsefenbi != 0 || canUseJifen != 0) {
                        youhuiHtml += "<span style='padding-left: 5px;'>(消耗";
                        if (canUseConpon == 1) {
                            youhuiHtml += "优惠劵:" + couponNum + "张 &nbsp;&nbsp;";
                        }
                        var fenbiNum = data.mallNotShopEntity.fenbiNum;
                        if (canUsefenbi == 1) {
                            youhuiHtml += "粉币：" + fenbiNum + "&nbsp;&nbsp; ";
                        }

                        var jifenNum = data.mallNotShopEntity.jifenNum;
                        if (canUseJifen == 1) {
                            youhuiHtml += "积分：" + jifenNum;
                        }
                        youhuiHtml += "）</span>";

                        $(".youhuiItem").html(youhuiHtml);
                    }
                }
            }
        })
    }

    //储值卡或现金支付
    function chuzhiPay(payType, obj) {
        var param = {};
        param["payType"] = payType;
        param["visitor"] = $("#visitor").val();
        var payMoney = $("#payMoney").val();
        param["payMoney"] = payMoney;
        param["memberId"] = $("#memberId").val();
        if ($(".boxfenbi ").hasClass("erp-billing-bg-color")) {
            param["useFenbi"] = 1;
        } else {
            param["useFenbi"] = 0;
        }

        if ($(".boxjifen ").hasClass("erp-billing-bg-color")) {
            param["userJifen"] = 1;
        } else {
            param["userJifen"] = 0;
        }

        if ($(".content").hasClass("erp-billing-bg-color")) {
            param["useCoupon"] = 1;
            param["couponType"] = $(".coupon-item").find(".erp-billing-bg-color").attr("couponType");
            param["coupondId"] = $(".coupon-item").find(".erp-billing-bg-color").attr("coupondId");
        } else {
            param["useCoupon"] = 0;
        }
        $.ajax({
            url: "/erpCount/erpChuzhiPayMent",
            data: {"mallAllEntityQuery": mallAllEntityQueryStr, "param": JSON.stringify(param)},
            type: "GET",
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    obj.submitOrderMsgSuccess = true;
                } else {
                    obj.errorsubmitOrderMsg = true;
                    obj.$nextTick(function () {
                        $(".errorHtml").html(data.msg);
                    })
                }
            }
        });
    }


    function saomaPayMent(obj) {
        var param = {};
        param["visitor"] = $("#visitor").val();
        param["payMoney"] = $("#payMoney").val();

        param["memberId"] = $("#memberId").val();
        if ($(".boxfenbi ").hasClass("erp-billing-bg-color")) {
            param["useFenbi"] = 1;
        } else {
            param["useFenbi"] = 0;
        }

        if ($(".boxjifen ").hasClass("erp-billing-bg-color")) {
            param["userJifen"] = 1;
        } else {
            param["userJifen"] = 0;
        }

        if ($(".content").hasClass("erp-billing-bg-color")) {
            param["useCoupon"] = 1;
            param["couponType"] = $(".coupon-item").find(".erp-billing-bg-color").attr("couponType");
            param["coupondId"] = $(".coupon-item").find(".erp-billing-bg-color").attr("coupondId");
        } else {
            param["useCoupon"] = 0;
        }
        $.ajax({
            url: "/erpCount/saomaPayMent",
            data: {"mallAllEntityQuery": mallAllEntityQueryStr, "param": JSON.stringify(param)},
            type: "POST",
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    obj.dialogScanCodePayMentVisible = true;
                    obj.$nextTick(function () {
                        $("#saomaImage").attr("src", data.saomaoPayUrl);
                        $("#auth_code").focus();
                    })
                } else {
                    obj.errorsubmitOrderMsg = true;
                    obj.$nextTick(function () {
                        $(".errorHtml").html(data.msg);
                    })

                }
            }
        });
    }

    //扫码钱包支付
    function saomaQianBaoPay(obj) {
        var param = {};
        param["visitor"] = $("#visitor").val();
        param["payMoney"] = $("#payMoney").val();

        var auth_code = $("#auth_code").val();
        if (auth_code == null || auth_code == "") {
            return;
        }
        param["auth_code"] = auth_code;
        param["memberId"] = $("#memberId").val();
        if ($(".boxfenbi ").hasClass("erp-billing-bg-color")) {
            param["useFenbi"] = 1;
        } else {
            param["useFenbi"] = 0;
        }

        if ($(".boxjifen ").hasClass("erp-billing-bg-color")) {
            param["userJifen"] = 1;
        } else {
            param["userJifen"] = 0;
        }

        if ($(".content").hasClass("erp-billing-bg-color")) {
            param["useCoupon"] = 1;
            param["couponType"] = $(".coupon-item").find(".erp-billing-bg-color").attr("couponType");
            param["coupondId"] = $(".coupon-item").find(".erp-billing-bg-color").attr("coupondId");
        } else {
            param["useCoupon"] = 0;
        }
        $.ajax({
            url: "/erpCount/saomaQianBaoPay",
            data: {"mallAllEntityQuery": mallAllEntityQueryStr, "param": JSON.stringify(param)},
            type: "POST",
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    obj.submitOrderMsgSuccess = true;
                } else {
                    obj.errorsubmitOrderMsg = true;
                    obj.$nextTick(function () {
                        $(".errorHtml").html(data.msg);
                    })
                }
            }
        });
    }

    //推送
    var userId = '${member_count}';
    var socket =  io.connect('${member_socketHost}');

    socket.on('connect', function() {
        var jsonObject = {userId: userId,
            message: "0"};
        socket.emit('auth', jsonObject);
    });

    socket.on('chatevent', function(data) {
        sendMessage();
    });

    socket.on('disconnect', function() {
        output('<span class="disconnect-msg">The client has disconnected!</span>');
    });

    function sendMessage() {
        vue.dialogScanCodePayMentVisible = false;
        vue.submitOrderMsgSuccess = true;
    }



</script>
</body>
</html>