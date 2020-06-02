package hr.fer.zemris.optjava.dz4.part2;

public class Stick {
    private int index;
    private int length;

    public Stick(int index, int length) {
        this.index = index;
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Integer) {
            return (Integer) o == index;
        }
        if (!(o instanceof Stick)) return false;

        Stick stick = (Stick) o;

        return index == stick.index;
    }

    @Override
    public int hashCode() {
        return index;
    }
}
