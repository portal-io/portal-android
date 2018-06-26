package com.whaleyvr.biz.hybrid.model;

import java.util.List;
import java.util.Map;

/**
 * Created by YangZhi on 2016/10/13 1:37.
 */
public class DialogPayload {
    private DialogModel dialogModel;


    public void setDialogModel(DialogModel dialogModel) {
        this.dialogModel = dialogModel;
    }


    public DialogModel getDialogModel() {
        return dialogModel;
    }

    public static class DialogModel {

        private int displayType;

        private String title;

        private String msg;

        private List<Button> buttons;


        public static class Button {
            public String text;

            public String type;

            public Map<Object, Object> payload;

        }
    }
}
