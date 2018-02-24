precision mediump float;
uniform mat4 u_MMatrix;
uniform mat4 u_VMatrix;
uniform mat4 u_PMatrix;
attribute vec4 a_Position;
attribute vec4 a_Color;
attribute vec4 a_Normal;

varying vec4 v_Color;
varying vec4 v_Normal;
varying vec4 v_Position;
void main(){
    gl_Position = u_PMatrix * u_VMatrix * u_MMatrix * a_Position;
    v_Color = a_Color;
    v_Normal = u_MMatrix * a_Normal;
    v_Position = u_MMatrix * a_Position;
}