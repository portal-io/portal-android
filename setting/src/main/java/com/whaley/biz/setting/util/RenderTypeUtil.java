package com.whaley.biz.setting.util;

import android.text.TextUtils;

import com.whaley.biz.setting.ServerRenderType;
import com.whaley.biz.setting.VideoConstantValue;

/**
 * Created by dell on 2017/9/20.
 */

public class RenderTypeUtil {
    public static int getRenderTypeByRenderTypeStr(String renderTypeStr) {
        int renderType = -1;
        if (!TextUtils.isEmpty(renderTypeStr)) {
            switch (renderTypeStr) {
                case ServerRenderType.RENDER_TYPE_360_2D:
                    renderType = VideoConstantValue.MODE_SPHERE;
                    break;
                case ServerRenderType.RENDER_TYPE_360_2D_OCTAHEDRAL:
                    renderType = VideoConstantValue.MODE_OCTAHEDRON;
                    break;
                case ServerRenderType.RENDER_TYPE_PLANE_2D:
                    renderType = VideoConstantValue.MODE_RECTANGLE;
                    break;
                case ServerRenderType.RENDER_TYPE_PLANE_3D_LR:
                    renderType = VideoConstantValue.MODE_RECTANGLE_STEREO;
                    break;
                case ServerRenderType.RENDER_TYPE_PLANE_3D_UD:
                    renderType = VideoConstantValue.MODE_RECTANGLE_STEREO_TD;
                    break;
                case ServerRenderType.RENDER_TYPE_360_3D_LF:
                    renderType = VideoConstantValue.MODE_SPHERE_STEREO_LR;
                    break;
                case ServerRenderType.RENDER_TYPE_360_3D_UD:
                    renderType = VideoConstantValue.MODE_SPHERE_STEREO_TD;
                    break;
                case ServerRenderType.RENDER_TYPE_180_PLANE:
                    renderType = VideoConstantValue.MODE_HALF_SPHERE;
                    break;
                case ServerRenderType.RENDER_TYPE_180_3D_UD:
                    renderType = VideoConstantValue.MODE_HALF_SPHERE_TD;
                    break;
                case ServerRenderType.RENDER_TYPE_180_3D_LF:
                    renderType = VideoConstantValue.MODE_HALF_SPHERE_LR;
                    break;
                case ServerRenderType.RENDER_TYPE_360_OCT_3D:
                    renderType = VideoConstantValue.MODE_OCTAHEDRON_STEREO_LR;
                    break;
                case ServerRenderType.RENDER_TYPE_180_OCT_3D:
                    renderType = VideoConstantValue.MODE_OCTAHEDRON_HALF_STEREO_LR;
                    break;
            }
        }
        return renderType;
    }
}
