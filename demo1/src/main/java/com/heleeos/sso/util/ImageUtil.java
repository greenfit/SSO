package com.heleeos.sso.util;

import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 *
 * Created by liyu on 2018/5/25.
 */
public class ImageUtil {

  private static int width = getPxFromMM(40);

  private static int height = getPxFromMM(30);

  /**
   * 画图章
   * @param name 企业名称
   * @param taxNumber 税号
   * @param index 图章序号
   */
  public static BufferedImage drawTextOnPath(String name, String taxNumber, String index) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();

    //1. 背景透明
    image = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    graphics.dispose();
    graphics = image.createGraphics();

    //2. 设置颜色
    graphics.setColor(new Color(255, 0, 0));

    //3. 外部椭圆
    drawOval(graphics);

    //4. 画税号
    drawTaxNo(graphics, taxNumber);

    //5. 画专用章文字
    drawText(graphics);

    //6. 画序号
    if (StringUtils.isNotBlank(index)) {
      graphics.drawString("(" + index + ")", 222, 320);
    }

    //7. 画企业名称
    drawName(graphics, name);

    return image;
  }

  /**
   * 画企业名称
   * @param graphics 画笔
   * @param name 企业名称
   */
  private static void drawName(Graphics2D graphics, String name) {
    int start = -15;
    int extent = 210;
    if(name.length() > 10) {
      start = -20;
      extent = 220;
    }
    if(name.length() > 15) {
      start = -30;
      extent = 240;
    }

    ArcPath arcPath = new ArcPath(35, 30,400, 300, start, extent);

    graphics.setColor(Color.YELLOW);
    graphics.setStroke(new BasicStroke(1));
    graphics.draw(arcPath);


    java.util.List<Point2D> points = new LinkedList<>();
    PathIterator pathIterator = arcPath.getPathIterator(null, 0.1);
    while (!pathIterator.isDone()) {
      double[] coords = new double[6];
      switch (pathIterator.currentSegment(coords)) {
        case PathIterator.SEG_MOVETO:
        case PathIterator.SEG_LINETO:
          points.add(new Point2D.Double(coords[0], coords[1]));
          break;
      }
      pathIterator.next();

    }

    Font font = new Font("仿宋体", Font.PLAIN, 25);
    graphics.setFont(font);

    int subCount = 1;
    int dispCount = StringUtils.countMatches(name, "(");


    int size = name.length() - (subCount + dispCount * 2); //需要空余的
    int step = points.size() / size;
    int startPoint = (points.size() % size / 2) + (points.size() % size % 2 != 0 ? 0 : 0);

    System.out.println("startPoint:" + startPoint + "step:" + step + ", all:" + points.size() + ", size:" + size + "," + (points.size() % size));


    // 在每个点上画文字
    int fontWidth = graphics.getFontMetrics().stringWidth(name.charAt(0) + "");
    int j = name.length() - 1;
    for(int i = startPoint; i < points.size(); j--) {
      if(j < 0) {
        break;
      }
      char ch = name.charAt(j);

      char nextCh = 0;
      if(j > 0) {
        nextCh = name.charAt(j - 1);
      }

      if(i == points.size() - 1) {
        i = i -1;
      }
      Point2D point = points.get(i);

      double angle = angleTo(points.get(i + 1), point);
      int x = (int) point.getX() - fontWidth / 2;
      int y = (int) point.getY() + fontWidth / 2;

      //旋转字体
      AffineTransform affineTransform = new AffineTransform();
      affineTransform.rotate(angle, 10, -10);
      Font newFont = font.deriveFont(affineTransform);

      graphics.setFont(newFont);
      graphics.setColor(Color.RED);

      graphics.drawString(ch + "", x, y);

      if(ch == '(' || ch == ')' || nextCh == '(' || nextCh == ')') {
        i = i + step / 4 * 3;
      } else {
        i = i + step;
      }
    }

  }

  /**
   * 计算旋转角度
   * @param from 基准点
   * @param to 要旋转的点
   */
  private static double angleTo(Point2D from, Point2D to) {
    return Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
  }

  /**
   * 画最外部的椭圆
   * @param graphics 画笔
   */
  private static void drawOval(Graphics2D graphics) {
    int size = getPxFromMM(1);
    graphics.setStroke(new BasicStroke(size));
    graphics.drawOval(size, size, width - size * 2, height - size * 2);
  }

  /**
   * 画税号
   * @param graphics 画笔
   * @param taxNumber 税号
   */
  private static void drawTaxNo(Graphics2D graphics, String taxNumber) {
    Font font = new Font("仿宋体", 0, getPxFromMM(2));

    AffineTransform affineTransform = new AffineTransform();
    affineTransform.setToScale(1, 1.5);

    Font newFont = font.deriveFont(affineTransform);

    graphics.setFont(newFont);
    graphics.drawString(taxNumber, 100, 220);
  }

  /**
   * 画专用章
   * @param graphics 画笔
   */
  private static void drawText(Graphics2D graphics) {
    String text = "发票专用章";

    Font font = new Font("仿宋体", 0, getPxFromMM(2.6));
    graphics.setFont(font);

    FontMetrics fontMetrics = graphics.getFontMetrics();
    int fWidth = fontMetrics.stringWidth(text);

    graphics.drawString(text, width / 2 - fWidth / 2, 280);
  }

  /**
   * 毫米转像素
   */
  private static int getPxFromMM(double mm) {
    return (int) (mm * 11.81102362204);
  }
}
