package com.example.wangduwei.demos.gles.shape;

public interface IShape {
    void draw();

    interface IShapeTranslateListener {
        void translateX(float x);

        void translateY(float y);

        void translateZ(float x);

        class IShapeTranslateListenerAdapter implements IShapeTranslateListener {

            @Override
            public void translateX(float x) {

            }

            @Override
            public void translateY(float y) {

            }

            @Override
            public void translateZ(float x) {

            }
        }
    }

    interface IShapeRotateListener {
        void rotateX(float x);

        void rotateY(float y);

        void rotateZ(float z);

        class IShapeRotateListenerAdapter implements IShapeRotateListener {
            @Override
            public void rotateX(float x) {

            }

            @Override
            public void rotateY(float y) {

            }

            @Override
            public void rotateZ(float z) {

            }
        }
    }

}
