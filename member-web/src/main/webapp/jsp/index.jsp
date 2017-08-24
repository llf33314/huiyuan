<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<script src="/js/jquery-2.2.2.js"></script>
<script>
   var param={};
   param["orderCode"]="ME123456789";
   param["totalMoney"]=100.0;
   param["ucType"]=103;
   param["busId"]=42;
   param["shopId"]=42;
   param["successNoticeUrl"]="http://www.baid.com";
   param["jumpUrl"]="http://www.baid.com";

   var goodsTypeArr=[];

   var goodsTypeJson={};

   goodsTypeJson["mallId"]=1;
   goodsTypeJson["number"]=2;
   goodsTypeJson["totalMoneyOne"]=50.0;
   goodsTypeJson["totalMoneyAll"]=100.0;
   goodsTypeJson["userCard"]=1;
   goodsTypeJson["useCoupon"]=1;
   goodsTypeJson["useFenbi"]=1;
   goodsTypeJson["userJifen"]=1;
   goodsTypeJson["useLeague"]=1;
   goodsTypeArr.push(goodsTypeJson);
   param["malls"]=JSON.stringify(goodsTypeArr);


    window.location.href="/erpCount/countErpIndex.do?orderCodeKey="+'${orderCodeKey}';



</script>
<head>
    <title>测试一下</title>
</head>
<body>
    <form>
        <input/>

    </form>
</body>
</html>
