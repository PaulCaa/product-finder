package ar.com.pablocaamano.product.finder.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author Pablo Caamaño
 */
public class RestConnector {

    private static RestConnector instance;
    private Integer statusCode;
    private String message;

    private RestConnector() {
        this.statusCode = 0;
        this.message = "";
    }

    public static RestConnector getInstance() {
        if (instance == null) {
            instance = new RestConnector();
        }
        return instance;
    }

    public Object get(String url, List<String> params) {
        if (url == null || url.length() == 0) {
            System.out.println("url vacía...");
            return null;
        }
        return execute(url,params);
    }

    private Object execute(String u, List<String> params) {
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept", "*/*");
            if(params != null && !params.isEmpty()) {
                int cant = params.size();
                String key = "";
                String value = "";
                for (int i = 0; i < cant; i++) {
                    key = params.get(i);
                    i++;
                    value = params.get(i);
                    i++;
                    conn.setRequestProperty(key,value);
                }

            }
            System.out.println("calling: " + url.getHost() + url.getPath());
            conn.connect();
            BufferedInputStream bs;
            this.statusCode = conn.getResponseCode();
            message = conn.getResponseMessage();
            if (statusCode != 200) {
                bs = new BufferedInputStream(conn.getErrorStream());
            } else {
                bs = new BufferedInputStream(conn.getInputStream());
            }
            System.out.println("mapeando respuesta...");
            ObjectMapper mapper = new ObjectMapper();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] buff = new byte[8192];
            int len = 0;
            int contentLength = conn.getContentLength();
            if(contentLength == -1){
                while((len = bs.read(buff)) != -1)
                    buffer.write(buff, 0, len);
            }
            int readLen = 0;
            while((len = bs.read(buff)) != -1){
                buffer.write(buff, 0, len);
                readLen += len;
                if(readLen >= contentLength)
                    break;
            }
            return mapper.readValue(buffer.toByteArray(), Object.class);
        } catch(Exception exception) {
            System.out.println("se produjo un error al ejecutar request: " + exception.getMessage());
            this.statusCode = 500;
            this.message = "se produjo un error al ejecutar request: " + exception.getMessage();
            return null;
        }
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
