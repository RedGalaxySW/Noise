/*
 * Copyright (c) 2019 RGSW
 * All rights reserved. Do not distribute.
 * This file is part of the Modernity, and is licensed under the terms and conditions of RedGalaxy.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package net.rgsw.noise;

/**
 * Generic interface of a noise generator for 3D space.
 */
@FunctionalInterface
public interface INoise3D {
    /**
     * Generates noise at integer coordinates. This simply converts the integers to doubles and calls {@link
     * #generate(double, double, double)}.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @return The generated noise value
     */
    default double generate( int x, int y, int z ) {
        return this.generate( (double) x, (double) y, (double) z );
    }


    default double generateMultiplied( double x, double y, double z, double multiplier ) {
        return generate( x, y, z ) * multiplier;
    }

    /**
     * Generates noise at the specified coordinates.
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @return The generated noise value
     */
    double generate( double x, double y, double z );

    /**
     * Creates a noise generator that adds a constant value to the generated noise of this noise generator.
     *
     * @param amount The constant value to add
     * @return The created noise generator
     */
    default INoise3D add( double amount ) {
        return ( x, y, z ) -> generate( x, y, z ) + amount;
    }

    /**
     * Creates a noise generator that subtracts a constant value from the generated noise of this noise generator.
     *
     * @param amount The constant value to subtract
     * @return The created noise generator
     */
    default INoise3D subtract( double amount ) {
        return ( x, y, z ) -> generate( x, y, z ) - amount;
    }

    /**
     * Creates a noise generator that multiplies the noise generated by this generator with a constant value.
     *
     * @param amount The constant value to multiply with
     * @return The created noise generator
     */
    default INoise3D multiply( double amount ) {
        return ( x, y, z ) -> generate( x, y, z ) * amount;
    }

    /**
     * Creates a noise generator that divides the noise generated by this generator by a constant value.
     *
     * @param amount The constant value to divide by
     * @return The created noise generator
     */
    default INoise3D divide( double amount ) {
        return ( x, y, z ) -> generate( x, y, z ) / amount;
    }


    /**
     * Creates a noise generator that adds the noise generated by the specified generator to the noise generated by this
     * generator.
     *
     * @param amount The noise generator that generates noise to add
     * @return The created noise generator
     */
    default INoise3D add( INoise3D amount ) {
        return ( x, y, z ) -> generate( x, y, z ) + amount.generate( x, y, z );
    }

    /**
     * Creates a noise generator that subtracts the noise generated by the specified generator from the noise generated
     * by this generator.
     *
     * @param amount The noise generator that generates noise to subtract
     * @return The created noise generator
     */
    default INoise3D subtract( INoise3D amount ) {
        return ( x, y, z ) -> generate( x, y, z ) - amount.generate( x, y, z );
    }

    /**
     * Creates a noise generator that multiplies the noise generated by this generator with the noise generated by the
     * specified generator.
     *
     * @param amount The noise generator that generates noise to multiply with
     * @return The created noise generator
     */
    default INoise3D multiply( INoise3D amount ) {
        return ( x, y, z ) -> generate( x, y, z ) * amount.generate( x, y, z );
    }

    /**
     * Creates a noise generator that divides the noise generated by this generator by the noise generated by the
     * specified generator.
     *
     * @param amount The noise generator that generates noise to divide by
     * @return The created noise generator
     */
    default INoise3D divide( INoise3D amount ) {
        return ( x, y, z ) -> generate( x, y, z ) / amount.generate( x, y, z );
    }

    /**
     * Creates a noise generator that generates the inverted noise field of this generator.
     *
     * @return The created noise generator
     */
    default INoise3D inverse() {
        return ( x, y, z ) -> - generate( x, y, z );
    }

    /**
     * Creates a noise generator that interpolates the noise of this generator between the specified constant limits,
     * using linear interpolation.
     *
     * @param min The constant minimum limit
     * @param max The constant maximum limit
     * @return The created noise generator
     */
    default INoise3D lerp( double min, double max ) {
        return ( x, y, z ) -> NoiseMath.lerp( min, max, ( generate( x, y, z ) + 1 ) / 2 );
    }

