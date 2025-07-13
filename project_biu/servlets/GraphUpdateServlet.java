package servlets;

import graph.*;
import views.HtmlGraphWriter;
import server.RequestParser.RequestInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * GraphUpdateServlet generates real-time visual graph updates using a template-based approach.
 * 
 * This servlet:
 * 1. Loads the graph.html template file
 * 2. Creates a graph from current topics and agents
 * 3. Updates graph nodes with current topic values and agent results
 * 4. Replaces template placeholders with current SVG and info content
 * 5. Returns the populated HTML template
 */
public class GraphUpdateServlet implements Servlet {

    private static final String SVG_PLACEHOLDER = "{{SVG_CONTENT}}";
    private static final String INFO_PLACEHOLDER = "{{GRAPH_INFO}}";

    @Override
    public void handle(RequestInfo ri, OutputStream toClient) throws Exception {
        // Create graph from current topics
        Graph graph = new Graph();
        graph.createFromTopics();
        
        // Update nodes with current topic values and agent states
        updateNodesWithCurrentValues(graph);
        
        // Load template and generate response
        generateTemplateBasedResponse(toClient, graph);
    }

    /**
     * Updates graph nodes with current topic values and calculated agent results.
     * This ensures the visual graph reflects the real-time state of the system.
     */
    private void updateNodesWithCurrentValues(Graph graph) {
        for (Node node : graph) {
            String nodeName = node.getName();
            
            if (nodeName.startsWith("T")) {
                // Topic node - update with current topic value
                String topicName = nodeName.substring(1); // Remove "T" prefix
                Topic topic = TopicManagerSingleton.get().getTopic(topicName);
                if (topic != null && topic.getResult() != null && !topic.getResult().isEmpty()) {
                    // Create a simple message that displays the actual value
                    Message valueMessage = new Message(topic.getResult());
                    node.setMsg(valueMessage);
                }
            } else if (nodeName.startsWith("A")) {
                // Agent node - update with calculated result if available
                String agentName = nodeName.substring(1); // Remove "A" prefix
                updateAgentNodeWithResult(node, agentName);
            }
        }
    }

