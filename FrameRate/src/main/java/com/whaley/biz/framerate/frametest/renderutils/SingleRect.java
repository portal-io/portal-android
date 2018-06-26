package com.whaley.biz.framerate.frametest.renderutils;

/**
 * Created by 95 on 2015/11/29.
 */
public class SingleRect {

	public static short drawOrder[] = { 0, 1, 2, 0, 2, 3};//顶点索引数组
	public float squareCoords[];
	public float textureCoords[];

	public SingleRect() {
			squareCoords = new float[]{
					-1f,    1f,    0.0f,     //左上
					-1f,   -1f,    0.0f,     //左下
					1f,    -1f,    0.0f,     //右下
					1f,     1f,    0.0f};   //右上

			textureCoords = new float[]{
					0.0f, 0.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					1.0f, 1.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f};
		}

	}
