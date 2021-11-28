package com.example.wangduwei.demos.fragment;

import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;
import com.example.wangduwei.demos.view.customview.MatrixUseView;
import com.example.wangduwei.demos.view.customview.matrix.MatrixImageView;

import org.w3c.dom.Text;

/**
 * @author : wangduwei
 * @date : 2020/3/3
 * @description : Matrix矩阵变换对图片的影响
 */
@PageInfo(description = "Matrix矩阵", navigationId = R.id.fragment_matrix)
public class MatrixFragment extends BaseSupportFragment implements CheckBox.OnCheckedChangeListener {

    private MatrixImageView imageView;
    private CheckBox preTranslate, postTranslate, preScale, postScale, preRotate, postRotate;
    private Button /*mBtn,*/ mReset;

    public static final int default_translate_10 = 10;
    public static final int default_rotate_45 = 45;
    public static final float default_scale = 0.5f;

    private TextView m11, m12, m13, m21, m22, m23, m31, m32, m33;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matrix, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.matrix_image_view);
        imageView.setImageResource(R.drawable.guide_enjoy_haha);

        preTranslate = view.findViewById(R.id.matrix_control_preTranslate);
        postTranslate = view.findViewById(R.id.matrix_control_postTranslate);
        preTranslate.setOnCheckedChangeListener(this);
        postTranslate.setOnCheckedChangeListener(this);

        preScale = view.findViewById(R.id.matrix_control_preScale);
        postScale = view.findViewById(R.id.matrix_control_postScale);
        preScale.setOnCheckedChangeListener(this);
        postScale.setOnCheckedChangeListener(this);

        preRotate = view.findViewById(R.id.matrix_control_preRotate);
        postRotate = view.findViewById(R.id.matrix_control_postRotate);
        preRotate.setOnCheckedChangeListener(this);
        postRotate.setOnCheckedChangeListener(this);

        mReset = view.findViewById(R.id.matrix_reset);
        mReset.setOnClickListener(this);

        m11 = view.findViewById(R.id.matrix_11);
        m12 = view.findViewById(R.id.matrix_12);
        m13 = view.findViewById(R.id.matrix_13);
        m21 = view.findViewById(R.id.matrix_21);
        m22 = view.findViewById(R.id.matrix_22);
        m23 = view.findViewById(R.id.matrix_23);
        m31 = view.findViewById(R.id.matrix_31);
        m32 = view.findViewById(R.id.matrix_32);
        m33 = view.findViewById(R.id.matrix_33);

        fullText(imageView.getProcessor().getMatrix());
    }

    public void fullText(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);

        m11.setText("" + values[0]);
        m12.setText("" + values[1]);
        m13.setText("" + values[2]);

        m21.setText("" + values[3]);
        m22.setText("" + values[4]);
        m23.setText("" + values[5]);

        m31.setText("" + values[6]);
        m32.setText("" + values[7]);
        m33.setText("" + values[8]);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) return;

        int width = imageView.getDrawable().getIntrinsicWidth();
        int height = imageView.getDrawable().getIntrinsicHeight();

        if (buttonView.equals(preTranslate)) {
            imageView.getProcessor().preTranslate(default_translate_10, default_translate_10);
        }
        if (buttonView.equals(postTranslate)) {
            imageView.getProcessor().postTranslate(default_translate_10, default_translate_10);
        }
        if (buttonView.equals(preScale)) {
            imageView.getProcessor().preScale(default_scale, default_scale, width / 2, height / 2);
        }
        if (buttonView.equals(postScale)) {
            imageView.getProcessor().postScale(default_scale, default_scale, width / 2, height / 2);
        }
        if (buttonView.equals(preRotate)) {
            imageView.getProcessor().preRotate(default_rotate_45);
        }
        if (buttonView.equals(postRotate)) {
            imageView.getProcessor().postRotate(default_rotate_45);
        }
        imageView.invalidate();//每次操作完之后刷新
        fullText(imageView.getProcessor().getMatrix());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.equals(mReset)) {
            imageView.getProcessor().resetMatrix();
            fullText(imageView.getProcessor().getMatrix());

            preTranslate.setChecked(false);
            postTranslate.setChecked(false);
            preScale.setChecked(false);
            postScale.setChecked(false);
            preRotate.setChecked(false);
            postRotate.setChecked(false);
            imageView.setImageResource(R.drawable.guide_enjoy_haha);
        }
    }
}
