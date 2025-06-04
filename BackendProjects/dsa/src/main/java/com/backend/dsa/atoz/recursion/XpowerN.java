package com.backend.dsa.atoz.recursion;

public class XpowerN {

    public XpowerN(int x, int n) {
        System.out.println("Power of " + x + " ^ " + n + " is " + xPowerN(x, n));
        System.out.println("Power of " + x + " ^ " + n + " is in optimized way " + xPowerNoptimized(x, n));
    }

    private int xPowerNoptimized(int x, int n) {
        if (n == 0) {
            return 1;
        }

        int halfPower = xPowerNoptimized(x, n / 2);
        int halfPowerSquare = halfPower * halfPower;

        if (n % 2 != 0) {
            return x * halfPowerSquare;
        }
        return halfPowerSquare;
    }

    private int xPowerN(int x, int n) {
        // 2 ^ 3 = 2 ^ 2 & 2 ^ 1;
        if (n == 0) {
            return 1;
        }
        return x * xPowerN(x, n - 1);
    }
}
