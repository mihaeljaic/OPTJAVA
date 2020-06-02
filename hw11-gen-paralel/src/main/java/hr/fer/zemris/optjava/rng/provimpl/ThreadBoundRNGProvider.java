package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;

public class ThreadBoundRNGProvider implements IRNGProvider {

    @Override
    public IRNG getRNG() {
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof EVOThread)) {
            throw new RuntimeException("Thread doesn't implement IRNGProvider interface.");
        }

        return ((EVOThread) currentThread).getRNG();
    }
}
