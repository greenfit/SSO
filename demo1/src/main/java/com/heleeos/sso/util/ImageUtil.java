package com.heleeos.sso.util;

import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

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
      graphics.drawString("(" + index + ")", 222, 320);
    }

    //画名字
    drawName(graphics, name);

    return image;
  }

  private static void drawName(Graphics2D graphics, String name) {
    int start = -15;
    int extent = 210;
    if(name.length() % 2 == 0) {
      start = -15;
      extent = 210;
    }

    ArcPath arcPath = new ArcPath(35, 30,400, 300, start, extent);

    graphics.setColor(Color.YELLOW);
    graphics.setStroke(new BasicStroke(1));
    graphics.draw(arcPath);

    int number = 0;
    java.util.List<Point2D> points = new LinkedList<>();
    PathIterator pathIterator = arcPath.getPathIterator(null, 0.1);
    while (!pathIterator.isDone()) {
      System.out.println(number++);
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

    int size = name.length() - 1; //需要空余的
    int step = points.size() / size;

    int startPoint = points.size() % size / 2;

    System.out.println("step:" + step + ", all:" + points.size() + ", size:" + size + "," + (points.size() % size));

    int fontWidth = graphics.getFontMetrics().stringWidth(name.charAt(0) + "");


    int j = name.length() - 1;
    for(int i = startPoint; i < points.size(); i = i + step, j--) {
      if(i == points.size() - 1) {
        i = i -1;
      }
      Point2D point = points.get(i);

      double angle = angleTo(points.get(i + 1), point);
//      System.out.println(angle);

      int x = (int) point.getX() - fontWidth / 2;
      int y = (int) point.getY() + fontWidth / 2;

      AffineTransform affineTransform = new AffineTransform();
      affineTransform.rotate(angle, 10, -10);

      Font newFont = font.deriveFont(affineTransform);

      graphics.setFont(newFont);
      graphics.setColor(Color.RED);

      graphics.drawString(name.charAt(j) + "", x, y);
    }

  }

  private static double angleTo(Point2D from, Point2D to) {
    return Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
  }

  private static Shape generateShapeFromText(Font font, String string) {
    BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = img.createGraphics();

    try {
      GlyphVector vect = font.createGlyphVector(g2.getFontRenderContext(), string);
      return vect.getOutline(0f, (float) -vect.getVisualBounds().getY());
    } finally {
      g2.dispose();
    }
  }

  private static int getWidth(char c, FontMetrics fm) {
    if (c == ' ' || Character.isSpaceChar(c)) {
      return fm.charWidth('A');
    }
    else {
      return fm.charWidth(c);
    }
  }

  private static void drawOval(Graphics2D graphics) {
    int size = getPxFromMM(1);
    graphics.setStroke(new BasicStroke(size));
    graphics.drawOval(size, size, width - size * 2, height - size * 2);
  }

  private static void drawTaxNo(Graphics2D graphics, String taxNumber) {
    Font font = new Font("仿宋体", 0, getPxFromMM(2));

    AffineTransform affineTransform = new AffineTransform();
    affineTransform.setToScale(1, 1.5);

    Font newFont = font.deriveFont(affineTransform);

    graphics.setFont(newFont);
    graphics.drawString(taxNumber, 100, 220);
  }

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
