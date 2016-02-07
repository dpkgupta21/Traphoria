package com.app.traphoria.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CustomJsonImageRequest extends Request<JSONObject> {

    public static final String KEY_PICTURE = "image";
    private Listener<JSONObject> listener;
    private Map<String, String> params;
    private File file;
    // private MultipartEntity entity = new MultipartEntity();
    private HttpEntity mHttpEntity;

    public CustomJsonImageRequest(String url, Map<String, String> params,
                                  Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;

    }

    public CustomJsonImageRequest(int method, String url, Map<String, String> params,
                                  Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;

    }


    public CustomJsonImageRequest(int method, String url,
                                  Map<String, String> params,
                                  File file,
                                  Listener<JSONObject> reponseListener,
                                  ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
        this.file = file;
        mHttpEntity = buildMultipartEntity(file);
        //buildMultipartEntity();
    }

    private HttpEntity buildMultipartEntity(File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (file != null) {
            String fileName = file.getName();

            FileBody fileBody = new FileBody(file);
            builder.addPart(KEY_PICTURE, fileBody);
        }
        try {
            for (String key : params.keySet())
                builder.addPart(key, new StringBody(params.get(key)));

        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
        return builder.build();
    }

//    private void buildMultipartEntity() {
//        entity.addPart("image", new FileBody(file));
//
//        try {
//            for (String key : params.keySet())
//                entity.addPart(key, new StringBody(params.get(key)));
//
//        } catch (UnsupportedEncodingException e) {
//            VolleyLog.e("UnsupportedEncodingException");
//        }
//    }


    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws com.android.volley.AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }

}
