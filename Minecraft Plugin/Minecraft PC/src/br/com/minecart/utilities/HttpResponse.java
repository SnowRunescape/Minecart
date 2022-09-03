package br.com.minecart.utilities;

public class HttpResponse {
    public int responseCode;
    public String response;

    public HttpResponse(int code, String response)
    {
        this.responseCode = code;
        this.response = response;
    }
}