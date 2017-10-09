/**
 * Created by Administrator on 2017/9/26.
 */

/**
 * 请求参数
 * 分装成对象
 *
 */
var member=null;
var youhuiquan=null;
var fenbiRule=null;
var jifenRule=null;
function count(){

}


/**
 * 参数 说明
 * @param ctId 会员卡类型
 * @param discount 会员卡折扣
 * @param isMemberDate 是否会员日
 * @param memberDateDiscount 会员日折扣
 * @param jifen 积分
 * @param fenbi 粉币
 */
function memberbo(ctId,discount,isMemberDate,memberDateDiscount){
    member=new Object();
    member.ctId = ctId;
    member.discount=discount;
    if(isMemberDate){
        member.discount=memberDateDiscount;
    }
}

/**
 * 优惠券参数
 * @param type 优惠券  0微信 1多粉
 * @param youhuiquanType 优惠券类型 0折扣券 1减免券
 * @param discount 折扣值
 * @param cash_least_cost 减免条件
 * @param reduce_cost  减免金额
 * @param number  优惠券数量
 * @param adduser 是否能叠加使用
 */
function youhuiquanbo(type,youhuiquanType,discount,cash_least_cost,reduce_cost,number,adduser){
    youhuiquan=new Object();
    youhuiquan.type=type;
    youhuiquan.youhuiquanType=youhuiquanType;
    youhuiquan.discount=discount;
    youhuiquan.cash_least_cost=cash_least_cost;
    youhuiquan.reduce_cost=reduce_cost;
    youhuiquan.number=number;
    youhuiquan.adduser=adduser;
}


/**
 * 粉币对象
 * @param fenbiMoney 粉币最高能抵扣金额
 * @param isUse 是否使用 0未使用 1使用
 */
function fenfiRuleBo(fenbiMoney,isUse){
    fenbiRule=new Object();
    fenbiRule.isUse=isUse;
    fenbiRule.fenbiMoney=fenbiMoney;
}

/**
 * 积分对象
 * @param isUse  是否使用 0未使用 1使用
 * @param jifenMoney 积分抵扣金额
 * @param startMoney  启兑金额
 */
function jifenRuleBo(isUse,jifenMoney,startMoney){
    jifenRule=new Object();
    jifenRule.isUse=isUse;
    jifenRule.jifenMoney =jifenMoney;
    jifenRule.startMoney=startMoney;
}





//加
function floatAdd(arg1,arg2){
    var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m;
}

//减
function floatSub(arg1,arg2){
    var r1,r2,m,n;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    //动态控制精度长度
    n=(r1>=r2)?r1:r2;
    return ((arg1*m-arg2*m)/m).toFixed(n);
}

//乘
function floatMul(arg1,arg2)   {
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
}


//除
function floatDiv(arg1,arg2){
    var t1=0,t2=0,r1,r2;
    try{t1=arg1.toString().split(".")[1].length}catch(e){}
    try{t2=arg2.toString().split(".")[1].length}catch(e){}

    r1=Number(arg1.toString().replace(".",""));

    r2=Number(arg2.toString().replace(".",""));
    return (r1/r2)*Math.pow(10,t2-t1);
}