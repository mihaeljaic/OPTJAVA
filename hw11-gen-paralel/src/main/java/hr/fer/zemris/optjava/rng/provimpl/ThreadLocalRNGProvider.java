package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

public class ThreadLocalRNGProvider implements IRNGProvider {
    private ThreadLocal<IRNG> threadLocal;

    public ThreadLocalRNGProvider() {
        threadLocal = new ThreadLocal<>();
    }

    public IRNG getRNG() {
        IRNG random = threadLocal.get();
        if (random == null) {
            System.out.println("Creating new rng implementation");
            random = new RNGRandomImpl();
            threadLocal.set(random);
        }

        return random;
    }
}
