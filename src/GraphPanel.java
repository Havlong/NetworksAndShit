import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.*;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;

import java.net.URI;
import java.util.function.Consumer;

public class GraphPanel<V, E> extends SmartGraphPanel<V, E> {
    private Consumer<GraphPanel<V, E>> panelLeftClickConsumer;

    private Consumer<SmartGraphVertex<V>> vertexRightClickConsumer;

    private Consumer<SmartGraphEdge<E, V>> edgeRightClickConsumer;

    private Consumer<SmartGraphEdge<E, V>> edgeLeftClickConsumer;

    private Consumer<SmartGraphVertex<V>> vertexMiddleClickConsumer;

    private Consumer<SmartGraphVertex<V>> vertexMiddleClickWithCtrlConsumer;

    public GraphPanel(Graph theGraph) {
        this(theGraph, new SmartGraphProperties(), (SmartPlacementStrategy)null);
    }

    public GraphPanel(Graph theGraph, SmartGraphProperties properties) {
        this(theGraph, properties, (SmartPlacementStrategy)null);
    }

    public GraphPanel(Graph theGraph, SmartPlacementStrategy placementStrategy) {
        this(theGraph, (SmartGraphProperties)null, placementStrategy);
    }

    public GraphPanel(Graph theGraph, SmartGraphProperties properties, SmartPlacementStrategy placementStrategy) {
        this(theGraph, properties, placementStrategy, (URI)null);
    }

    public GraphPanel(Graph theGraph, SmartGraphProperties properties, SmartPlacementStrategy placementStrategy, URI cssFile) {
        super(theGraph, properties, placementStrategy, cssFile);
        this.panelLeftClickConsumer = null;
        this.vertexRightClickConsumer = null;
        this.edgeRightClickConsumer = null;
        this.edgeLeftClickConsumer = null;
        this.vertexMiddleClickConsumer = null;
        this.vertexMiddleClickWithCtrlConsumer = null;
        this.enableListeners();
    }

    public void setVertexRightClickConsumer(Consumer<SmartGraphVertex<V>> vertexRightClickConsumer) {
        this.vertexRightClickConsumer = vertexRightClickConsumer;
    }

    public void setEdgeRightClickConsumer(Consumer<SmartGraphEdge<E, V>> edgeRightClickConsumer) {
        this.edgeRightClickConsumer = edgeRightClickConsumer;
    }

    public void setVertexMiddleClickConsumer(Consumer<SmartGraphVertex<V>> vertexMiddleClickConsumer) {
        this.vertexMiddleClickConsumer = vertexMiddleClickConsumer;
    }

    public void setEdgeLeftClickConsumer(Consumer<SmartGraphEdge<E, V>> edgeLeftClickConsumer) {
        this.edgeLeftClickConsumer = edgeLeftClickConsumer;
    }

    public void setPanelLeftClickConsumer(Consumer<GraphPanel<V, E>> panelLeftClickConsumer) {
        this.panelLeftClickConsumer = panelLeftClickConsumer;
    }

    public void setVertexMiddleClickWithCtrlConsumer(Consumer<SmartGraphVertex<V>> vertexMiddleClickWithCtrlConsumer) {
        this.vertexMiddleClickWithCtrlConsumer = vertexMiddleClickWithCtrlConsumer;
    }

    private void enableListeners() {
        this.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                if (this.panelLeftClickConsumer == null) {
                    return;
                }
                this.panelLeftClickConsumer.accept(this);
            } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY) && mouseEvent.getClickCount() == 1) {
                if (this.vertexRightClickConsumer == null && this.edgeRightClickConsumer == null) {
                    return;
                }
                Node node = UtilitiesJavaFX.pick(this, mouseEvent.getSceneX(), mouseEvent.getSceneY());
                if (node == null) {
                    return;
                }
                if (node instanceof SmartGraphVertex) {
                    SmartGraphVertex v = (SmartGraphVertex)node;
                    this.vertexRightClickConsumer.accept(v);
                } else if (node instanceof SmartGraphEdge) {
                    SmartGraphEdge e = (SmartGraphEdge)node;
                    this.edgeRightClickConsumer.accept(e);
                }
            } else if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 1) {
                if (this.edgeLeftClickConsumer == null) {
                    return;
                }
                Node node = UtilitiesJavaFX.pick(this, mouseEvent.getSceneX(), mouseEvent.getSceneY());
                if (node == null) {
                    return;
                }
                if (node instanceof SmartGraphEdge) {
                    SmartGraphEdge e = (SmartGraphEdge)node;
                    this.edgeLeftClickConsumer.accept(e);
                }
            } else if (mouseEvent.isControlDown() && mouseEvent.getButton().equals(MouseButton.MIDDLE) && mouseEvent.getClickCount() == 1) {
                if (this.vertexMiddleClickWithCtrlConsumer == null) {
                    return;
                }
                Node node = UtilitiesJavaFX.pick(this, mouseEvent.getSceneX(), mouseEvent.getSceneY());
                if (node == null) {
                    return;
                }
                if (node instanceof SmartGraphVertex) {
                    SmartGraphVertex v = (SmartGraphVertex) node;
                    this.vertexMiddleClickWithCtrlConsumer.accept(v);
                }
            } else if (mouseEvent.getButton().equals(MouseButton.MIDDLE) && mouseEvent.getClickCount() == 1) {
                if (this.vertexMiddleClickConsumer == null) {
                    return;
                }
                Node node = UtilitiesJavaFX.pick(this, mouseEvent.getSceneX(), mouseEvent.getSceneY());
                if (node == null) {
                    return;
                }
                if (node instanceof SmartGraphVertex) {
                    SmartGraphVertex v = (SmartGraphVertex) node;
                    this.vertexMiddleClickConsumer.accept(v);
                }
            }
        });
    }
}
