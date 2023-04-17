package com.rld.app.mycampaign.camera;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BorderDetect {

    public static void detectFromBitmap(Bitmap bitmap, GraphicOverlay graphicOverlay) {
        try {
            Mat img = new Mat();
            Utils.bitmapToMat(bitmap, img);

            Mat imgGray = new Mat();
            Mat imgCanny = new Mat();
            Mat imgDilate = new Mat();

            Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.Canny(imgGray, imgCanny,50,100);
            Imgproc.dilate(imgCanny, imgDilate, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2,2)));

            List<MatOfPoint> contours = new ArrayList<>();
            final Mat hierarchy = new Mat();
            Imgproc.findContours(imgDilate, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            //Ordena los contornos de menor a mayor, el mayor es la ine
            Collections.sort(contours, (c1, c2) -> (int) (Imgproc.contourArea(c1) - Imgproc.contourArea(c2)));

            MatOfPoint c = contours.get(contours.size() - 1);

            MatOfPoint2f c2f = new MatOfPoint2f( c.toArray() );
            double epsilon = 0.01 * Imgproc.arcLength(c2f, true);

            MatOfPoint2f approx2f = new MatOfPoint2f();
            Imgproc.approxPolyDP(c2f, approx2f, epsilon, true);

            graphicOverlay.clear();
            if ( approx2f.rows() == 4 ){
                MatOfPoint2f points = sortCorners(approx2f);
                graphicOverlay.add(
                        new RectOverlay(
                                graphicOverlay, Color.RED,
                                (int) points.toList().get(0).x,
                                (int) points.toList().get(0).y,
                                (int) points.toList().get(2).x,
                                (int) points.toList().get(2).y
                        )
                );
            }
        } catch ( Exception ignored) {
            graphicOverlay.clear();
        }
    }

    private static MatOfPoint2f sortCorners(MatOfPoint2f corners) {
        Point center = getMassCenter(corners);
        List<Point> points = corners.toList();
        List<Point> topPoints = new ArrayList<>();
        List<Point> bottomPoints = new ArrayList<>();

        for ( Point point : points ) {
            if ( point.y < center.y ) {
                topPoints.add(point);
            } else {
                bottomPoints.add(point);
            }
        }

        Point topLeft = topPoints.get(0).x > topPoints.get(1).x ? topPoints.get(1) : topPoints.get(0);
        Point topRight = topPoints.get(0).x > topPoints.get(1).x ? topPoints.get(0) : topPoints.get(1);
        Point bottomLeft = bottomPoints.get(0).x > bottomPoints.get(1).x ? bottomPoints.get(1) : bottomPoints.get(0);
        Point bottomRight = bottomPoints.get(0).x > bottomPoints.get(1).x ? bottomPoints.get(0) : bottomPoints.get(1);

        MatOfPoint2f result = new MatOfPoint2f();
        Point[] sortedPoints = {topLeft, topRight, bottomRight, bottomLeft};
        result.fromArray(sortedPoints);

        return result;
    }

    private static Point getMassCenter(MatOfPoint2f points) {
        double xSum = 0;
        double ySum = 0;
        List<Point> pointList = points.toList();
        int len = pointList.size();
        for ( Point point : pointList ) {
            xSum += point.x;
            ySum += point.y;
        }
        return new Point(xSum / len, ySum / len);
    }

    private static class RectOverlay extends GraphicOverlay.Graphic {

        private final Paint mPaint;
        private final android.graphics.Rect mRect;

        public RectOverlay(GraphicOverlay graphicOverlay, int color, int left, int top, int right, int bottom)
        {
            super(graphicOverlay);

            mPaint = new Paint();
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(10);

            mRect = new android.graphics.Rect(left, top, right, bottom);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRect(mRect, mPaint);
        }

    }

}