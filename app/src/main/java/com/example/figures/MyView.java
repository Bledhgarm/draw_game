package com.example.figures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Point;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyView extends View {
    public static final int typeRect = 0;
    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context) {
        super(context);
    }


    String type = "rect";
    List<Point> points = new ArrayList<Point>();
    int cellSize = 30;
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
        drawPoints(canvas);
        drawTriangle(canvas);
        drawCircle(canvas);
        drawRect(canvas);
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
        switch (type) {
            case "rect": checkRectCreating(); break;
            case "circle": checkCircleCreating(); break;
            case "triangle": checkTriangeCreating(); break;
        }
        points.add(new Point((int)x,(int)y));
        invalidate();
    }

    public void drawPoints(Canvas canvas) {
        Paint paint = new Paint();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            canvas.drawCircle(point.x, point.y, 5, paint);

        }
    }

    public void drawCircle (Canvas canvas){
        Paint paint = new Paint();
        canvas.drawCircle(center.x, center.y, radius, paint);
    }
    public void drawRect (Canvas canvas){
        Paint paint = new Paint();
        canvas.drawRect(angle.x, angle.y,angle.x + widthRect, angle.y + heightRect, paint);
    }

    public void drawTriangle (Canvas canvas) {
        Paint paint = new Paint();
        Path path = new Path();
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        canvas.drawPath(path, paint);
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

    public void checkTriangeCreating () {
        if (points.size() >= 3)
        createTriangle(new Point(points.get(0)), new Point(points.get(1)), new Point(points.get(2)));
        points.clear();
    }

    public void checkCircleCreating () {
        if (points.size() == 2) {
            int a = points.get(1).x - points.get(0).x;
            int b = points.get(1).y - points.get(0).y;
            float radius = (float)Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
            createCircle(new Point(points.get(0)), radius);
            points.clear();

        }
    }
                               
    public void checkRectCreating () {
        if (points.size() >= 2) {
            int react_width = points.get(1).x - points.get(0).x;
            int react_height = points.get(1).y - points.get(0).y;
            createRect(react_width, react_height, angle);
        }
    }
}