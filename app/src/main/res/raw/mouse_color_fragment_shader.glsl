precision mediump float;

uniform vec3 u_color;
uniform vec2 u_mouse;
uniform vec2 u_resolution;
uniform float u_time;

void main(void) {
    vec3 color = vec3(
        u_mouse.x / max(u_resolution.x, 1.0),
        0.0,
        u_mouse.y / max(u_resolution.y, 1.0)
    );
    // color = vec3((sin(u_time) + 1.0) * 0.5, 0.0, (cos(u_time) + 1.0) * 0.5);
    gl_FragColor = vec4(color, 1.0);
}
