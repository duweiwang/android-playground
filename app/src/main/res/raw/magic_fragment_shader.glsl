precision mediump float;

uniform vec2 iResolution; // (width, height)
uniform float iTime;      // seconds

// https://iquilezles.org/articles/palettes/
vec3 palette(float t) {
    vec3 a = vec3(0.5, 0.5, 0.5);
    vec3 b = vec3(0.5, 0.5, 0.5);
    vec3 c = vec3(1.0, 1.0, 1.0);
    vec3 d = vec3(0.263, 0.416, 0.557);
    return a + b * cos(6.28318 * (c * t + d));
}

void main() {
    // fragCoord 来自 gl_FragCoord
    vec2 fragCoord = gl_FragCoord.xy;

    // 这行等价于你 shadertoy 里的 uv 计算
    vec2 uv = (fragCoord * 2.0 - iResolution.xy) / iResolution.y;
    vec2 uv0 = uv;

    vec3 finalColor = vec3(0.0);

    // 注意：ES2 对 for 循环更挑剔，最好用 int 常量循环
    for (int ii = 0; ii < 4; ii++) {
        float i = float(ii);

        uv = fract(uv * 1.5) - 0.5;

        float d = length(uv) * exp(-length(uv0));
        vec3 col = palette(length(uv0) + i * 0.4 + iTime * 0.4);

        d = sin(d * 8.0  + iTime) / 8.0;
        d = abs(d);

        // 避免除 0（移动端很容易炸出 NaN）
        d = max(d, 1e-4);

        d = pow(0.01 / d, 1.2);

        finalColor += col * d;
    }

    gl_FragColor = vec4(finalColor, 1.0);
}
