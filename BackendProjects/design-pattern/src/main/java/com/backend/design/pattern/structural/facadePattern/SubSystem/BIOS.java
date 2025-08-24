package com.backend.design.pattern.structural.facadePattern.SubSystem;

public class BIOS {

    Cpu _cpu;
    Memory _memory;

    public BIOS(Cpu cpu, Memory memory) {
        _cpu = cpu;
        _memory = memory;
    }

    public void startUpBIOS() {
        System.out.println("Starting UP BIOS");
    }
}
