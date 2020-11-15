/*
 * Copyright (c) 2020 RGSW
 * Licensed under Apache 2.0 license
 */

package net.shadew.ptg.noise.perlin;

import net.shadew.ptg.noise.util.Hash;
import net.shadew.ptg.noise.RepetitiveNoise2D;

/**
 * 2D Perlin noise generator.
 */
public class RepetitivePerlin2D extends RepetitiveNoise2D {

    /**
     * Constructs a Perlin noise generator
     *
     * @param seed The seed, may be any {@link int}
     */
    public RepetitivePerlin2D( int seed, int repeat ) {
        super( seed, repeat );
    }

    /**
     * Constructs a Perlin noise generator
     *
     * @param seed  The seed, may be any {@link int}
     * @param scale The coordinate scaling along all axes
     */
    public RepetitivePerlin2D( int seed, double scale, int repeat ) {
        super( seed, scale, repeat );
    }

    /**
     * Constructs a Perlin noise generator
     *
     * @param seed   The seed, may be any {@link int}
     * @param scaleX The coordinate scaling along X axis
     * @param scaleY The coordinate scaling along Y axis
     */
    public RepetitivePerlin2D(int seed, double scaleX, double scaleY, int repeatX, int repeatY) {
        super(seed, scaleX, scaleY, repeatX, repeatY);
    }

    private long repeat(long val, int repeat) {
        return val % repeat + (val < 0 ? repeat : 0);
    }

    private int hash(long x, long y) {
        return Hash.hash2I(seed, repeat(x, repeatX), repeat(y, repeatY));
    }

    @Override
    public double generate(double x, double y) {
        x /= scaleX;
        y /= scaleY;

        return Perlin.compute(x, y, this::hash);
    }
}
