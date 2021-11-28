package com.example.wangduwei.demos.view.animation.interpolator;

/**
 * The Easing class provides a collection of ease functions. It does not use the standard 4 param
 * ease signature. Instead it uses a single param which indicates the current linear ratio (0 to 1) of the tween.
 */
class EasingProvider {
    /**
     * @param ease            Easing type
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    public static float get(@Ease int ease, float elapsedTimeRate) {
        switch (ease) {
            case Ease.LINEAR:
                return elapsedTimeRate;
            case Ease.QUAD_IN:
                return getPowIn(elapsedTimeRate, 2);
            case Ease.QUAD_OUT:
                return getPowOut(elapsedTimeRate, 2);
            case Ease.QUAD_IN_OUT:
                return getPowInOut(elapsedTimeRate, 2);
            case Ease.CUBIC_IN:
                return getPowIn(elapsedTimeRate, 3);
            case Ease.CUBIC_OUT:
                return getPowOut(elapsedTimeRate, 3);
            case Ease.CUBIC_IN_OUT:
                return getPowInOut(elapsedTimeRate, 3);
            case Ease.QUART_IN:
                return getPowIn(elapsedTimeRate, 4);
            case Ease.QUART_OUT:
                return getPowOut(elapsedTimeRate, 4);
            case Ease.QUART_IN_OUT:
                return getPowInOut(elapsedTimeRate, 4);
            case Ease.QUINT_IN:
                return getPowIn(elapsedTimeRate, 5);
            case Ease.QUINT_OUT:
                return getPowOut(elapsedTimeRate, 5);
            case Ease.QUINT_IN_OUT:
                return getPowInOut(elapsedTimeRate, 5);
            case Ease.SINE_IN:
                return (float) (1f - Math.cos(elapsedTimeRate * Math.PI / 2f));
            case Ease.SINE_OUT:
                return (float) Math.sin(elapsedTimeRate * Math.PI / 2f);
            case Ease.SINE_IN_OUT:
                return (float) (-0.5f * (Math.cos(Math.PI * elapsedTimeRate) - 1f));
            case Ease.BACK_IN:
                return (float) (elapsedTimeRate * elapsedTimeRate * ((1.7 + 1f) * elapsedTimeRate - 1.7));
            case Ease.BACK_OUT:
                return (float) (--elapsedTimeRate * elapsedTimeRate * ((1.7 + 1f) * elapsedTimeRate + 1.7) + 1f);
            case Ease.BACK_IN_OUT:
                return getBackInOut(elapsedTimeRate, 1.7f);
            case Ease.CIRC_IN:
                return (float) -(Math.sqrt(1f - elapsedTimeRate * elapsedTimeRate) - 1);
            case Ease.CIRC_OUT:
                return (float) Math.sqrt(1f - (--elapsedTimeRate) * elapsedTimeRate);
            case Ease.CIRC_IN_OUT:
                if ((elapsedTimeRate *= 2f) < 1f) {
                    return (float) (-0.5f * (Math.sqrt(1f - elapsedTimeRate * elapsedTimeRate) - 1f));
                }
                return (float) (0.5f * (Math.sqrt(1f - (elapsedTimeRate -= 2f) * elapsedTimeRate) + 1f));
            case Ease.BOUNCE_IN:
                return getBounceIn(elapsedTimeRate);
            case Ease.BOUNCE_OUT:
                return getBounceOut(elapsedTimeRate);
            case Ease.BOUNCE_IN_OUT:
                if (elapsedTimeRate < 0.5f) {
                    return getBounceIn(elapsedTimeRate * 2f) * 0.5f;
                }
                return getBounceOut(elapsedTimeRate * 2f - 1f) * 0.5f + 0.5f;
            case Ease.ELASTIC_IN:
                return getElasticIn(elapsedTimeRate, 1, 0.3);

            case Ease.ELASTIC_OUT:
                return getElasticOut(elapsedTimeRate, 1, 0.3);

            case Ease.ELASTIC_IN_OUT:
                return getElasticInOut(elapsedTimeRate, 1, 0.45);

            default:
                return elapsedTimeRate;

        }

    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private static float getPowIn(float elapsedTimeRate, double pow) {
        return (float) Math.pow(elapsedTimeRate, pow);
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private static float getPowOut(float elapsedTimeRate, double pow) {
        return (float) ((float) 1 - Math.pow(1 - elapsedTimeRate, pow));
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param pow             pow The exponent to use (ex. 3 would return a cubic ease).
     * @return easedValue
     */
    private static float getPowInOut(float elapsedTimeRate, double pow) {
        if ((elapsedTimeRate *= 2) < 1) {
            return (float) (0.5 * Math.pow(elapsedTimeRate, pow));
        }

        return (float) (1 - 0.5 * Math.abs(Math.pow(2 - elapsedTimeRate, pow)));
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amount          amount The strength of the ease.
     * @return easedValue
     */
    private static float getBackInOut(float elapsedTimeRate, float amount) {
        amount *= 1.525;
        if ((elapsedTimeRate *= 2) < 1) {
            return (float) (0.5 * (elapsedTimeRate * elapsedTimeRate * ((amount + 1) * elapsedTimeRate - amount)));
        }
        return (float) (0.5 * ((elapsedTimeRate -= 2) * elapsedTimeRate * ((amount + 1) * elapsedTimeRate + amount) + 2));
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private static float getBounceIn(float elapsedTimeRate) {
        return 1f - getBounceOut(1f - elapsedTimeRate);
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @return easedValue
     */
    private static float getBounceOut(float elapsedTimeRate) {
        if (elapsedTimeRate < 1 / 2.75) {
            return (float) (7.5625 * elapsedTimeRate * elapsedTimeRate);
        } else if (elapsedTimeRate < 2 / 2.75) {
            return (float) (7.5625 * (elapsedTimeRate -= 1.5 / 2.75) * elapsedTimeRate + 0.75);
        } else if (elapsedTimeRate < 2.5 / 2.75) {
            return (float) (7.5625 * (elapsedTimeRate -= 2.25 / 2.75) * elapsedTimeRate + 0.9375);
        } else {
            return (float) (7.5625 * (elapsedTimeRate -= 2.625 / 2.75) * elapsedTimeRate + 0.984375);
        }
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private static float getElasticIn(float elapsedTimeRate, double amplitude, double period) {
        if (elapsedTimeRate == 0 || elapsedTimeRate == 1) return elapsedTimeRate;
        double pi2 = Math.PI * 2;
        double s = period / pi2 * Math.asin(1 / amplitude);
        return (float) -(amplitude * Math.pow(2f, 10f * (elapsedTimeRate -= 1f)) * Math.sin((elapsedTimeRate - s) * pi2 / period));
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private static float getElasticOut(float elapsedTimeRate, double amplitude, double period) {
        if (elapsedTimeRate == 0 || elapsedTimeRate == 1) return elapsedTimeRate;

        double pi2 = Math.PI * 2;
        double s = period / pi2 * Math.asin(1 / amplitude);
        return (float) (amplitude * Math.pow(2, -10 * elapsedTimeRate) * Math.sin((elapsedTimeRate - s) * pi2 / period) + 1);
    }

    /**
     * @param elapsedTimeRate Elapsed time / Total time
     * @param amplitude       Amplitude of easing
     * @param period          Animation of period
     * @return easedValue
     */
    private static float getElasticInOut(float elapsedTimeRate, double amplitude, double period) {
        double pi2 = Math.PI * 2;

        double s = period / pi2 * Math.asin(1 / amplitude);
        if ((elapsedTimeRate *= 2) < 1) {
            return (float) (-0.5f * (amplitude * Math.pow(2, 10 * (elapsedTimeRate -= 1f)) * Math.sin((elapsedTimeRate - s) * pi2 / period)));
        }
        return (float) (amplitude * Math.pow(2, -10 * (elapsedTimeRate -= 1)) * Math.sin((elapsedTimeRate - s) * pi2 / period) * 0.5 + 1);

    }
}