package hr.fer.zemris.trisat;

import java.util.Iterator;

public class BitVectorNGenerator implements Iterable<MutableBitVector> {
    private BitVector assignment;

    public BitVectorNGenerator(BitVector assignment) {
        this.assignment = assignment;
    }

    public void setAssignment(BitVector assignment) {
        this.assignment = assignment;
    }

    @Override
    public Iterator<MutableBitVector> iterator() {
        return new NeighborhoodIterator();
    }

    private class NeighborhoodIterator implements Iterator<MutableBitVector> {
        private int currentNeighboor = 0;

        private NeighborhoodIterator() {
        }

        @Override
        public boolean hasNext() {
            return currentNeighboor < assignment.getSize();
        }

        @Override
        public MutableBitVector next() {
            MutableBitVector neighboor = assignment.copy();
            neighboor.set(currentNeighboor, assignment.get(currentNeighboor) ^ true);

            currentNeighboor++;
            return neighboor;
        }

        @Override
        public void remove() {
        }
    }

    public MutableBitVector[] createNeighborhood() {
        MutableBitVector[] neighborhood = new MutableBitVector[assignment.getSize()];
        int i = 0;
        Iterator<MutableBitVector> it = new NeighborhoodIterator();
        while (it.hasNext()) {
            neighborhood[i] = it.next();
            i++;
        }

        return neighborhood;
    }

}
