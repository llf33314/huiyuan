package com.gt.member.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码工具类 需要zxingcore-2.2.jar
 * 
 * @author Camey
 *
 */
public final class QRcodeKit {
	private static final int BLACK = 0xFF000000;

	private static final int WHITE = 0xFFFFFFFF;


 
	
	/**
	 * 生成二维码 输出
	 * 
	 * @param contents
	 *            文本
	 * @param path
	 *            路径
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @return
	 */
	public static void buildQRcode(String contents,
			Integer width,
			Integer height,HttpServletResponse response) {
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		// 用于设置QR二维码参数
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 设置编码方式
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		try {
			BitMatrix bitMatrix = multiFormatWriter.encode(contents,
					BarcodeFormat.QR_CODE,
					width,
					height,
					hints);
			OutputStream stream=response.getOutputStream();
			writeToStream(bitMatrix, "jpg", stream);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width,
				height,
				BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}



	public static void writeToStream(BitMatrix matrix,
			String format,
			OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format "
					+ format);
		}
	}
}
