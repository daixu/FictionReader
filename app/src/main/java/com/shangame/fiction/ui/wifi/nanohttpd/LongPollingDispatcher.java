package com.shangame.fiction.ui.wifi.nanohttpd;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;

import java.util.Random;

public class LongPollingDispatcher implements IDispatcher {

    @Override
    public Response handle(IHTTPSession session) {
        try {
            Thread.sleep(15000 + new Random().nextInt(1000));
        } catch (Exception e) {
        }
        return Response.newFixedLengthResponse(Status.OK, NanoHTTPD.MIME_PLAINTEXT, "");
    }
}