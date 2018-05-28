package com.heleeos.sso.util;

import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Created by liyu on 2018/5/25.
 */
public class ImageUtil {

  private static int width = getPxFromMM(40);

  private static int height = getPxFromMM(30);

  public static BufferedImage drawTextOnPath(String name, String taxNumber, String index) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();

    //1. 背景透明
    image = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    graphics.dispose();
    graphics = image.createGraphics();

    //
    graphics.setColor(new Color(255, 0, 0));

    //外部椭圆
    drawOval(graphics);

    //画税号
    drawTaxNo(graphics, taxNumber);

    //画专用章
    drawText(graphics);

    //画序号
    if (StringUtils.isNotBlank(index)) {
      graphics.drawString("(" + index + ")", 222, 270);
    }

    //画名字
    drawName(graphics, name);

    return image;
  }

  private static void drawName(Graphics2D graphics, String name) {
    Font font = new Font("仿宋体", 0, 24);
    FontRenderContext fontRenderContext = graphics.getFontRenderContext();
    graphics.translate(width / 2 - 10, 40);

    double allTheta = 210 / 180 * Math.PI;
    double start = 105 / 180 * Math.PI;


    GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, name);
    int length = glyphVector.getNumGlyphs();
    for (int i = 0; i < length; i++) {


//      double theta = i * (allTheta / length) / 4;
//    double theta = (double) i / (double) (length - 1) * Math.PI / 2;

      double theta = (1.5) * Math.PI;
      Point2D point2D = glyphVector.getGlyphPosition(i);
      AffineTransform affineTransform = AffineTransform.getTranslateInstance(point2D.getX(), point2D.getY());
      affineTransform.rotate(theta);
      Shape glyph = glyphVector.getGlyphOutline(i);
      Shape transformedGlyph = affineTransform.createTransformedShape(glyph);
      graphics.fill(transformedGlyph);
    }
  }

  private static void drawOval(Graphics2D graphics) {
    int size = getPxFromMM(1);
    graphics.setStroke(new BasicStroke(size));
    graphics.drawOval(size, size, width - size * 2, height - size * 2);
  }

  private static void drawTaxNo(Graphics2D graphics, String taxNumber) {
    graphics.setFont(new Font("仿宋体", 0, getPxFromMM(1.3)));
    graphics.drawString(taxNumber, 120, 150);
  }

  private static void drawText(Graphics2D graphics) {
    String text = "发票专用章";

    Font font = new Font("仿宋体", 0, getPxFromMM(2.6));
    graphics.setFont(font);

    FontMetrics fontMetrics = graphics.getFontMetrics();
    int fWidth = fontMetrics.stringWidth(text);

    graphics.drawString(text, width / 2 - fWidth / 2, 207);
  }

  /**
   * 毫米转像素
   */
  private static int getPxFromMM(double mm) {
    return (int) (mm * 11.81102362204);
  }
}
