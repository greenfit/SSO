package com.heleeos.sso.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Created by liyu on 2018/7/17.
 */
public class TestPath {
  public static void main(String[] args) {

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
          ex.printStackTrace();
        }

        JFrame frame = new JFrame("Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TestPane());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(800, 600);
      }
    });

  }


   static class TestPane extends JPanel {

    private ArcPath arcPath;
    private Shape shape;

    private List<Point2D> points;
    private double angle;
    private Point2D pos;
    private int index;

    protected static final double PLAY_TIME = 5000; // 5 seconds...

    private Long startTime;

    public TestPane() {
      arcPath = new ArcPath(0,0, 400, 300, -15, 210);
//      shape = generateShapeFromText(new Font("宋体", 0, 20), "上");

      points = new ArrayList<>(6);
      PathIterator pi = arcPath.getPathIterator(null, 0.1);
      while (!pi.isDone()) {
        double[] coords = new double[6];
        switch (pi.currentSegment(coords)) {
          case PathIterator.SEG_MOVETO:
          case PathIterator.SEG_LINETO:
            points.add(new Point2D.Double(coords[0], coords[1]));
            break;
        }
        pi.next();
      }

      final String message = "上海张三的有限公司";
      pos = points.get(0);
      Timer timer = new Timer(40, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

          if (startTime == null) {
            startTime = System.currentTimeMillis();
          }
          long playTime = System.currentTimeMillis() - startTime;
          double progress = playTime / PLAY_TIME;
          if (progress >= 1.0) {
            progress = 1d;
            ((Timer) e.getSource()).stop();
          }

          shape = generateShapeFromText(new Font("宋体", 0, 20), message.charAt(index % message.length()) + "");

          int index = Math.min(Math.max(0, (int) (points.size() * progress)), points.size() - 1);
          System.out.println(index + "," + message.charAt(index % message.length()));

          pos = points.get(index);
          if (index < points.size() - 1) {
            angle = angleTo(pos, points.get(index + 1));
          }
          repaint();
        }
      });

      timer.start();
    }

    protected double angleTo(Point2D from, Point2D to) {
      return Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(200, 200);
    }

    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g.create();
      applyQualityRenderingHints(g2d);
      int x = (getWidth() - arcPath.getBounds().width) / 2;
      int y = (getHeight() - arcPath.getBounds().height) / 2;
      g2d.translate(x, y);
      g2d.draw(arcPath);
      AffineTransform at = new AffineTransform();

      if (pos != null) {


        Rectangle bounds = shape.getBounds();
        System.out.println(angle);
        at.rotate(angle, (bounds.width / 2), (bounds.width / 2));

        Path2D player = new Path2D.Double(shape, at);

        g2d.translate(pos.getX() - (bounds.width / 2), pos.getY() - (bounds.height / 2));
        g2d.setColor(Color.RED);
        g2d.draw(player);

      }
//      g2d.dispose();
    }

  }

  public static Shape generateShapeFromText(Font font, String string) {
    BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = img.createGraphics();

    try {
      GlyphVector vect = font.createGlyphVector(g2.getFontRenderContext(), string);
      return vect.getOutline(0f, (float) -vect.getVisualBounds().getY());
    } finally {
      g2.dispose();
    }
  }

  public static void applyQualityRenderingHints(Graphics2D g2d) {

    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

  }
}
