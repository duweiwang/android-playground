package com.example.wangduwei.demos.gles;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

/**
 * 物体构建器
 */
public class ObjectBuilder {
    private static final int FLOAT_PER_VERTEX = 3;

    private final float[] vertexData;
    private int offset = 0;//下一个顶点的位置

    private final List<DrawCommand> drawCommandList = new ArrayList<>();

    static interface DrawCommand {
        void draw();
    }

    static class GeneratedData {
        final float[] vertexData;
        final List<DrawCommand> drawCommandList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawCommandList) {
            this.vertexData = vertexData;

            this.drawCommandList = drawCommandList;
        }
    }

    public ObjectBuilder(int sizeOfVertices) {
        vertexData = new float[sizeOfVertices * FLOAT_PER_VERTEX];
    }


    public static int getCylinderSidePointNum(int numPoints) {
        return (numPoints + 1) * 2;
    }

    public static int getCylinderTopPointNum(int numPoints) {
        return 1 + (numPoints + 1);
    }


    /**
     * 创建冰球
     *
     * @param puck
     * @param numPoint
     * @return
     */
    public static GeneratedData createPuck(Geometry.Cylinder puck, int numPoint) {
        int size = getCylinderSidePointNum(numPoint) + getCylinderTopPointNum(numPoint);

        ObjectBuilder builder = new ObjectBuilder(size);
        Geometry.Circle top = new Geometry.Circle(puck.center.translateY(puck.height / 2f), puck.radius);
        builder.appendTopCircle(top, numPoint);
        builder.appendSideCylinder(puck, numPoint);
        return builder.build();
    }

    public static GeneratedData createMallet(Geometry.Point center, float radius, float height, int numPoints) {
        int size = getCylinderTopPointNum(numPoints) * 2
                + getCylinderSidePointNum(numPoints) * 2;

        ObjectBuilder builder = new ObjectBuilder(size);

        float baseHeight = height * 0.25f;

        Geometry.Circle baseCircle = new Geometry.Circle(center.translateY(-baseHeight), radius);
        Geometry.Cylinder baseCylinder = new Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2f),
                radius, baseHeight);

        builder.appendTopCircle(baseCircle, numPoints);
        builder.appendSideCylinder(baseCylinder, numPoints);

        float handleHeight = height * 0.75f;
        float handleRadius = radius / 3f;

        Geometry.Circle handleCircle = new Geometry.Circle(center.translateY(height * 0.5f), handleRadius);
        Geometry.Cylinder handleCylinder = new Geometry.Cylinder(handleCircle.center.translateY(-handleHeight / 2f),
                handleRadius, handleHeight);

        builder.appendTopCircle(handleCircle, numPoints);
        builder.appendSideCylinder(handleCylinder, numPoints);

        return builder.build();
    }

    private GeneratedData build() {
        return new GeneratedData(vertexData, drawCommandList);
    }

    /**
     * 生成一个圆的坐标
     *
     * @param circle   圆心对象
     * @param numPoint 圆边上有多少个点
     */
    private void appendTopCircle(Geometry.Circle circle, int numPoint) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertices = getCylinderTopPointNum(numPoint);

        //圆心坐标
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        //圆边缘的点
        for (int i = 0; i <= numPoint; i++) {
            //将360度分成numPoint份，第i份的度数
            float angleInRadians =
                    ((float) i / (float) numPoint) * ((float) Math.PI * 2f);

            vertexData[offset++] = circle.center.x +
                    circle.radius * (float) Math.cos(angleInRadians);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z +
                    circle.radius * (float) Math.sin(angleInRadians);

        }

        drawCommandList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }


    private void appendSideCylinder(Geometry.Cylinder cylinder, int numPoints) {
        final int startVertex = offset / FLOAT_PER_VERTEX;
        final int numVertex = getCylinderSidePointNum(numPoints);
        final float yStart = cylinder.center.y - (cylinder.height / 2f);
        final float yEnd = cylinder.center.y + (cylinder.height / 2f);

        for (int i = 0; i <= numPoints; i++) {
            float angleInRadians = ((float) i / (float) numPoints) * ((float) Math.PI * 2f);

            float xPosition = cylinder.center.x + cylinder.radius * (float) Math.cos(angleInRadians);

            float zPosition = cylinder.center.z + cylinder.radius * (float) Math.sin(angleInRadians);

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }

        drawCommandList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, startVertex, numVertex);
            }
        });
    }


}
