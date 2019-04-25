package com.tany.base.net;


import com.google.gson.annotations.SerializedName;

public class BaseEntity<T> {
    public Head head;
    @SerializedName("body")
    public T data;
    public static class Head {
        @SerializedName("respCode")
        private String respCode;
        @SerializedName("respContent")
        private String respContent;

        public String getRespCode() {
            return respCode;
        }

        public void setRespCode(String respCode) {
            this.respCode = respCode;
        }

        public String getRespContent() {
            return respContent;
        }

        public void setRespContent(String respContent) {
            this.respContent = respContent;
        }
    }
}
