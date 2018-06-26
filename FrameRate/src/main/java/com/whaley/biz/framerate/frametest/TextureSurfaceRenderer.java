package com.whaley.biz.framerate.frametest;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * Created by caojianglong on 16-8-20.
 */
public abstract class TextureSurfaceRenderer implements Runnable{
    public static String LOG_TAG = TextureSurfaceRenderer.class.getSimpleName();

    protected final SurfaceTexture surfaceTexture;

    private EGL10 egl;
    private EGLContext eglContext;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface;
    private boolean running = false;

    public TextureSurfaceRenderer(SurfaceTexture surfaceTexture) {

        this.running = true;
        this.surfaceTexture = surfaceTexture;

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        initEGL();
        initGLComponents();
        Log.e(LOG_TAG, "EGL/ES初始化完毕！");

        while (running) {
            if (draw()) {
                egl.eglSwapBuffers(eglDisplay, eglSurface);

                try {
                    Thread.sleep(12);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        deinitGLComponents();
        deinitEGL();
    }

    protected abstract void initGLComponents();
    protected abstract void deinitGLComponents();

    protected abstract boolean draw();
    public void stopRender()
    {
        running = false;
    }


    private void initEGL() {

        egl = (EGL10)EGLContext.getEGL();
        eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int version[] = new int[2];
        egl.eglInitialize(eglDisplay, version);
        EGLConfig eglConfig = chooseEglConfig();
        eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceTexture, null);
        eglContext = createContext(egl, eglDisplay, eglConfig);

        try {
            if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
                throw new RuntimeException("GL error:" + GLUtils.getEGLErrorString(egl.eglGetError()));
            }
            if (!egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
                throw new RuntimeException("GL Make current Error"+ GLUtils.getEGLErrorString(egl.eglGetError()));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deinitEGL() {
        egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(eglDisplay, eglSurface);
        egl.eglDestroyContext(eglDisplay, eglContext);
        egl.eglTerminate(eglDisplay);
        Log.e(LOG_TAG, "关闭EGL");
    }

    private EGLContext createContext(EGL10 egl, EGLDisplay eglDisplay, EGLConfig eglConfig) {
        int[] attrs = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,//CLIENT代表的就是cpu端（server表示gpu端），也就是OpenGl_ES，表明其版本为2.0
                EGL10.EGL_NONE
        };
        return egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrs);
    }

    private EGLConfig chooseEglConfig() {
        int[] configsCount = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        int[] attributes = getAttributes();
        int confSize = 1;

        if (!egl.eglChooseConfig(eglDisplay, attributes, configs, confSize, configsCount)) {    //获取满足attributes的config个数
            throw new IllegalArgumentException("Failed to choose config:"+ GLUtils.getEGLErrorString(egl.eglGetError()));
        }
        else if (configsCount[0] > 0) {
            return configs[0];
        }

        return null;
    }

    private int[] getAttributes()
    {
        return new int[] {
                EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,  //指定渲染api类别
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 0,
                EGL10.EGL_STENCIL_SIZE, 0,
                EGL10.EGL_NONE//总是以EGL10.EGL_NONE结尾
        };
    }

    @Override
    protected  void finalize() throws Throwable {
        super.finalize();
        running = false;
    }
}
