/*
 * Copyright (c) 2020 RGSW
 * Licensed under Apache 2.0 license
 */

package net.shadew.ptg.noise.opensimplex;

import net.shadew.ptg.noise.BaseNoise3D;
import net.shadew.ptg.noise.util.Hash;

/**
 * 3D OpenSimplex noise generator based on an
 * <a href=https://gist.github.com/KdotJPG/b1270127455a94ac5d19>implementation by Kurt Spencer</a>
 */
public class OpenSimplex3D extends BaseNoise3D {

    /**
     * Constructs an OpenSimplex noise generator
     *
     * @param seed The seed, may be any {@link int}
     */
    public OpenSimplex3D(int seed) {
        super(seed);
    }

    /**
     * Constructs an OpenSimplex noise generator
     *
     * @param seed  The seed, may be any {@link int}
     * @param scale The coordinate scaling along all axes
     */
    public OpenSimplex3D(int seed, double scale) {
        super(seed, scale);
    }

    /**
     * Constructs an OpenSimplex noise generator
     *
     * @param seed   The seed, may be any {@link int}
     * @param scaleX The coordinate scaling along X axis
     * @param scaleY The coordinate scaling along Y axis
     * @param scaleZ The coordinate scaling along Z axis
     */
    public OpenSimplex3D(int seed, double scaleX, double scaleY, double scaleZ) {
        super(seed, scaleX, scaleY, scaleZ);
    }

    private int hash(long x, long y, long z) {
        return Hash.hash3I(seed, x, y, z);
    }

    @Override
    public double generate(double x, double y, double z) {
        x /= scaleX;
        y /= scaleY;
        z /= scaleZ;

        return OpenSimplex.compute(x / 2, y / 2, z / 2, this::hash);
    }
}
