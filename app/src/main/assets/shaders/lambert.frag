precision mediump float;
uniform vec4 u_lightPos;
varying vec4 v_Normal;
varying vec4 v_Position;
varying vec4 v_Color;
void main(){
    float I = abs(dot(normalize(u_lightPos - v_Position), normalize(v_Normal)));
    gl_FragColor = I * v_Color; //输出，每个定点在屏幕上的颜色
}