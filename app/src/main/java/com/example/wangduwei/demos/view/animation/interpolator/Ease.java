package com.example.wangduwei.demos.view.animation.interpolator;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The Easing class provides a collection of ease functions. It does not use the standard 4 param
 * ease signature. Instead it uses a single param which indicates the current linear ratio (0 to 1) of the tween.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({Ease.LINEAR, Ease.QUAD_IN, Ease.QUAD_OUT, Ease.QUAD_IN_OUT, Ease.CUBIC_IN, Ease.CUBIC_OUT,
        Ease.CUBIC_IN_OUT, Ease.QUART_IN, Ease.QUART_OUT, Ease.QUART_IN_OUT, Ease.QUINT_IN,
        Ease.QUINT_OUT, Ease.QUINT_IN_OUT, Ease.SINE_IN, Ease.SINE_OUT, Ease.SINE_IN_OUT, Ease.BACK_IN,
        Ease.BACK_OUT, Ease.BACK_IN_OUT, Ease.CIRC_IN, Ease.CIRC_OUT, Ease.CIRC_IN_OUT, Ease.BOUNCE_IN,
        Ease.BOUNCE_OUT, Ease.BOUNCE_IN_OUT, Ease.ELASTIC_IN, Ease.ELASTIC_OUT, Ease.ELASTIC_IN_OUT})
public @interface Ease {
    int LINEAR = 0;
    int QUAD_IN = 1;
    int QUAD_OUT = 2;
    int QUAD_IN_OUT = 3;
    int CUBIC_IN = 4;
    int CUBIC_OUT = 5;
    int CUBIC_IN_OUT = 6;
    int QUART_IN = 7;
    int QUART_OUT = 8;
    int QUART_IN_OUT = 9;
    int QUINT_IN = 10;
    int QUINT_OUT = 11;
    int QUINT_IN_OUT = 12;
    int SINE_IN = 13;
    int SINE_OUT = 14;
    int SINE_IN_OUT = 15;
    int BACK_IN = 16;
    int BACK_OUT = 17;
    int BACK_IN_OUT = 18;
    int CIRC_IN = 19;
    int CIRC_OUT = 20;
    int CIRC_IN_OUT = 21;
    int BOUNCE_IN = 22;
    int BOUNCE_OUT = 23;
    int BOUNCE_IN_OUT = 24;
    int ELASTIC_IN = 25;
    int ELASTIC_OUT = 26;
    int ELASTIC_IN_OUT = 27;
}
