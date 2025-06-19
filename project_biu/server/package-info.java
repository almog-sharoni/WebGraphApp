/**
 * Provides HTTP server functionality and request parsing utilities.
 * 
 * <p>This package contains the core components for building a multi-threaded HTTP server
 * that can handle concurrent requests and route them to appropriate servlets based on
 * HTTP methods and URI patterns.</p>
 * 
 * <h2>Key Components:</h2>
 * <dl>
 * <dt>{@link server.HTTPServer}</dt>
 * <dd>Interface defining the contract for HTTP server implementations</dd>
 * 
 * <dt>{@link server.MyHTTPServer}</dt>
 * <dd>Concrete implementation of HTTPServer with thread pool support and servlet routing</dd>
 * 
 * <dt>{@link server.RequestParser}</dt>
 * <dd>Utility class for parsing HTTP requests and extracting request components</dd>
 * 
 * <dt>{@link server.RequestParser.RequestInfo}</dt>
 * <dd>Data class encapsulating parsed HTTP request information</dd>
 * </dl>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * {@code
 * // Create and configure server
 * HTTPServer server = new MyHTTPServer(8080, 10);
 * server.addServlet("GET", "/api", new ApiServlet());
 * server.addServlet("POST", "/upload", new UploadServlet());
 * 
 * // Start server
 * server.start();
 * 
 * // ... handle requests ...
 * 
 * // Shutdown server
 * server.close();
 * }
 * </pre>
 * 
 * <h2>Thread Safety:</h2>
 * <p>All classes in this package are designed to be thread-safe for concurrent use.
 * The {@link server.MyHTTPServer} uses a thread pool to handle multiple requests
 * simultaneously while maintaining thread safety through proper synchronization.</p>
 * 
 * <h2>Request Flow:</h2>
 * <ol>
 * <li>Client connects to server</li>
 * <li>{@link server.RequestParser} parses the HTTP request</li>
 * <li>Server matches URI to registered servlet using longest-prefix matching</li>
 * <li>Request is delegated to appropriate servlet for processing</li>
 * <li>Response is sent back to client</li>
 * <li>Connection is closed</li>
 * </ol>
 * 
 * @author Almog Sharoni Yuval Harary
 * @version 1.0
 * @since 1.0
 */
package server;
