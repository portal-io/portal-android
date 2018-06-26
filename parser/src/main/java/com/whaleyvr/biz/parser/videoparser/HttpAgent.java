package com.whaleyvr.biz.parser.videoparser;

import android.os.Message;

import com.peersless.agent.HttpAgentListener;
import com.peersless.agent.core.HttpStreamProxy;
import com.peersless.agent.core.NativeStreamProxy;
import com.peersless.agent.core.RequestInformation;
import com.peersless.agent.utils.AgentLog;
import com.peersless.agent.utils.HttpUtil;
import com.peersless.agent.utils.StringUtils;
import com.peersless.http.HTTPRequest;
import com.peersless.http.HTTPRequestListener;
import com.peersless.http.HTTPServer;
import com.peersless.videoParser.utils.MD5Util;

import java.net.URLEncoder;
import java.util.HashMap;

public class HttpAgent implements HTTPRequestListener {
    static final String TAG = "HttpAgent";

    private static HttpAgent instance = null;
    private HTTPServer mHTTPServer = null;
    private final int retryOpenCount_ = 10;
    private boolean isStart = false;

    private HashMap<String, String> headers = null;
    private String urlPrefix = null;

    private String mBindIp = "";
    private int mBindPort = 0;
    private long bufferSize = 4 * 1024 * 1024;
    private HttpAgentListener mHttpAgentListener = null;

    public void setInformationListener(HttpAgentListener listener) {
        mHttpAgentListener = listener;
    }

    public synchronized static HttpAgent getInstance() {
        if (instance == null) {
            instance = new HttpAgent();
        }
        return instance;
    }

    private HttpAgent() {
        AgentLog.i(TAG, "create HttpAgent " + this.hashCode());
        mHTTPServer = new HTTPServer(TAG);
        mHTTPServer.addRequestListener(this);
    }

    public void SetBufferSize(long size) {
        bufferSize = size;
    }

    public long GetBufferSize() {
        return bufferSize;
    }

    public void setRequestHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    // start server
    public boolean Start(String bindIp, int bindPort) {
        if (!isStart) {
            if (mHTTPServer.open(bindIp, bindPort)) {
                if (mHTTPServer.start()) {
                    isStart = true;
                    urlPrefix = "http://" + mBindIp + ":" + mBindPort + "/?Action=agent";
                    AgentLog.i(TAG, "Start At " + mBindIp + ":" + mBindPort);
                    return true;
                }
            }
        } else {
            AgentLog.i(TAG, "already Start At " + mBindIp + ":" + mBindPort);
        }

        return isStart;
    }

    // start server and auto allocate port
    public boolean Start(int bindPort) {
        for (int i = 0; i < retryOpenCount_; i++) {
            int port = bindPort + i;
            if (!isStart) {
                if (mHTTPServer.open(port)) {
                    if (mHTTPServer.start()) {
                        mBindIp = "127.0.0.1";
                        mBindPort = mHTTPServer.getBindPort();
                        isStart = true;
                        urlPrefix = "http://" + mBindIp + ":" + mBindPort + "/?Action=agent";
                        AgentLog.i(TAG, "Start At " + mBindIp + ":" + mBindPort);
                        return true;
                    }
                }
            } else {
                AgentLog.i(TAG, "already Start At " + mBindIp + ":" + mBindPort);
            }
        }

        return isStart;
    }

    // stop server
    public void Stop() {
        if (isStart) {
            mHTTPServer.stop();
            isStart = false;
        }
        AgentLog.i(TAG, "Stop");
    }

    public boolean IsOpen() {
        return isStart;
    }

    public String GetAgentAddr() {
        return "http://" + mBindIp + mBindPort;
    }

    public String generateUID(String url) {
        return MD5Util.getMD5String(url) + System.currentTimeMillis();
    }

    public boolean isSupportUrl(String url) {
        if (url == null)
            return false;
        return url.startsWith("http") || url.startsWith("/");
    }

    @SuppressWarnings("deprecation")
    public String generatePlayUrl(String sessionId, String url, String ext) {
        if (ext == null || ext.equals(""))
            return urlPrefix + "&SessionId=" + sessionId + "&url=" + URLEncoder.encode(url);
        return urlPrefix + "&&SessionId=" + sessionId + "&url=" + URLEncoder.encode(url) + "&curExt=" + ext;
    }

    @Override
    public void httpRequestRecieved(HTTPRequest httpReq) {

        String action = httpReq.getParameterValue("Action");        

        if (action.equalsIgnoreCase("agent")){
            long id = Thread.currentThread().getId();
            RequestInformation reqInfo = new RequestInformation(httpReq, headers);
            String url = reqInfo.getUrl(); 
            /*htf_20150820: @{*/
            if(url == null || "".equals(url)){
                return;
            }
            /*@}*/
            AgentLog.i(TAG, "Thread " + id + " handle " + url);
            if(url.startsWith("/")){
                new NativeStreamProxy(urlPrefix).handleRequest(reqInfo);
            }else if(url.startsWith("http")){
                String preDur = httpReq.getParameterValue("preDur");
                if(preDur.length() > 0 && mHttpAgentListener != null){
                    double t = StringUtils.StringToDouble(preDur);
                    Message msg = Message.obtain();
                    msg.what = 1028;
                    msg.arg1 =  (int) t;
                    mHttpAgentListener.onInfoUpdate(msg);
                }                
                new HttpStreamProxy(urlPrefix).handleRequest(reqInfo);
            }
        } else {
            httpReq.post(HttpUtil.EasyResponse(404, "unsupport action"));
        }
    }

    public void NotifyPlayBuffering(String SessionId, int duration) {
        AgentLog.i(TAG, "NotifyPlayBuffering");
    }

    public void NotifyPlayBufferDone(String SessionId) {
        AgentLog.i(TAG, "NotifyPlayBufferDone");
    }
}
