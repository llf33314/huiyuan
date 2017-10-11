//手机验证
function Mobilephone(obj){
	var result = true;
	var  isPhone = /^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[0123456789][0-9]{8}|17[0123456789][0-9]{8}|18[0123456789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	if (isPhone.test(obj) ) {
			
	}else{
		result = false;
	}
	return result;
}
//固话验证
function fixedtelephone(obj){
	var result = true;
	var  isfixed =/^([0-9]{3,4})?[0-9]{7,8}$/;
	if (isfixed.test(obj) ) {
			
	}else{
		result = false;
	}
	return result;
}

function phone(obj){
	var result = true;
	var  isPhone = /^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[0123456789][0-9]{8}|17[0123456789][0-9]{8}|18[0123456789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;
	var  isfixed =/^([0-9]{3,4})?[0-9]{7,8}$/;
	if (isPhone.test(obj)||isfixed.test(obj) ) {
			
	}else{
		result = false;
	}
	return result;
}