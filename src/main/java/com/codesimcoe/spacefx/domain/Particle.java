package com.codesimcoe.spacefx.domain;

import com.codesimcoe.spacefx.configuration.Configuration;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public class Particle {

  public record Position(double x, double y) {
  }

  // Position
  private double x;
  private double y;

  // Speed
  private double vx;
  private double vy;

  // Acceleration
  private double ax;
  private double ay;

  private final Color color;

  private final List<Position> positions = new LinkedList<>();
  private final Color[] previousColors = new Color[Configuration.PARTICLE_POSITION_HISTORY_COUNT];

  public Particle(
    final double x,
    final double y,
    final double vx,
    final double vy,
    final Color color) {

    this.x = x;
    this.y = y;
    this.vx = vx;
    this.vy = vy;
    this.color = color;

    // Create history fading colors
    for (int i = 0; i < Configuration.PARTICLE_POSITION_HISTORY_COUNT; i++) {
      Color hColor = color.deriveColor(
        0,
        1,
        1,
        1 - (double) i / Configuration.PARTICLE_POSITION_HISTORY_COUNT
      );
      this.previousColors[i] = hColor;
    }
  }

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public Color getColor() {
    return this.color;
  }

  public void setAx(final double ax) {
    this.ax = ax;
  }

  public void setAy(final double ay) {
    this.ay = ay;
  }

  public Color getHistoryColor(final int i) {
    return this.previousColors[i];
  }

  public final void update() {

    this.positions.addFirst(new Position(this.x, this.y));
    if (this.positions.size() > Configuration.PARTICLE_POSITION_HISTORY_COUNT) {
      this.positions.removeLast();
    }

    // Speed update depending on acceleration
    this.vx += this.ax;
    this.vy += this.ay;

    // Position update depending on speed
    this.x += this.vx;
    this.y += this.vy;
  }

  public List<Position> getPositions() {
    return this.positions;
  }

  private void performSphericWorldProcessing() {
    // If the particle goes too far to right, pops left
    if (this.x > Configuration.CANVAS_WIDTH) {
      this.x = 0;
    }

    // Same for left
    if (this.x < 0) {
      this.x = Configuration.CANVAS_WIDTH;
    }

    // Same for top
    if (this.y > Configuration.CANVAS_HEIGHT) {
      this.y = 0;
    }

    // Same for bottom
    if (this.y < 0) {
      this.y = Configuration.CANVAS_HEIGHT;
    }
  }
}