package ru.albemuth.jcuda.jcusegsort;

/**
 * @author Vladimir Kornyshev { @literal <gnuzzz@mail.ru>}
 */
public enum Datatype {

    BOOLEAN(0), BYTE(1), CHAR(2), SHORT(3), INT(4), LONG(5), FLOAT(6), DOUBLE(7);

    int type;

    Datatype(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
