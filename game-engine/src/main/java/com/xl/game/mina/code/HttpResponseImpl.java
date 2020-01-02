package com.xl.game.mina.code;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.http.api.HttpResponse;
import org.apache.mina.http.api.HttpStatus;
import org.apache.mina.http.api.HttpVersion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * http返回消息
 *
 * @author xuliang
 * @date 2019-12-20
 * QQ:2755055412
 */

@Slf4j
public class HttpResponseImpl implements HttpResponse {


    private final HashMap<String, String> headers = new HashMap<>();        //头信息

    private final HttpVersion version = HttpVersion.HTTP_1_1;

    private HttpStatus status = HttpStatus.CLIENT_ERROR_FORBIDDEN;

    private final StringBuffer bodyStringBuffer;    //内容

    private byte[] body;

    public HttpResponseImpl() {
        headers.put("Server", "HttpServer (" + "Mina 2.0.13" + ')');
        headers.put("Cache-Control", "private");
        headers.put("Content-Type", "text/html; charset=UTF-8");
        headers.put("Connection", "keep-alive");
        headers.put("Keep-Alive", "500");
        headers.put("Date", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));
        headers.put("Last-Modified", new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date()));
        status = HttpStatus.SUCCESS_OK;
        bodyStringBuffer = new StringBuffer();
    }

    public void setContentType(String contentType) {
        headers.put("Content-Type", contentType);
    }


    /**
     * 追加内容
     */
    public HttpResponseImpl appendBody(String s) {
        bodyStringBuffer.append(s);
        return this;
    }

    /**
     * body长度
     */
    public int bodyLength() {
        return bodyStringBuffer.length();
    }

    public byte[] getBody() {

        try {
            if (body == null) {
                body = bodyStringBuffer.toString().getBytes("UTF-8");
            }

        } catch (Exception ex) {
            log.error("getBody : {}", ex);
        }
        return body;

    }


    @Override
    public HttpStatus getStatus() {
        return status;
    }


    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public HttpVersion getProtocolVersion() {
        return version;
    }

    @Override
    public String getContentType() {
        return headers.get("Content-Type");
    }

    @Override
    public boolean isKeepAlive() {
        return false;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }
}
