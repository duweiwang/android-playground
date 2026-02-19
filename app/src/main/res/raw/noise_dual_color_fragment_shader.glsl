precision highp float;

uniform vec2 u_resolution;
uniform vec2 u_mouse;
uniform float u_time;
uniform vec3 u_color_a;
uniform vec3 u_color_b;

varying vec2 vUv;

float random(vec2 st) {
    return fract(sin(dot(st, vec2(12.9898, 78.233))) * 43758.5453123);
}

float noise(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    vec2 u = f * f * (3.0 - 2.0 * f);

    return mix(a, b, u.x) +
        (c - a) * u.y * (1.0 - u.x) +
        (d - b) * u.x * u.y;
}

void main() {
    vec2 uv = vUv;
    float aspect = u_resolution.x / max(u_resolution.y, 1.0);
    if (aspect >= 1.0) {
        // Match the orthographic camera crop in the Three.js sample (landscape).
        uv.y = (uv.y - 0.5) / aspect + 0.5;
    } else {
        // Match the orthographic camera crop in the Three.js sample (portrait).
        uv.x = (uv.x - 0.5) * aspect + 0.5;
    }

    vec2 n = vec2(0.0);
    vec2 pos;

    pos = vec2(uv.x * 1.4 + 0.01, uv.y - u_time * 0.69);
    n.x = noise(pos * 12.0);
    pos = vec2(uv.x * 0.5 - 0.033, uv.y * 2.0 - u_time * 0.12);
    n.x += noise(pos * 8.0);
    pos = vec2(uv.x * 0.94 + 0.02, uv.y * 3.0 - u_time * 0.61);
    n.x += noise(pos * 4.0);

    pos = vec2(uv.x * 0.7 - 0.01, uv.y - u_time * 0.27);
    n.y = noise(pos * 12.0);
    pos = vec2(uv.x * 0.45 + 0.033, uv.y * 1.9 - u_time * 0.61);
    n.y += noise(pos * 8.0);
    pos = vec2(uv.x * 0.8 - 0.02, uv.y * 2.5 - u_time * 0.51);
    n.y += noise(pos * 4.0);

    n /= 2.3;

    vec3 color = mix(u_color_a, u_color_b, n.y * n.x);
    gl_FragColor = vec4(color, 1.0);
}
