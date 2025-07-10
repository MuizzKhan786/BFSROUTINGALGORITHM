package com.Muizzkhan;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.List;

public class BFSROUTINGALGORITHM extends Application {
    GraphManager graphManager = new GraphManager();
    Pane canvas = new Pane();
    TextArea log = new TextArea();
    TextField nodeField = new TextField(), fromField = new TextField(), toField = new TextField();
    ComboBox<String> sourceBox = new ComboBox<>(), destBox = new ComboBox<>();
    final int RADIUS = 20;

    public static void launchApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Controls panel (right side)
        VBox controls = new VBox(12,
                label("Node:"), nodeField,
                btn("Add Node", e -> addNode()),
                label("From:"), fromField,
                label("To:"), toField,
                btn("Add Edge", e -> addEdge()),
                label("Source:"), sourceBox,
                label("Destination:"), destBox,
                btn("Find Path", e -> findPath()),
                btn("Random Graph", e -> genRandomGraph())
        );
        controls.setPadding(new Insets(15));
        controls.setAlignment(Pos.TOP_LEFT);
        controls.setPrefWidth(220);
        controls.setStyle("-fx-background-color: #111111;");

        // Canvas and log area
        canvas.setPrefSize(600, 400);
        canvas.setStyle("-fx-background-color: #222222; -fx-border-color: #880000; -fx-border-width: 2px;");

        log.setPrefHeight(100);
        log.setEditable(false);
        log.setStyle("-fx-control-inner-background: #111111; -fx-text-fill: white; -fx-font-family: 'Consolas';");

        VBox centerLayout = new VBox(10, canvas, label("Log:"), log);
        centerLayout.setPadding(new Insets(10));
        centerLayout.setStyle("-fx-background-color: #000000;");

        BorderPane root = new BorderPane();
        root.setCenter(centerLayout);
        root.setRight(controls);

        Scene scene = new Scene(root, 900, 600, Color.BLACK);
        stage.setTitle("BFS Routing Simulator");
        stage.setScene(scene);
        stage.show();
        log("Simulator ready. Add nodes and edges.");
    }

    void addNode() {
        String name = nodeField.getText().trim().toUpperCase();
        if (graphManager.addNode(name)) {
            sourceBox.getItems().add(name);
            destBox.getItems().add(name);
            draw();
            log("Node added: " + name);
        } else {
            log("Invalid or duplicate node.");
        }
        nodeField.clear();
    }

    void addEdge() {
        String from = fromField.getText().trim().toUpperCase();
        String to = toField.getText().trim().toUpperCase();
        if (graphManager.addEdge(from, to)) {
            draw();
            log("Edge added: " + from + " - " + to);
        } else {
            log("Invalid nodes for edge.");
        }
        fromField.clear();
        toField.clear();
    }

    void findPath() {
        String src = sourceBox.getValue();
        String dst = destBox.getValue();
        List<String> path = graphManager.bfsPath(src, dst);
        draw();
        if (path == null) {
            log("No path found.");
        } else {
            for (int i = 0; i < path.size() - 1; i++) {
                drawEdge(path.get(i), path.get(i + 1), Color.LIME, 3);
            }
            log("Path found: " + path);
        }
    }

    void genRandomGraph() {
        graphManager.generateRandomGraph();
        sourceBox.getItems().setAll(graphManager.getAllNodes());
        destBox.getItems().setAll(graphManager.getAllNodes());
        draw();
        log("Random graph generated.");
    }

    void draw() {
        canvas.getChildren().clear();
        for (String from : graphManager.getAllNodes()) {
            for (String to : graphManager.getNeighbors(from)) {
                if (from.compareTo(to) < 0) {
                    drawEdge(from, to, Color.RED, 1);
                }
            }
        }
        for (String node : graphManager.getAllNodes()) {
            double[] pos = graphManager.getPosition(node);
            Circle circle = new Circle(pos[0], pos[1], RADIUS, Color.RED);
            circle.setStroke(Color.BLACK);
            Text text = new Text(pos[0] - 5, pos[1] + 5, node);
            text.setFill(Color.WHITE);
            canvas.getChildren().addAll(circle, text);
        }
    }

    void drawEdge(String from, String to, Color color, int width) {
        double[] p1 = graphManager.getPosition(from), p2 = graphManager.getPosition(to);
        Line line = new Line(p1[0], p1[1], p2[0], p2[1]);
        line.setStroke(color);
        line.setStrokeWidth(width);
        canvas.getChildren().add(line);
    }

    void log(String msg) {
        log.appendText(msg + "\n");
    }

    Button btn(String label, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button b = new Button(label);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("-fx-background-color: #880000; -fx-text-fill: white;");
        b.setOnAction(handler);
        return b;
    }

    Label label(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        return l;
    }
}
