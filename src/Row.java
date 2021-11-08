import javafx.beans.property.SimpleStringProperty;

public class Row {
    private SimpleStringProperty name;
    private SimpleStringProperty a;
    private SimpleStringProperty b;
    private SimpleStringProperty c;
    private SimpleStringProperty d;
    private SimpleStringProperty e;
    private SimpleStringProperty f;
    private SimpleStringProperty g;
    private SimpleStringProperty h;
    private SimpleStringProperty i;
    private SimpleStringProperty j;

    public Row(String name, String a, String b, String c, String d, String e, String f, String g, String h, String i, String j) {
        this.name = new SimpleStringProperty(name);
        this.a = new SimpleStringProperty(a);
        this.b = new SimpleStringProperty(b);
        this.c = new SimpleStringProperty(c);
        this.d = new SimpleStringProperty(d);
        this.e = new SimpleStringProperty(e);
        this.f = new SimpleStringProperty(f);
        this.g = new SimpleStringProperty(g);
        this.h = new SimpleStringProperty(h);
        this.i = new SimpleStringProperty(i);
        this.j = new SimpleStringProperty(j);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getA() {
        return a.get();
    }

    public SimpleStringProperty aProperty() {
        return a;
    }

    public void setA(String a) {
        this.a.set(a);
    }

    public String getB() {
        return b.get();
    }

    public SimpleStringProperty bProperty() {
        return b;
    }

    public void setB(String b) {
        this.b.set(b);
    }

    public String getC() {
        return c.get();
    }

    public SimpleStringProperty cProperty() {
        return c;
    }

    public void setC(String c) {
        this.c.set(c);
    }

    public String getD() {
        return d.get();
    }

    public SimpleStringProperty dProperty() {
        return d;
    }

    public void setD(String d) {
        this.d.set(d);
    }

    public String getE() {
        return e.get();
    }

    public SimpleStringProperty eProperty() {
        return e;
    }

    public void setE(String e) {
        this.e.set(e);
    }

    public String getF() {
        return f.get();
    }

    public SimpleStringProperty fProperty() {
        return f;
    }

    public void setF(String f) {
        this.f.set(f);
    }

    public String getG() {
        return g.get();
    }

    public SimpleStringProperty gProperty() {
        return g;
    }

    public void setG(String g) {
        this.g.set(g);
    }

    public String getH() {
        return h.get();
    }

    public SimpleStringProperty hProperty() {
        return h;
    }

    public void setH(String h) {
        this.h.set(h);
    }

    public String getI() {
        return i.get();
    }

    public SimpleStringProperty iProperty() {
        return i;
    }

    public void setI(String i) {
        this.i.set(i);
    }

    public String getJ() {
        return j.get();
    }

    public SimpleStringProperty jProperty() {
        return j;
    }

    public void setJ(String j) {
        this.j.set(j);
    }
}
