precision mediump float;

uniform vec2 uResolution;
uniform float uTime;
varying vec2 vUv;

void main() {
    vec2 uv = (vUv * 2.0 - 1.0);
    uv.x *= uResolution.x / uResolution.y;

    float dist = length(uv);
    float angle = atan(uv.y, uv.x);

    float wave = sin(dist * 12.0 - uTime * 2.2);
    float ring = smoothstep(0.35, 0.34, abs(dist - 0.45 + wave * 0.02));
    float glow = 0.06 / (abs(dist - 0.5 + 0.03 * sin(angle * 6.0 + uTime * 1.6)) + 0.02);

    vec3 base = vec3(0.05, 0.06, 0.1);
    vec3 colorA = vec3(0.06, 0.78, 0.95);
    vec3 colorB = vec3(0.98, 0.47, 0.13);

    vec3 gradient = mix(colorA, colorB, 0.5 + 0.5 * sin(angle * 3.0 + uTime));
    vec3 color = base + gradient * (ring * 0.9 + glow * 0.7);

    gl_FragColor = vec4(color, 1.0);
}
