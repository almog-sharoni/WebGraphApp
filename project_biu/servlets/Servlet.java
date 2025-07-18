package servlets;

import java.io.IOException;
import java.io.OutputStream;

import server.RequestParser.RequestInfo;

public interface Servlet {
    void handle(RequestInfo ri, OutputStream toClient) throws Exception;
    void close() throws IOException;
}
