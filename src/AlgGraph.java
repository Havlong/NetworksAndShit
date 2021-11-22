import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 07.09.2021
 * AlgGraph
 *
 * @author Havlong
 * @version v1.0
 */
public class AlgGraph {
    public static final int EMPTY = -1;

    private int size;

    private int[][] matrix;

    public AlgGraph(int size) {
        this.size = size;
        this.matrix = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.matrix[i][j] = EMPTY;
            }
        }
    }

    public AlgGraph(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length != matrix.length)
            throw new IllegalArgumentException();
        this.size = matrix.length;
        this.matrix = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    public static AlgGraph load(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int size = Integer.parseInt(reader.readLine());
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            String toParse = reader.readLine();
            String[] toUse = toParse.trim().split("\\s+");
            if (toUse.length != size)
                throw new IllegalArgumentException();
            for (int j = 0; j < toUse.length; j++) {
                matrix[i][j] = Integer.parseInt(toUse[j]);
            }
        }
        return new AlgGraph(matrix);
    }

    public int getSize() {
        return size;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void save(String filename) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(filename);
        writer.println(size);
        Arrays.stream(matrix).forEach(ints -> {
            String toParse = Arrays.stream(ints).toString();
            writer.println(toParse.substring(1, toParse.length() - 1));
        });
    }

    public void addVertex() {
        int[][] newMatrix = new int[size + 1][size + 1];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newMatrix[i][j] = matrix[i][j];
            }
        }
        for (int i = 0; i < size + 1; i++) {
            newMatrix[size][0] = newMatrix[0][size] = EMPTY;
        }
        size++;
        this.matrix = newMatrix;
    }

    public void removeVertex(int v) {
        if (!isValid(v))
            throw new IllegalArgumentException();

        size--;
        int[][] newMatrix = new int[size][size];

        int newI = 0, newJ = 0;
        for (int i = 0; i <= size; i++, newI++) {
            if (i == v) {
                newI--;
                continue;
            }
            for (int j = 0; j <= size; j++, newJ++) {
                if (j == v) {
                    newJ--;
                    continue;
                }
                newMatrix[newI][newJ] = matrix[i][j];
            }
        }
        this.matrix = newMatrix;
    }

    public void addEdge(int from, int to, int cost) {
        if (isValid(from) && isValid(to) && matrix[from][to] == EMPTY && cost > EMPTY) {
            matrix[from][to] = cost;
        } else throw new IllegalArgumentException();
    }

    public void editEdge(int from, int to, int cost) {
        if (isValid(from) && isValid(to) && matrix[from][to] > EMPTY && cost > EMPTY) {
            matrix[from][to] = cost;
        } else throw new IllegalArgumentException();
    }

    public void removeEdge(int from, int to) {
        if (isValid(from) && isValid(to) && matrix[from][to] > EMPTY) {
            matrix[from][to] = EMPTY;
        } else throw new IllegalArgumentException();
    }

    public DijkstraResult getDijkstraDistances(int from, int[][] matrix) {
        int[] parent = new int[size];
        int[] d = new int[size];
        int[] v = new int[size];
        for (int i = 0; i < size; i++) {
            d[i] = Integer.MAX_VALUE;
            parent[i] = -1;
            v[i] = 0;
        }
        d[from] = 0;
        parent[from] = -2;
        for (int i = 0; i < size; i++) {
            int next = -1;
            for (int j = 0; j < size; j++) {
                if ((next == -1 || d[j] < d[next]) && v[j] == 0)
                    next = j;
            }
            if (next == -1 || d[next] == Integer.MAX_VALUE) {
                break;
            }
            v[next] = 1;
            for (int nextV = 0; nextV < size; nextV++) {
                if (matrix[next][nextV] != EMPTY && d[next] + matrix[next][nextV] < d[nextV]) {
                    parent[nextV] = next;
                    d[nextV] = d[next] + matrix[next][nextV];
                }
            }
        }

        return new DijkstraResult(d, v, parent);
    }

    public FloydResult getFloydDistances(int[][] matrix) {
        int[][] parent = new int[size][size];
        int[][] d = new int[size][size];
        for (int i = 0; i < size; ++size) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != EMPTY) {
                    d[i][j] = matrix[i][j];
                    parent[i][j] = i;
                } else {
                    d[i][j] = Integer.MAX_VALUE;
                    parent[i][j] = -1;
                }
            }
            d[i][i] = 0;
            parent[i][i] = -2;
        }

        for (int p = 0; p < size; p++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (d[i][j] < d[i][p] + d[p][j]) {
                        d[i][j] = d[i][p] + d[p][j];
                        parent[i][j] = p;
                    }
                }
            }
        }

        return new FloydResult(d, parent);
    }

    public List<Integer> useDijkstra(int from, int to, int[][] matrix) {
        if (isValid(from) && isValid(to)) {
            if (from == to) {
                return Collections.singletonList(from);
            }

            DijkstraResult result = getDijkstraDistances(from, matrix);

            if (result.v[to] == 1) {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(to);
                int cur = to;
                while (result.parent[cur] != -2) {
                    list.add(result.parent[cur]);
                    cur = result.parent[cur];
                }
                Collections.reverse(list);
                return list;
            } else {
                return Collections.emptyList();
            }
        } else throw new IllegalArgumentException();
    }

    public List<Integer> useFloyd(int from, int to, int[][] matrix) {
        if (isValid(from) && isValid(to)) {
            if (from == to) {
                return Collections.singletonList(from);
            }

            FloydResult result = getFloydDistances(matrix);

            if (result.parent[from][to] > 0) {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(to);
                int cur = to;
                while (result.parent[from][cur] != -2) {
                    list.add(result.parent[from][cur]);
                    cur = result.parent[from][cur];
                }
                Collections.reverse(list);
                return list;
            } else {
                return Collections.emptyList();
            }
        } else throw new IllegalArgumentException();
    }

    public int cost(List<Integer> way, int[][] matrix) {
        int last = -1;
        int cost = 0;
        for (Integer v : way) {
            if (!isValid(v))
                throw new IllegalArgumentException();
            if (last != -1) {
                cost += matrix[last][v];
            }
            last = v;
        }
        return cost;
    }

    private boolean isValid(int v) {
        return v >= 0 && v < size;
    }

    static class DijkstraResult {
        int[] d;
        int[] v;
        int[] parent;

        public DijkstraResult(int[] d, int[] v, int[] parent) {
            this.d = d;
            this.v = v;
            this.parent = parent;
        }
    }

    static class FloydResult {
        int[][] d;
        int[][] parent;

        public FloydResult(int[][] d, int[][] parent) {
            this.d = d;
            this.parent = parent;
        }
    }
}