    /**
     * Creates a noise generator that interpolates the noise of this generator between the the limits generated by the
     * specified noise generators, using linear interpolation.
     *
     * @param min The minimum limit generator
     * @param max The maximum limit generator
     * @return The created noise generator
     */
    default INoise3D lerp( INoise3D min, INoise3D max ) {
        return ( x, y, z ) -> NoiseMath.lerp( min.generate( x, y, z ), max.generate( x, y, z ), ( generate( x, y, z ) + 1 ) / 2 );
    }

    /**
     * Creates a noise generator that generates a constant value.
     *
     * @param value The value to generate
     * @return The created noise generator
     */
    static INoise3D constant( double value ) {
        return ( x, y, z ) -> value;
    }

    /**
     * Creates a 3D noise generator from a 2D noise generator, ignoring the X axis.
     *
     * @param noise The 2D noise generator
     * @return The created 3D noise generator
     */
    static INoise3D from2DX( INoise2D noise ) {
        return ( x, y, z ) -> noise.generate( y, z );
    }

    /**
     * Creates a 3D noise generator from a 2D noise generator, ignoring the Y axis.
     *
     * @param noise The 2D noise generator
     * @return The created 3D noise generator
     */
    static INoise3D from2DY( INoise2D noise ) {
        return ( x, y, z ) -> noise.generate( x, z );
    }

    /**
     * Creates a 3D noise generator from a 2D noise generator, ignoring the Z axis.
     *
     * @param noise The 2D noise generator
     * @return The created 3D noise generator
     */
    static INoise3D from2DZ( INoise2D noise ) {
        return ( x, y, z ) -> noise.generate( x, y );
    }

    /**
     * Creates a noise generator that combines the noises generated by multiple noise generators using the specified
     * function. The values of the noise generators will be passed as varargs to the function.
     *
     * @param combiner The combiner function
     * @param noises   The noise generators to combine
     * @return The created noise generator
     */
    static INoise3D combine( IDoubleFunction combiner, INoise3D... noises ) {
        return ( x, y, z ) -> {
            double[] arr = new double[ noises.length ];
            int i = 0;
            for( INoise3D noise : noises ) {
                arr[ i ] = noise.generate( x, y, z );
            }
            return combiner.combine( arr );
        };
    }

    /**
     * Creates a noise generator that scales the noise field generated by this generator.
     *
     * @param scale The scaling of the noise field for every coordinate.
     * @return The created noise generator
     */
    default INoise3D scale( double scale ) {
        return ( x, y, z ) -> generate( x * scale, y * scale, z * scale );
    }

    /**
     * Creates a noise generator that scales the noise field generated by this generator.
     *
     * @param x The scaling of the noise field for the X coordinate.
     * @param y The scaling of the noise field for the Y coordinate.
     * @param z The scaling of the noise field for the Z coordinate.
     * @return The created noise generator
     */
    default INoise3D scale( double x, double y, double z ) {
        return ( x1, y1, z1 ) -> generate( x1 * x, y1 * y, z1 * z );
    }

    /**
     * Creates a noise generator that translates the noise field generated by this generator.
     *
     * @param x The translation of the noise field along the X coordinate.
     * @param y The translation of the noise field along the Y coordinate.
     * @param z The translation of the noise field along the Z coordinate.
     * @return The created noise generator
     */
    default INoise3D translate( double x, double y, double z ) {
        return ( x1, y1, z1 ) -> generate( x1 + x, y1 + y, z1 * z );
    }

    /**
     * Generates a fractal noise generator from this noise generator using a specified amount of octaves.
     *
     * @param octaves The amount of octaves
     * @return The created noise generator
     */
    default INoise3D fractal( int octaves ) {
        return ( x, y, z ) -> {
            double n = 0;
            double m = 1;
            for( int i = 0; i < octaves; i++ ) {
                n += generate( x / m, y / m, z / m ) * m;
                m /= 2;
            }
            return n;
        };
    }

    /**
     * Creates a cell noise generator with a specific seed.
     *
     * @param seed The seed
     * @return The created noise generator
     */
    static INoise3D random( int seed ) {
        return ( x, y, z ) -> Hash.hash3D( seed, (int) x, (int) y, (int) z ) * 2 - 1;
    }
}
