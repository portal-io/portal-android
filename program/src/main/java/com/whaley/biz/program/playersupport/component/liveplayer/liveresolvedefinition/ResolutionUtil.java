package com.whaley.biz.program.playersupport.component.liveplayer.liveresolvedefinition;

import com.whaley.biz.program.constants.VideoBitType;
import com.whaley.biz.program.model.LiveMediaModel;

/**
 * Created by dell on 2017/2/24.
 */

public class ResolutionUtil {

    public static int getVideoBitTypeFromResolution(String resolution){
        if(resolution.equalsIgnoreCase("8p")){
            return VideoBitType.SD;
        }else if(resolution.equalsIgnoreCase("4k")){
            return VideoBitType.HD;
        }else{
            return VideoBitType.ST;
        }
    }

}
