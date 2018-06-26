package com.whaley.biz.framerate.frametest.renderutils;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


/**
 * Created by 95 on 2015/12/15.
 */
public class Draw {

	public static void drawSingleWithTexture(int singleShaderProgram,FloatBuffer vertexBufferSingle,FloatBuffer textureBufferSingle,int[] textures,ShortBuffer drawListBufferSingle)
	{
		GLES20.glUseProgram(singleShaderProgram);

		/**因为在不同模式之间切换实际上就是切换当前着色器程序和绘制代码，所以着色器的所有属性都要实时绑定。*/
		int textureParamHandle = GLES20.glGetUniformLocation(singleShaderProgram, "texture");
		int textureCoordinateHandle = GLES20.glGetAttribLocation(singleShaderProgram, "vTexCoordinate");
		int positionHandle = GLES20.glGetAttribLocation(singleShaderProgram, "vPosition");


		GLES20.glEnableVertexAttribArray(positionHandle);
		GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 4 * 3, vertexBufferSingle);
		GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
		GLES20.glVertexAttribPointer(textureCoordinateHandle, 4, GLES20.GL_FLOAT, false, 0, textureBufferSingle);


		GLES20.glBindTexture(GLES20.GL_TEXTURE0, textures[0]);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glUniform1i(textureParamHandle, 0);

		/**这里是依据索引数组绘制三角形*/
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, SingleRect.drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBufferSingle);
		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(textureCoordinateHandle);
	}

	public static void drawSingleWithoutTexture(int singleShaderProgram,FloatBuffer vertexBufferSingle,FloatBuffer textureBufferSingle,ShortBuffer drawListBufferSingle) {
		GLES20.glUseProgram(singleShaderProgram);

		/**因为这时纹理对象是没有更新的，所以不需要重新绑定纹理对象，但是顶点坐标数据是每次绘制操作都需要绑定的*/
//        int textureParamHandle = GLES20.glGetUniformLocation(singleShaderProgram, "texture");
		int textureCoordinateHandle = GLES20.glGetAttribLocation(singleShaderProgram, "vTexCoordinate");
		int positionHandle = GLES20.glGetAttribLocation(singleShaderProgram, "vPosition");

		GLES20.glEnableVertexAttribArray(positionHandle);
		GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 4 * 3, vertexBufferSingle);
		GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
		GLES20.glVertexAttribPointer(textureCoordinateHandle, 4, GLES20.GL_FLOAT, false, 0, textureBufferSingle);

//        /**这里是依据索引数组绘制三角形*/
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, SingleRect.drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBufferSingle);
		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(textureCoordinateHandle);
	}
}
