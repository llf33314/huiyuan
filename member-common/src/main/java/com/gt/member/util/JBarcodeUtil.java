package com.gt.member.util;

import com.gt.member.enums.ResponseMemberEnums;
import com.gt.member.exception.BusinessException;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code128Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WidthCodedPainter;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * 条形码工具类
 *
 * Created by Administrator on 2017/11/27.
 */
public class JBarcodeUtil {

    public static void getJbarCode(String cardNo,HttpServletResponse response){
        try {
	    ByteArrayOutputStream outputStream = null;
	    BufferedImage bi = null;
	    int len = cardNo.length();
	    JBarcode productBarcode = new JBarcode( Code128Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance() );

	    // 尺寸，面积，大小 密集程度
	    productBarcode.setXDimension( Double.valueOf( "0.5" ).doubleValue() );
	    // 高度 10.0 = 1cm 默认1.5cm
	    productBarcode.setBarHeight( Double.valueOf( "30" ).doubleValue() );
	    // 宽度
	    productBarcode.setWideRatio( Double.valueOf( 30 ).doubleValue() );
	    //              是否显示字体
	    productBarcode.setShowText( true );
	    //             显示字体样式
	    //System.out.println(BaseLineTextPainter.getInstance().toString());
	    productBarcode.setTextPainter( BaseLineTextPainter.getInstance() );

	    // 生成二维码
	    bi = productBarcode.createBarcode( cardNo );

	    ImageIO.write( bi, "jpg", response.getOutputStream() );
	}catch ( Exception e ){
            throw new BusinessException( ResponseMemberEnums.ORDER_PAY_REPEAT );
	}
    }

}
