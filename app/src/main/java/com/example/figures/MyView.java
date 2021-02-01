package com.example.figures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Point;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyView extends View {
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context) {
        super(context);
    }


    String type = "circle";
    List<Point> points = new ArrayList<Point>();
    final int cellSize = 30;
    int width;
    int height;
    int heightRect;
    int widthRect;
    Point angle;
    Point center;
    float radius;
    Point a;
    Point b;
    Point c;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        drawGrid(canvas);

        switch (points.size()) {
            case 2 :
            //drawRect(canvas);
            drawCircle(canvas);
            break;
            case 3 : drawTriangle(canvas); break;
        }
        drawPoints(canvas);

    }

    public void drawGrid(Canvas canvas) {
        Paint paint = new Paint();
        DashPathEffect effects = new DashPathEffect(new float[]{3, 9}, 0);
        paint.setPathEffect(effects);
        for (int i = 0; i < width / cellSize; i++) {
            int x = i * cellSize;
            canvas.drawLine(x, 0, x, height, paint);
            paint.setColor(Color.parseColor("black"));
        }
        for (int i = 0; i < height / cellSize; i++) {
            int y = i * cellSize;
            canvas.drawLine(0, y, width, y, paint);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          downTouch(event.getX(), event.getY());
        }
        return true;
    }
    public void downTouch(float x, float y) {
        points.add(new Point((int)x,(int)y));
        switch (type) {
            case "rect": checkRectCreating(); break;
            case "circle": checkCircleCreating(); break;
            case "triangle": checkTriangleCreating(); break;
        }
        invalidate();
    }

    public void drawPoints(Canvas canvas) {
        Paint paint = new Paint();
        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, 5, paint);
        }
    }

    public void drawCircle (Canvas canvas){
        Paint paint = new Paint();
        canvas.drawCircle(center.x, center.y, radius, paint);
        points.clear();
    }
    public void drawRect (Canvas canvas){
        Paint paint = new Paint();
        canvas.drawRect(angle.x, angle.y,angle.x + widthRect, angle.y + heightRect, paint);
        points.clear();
    }

    public void drawTriangle (Canvas canvas) {
        Paint paint = new Paint();
        Path path = new Path();
        path.moveTo(points.get(0).x, points.get(0).y);
        path.lineTo(points.get(1).x, points.get(1).y);
        path.lineTo(points.get(2).x, points.get(2).y);
        path.lineTo(points.get(0).x, points.get(0).y);
        canvas.drawPath(path, paint);
        points.clear();
    }

    private void createTriangle (Point a, Point b, Point c){
        this.a = a;
        this.b = b;
        this.c = c;
    }
    public void createCircle(Point center, float radius) {
        this.center = center;
        this.radius = radius;
    }
    public void createRect (int width, int height, Point angle) {
        this.width = width;
        this.height = height;
        this.angle = angle;
    }

    public void checkTriangleCreating() {
        if (points.size() == 3)
        createTriangle(points.get(0), points.get(1), points.get(2));
    }

    public void checkCircleCreating () {
        if (points.size() == 2) {
            int a = points.get(1).x - points.get(0).x;
            int b = points.get(1).y - points.get(0).y;
            float radius = (float)Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
            createCircle(points.get(0), radius);
        }
    }

    public void checkRectCreating () {
        if (points.size() == 2) {
            int react_width = points.get(1).x - points.get(0).x;
            int react_height = points.get(1).y - points.get(0).y;
            createRect(react_width, react_height, angle);
        }
    }
}