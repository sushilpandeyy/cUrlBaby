package com.curlbaby;

public class CommandProcessor {
    private final UIManager uiManager;
    private final HttpRequestHandler requestHandler;
    
    public CommandProcessor(UIManager uiManager, HttpRequestHandler requestHandler) {
        this.uiManager = uiManager;
        this.requestHandler = requestHandler;
    }
    
    public void processCommand(String command, String argument) {
        switch (command) {
            case "exit":
                uiManager.printExitMessage();
                System.exit(0);
                break;
            case "help":
                uiManager.printHelp();
                break;
            case "get":
                if (argument.isEmpty()) {
                    uiManager.printError("Usage: get <url>");
                } else {
                    requestHandler.executeGetRequest(argument);
                }
                break;
            case "post": 
                if (argument.isEmpty()) {
                    uiManager.printError("Usage: post <url>");
                } else {
                    requestHandler.executePostRequest(argument);
                }
                break;
            case "put":
                if (argument.isEmpty()) {
                    uiManager.printError("Usage: put <url>");
                } else {
                    requestHandler.executePutRequest(argument);
                }
                break;
            case "delete":
                if (argument.isEmpty()) {
                    uiManager.printError("Usage: delete <url>");
                } else {
                    requestHandler.executeDeleteRequest(argument);
                }
                break;
            default:
                uiManager.printError("Unknown command: " + command);
                System.out.println("Type 'help' for available commands");
        }
    }
}