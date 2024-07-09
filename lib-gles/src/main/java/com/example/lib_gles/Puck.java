package com.example.lib_gles;

import java.util.List;

/**
 * @desc: 冰球
 * @auther:duwei
 * @date:2018/10/26
 */

public class Puck {
    private static final int POSITION_COMPONENT_COUNT = 3;


    public final float radius, height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawCommandList;


    public Puck(float radius, float height, int numPointsAroundPuck) {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createPuck(
                new Geometry.Cylinder(new Geometry.Point(0f, 0f, 0f),
                        radius, height),
                numPointsAroundPuck
        );

        this.radius = radius;
        this.height = height;
        vertexArray = new VertexArray(generatedData.vertexData);
        drawCommandList = generatedData.drawCommandList;

    }


}
