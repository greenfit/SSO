package com.heleeos.sso.util;

import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

/**
 * Created by liyu on 2018/7/17.
 */
public class ArcPath extends Path2D.Double {

  public ArcPath(int x, int y, int width, int height, int start, int extent) {
    append(new Arc2D.Double(x, y, 400, 300, -15, 210, Arc2D.OPEN), true);
  }
}
