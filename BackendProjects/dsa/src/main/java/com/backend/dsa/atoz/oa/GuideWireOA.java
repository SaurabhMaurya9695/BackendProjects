package com.backend.dsa.atoz.oa;

public class GuideWireOA {

    public GuideWireOA(int AA, int AB, int BB) {
        guideWireOA(AA, AB, BB);
    }

    private void guideWireOA(int AA, int AB, int BB) {
        if (AA == 0 && AB == 0 && BB == 0) {
            System.out.println("");
        } else if (AA > 0 && (AA == AB && AA == BB)) {
            boolean ok = false;
            for (int i = 0; i < AA + BB; i++) {
                if (ok) {
                    System.out.print("AA");
                    ok = false;
                } else {
                    System.out.print("BB");
                    ok = true;
                }
            }
            while (AB-- > 0) {
                System.out.print("AB");
            }
            System.out.println();
        } else if (AA == 0 && AB == BB) {
            System.out.print("BB");
            while (AB-- > 0) {
                System.out.print("AB");
            }
            System.out.println();
        } else if (BB == 0 && AB == AA) {
            while (AB-- > 0) {
                System.out.print("AB");
            }
            System.out.println("AA");
        } else if (AB == 0 && AA == BB) {
            boolean ok = false;
            String prev = "";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < AA + BB; i++) {
                if (ok) {
                    if (prev == "AA") {
                        break;
                    }
                    sb.append("AA");
                    prev = "AA";
                    ok = false;
                } else {
                    if (prev == "BB") {
                        break;
                    }
                    sb.append("BB");
                    prev = "BB";
                    ok = true;
                }
            }
            System.out.println(sb.toString());
        } else if (AA == 0 && AB != BB) {
            if (BB > 0) {
                System.out.print("BB");
            }
            while (AB-- > 0) {
                System.out.print("AB");
            }
            System.out.println();
        } else if (BB == 0 && AA != BB) {
            while (AB-- > 0) {
                System.out.print("AB");
            }
            if (AA > 0) {
                System.out.println("AA");
            }
            System.out.println();
        } else if (AB == 0 && AA != BB) {
            boolean ok = false;
            String prev = "";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < AA + BB; i++) {
                if (ok) {
                    if (prev == "AA") {
                        break;
                    }
                    sb.append("AA");
                    prev = "AA";
                    ok = false;
                } else {
                    if (prev == "BB") {
                        break;
                    }
                    sb.append("BB");
                    prev = "BB";
                    ok = true;
                }
            }
            System.out.println(sb.toString());
        } else if (AB != AA && BB != AA) {
            if (AA > BB && AA > AB) {
                System.out.print("AABB");
                AA--;
                BB--;
                while (AB-- > 0) {
                    System.out.print("AB");
                }
                boolean ok = false;
                String prev = "";
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < AA + BB; i++) {
                    if (ok) {
                        if (prev == "AA") {
                            break;
                        }
                        sb.append("AA");
                        prev = "AA";
                        ok = false;
                    } else {
                        if (prev == "BB") {
                            break;
                        }
                        sb.append("BB");
                        prev = "BB";
                        ok = true;
                    }
                }
                System.out.println(sb.toString());
            } else {
                System.out.print("BBAABB");
                BB -= 2;
                AA -= 1;
                boolean ok = false;
                String prev = "";
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < AA + BB; i++) {
                    if (ok) {
                        if (prev == "AA") {
                            break;
                        }
                        sb.append("AA");
                        prev = "AA";
                        ok = false;
                    } else {
                        if (prev == "BB") {
                            break;
                        }
                        sb.append("BB");
                        prev = "BB";
                        ok = true;
                    }
                }
                System.out.println(sb.toString());
            }
        }
    }
}
