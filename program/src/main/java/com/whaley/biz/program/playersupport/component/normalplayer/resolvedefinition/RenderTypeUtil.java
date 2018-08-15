package com.whaley.biz.program.playersupport.component.normalplayer.resolvedefinition;

import android.text.TextUtils;

import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.constants.ServerRenderType;
import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.wvrplayer.vrplayer.external.event.VideoConstantValue;


/**
 * Created by YangZhi on 2017/4/25 21:01.
 */

public class RenderTypeUtil {

    public static int getRenderType(String renderTypeStr,int type,int definition) {
        int renderType;
        if (type == PlayerType.TYPE_MORETV_2D || type == PlayerType.TYPE_MORETV_TV) {
            renderType = VideoConstantValue.MODE_RECTANGLE;
        } else if (definition == VideoBitType.SD || definition == VideoBitType.SDA || definition == VideoBitType.SDB ) {
            renderType = VideoConstantValue.MODE_OCTAHEDRON;
        } else {
            renderType = getRenderTypeByRenderTypeStr(renderTypeStr);
        }
        if (renderType == -1) {
            switch (type) {
                case PlayerType.TYPE_3D:
                    renderType = VideoConstantValue.MODE_RECTANGLE_STEREO;
                    break;
                case PlayerType.TYPE_LIVE:
                    renderType = VideoConstantValue.MODE_SPHERE;
                    break;
                default:
                    renderType = VideoConstantValue.MODE_SPHERE;
                    break;
            }
        }
        return renderType;
    }


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

    public static String getRenderTypeStrByRenderType(int renderType) {
        String renderTypeStr = "";
        switch (renderType) {
            case VideoConstantValue.MODE_SPHERE:
                renderTypeStr = ServerRenderType.RENDER_TYPE_360_2D;
                break;
            case VideoConstantValue.MODE_OCTAHEDRON:
                renderTypeStr = ServerRenderType.RENDER_TYPE_360_2D_OCTAHEDRAL;
                break;
            case VideoConstantValue.MODE_RECTANGLE:
                renderTypeStr = ServerRenderType.RENDER_TYPE_PLANE_2D;
                break;
            case VideoConstantValue.MODE_RECTANGLE_STEREO:
                renderTypeStr = ServerRenderType.RENDER_TYPE_PLANE_3D_LR;
                break;
            case VideoConstantValue.MODE_RECTANGLE_STEREO_TD:
                renderTypeStr = ServerRenderType.RENDER_TYPE_PLANE_3D_UD;
                break;
            case VideoConstantValue.MODE_SPHERE_STEREO_LR:
                renderTypeStr = ServerRenderType.RENDER_TYPE_360_3D_LF;
                break;
            case VideoConstantValue.MODE_SPHERE_STEREO_TD:
                renderTypeStr = ServerRenderType.RENDER_TYPE_360_3D_UD;
                break;
            case VideoConstantValue.MODE_HALF_SPHERE:
                renderTypeStr = ServerRenderType.RENDER_TYPE_180_PLANE;
                break;
            case VideoConstantValue.MODE_HALF_SPHERE_TD:
                renderTypeStr = ServerRenderType.RENDER_TYPE_180_3D_UD;
                break;
            case VideoConstantValue.MODE_HALF_SPHERE_LR:
                renderTypeStr = ServerRenderType.RENDER_TYPE_180_3D_LF;
                break;
            case VideoConstantValue.MODE_OCTAHEDRON_STEREO_LR:
                renderTypeStr = ServerRenderType.RENDER_TYPE_360_OCT_3D;
                break;
            case VideoConstantValue.MODE_OCTAHEDRON_HALF_STEREO_LR:
                renderTypeStr = ServerRenderType.RENDER_TYPE_180_OCT_3D;
                break;
            }
        return renderTypeStr;
    }

}