    /**
     * Updates an agent node with its calculated result by checking output topics.
     * This allows the graph to show agent computation results visually.
     */
    private void updateAgentNodeWithResult(Node node, String agentName) {
        // Find agents with this name and check their output topics
        for (Topic topic : TopicManagerSingleton.get().getTopics()) {
            for (Agent publisher : topic.getPublishers()) {
                if (publisher.getName().equals(agentName)) {
                    // This agent publishes to this topic - use topic's result as agent's output
                    String result = topic.getResult();
                    if (result != null && !result.isEmpty()) {
                        // Create a message that shows the result value properly
                        Message resultMessage = new Message("→ " + result);
                        node.setMsg(resultMessage);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Loads the HTML template and replaces placeholders with current graph data.
     * This approach is more efficient than generating the entire HTML structure each time.
     */
    private void generateTemplateBasedResponse(OutputStream toClient, Graph graph) throws IOException {
        PrintWriter writer = new PrintWriter(toClient);
        
        try {
            // Load the HTML template
            String template = loadTemplate();
            
            // Generate SVG content
            String svgContent = generateSvgContent(graph);
            
            // Generate graph info content
            String graphInfo = generateGraphInfo(graph);
            
            // Replace placeholders in template
            String finalHtml = template
                .replace(SVG_PLACEHOLDER, svgContent)
                .replace(INFO_PLACEHOLDER, graphInfo);
            
            // Send HTTP headers
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html; charset=UTF-8");
            writer.println("Connection: close");
            writer.println(); // Empty line to separate headers from body
            
            // Send the populated template
            writer.print(finalHtml);
            
        } catch (Exception e) {
            // If any error occurs, send error response
            sendErrorResponse(writer, "Error generating graph: " + e.getMessage());
            e.printStackTrace(); // Debug: print stack trace to console
        }
        
        writer.flush();
    }

    /**
     * Loads the HTML template from the file system.
     * Tries multiple possible paths to find the template file.
     */
    private String loadTemplate() throws IOException {
        // Try multiple possible paths for the template file
        String[] possiblePaths = {
            "../html_files/graph.html",           // From project_biu directory
            "html_files/graph.html",              // From app_main directory  
            "../../html_files/graph.html",        // From deeper nested directory
            "/Users/lmwgsrwny/Library/Mobile Documents/com~apple~CloudDocs/Desktop/לימודים/תואר שני/קורסים/Advanced Programming/app_main/html_files/graph.html" // Absolute path as fallback
        };
        
        // Debug: log current working directory
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        
        for (String path : possiblePaths) {
            try {
                System.out.println("Trying template path: " + path);
                if (Files.exists(Paths.get(path))) {
                    System.out.println("Found template at: " + path);
                    return new String(Files.readAllBytes(Paths.get(path)));
                }
            } catch (Exception e) {
                System.out.println("Failed to load from path " + path + ": " + e.getMessage());
                // Continue to next path
            }
        }
        
        System.out.println("Could not find template file, using inline template");
        // If all paths fail, return a simple inline template
        return getInlineTemplate();
    }

    /**
     * Returns an inline HTML template as a fallback when the external template file cannot be loaded.
     */
    private String getInlineTemplate() {
        return "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head>\n" +
               "    <title>Real-time Computation Graph</title>\n" +
               "    <style>\n" +
               "        body { font-family: Arial, sans-serif; background: #f5f5f5; margin: 0; padding: 20px; }\n" +
               "        .graph-container { background: white; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); padding: 20px; }\n" +
               "        .graph-title { text-align: center; color: #333; margin-bottom: 20px; font-size: 24px; font-weight: bold; }\n" +
               "        .graph-canvas { border: 2px solid #ddd; border-radius: 8px; background: #fafafa; width: 100%; height: 600px; position: relative; display: flex; justify-content: center; align-items: center; }\n" +
               "        svg { width: 800px; height: 600px; max-width: 100%; max-height: 100%; }\n" +
               "        .topic-node { fill: #4ECDC4; stroke: #26A69A; stroke-width: 2; }\n" +
               "        .topic-text { fill: white; font-weight: bold; font-size: 12px; }\n" +
               "        .agent-node { fill: #FF6B6B; stroke: #E57373; stroke-width: 2; }\n" +
               "        .agent-text { fill: white; font-weight: bold; font-size: 12px; }\n" +
               "        .value-text { fill: #0066CC; font-weight: bold; font-size: 16px; }\n" +
               "        .info-panel { background: #f8f9fa; border: 1px solid #dee2e6; border-radius: 5px; padding: 15px; margin-top: 20px; }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class='graph-container'>\n" +
               "        <div class='graph-title'>🔗 Real-time Computation Graph</div>\n" +
               "        <div class='graph-canvas'>\n" +
               "            <svg viewBox='0 0 800 600' width='800' height='600'>\n" +
               "                {{SVG_CONTENT}}\n" +
               "            </svg>\n" +
               "        </div>\n" +
               "        <div class=\"info-panel\">\n" +
               "            <h4>📋 Graph Information</h4>\n" +
               "            <div id=\"graphInfo\">\n" +
               "                {{GRAPH_INFO}}\n" +
               "            </div>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "    <script>\n" +
               "        console.log('Real-time computation graph loaded (inline template)');\n" +
               "    </script>\n" +
               "</body>\n" +
               "</html>";
    }

    /**
     * Generates SVG content using HtmlGraphWriter and joins it into a single string.
     */
    private String generateSvgContent(Graph graph) {
        StringBuilder svgBuilder = new StringBuilder();
        for (String svgLine : HtmlGraphWriter.getGraphSVG(graph)) {
            svgBuilder.append(svgLine).append("\n");
        }
        return svgBuilder.toString();
    }

    /**
     * Generates graph information content for the info panel.
     */
    private String generateGraphInfo(Graph graph) {
        StringBuilder info = new StringBuilder();
        
        int nodeCount = graph.size();
        int topicCount = 0;
        int agentCount = 0;
        
        for (Node node : graph) {
            if (node.getName().startsWith("T")) {
                topicCount++;
            } else if (node.getName().startsWith("A")) {
                agentCount++;
            }
        }
        
        info.append("<p><strong>Graph Statistics:</strong></p>");
        info.append("<ul>");
        info.append("<li>Total Nodes: ").append(nodeCount).append("</li>");
        info.append("<li>Topics: ").append(topicCount).append("</li>");
        info.append("<li>Agents: ").append(agentCount).append("</li>");
        info.append("</ul>");
        
        if (nodeCount == 0) {
            info.append("<p>No computation graph available. Upload a configuration to see the graph.</p>");
        } else {
            info.append("<p>Graph shows real-time computation flow with current topic values.</p>");
            info.append("<p><strong>Legend:</strong></p>");
            info.append("<ul>");
            info.append("<li><span style='color: #4ECDC4; font-weight: bold;'>■</span> Topics (rectangles)</li>");
            info.append("<li><span style='color: #FF6B6B; font-weight: bold;'>■</span> Agents (circles)</li>");
            info.append("</ul>");
        }
        
        return info.toString();
    }

    /**
     * Sends an error response when template loading or processing fails.
     */
    private void sendErrorResponse(PrintWriter writer, String errorMessage) {
        writer.println("HTTP/1.1 500 Internal Server Error");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println("Connection: close");
        writer.println();
        
        writer.println("<!DOCTYPE html>");
        writer.println("<html><head><title>Graph Error</title></head>");
        writer.println("<body>");
        writer.println("<h1>Graph Loading Error</h1>");
        writer.println("<p>" + errorMessage + "</p>");
        writer.println("</body></html>");
    }

    @Override
    public void close() throws IOException {
        // No resources to clean up for this servlet
    }
}
