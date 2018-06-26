package com.whaley.biz.launcher.util;

import com.snailvr.manager.R;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.image.ImageSize;

/**
 * Created by YangZhi on 2017/2/7 18:25.
 */

public class ImageProvider {
    private static ImageRequest.ImageSizeGetter imageSizeGetter;

    public static final String STR_SPLIT_ZOOM="zoom/";
    public static final String STR_SPLIT_LINE="/";

    private static final int DEFAULT_PLACEHOLDER_RESID= R.drawable.bg_placeholder_color_shape;

    private static final int DEFAULT_ERROR_RESID=R.drawable.bg_placeholder_color_shape;

    public static int getPlacHolderResId(){
        return DEFAULT_PLACEHOLDER_RESID;
    }

    public static int getErrorResId(){
        return DEFAULT_ERROR_RESID;
    }

    public static ImageRequest.ImageSizeGetter createImageSizeGetter(){
        if(imageSizeGetter==null)
            imageSizeGetter = new ImageRequest.ImageSizeGetter() {
                @Override
                public ImageSize getImageSize(String url, int maxSize) {
                    int[] scaleSize = new int[]{maxSize, maxSize};
                    boolean isMax = true;
                    if (url.contains(STR_SPLIT_ZOOM)) {
                        int[] size = new int[2];
                        String[] str = url.split(STR_SPLIT_ZOOM);
                        if (str.length > 1) {
                            String[] s = str[1].split(STR_SPLIT_LINE);
                            if (s.length > 1) {
                                try {
                                    size[0] = Integer.valueOf(s[0]);
                                    size[1] = Integer.valueOf(s[1]);
                                    scaleSize = getScaleSize(size, maxSize);
                                    url = url.replace(str[1], scaleSize[0] + "/" + scaleSize[1]);
                                    isMax = false;
                                }catch (NumberFormatException e){

                                }
                            }
                        }
                    }
                    ImageSize imageSize = new ImageSize();
                    imageSize.setUrl(url);
                    imageSize.setWidth(scaleSize[0]);
                    imageSize.setHeight(scaleSize[1]);
                    imageSize.setMax(isMax);
                    return imageSize;
                }
            };
        return imageSizeGetter;
    }


    private static int[] getScaleSize(int[] sizeArr, int maxScaleSize) {
        try {
            if (sizeArr != null && sizeArr.length == 2 && sizeArr[0] > 0 && sizeArr[1] > 0) {
                int maxSize;
                if (sizeArr[0] > sizeArr[1]) {
                    maxSize = sizeArr[0];
                    if (maxSize > maxScaleSize) {
                        float scale = 1f * maxSize / maxScaleSize;
                        sizeArr[0] = maxScaleSize;
                        sizeArr[1] = (int) (sizeArr[1] / scale);
                    }
                } else {
                    maxSize = sizeArr[1];
                    if (maxSize > maxScaleSize) {
                        float scale = 1f * maxSize / maxScaleSize;
                        sizeArr[1] = maxScaleSize;
                        sizeArr[0] = (int) (sizeArr[0] / scale);
                    }
                }

            } else {
                sizeArr[0] = maxScaleSize;
                sizeArr[1] = maxScaleSize;
            }
            return sizeArr;
        } catch (Exception e) {
//            Log.e(e.getMessage() + ",sizeArr=" + (sizeArr == null ? null : sizeArr.toString()) + ",maxScaleSize=" + maxScaleSize);
            sizeArr = new int[]{maxScaleSize, maxScaleSize};
            return sizeArr;
        }
    }
}
