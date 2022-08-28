package br.com.minecart.utilities;

public class HttpRequestException extends Exception
{
    private HttpResponse response;

    public HttpRequestException(HttpResponse response)
    {
        this.response = response;
    }

    public HttpResponse getResponse()
    {
        return this.response;
    }
}