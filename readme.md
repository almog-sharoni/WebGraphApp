# Computation Graph System

A Java-based computation graph system with an interactive web interface for creating, managing, and visualizing computational workflows using agent-based processing nodes.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [How to Run](#how-to-run)
- [Usage](#usage)
- [Configuration Files](#configuration-files)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Development](#development)

## 🔍 Overview

This project implements a distributed computation graph system where:
- **Agents** are computational units that process data
- **Topics** are message channels for communication between agents
- **Graphs** represent the computational workflow
- **Web Interface** provides real-time visualization and interaction

The system supports various types of agents including mathematical operations (Plus, Multiply, Min, Max, Average), binary operations, and parallel processing agents.

## ✨ Features

- **Agent-Based Computing**: Modular computational units with different specializations
- **Topic-Based Messaging**: Publish/subscribe pattern for agent communication
- **Real-time Web Interface**: Interactive HTML interface for graph visualization
- **Configuration Management**: File-based configuration system for defining computational workflows
- **HTTP Server**: Built-in web server with servlet-based architecture
- **Graph Visualization**: Dynamic graph rendering with real-time updates
- **Parallel Processing**: Support for concurrent agent execution

## 🏗️ Architecture

### Core Components

1. **Graph Package** (`graph/`)
   - `Agent`: Base class for all computational agents
   - `Topic`: Message channel implementation
   - `TopicManagerSingleton`: Centralized topic management
   - `Graph`: Graph representation and cycle detection
   - Various specialized agents (PlusAgent, MaxAgent, etc.)

2. **Server Package** (`server/`)
   - HTTP server implementation
   - Request/response handling

3. **Servlets Package** (`servlets/`)
   - Web request handlers
   - Configuration loaders
   - Graph update services

4. **Configuration System** (`configs/`)
   - Configuration file parsing
   - Agent instantiation from configuration

## 🛠️ Prerequisites

- **Java**: JDK 8 or higher
- **Operating System**: macOS, Linux, or Windows
- **Web Browser**: Any modern browser for the web interface
- **Terminal/Command Line**: For running the startup script

## 📦 Installation & Setup

1. **Clone/Download the Project**
   ```bash
   # If using git
   git clone <repository-url>
   
   # Or download and extract the project files
   ```

2. **Navigate to Project Directory**
   ```bash
   cd app_main
   ```

3. **Verify Java Installation**
   ```bash
   java -version
   javac -version
   ```

## 🚀 How to Run

### Quick Start (on macOS/Linux)

1. **Make the startup script executable** (if on macOS/Linux):
   ```bash
   chmod +x start_server.sh
   ```

2. **Run the server**:
   ```bash
   ./start_server.sh
   ```

3. **Access the application**:
   - Open your web browser
   - Navigate to: `http://localhost:8080/app/index.html`

### Manual Start

If you prefer to run manually:

```bash
# Navigate to the Java source directory
cd project_biu

# Compile all Java files
find . -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -cp .

# Run the main server
java -cp . Main
```

### What the Startup Script Does

The `start_server.sh` script:
1. **Compiles** all Java source files in the project
2. **Validates** compilation success
3. **Starts** the HTTP server on port 8080
4. **Provides** direct link to the web interface
5. **Waits** for user input before shutdown

## 💻 Usage

### Web Interface

1. **Access**: Open `http://localhost:8080/app/index.html`
2. **Upload Configuration**: Use the interface to upload `.conf` files
3. **View Graph**: Real-time visualization of the computation graph
4. **Monitor Topics**: Observe topic states and message flow
5. **Interact**: Submit data and see processing results

### Configuration Files

Sample configuration files are provided in `config_files/`:
- `demo_sensors.conf`: Basic sensor data processing
- `test_config.conf`: Test configuration
- `working_config.conf`: Working example configuration

### Creating Custom Configurations

Configuration files define:
```
AgentType
input_topics,comma_separated
output_topic
```

Example:
```
graph.PlusAgent
temperature,offset
adjusted_temp
graph.MaxAgent
adjusted_temp,threshold
max_temp
```

## 📁 Configuration Files

### Available Agent Types

- **`graph.PlusAgent`**: Addition operation
- **`graph.MultiplyAgent`**: Multiplication operation
- **`graph.MaxAgent`**: Maximum value selection
- **`graph.MinAgent`**: Minimum value selection
- **`graph.AverageAgent`**: Average calculation
- **`graph.BinOpAgent`**: Binary operations
- **`graph.ParallelAgent`**: Parallel processing
- **`graph.IncAgent`**: Increment operation


## 🌐 API Endpoints

- **GET `/app/`**: Static file serving (HTML, CSS, JS)
- **GET `/publish`**: Topic state display
- **GET `/graph`**: Graph visualization data
- **POST `/upload`**: Configuration file upload

## 📂 Project Structure

```
app_main/
├── start_server.sh          # Server startup script
├── readme.md               # This file
├── config_files/           # Sample configuration files
├── html_files/             # Web interface files
└── project_biu/            # Main Java project
    ├── Main.java           # Server entry point
    ├── configs/            # Configuration parsing
    ├── graph/              # Core computation graph system
    ├── server/             # HTTP server implementation
    ├── servlets/           # Web request handlers
    └── views/              # Additional view components
```

## � Documentation

### Javadoc API Documentation

The project includes comprehensive Javadoc documentation, particularly for the server package. The documentation is automatically generated and provides detailed API reference.

**Location**: `project_biu/javadoc/`

**Available Documentation**:
- **Server Package**: Complete API documentation for HTTP server components
  - `HTTPServer`: Core server interface and methods
  - `MyHTTPServer`: Main server implementation
  - `RequestParser`: HTTP request parsing functionality
  - `RequestInfo`: Request data structures

**Accessing Javadoc**:
1. **Local Access**: Open `project_biu/javadoc/index.html` in your browser
2. **Server Package**: Navigate to `project_biu/javadoc/server/package-summary.html`
3. **Search**: Use the built-in search functionality for quick navigation

**Key Documentation Features**:
- Method signatures and parameters
- Return types and exceptions
- Usage examples and descriptions
- Class hierarchies and relationships
- Package overviews and dependencies

**Generating Updated Javadoc** (if needed):
```bash
cd project_biu
javadoc -d javadoc -sourcepath . -subpackages server
```

## �🔧 Development

### Adding New Agent Types

1. Extend the `Agent` class
2. Implement required abstract methods
3. Add agent to configuration system
4. Test with sample configuration

### Extending the Web Interface

- Modify files in `html_files/`
- Update servlets for new endpoints
- Restart server to see changes

### Debugging

- Check `server_error.log` for error messages
- Use browser developer tools for frontend issues
- Verify configuration file syntax
- Consult Javadoc for API details and method specifications

## 🛑 Stopping the Server

- **Interactive Mode**: Press Enter in the terminal where the server is running
- **Force Stop**: Press `Ctrl+C` (or `Cmd+C` on macOS)

## 📝 Notes

- The server runs on port 8080 by default
- Configuration changes require server restart
- All computational graphs are processed in real-time
- The system supports concurrent agent execution

## 🤝 Contributing

1. Make changes to the appropriate package
2. Test thoroughly with sample configurations
3. Update documentation as needed
4. Ensure all Java files compile successfully

---

**Happy Computing!** 🎉