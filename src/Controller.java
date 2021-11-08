import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    private static int SIZE = 10;

    private static int EMPTY = -1;

    public Button calculateButton;

    public Label resultLabel;

    public ChoiceBox<String> startVertexBox;

    public ChoiceBox<String> finishVertexBox;

    @FXML
    private VBox graphBox;

    @FXML
    private TableView<Row> matrixView;

    private static GraphPanel<String, String> graphView;

    private Graph<String, String> graph;

    private AlgGraph algGraph;

    private int[][] weights = new int[SIZE][SIZE];

    private List<Vertex<String>> selectedVertexes;

    private List<Vertex<String>> selectedVertexesWithCtrl;

    private static final List<String> vertexes =
            new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        graph = new GraphEdges<>();
        selectedVertexes = new ArrayList<>();
        selectedVertexesWithCtrl = new ArrayList<>();
        algGraph = new AlgGraph(SIZE);

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphView = new GraphPanel<>(graph, strategy);
        graphView.prefHeightProperty().bind(graphBox.heightProperty());
        graphView.prefWidthProperty().bind(graphBox.widthProperty());
        graphBox.getChildren().add(graphView);

        startVertexBox.getItems().addAll(vertexes);
        startVertexBox.setValue(startVertexBox.getItems().get(0));
        finishVertexBox.getItems().addAll(vertexes);
        finishVertexBox.setValue(finishVertexBox.getItems().get(0));

        initMatrixView();
        initListeners();
    }

    public static GraphPanel<String, String> getGraphView() {
        return graphView;
    }

    @FXML
    public void calculateMinPath() {
        List<Integer> path = algGraph.useDijkstra(getNumByName(startVertexBox.getValue()),
                getNumByName(finishVertexBox.getValue()), weights);

        StringBuilder text = new StringBuilder();
        if (path.isEmpty()) {
            text = new StringBuilder("Пути между вершинами " + startVertexBox.getValue() + " и " +
                    getNumByName(finishVertexBox.getValue()) + " нет.");
        } else {
            for (int i = 0; i < path.size() - 1; i++) {
                text.append(getNameByNum(path.get(i))).append(" -> ");
                int finalI = i;
                graph.edges().stream()
                        .filter(e -> e.vertices()[0].element().equals(getNameByNum(path.get(finalI)))
                                && e.vertices()[1].element().equals(getNameByNum(path.get(finalI + 1))))
                        .findFirst()
                        .ifPresent(edge -> {
                            graphView.getStylableEdge(edge).setStyleClass("chosenEdge");
                            //стрелки
                        });
            }
            text.append(finishVertexBox.getValue())
                    .append(" (")
                    .append(algGraph.cost(path, weights))
                    .append(")");
        }
        resultLabel.setText(text.toString());
    }
    
    private void initWeightsByMatrix() {
        String[][] weightsView = new String[weights.length][weights[0].length];
        for (int i = 0; i < weightsView.length; i++) {
            for (int j = 0; j < weightsView[0].length; j++) {
                String value = "";
                switch (j) {
                    case 0: value = matrixView.getItems().get(i).getA();
                        break;
                    case 1: value = matrixView.getItems().get(i).getB();
                        break;
                    case 2: value = matrixView.getItems().get(i).getC();
                        break;
                    case 3: value = matrixView.getItems().get(i).getD();
                        break;
                    case 4: value = matrixView.getItems().get(i).getE();
                        break;
                    case 5: value = matrixView.getItems().get(i).getF();
                        break;
                    case 6: value = matrixView.getItems().get(i).getG();
                        break;
                    case 7: value = matrixView.getItems().get(i).getH();
                        break;
                    case 8: value = matrixView.getItems().get(i).getI();
                        break;
                    case 9: value = matrixView.getItems().get(i).getJ();
                        break;
                }
                weightsView[i][j] = value;
            }
        }
        for (int i = 0; i < weightsView.length; i++) {
            for (int j = 0; j < weightsView[0].length; j++) {
                if (weightsView[i][j].equals("")) {
                    weights[i][j] = EMPTY;
                } else {
                    weights[i][j] = Integer.parseInt(weightsView[i][j]);
                }
            }
        }
    }

    private String[][] getWeightsViewForMatrix() {
        String[][] weightsView = new String[weights.length][weights[0].length];
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                if (weights[i][j] == EMPTY) {
                    weightsView[i][j] = "";
                } else {
                    weightsView[i][j] = weights[i][j] + "";
                }
            }
        }
        return weightsView;
    }

    private void initMatrixByWeights() {
        String[][] weightsView = getWeightsViewForMatrix();
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                switch (j) {
                    case 0: matrixView.getItems().get(i).setA(weightsView[i][j]);
                        break;
                    case 1: matrixView.getItems().get(i).setB(weightsView[i][j]);
                        break;
                    case 2: matrixView.getItems().get(i).setC(weightsView[i][j]);
                        break;
                    case 3: matrixView.getItems().get(i).setD(weightsView[i][j]);
                        break;
                    case 4: matrixView.getItems().get(i).setE(weightsView[i][j]);
                        break;
                    case 5: matrixView.getItems().get(i).setF(weightsView[i][j]);
                        break;
                    case 6: matrixView.getItems().get(i).setG(weightsView[i][j]);
                        break;
                    case 7: matrixView.getItems().get(i).setH(weightsView[i][j]);
                        break;
                    case 8: matrixView.getItems().get(i).setI(weightsView[i][j]);
                        break;
                    case 9: matrixView.getItems().get(i).setJ(weightsView[i][j]);
                        break;
                }
            }
        }
    }

    private void initGraphByWeights() {
        for (int i = 0; i < weights.length; i++) {
            String vertexOutName = matrixView.getColumns().get(i + 1).getText();
            for (int j = 0; j < weights[0].length; j++) {
                String vertexInName = matrixView.getItems().get(j).getName();
                graph.edges().stream()
                        .filter(edge -> edge.vertices()[0].element().equals(vertexOutName)
                                && edge.vertices()[1].element().equals(vertexInName))
                        .findFirst()
                        .ifPresent(e -> graph.removeEdge(e));
                if (weights[i][j] != EMPTY) {
                    if (graph.vertices().stream().noneMatch(v -> v.element().equals(vertexOutName))) {
                        graph.insertVertex(vertexOutName);
                    }
                    if (graph.vertices().stream().noneMatch(v -> v.element().equals(vertexInName))) {
                        graph.insertVertex(vertexInName);
                    }
                    graph.insertEdge(vertexOutName, vertexInName, weights[i][j] + "");
                }
            }
        }
    }

    private int getNumByName(String name) {
        return name.charAt(0) - 'A';
    }

    private String getNameByNum(int num) {
        return (char)('A' + num) + "";
    }

    private void initWeightsByGraph() {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                weights[i][j] = EMPTY;
            }
        }
        for (var edge : graph.edges()) {
            int i = getNumByName(edge.vertices()[0].element());
            int j = getNumByName(edge.vertices()[1].element());
            weights[i][j] = Integer.parseInt(edge.element());
        }
    }

    private void updateGraphView() {
        resultLabel.setText("");

        graphView.update();

        for (var edge : graph.edges()) {
            if (graphView.getStylableEdge(edge) != null) {
                graphView.getStylableEdge(edge).setStyleClass("edge");
            }
        }
        for (var vertex : graph.vertices()) {
            if (graphView.getStylableVertex(vertex) != null) {
                graphView.getStylableVertex(vertex).setStyleClass("vertex");
            }
        }

        graphView.update();
    }

    private void initMatrixView() {
        matrixView.setEditable(true);
        TableColumn<Row, String> columnName = new TableColumn<>("");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnName.setMinWidth(50);

        TableColumn<Row, String> columnA = new TableColumn<>("A");
        columnA.setCellValueFactory(new PropertyValueFactory<>("a"));
        columnA.setCellFactory(TextFieldTableCell.forTableColumn());
        columnA.setOnEditCommit(
                (TableColumn.CellEditEvent<Row, String> t) -> { if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setA(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnA.setMinWidth(50);

        TableColumn<Row, String> columnB = new TableColumn<>("B");
        columnB.setCellValueFactory(new PropertyValueFactory<>("b"));
        columnB.setCellFactory(TextFieldTableCell.forTableColumn());
        columnB.setOnEditCommit(
                t -> {if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setB(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnB.setMinWidth(50);

        TableColumn<Row, String> columnC = new TableColumn<>("C");
        columnC.setCellValueFactory(new PropertyValueFactory<>("c"));
        columnC.setCellFactory(TextFieldTableCell.forTableColumn());
        columnC.setOnEditCommit(
                t -> {if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setC(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnC.setMinWidth(50);

        TableColumn<Row, String> columnD = new TableColumn<>("D");
        columnD.setCellValueFactory(new PropertyValueFactory<>("d"));
        columnD.setCellFactory(TextFieldTableCell.forTableColumn());
        columnD.setOnEditCommit(
                t -> {if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setD(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnD.setMinWidth(50);

        TableColumn<Row, String> columnE = new TableColumn<>("E");
        columnE.setCellValueFactory(new PropertyValueFactory<>("e"));
        columnE.setCellFactory(TextFieldTableCell.forTableColumn());
        columnE.setOnEditCommit(
                t -> {if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setE(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnE.setMinWidth(50);

        TableColumn<Row, String> columnF = new TableColumn<>("F");
        columnF.setCellValueFactory(new PropertyValueFactory<>("f"));
        columnF.setCellFactory(TextFieldTableCell.forTableColumn());
        columnF.setOnEditCommit(
                t -> {if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setF(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnF.setMinWidth(50);

        TableColumn<Row, String> columnG = new TableColumn<>("G");
        columnG.setCellValueFactory(new PropertyValueFactory<>("g"));
        columnG.setCellFactory(TextFieldTableCell.forTableColumn());
        columnG.setOnEditCommit(
                t -> {if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setG(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnG.setMinWidth(50);

        TableColumn<Row, String> columnH = new TableColumn<>("H");
        columnH.setCellValueFactory(new PropertyValueFactory<>("h"));
        columnH.setCellFactory(TextFieldTableCell.forTableColumn());
        columnH.setOnEditCommit(
                t -> {if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setH(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnH.setMinWidth(50);

        TableColumn<Row, String> columnI = new TableColumn<>("I");
        columnI.setCellValueFactory(new PropertyValueFactory<>("i"));
        columnI.setCellFactory(TextFieldTableCell.forTableColumn());
        columnI.setOnEditCommit(
                t -> {if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setI(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnI.setMinWidth(50);

        TableColumn<Row, String> columnJ = new TableColumn<>("J");
        columnJ.setCellValueFactory(new PropertyValueFactory<>("j"));
        columnJ.setCellFactory(TextFieldTableCell.forTableColumn());
        columnJ.setOnEditCommit(
                t -> {if (t.getNewValue().matches("\\d+")) {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setJ(t.getNewValue());
                    initWeightsByMatrix();
                    initGraphByWeights();
                    updateGraphView();}}
        );
        columnJ.setMinWidth(50);

        matrixView.getColumns().addAll(columnName, columnA, columnB, columnC, columnD, columnE, columnF, columnG,
                columnH, columnI, columnJ);

        ObservableList<Row> observableList = FXCollections.observableArrayList(
                new Row("A", "", "", "", "", "", "", "", "", "", ""),
                new Row("B", "", "", "", "", "", "", "", "", "", ""),
                new Row("C", "", "", "", "", "", "", "", "", "", ""),
                new Row("D", "", "", "", "", "", "", "", "", "", ""),
                new Row("E", "", "", "", "", "", "", "", "", "", ""),
                new Row("F", "", "", "", "", "", "", "", "", "", ""),
                new Row("G", "", "", "", "", "", "", "", "", "", ""),
                new Row("H", "", "", "", "", "", "", "", "", "", ""),
                new Row("I", "", "", "", "", "", "", "", "", "", ""),
                new Row("J", "", "", "", "", "", "", "", "", "", ""));
        matrixView.setItems(observableList);
    }

    private void initListeners() {
        graphView.setPanelLeftClickConsumer(event -> {
            List<String> notPresentVertexes = new ArrayList<>();
            for (var vertex : vertexes) {
                if (graph.vertices().stream().noneMatch(v -> v.element().equals(vertex))) {
                    notPresentVertexes.add(vertex);
                }
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>();
            dialog.getItems().addAll(notPresentVertexes);
            if (!notPresentVertexes.isEmpty()) {
                dialog.setSelectedItem(notPresentVertexes.get(0));
            }
            dialog.setTitle("Добавление вершины");
            dialog.setHeaderText("Выберите добавляемую вершину:");
            dialog.setContentText("Вершина:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(vertex -> {
                graph.insertVertex(vertex);
                graphView.update();
            });
        });
        graphView.setEdgeRightClickConsumer(graphEdge -> {
            graph.removeEdge(graphEdge.getUnderlyingEdge());
            initWeightsByGraph();
            updateGraphView();
            initMatrixByWeights();
        });
        graphView.setVertexRightClickConsumer(graphVertex -> {
            graph.removeVertex(graphVertex.getUnderlyingVertex());
            graph.edges().removeIf(it -> Arrays.asList(it.vertices()).contains(graphVertex.getUnderlyingVertex()));
            initWeightsByGraph();
            updateGraphView();
            initMatrixByWeights();
        });
        graphView.setEdgeLeftClickConsumer(graphEdge -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Изменение веса ребра");
            dialog.setHeaderText("Введите вес ребра:");
            dialog.setContentText("Вес:");
            Optional<String> result = dialog.showAndWait();

            result.ifPresent(weight -> {
                if (weight.matches("\\d+")) {
                    graph.replace(graphEdge.getUnderlyingEdge(), weight + "");
                    initWeightsByGraph();
                    updateGraphView();
                    initMatrixByWeights();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Некорректный ввод");
                    alert.setHeaderText(null);
                    alert.setContentText("Вес должен иметь числовое значение");
                    alert.showAndWait();
                }
            });
        });
        graphView.setVertexMiddleClickConsumer(graphVertex -> {
            selectedVertexes.add(graphVertex.getUnderlyingVertex());

            if (selectedVertexes.size() == 2) {
                TextInputDialog dialog = new TextInputDialog("1");
                dialog.setTitle("Добавление ребра между вершинами " + selectedVertexes.get(0).element() + " и " +
                        selectedVertexes.get(1).element());
                dialog.setHeaderText("Введите вес ребра:");
                dialog.setContentText("Вес:");
                Optional<String> result = dialog.showAndWait();

                result.ifPresent(weight -> {
                    if (weight.matches("\\d+")) {
                        graph.insertEdge(selectedVertexes.get(0).element(), selectedVertexes.get(1).element(), weight);
                        selectedVertexes.clear();
                        initWeightsByGraph();
                        updateGraphView();
                        initMatrixByWeights();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Некорректный ввод");
                        alert.setHeaderText(null);
                        alert.setContentText("Вес должен иметь числовое значение");
                        alert.showAndWait();
                    }
                });
            }
        });
        graphView.setVertexMiddleClickWithCtrlConsumer(graphVertex -> {
            graphView.getStylableVertex(graphVertex.getUnderlyingVertex()).setStyleClass("chosenVertex");
            selectedVertexesWithCtrl.add(graphVertex.getUnderlyingVertex());
            if (selectedVertexesWithCtrl.size() == 2) {
                startVertexBox.setValue(selectedVertexesWithCtrl.get(0).element());
                finishVertexBox.setValue(selectedVertexesWithCtrl.get(1).element());
                calculateMinPath();
                selectedVertexesWithCtrl.clear();
            }
        });
    }

    /*
    * поиск (отсутствие пути пофиксить)
    * что-то с петлями (нулями задавать петли?)
    *
    * поправить вью графа
    * стрелки покрасить
     */
}
