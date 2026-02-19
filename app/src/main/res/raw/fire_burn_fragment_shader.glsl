precision mediump float;

uniform vec2 iResolution;
uniform float iTime;

float hash(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));

    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i = 0; i < 5; i++) {
        v += a * noise(p);
        p = p * 2.02 + vec2(13.1, 7.3);
        a *= 0.5;
    }
    return v;
}

void mainImage(out vec4 fragColor, in vec2 fragCoord) {
    vec2 uv = fragCoord / iResolution.xy;
    vec2 p = uv * 2.0 - 1.0;
    p.x *= iResolution.x / iResolution.y;

    float t = iTime * 1.2;

    float n1 = fbm(vec2(p.x * 2.3, p.y * 3.0 - t * 2.0));
    float n2 = fbm(vec2(p.x * 4.0 + 2.0, p.y * 6.5 - t * 3.2));
    float flow = mix(n1, n2, 0.45);

    float shape = 1.0 - smoothstep(-0.9, 0.8, p.y);
    shape *= 1.0 - smoothstep(0.15, 1.1, abs(p.x) + p.y * 0.35);

    float flame = smoothstep(0.28, 0.9, flow + shape * 0.95);
    flame *= shape;

    vec3 colLow = vec3(0.9, 0.15, 0.02);
    vec3 colMid = vec3(1.0, 0.5, 0.06);
    vec3 colHigh = vec3(1.0, 0.92, 0.55);

    vec3 color = mix(colLow, colMid, flame);
    color = mix(color, colHigh, pow(flame, 2.2));

    float core = smoothstep(0.65, 1.0, flame) * (1.0 - uv.y);
    color += vec3(1.0, 0.65, 0.15) * core * 0.85;

    float smoke = smoothstep(0.35, 1.1, p.y + flow * 0.25) * (1.0 - flame);
    color = mix(color, vec3(0.08, 0.08, 0.09), smoke * 0.3);

    fragColor = vec4(color, 1.0);
}

void main() {
    vec4 color = vec4(0.0);
    mainImage(color, gl_FragCoord.xy);
    gl_FragColor = color;
}

