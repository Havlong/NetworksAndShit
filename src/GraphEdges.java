import com.brunomnsilva.smartgraph.graph.*;

import java.util.*;

public class GraphEdges<V, E> implements Digraph<V, E> {
    private final Map<V, Vertex<V>> vertices = new HashMap();
    private final List<Edge<E, V>> edges = new ArrayList<>();

    GraphEdges() {
    }

    public synchronized Collection<Edge<E, V>> incidentEdges(Vertex<V> inbound) throws InvalidVertexException {
        this.checkVertex(inbound);
        List<Edge<E, V>> incidentEdges = new ArrayList();

        for (Edge<E, V> evEdge : this.edges) {
            Edge<E, V> edge = (Edge) evEdge;
            if (((MyEdge) edge).getInbound() == inbound) {
                incidentEdges.add(edge);
            }
        }

        return incidentEdges;
    }

    public synchronized Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound) throws InvalidVertexException {
        this.checkVertex(outbound);
        List<Edge<E, V>> outboundEdges = new ArrayList();

        for (Edge<E, V> evEdge : this.edges) {
            Edge<E, V> edge = (Edge) evEdge;
            if (((MyEdge) edge).getOutbound() == outbound) {
                outboundEdges.add(edge);
            }
        }

        return outboundEdges;
    }

    public boolean areAdjacent(Vertex<V> outbound, Vertex<V> inbound) throws InvalidVertexException {
        this.checkVertex(outbound);
        this.checkVertex(inbound);
        Iterator var3 = this.edges.iterator();

        Edge edge;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            edge = (Edge)var3.next();
        } while(((GraphEdges.MyEdge)edge).getOutbound() != outbound || ((GraphEdges.MyEdge)edge).getInbound() != inbound);

        return true;
    }

    public synchronized Edge<E, V> insertEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        GraphEdges<V, E>.MyVertex outVertex = this.checkVertex(outbound);
        GraphEdges<V, E>.MyVertex inVertex = this.checkVertex(inbound);
        GraphEdges<V, E>.MyEdge newEdge = new GraphEdges.MyEdge(edgeElement, outVertex, inVertex);
        this.edges.add(newEdge);
        return newEdge;
    }

    public synchronized Edge<E, V> insertEdge(V outboundElement, V inboundElement, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        if (!this.existsVertexWith(outboundElement)) {
            throw new InvalidVertexException("No vertex contains " + outboundElement);
        } else if (!this.existsVertexWith(inboundElement)) {
            throw new InvalidVertexException("No vertex contains " + inboundElement);
        } else {
            GraphEdges<V, E>.MyVertex outVertex = this.vertexOf(outboundElement);
            GraphEdges<V, E>.MyVertex inVertex = this.vertexOf(inboundElement);
            GraphEdges<V, E>.MyEdge newEdge = new GraphEdges.MyEdge(edgeElement, outVertex, inVertex);
            this.edges.add(newEdge);
            return newEdge;
        }
    }

    public int numVertices() {
        return this.vertices.size();
    }

    public int numEdges() {
        return this.edges.size();
    }

    public synchronized Collection<Vertex<V>> vertices() {
        List<Vertex<V>> list = new ArrayList();
        list.addAll(this.vertices.values());
        return list;
    }

    public synchronized Collection<Edge<E, V>> edges() {
        List<Edge<E, V>> list = new ArrayList();
        list.addAll(this.edges);
        return list;
    }

    public synchronized Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        this.checkVertex(v);
        GraphEdges<V, E>.MyEdge edge = this.checkEdge(e);
        if (!edge.contains(v)) {
            return null;
        } else {
            return edge.vertices()[0] == v ? edge.vertices()[1] : edge.vertices()[0];
        }
    }

    public synchronized Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if (this.existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        } else {
            GraphEdges<V, E>.MyVertex newVertex = new GraphEdges.MyVertex(vElement);
            this.vertices.put(vElement, newVertex);
            return newVertex;
        }
    }

    public synchronized V removeVertex(Vertex<V> v) throws InvalidVertexException {
        this.checkVertex(v);
        V element = v.element();
        Collection<Edge<E, V>> inOutEdges = this.incidentEdges(v);
        inOutEdges.addAll(this.outboundEdges(v));

        for (Edge<E, V> inOutEdge : inOutEdges) {
            Edge<E, V> edge = (Edge) inOutEdge;
            this.edges.remove(edge);
        }

        this.vertices.remove(v.element());
        return element;
    }

    public synchronized E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        this.checkEdge(e);
        E element = e.element();
        this.edges.remove(e);
        return element;
    }

    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        if (this.existsVertexWith(newElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        } else {
            GraphEdges<V, E>.MyVertex vertex = this.checkVertex(v);
            V oldElement = vertex.element;
            vertex.element = newElement;
            return oldElement;
        }
    }

    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        if (this.existsEdgeWith(newElement)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        } else {
            GraphEdges<V, E>.MyEdge edge = this.checkEdge(e);
            E oldElement = edge.element;
            edge.element = newElement;
            return oldElement;
        }
    }

    private GraphEdges<V, E>.MyVertex vertexOf(V vElement) {
        Iterator var2 = this.vertices.values().iterator();

        Vertex v;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            v = (Vertex)var2.next();
        } while(!v.element().equals(vElement));

        return (GraphEdges.MyVertex)v;
    }

    private boolean existsVertexWith(V vElement) {
        return this.vertices.containsKey(vElement);
    }

    private boolean existsEdgeWith(E edgeElement) {
        return this.edges.contains(edgeElement);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("AlgGraph with %d vertices and %d edges:\n", this.numVertices(), this.numEdges()));
        sb.append("--- Vertices: \n");
        Iterator var2 = this.vertices.values().iterator();

        while(var2.hasNext()) {
            Vertex<V> v = (Vertex)var2.next();
            sb.append("\t").append(v.toString()).append("\n");
        }

        sb.append("\n--- Edges: \n");
        var2 = this.edges.iterator();

        while(var2.hasNext()) {
            Edge<E, V> e = (Edge)var2.next();
            sb.append("\t").append(e.toString()).append("\n");
        }

        return sb.toString();
    }

    private GraphEdges<V, E>.MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {
        if (v == null) {
            throw new InvalidVertexException("Null vertex.");
        } else {
            GraphEdges.MyVertex vertex;
            try {
                vertex = (GraphEdges.MyVertex)v;
            } catch (ClassCastException var4) {
                throw new InvalidVertexException("Not a vertex.");
            }

            if (!this.vertices.containsKey(vertex.element)) {
                throw new InvalidVertexException("Vertex does not belong to this graph.");
            } else {
                return vertex;
            }
        }
    }

    private GraphEdges<V, E>.MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if (e == null) {
            throw new InvalidEdgeException("Null edge.");
        } else {
            GraphEdges.MyEdge edge;
            try {
                edge = (GraphEdges.MyEdge)e;
            } catch (ClassCastException var4) {
                throw new InvalidVertexException("Not an adge.");
            }

            if (!this.edges.contains(edge)) {
                throw new InvalidEdgeException("Edge does not belong to this graph.");
            } else {
                return edge;
            }
        }
    }

    private class MyEdge implements Edge<E, V> {
        E element;
        Vertex<V> vertexOutbound;
        Vertex<V> vertexInbound;

        MyEdge(E element, Vertex<V> vertexOutbound, Vertex<V> vertexInbound) {
            this.element = element;
            this.vertexOutbound = vertexOutbound;
            this.vertexInbound = vertexInbound;
        }

        public E element() {
            return this.element;
        }

        boolean contains(Vertex<V> v) {
            return this.vertexOutbound == v || this.vertexInbound == v;
        }

        public Vertex<V>[] vertices() {
            return new Vertex[]{this.vertexOutbound, this.vertexInbound};
        }

        public String toString() {
            return "Edge{{" + this.element + "}, vertexOutbound=" + this.vertexOutbound.toString() + ", vertexInbound=" + this.vertexInbound.toString() + '}';
        }

        Vertex<V> getOutbound() {
            return this.vertexOutbound;
        }

        Vertex<V> getInbound() {
            return this.vertexInbound;
        }
    }

    private class MyVertex implements Vertex<V> {
        V element;

        MyVertex(V element) {
            this.element = element;
        }

        public V element() {
            return this.element;
        }

        public String toString() {
            return "Vertex{" + this.element + '}';
        }
    }
}
