package com.whaley.biz.framerate.frametest;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.Surface;

import com.whaley.biz.framerate.R;
import com.whaley.biz.framerate.frametest.renderutils.Draw;
import com.whaley.biz.framerate.frametest.renderutils.RawResourceReader;
import com.whaley.biz.framerate.frametest.renderutils.ShaderHelper;
import com.whaley.biz.framerate.frametest.renderutils.SingleRect;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.glBindTexture;

/**
 * Created by caojianglong on 16-8-20.
 */
public class FrameTestImpl extends TextureSurfaceRenderer implements IFrameTest,
        SurfaceTexture.OnFrameAvailableListener, MediaPlayer.OnPreparedListener {

    private String TAG = "FrameTestImpl";
    private Context context;

    private String videoPath = "";
    private MediaPlayer mediaPlayer;
    private Surface surface;
    private boolean isPlaying = false;
    private ProgressUpdate mProgressUpdate;

    private SurfaceTexture surfaceTexture;
    private int[] textures = new int[1];
    Object lock = new Object();
    private SingleRect singleRect;
    private FloatBuffer vertexBufferSingle;
    private ShortBuffer drawListBufferSingle;
    private FloatBuffer textureBufferSingle;
    private int singleShaderProgram;
    private int width, height;

    private long openTime, currentTime;
    private int frameCount = 0;
    private float frameRate = 0f;
    private int VideoDuration = 0;

    private boolean frameAvailable = false;

    public FrameTestImpl(SurfaceTexture surfaceTexture, int width, int height) {
        super(surfaceTexture);

        this.width = width;
        this.height = height;
    }

    @Override
    public void setMediaUrl(String url) {

        if (url != null) {
            this.videoPath = url;
        } else {
            Log.e(TAG, "url不存在");
        }
    }

    @Override
    public float getMediaInfo(Context context) {

        return 30f;
//        if(videoPath == null)
//            return null;
//
//        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//        String originalFrameRate = null;
//        try {
//            mediaMetadataRetriever.setDataSource(videoPath);
//            originalFrameRate = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE);
//        } catch (IllegalArgumentException ex) {
//
//        } catch (RuntimeException ex) {
//
//        } finally {
//            try {
//                mediaMetadataRetriever.release();
//            } catch (RuntimeException ex) {
//                // Ignore failures while cleaning up.
//            }
//        }
//        return originalFrameRate;
    }

    @Override
    public  void startFrameTest(Context context) {


            try {
                synchronized (lock) {
                    initValue();

                    /**
                     * testing过程中pause之后再resume则画面补更新，这因为在onResume之后回调了onSurfaceTextureAcailable()函数，若想解决，可更新EGL中的surfaceTexture;
                     * */
                    if (this.context == null)
                        this.context = context;
                    if (videoPath == null)
                        return;

                    if (this.mediaPlayer == null) {
                        this.mediaPlayer = new MediaPlayer();
                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                /**
                                 * 1/-38：不支持的格式会报此错误，所以做相应处理
                                 * */
                                if (mProgressUpdate != null) {
                                    mProgressUpdate.onCompleted(new TestResult(false, frameRate));
                                }

                                shutTest();
                                if (isPlaying == true)
                                    isPlaying = false;

                                return false;
                            }
                        });

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {

                                if (mProgressUpdate != null) {
                                    mProgressUpdate.onCompleted(new TestResult(true, frameRate));
                                }

                                shutTest();
                                if (isPlaying == true)
                                    isPlaying = false;
                            }
                        });
                    }

                    this.surfaceTexture = new SurfaceTexture(textures[0]);
                    this.surfaceTexture.setOnFrameAvailableListener(this);
                    this.surface = new Surface(surfaceTexture);


                    if (videoPath != null)
                        mediaPlayer.setDataSource(videoPath);
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setLooping(false);

                }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (SecurityException e1) {
                e1.printStackTrace();
            } catch (IllegalStateException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();

                /**url不正确，或者不支持的视频格式*/
                if (mProgressUpdate != null) {
                    mProgressUpdate.onCompleted(new TestResult(false, frameRate));
                }

                shutTest();
                if (isPlaying == true)
                    isPlaying = false;
            }
    }

    public void initValue() {
        isPlaying = false;
        frameCount = 0;
        frameRate = 0f;
        openTime = 0;
        currentTime = 0;
        VideoDuration = 0;
    }

    @Override
    public void shutTest() {
        synchronized (lock) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            stopRender();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();
        if (isPlaying == false)
            isPlaying = true;
        VideoDuration = mp.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

        synchronized (this) {
            frameAvailable = true;
        }

        frameCount++;
        if (frameCount == 1) {
            openTime = System.currentTimeMillis();
            currentTime = openTime;
        } else {
            currentTime = System.currentTimeMillis();
        }

        frameRate = (float) frameCount * (1000f / (currentTime - openTime));
        if (mProgressUpdate != null) {
            mProgressUpdate.onProgress((int) ((currentTime - openTime) * 100 / VideoDuration));
        }
    }

    @Override
    protected boolean draw() {
        synchronized (this) {

            if (frameAvailable) {
                GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                surfaceTexture.updateTexImage();
                frameAvailable = false;

                GLES20.glViewport(0, 0, width, height);
                Draw.drawSingleWithTexture(singleShaderProgram, vertexBufferSingle, textureBufferSingle, textures, drawListBufferSingle);
                return true;
            } else {
                GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

                GLES20.glViewport(0, 0, width, height);
                /**因为纹理对象是没有变化的，所以这里去掉的纹理绑定*/
                Draw.drawSingleWithoutTexture(singleShaderProgram, vertexBufferSingle, textureBufferSingle, drawListBufferSingle);
                return true;
            }
        }
    }

    @Override
    protected void initGLComponents() {

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textures, 0);
        checkGlError("Texture generate");
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textures[0]);
        checkGlError("Texture bind");

        if (surfaceTexture == null) {
            surfaceTexture = new SurfaceTexture(textures[0]);
            surfaceTexture.setOnFrameAvailableListener(this);
        }

        singleRect = new SingleRect();
        initSingle();
    }


    @Override
    protected void deinitGLComponents() {

        GLES20.glDeleteTextures(1, textures, 0);
        GLES20.glDeleteProgram(singleShaderProgram);
        surfaceTexture.release();
        surfaceTexture.setOnFrameAvailableListener(null);
    }

    private void initSingle() {

        ByteBuffer dlb = ByteBuffer.allocateDirect(singleRect.drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBufferSingle = dlb.asShortBuffer();
        drawListBufferSingle.put(singleRect.drawOrder);
        drawListBufferSingle.position(0);

        ByteBuffer bb = ByteBuffer.allocateDirect(singleRect.squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBufferSingle = bb.asFloatBuffer();
        vertexBufferSingle.put(singleRect.squareCoords);
        vertexBufferSingle.position(0);

        ByteBuffer texturebb = ByteBuffer.allocateDirect(singleRect.textureCoords.length * 4);
        texturebb.order(ByteOrder.nativeOrder());
        textureBufferSingle = texturebb.asFloatBuffer();
        textureBufferSingle.put(singleRect.textureCoords);
        textureBufferSingle.position(0);

        final String vertexShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.normal_vetext_sharder);
        final String fragmentShader = RawResourceReader.readTextFileFromRawResource(context, R.raw.normal_fragment_sharder);

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        singleShaderProgram = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"texture", "vPosition", "vTexCoordinate", "textureTransform"});
    }

    public void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("SurfaceTest", op + ": glError " + GLUtils.getEGLErrorString(error));
        }
    }

    @Override
    public void setProgressUpdate(ProgressUpdate mProgressUpdate) {

        this.mProgressUpdate = mProgressUpdate;
    }

    public interface ProgressUpdate {

        void onProgress(int percent);

        void onCompleted(TestResult testResult);
    }
}
