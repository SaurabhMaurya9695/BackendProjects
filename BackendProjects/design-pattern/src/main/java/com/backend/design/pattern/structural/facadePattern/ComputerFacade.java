package com.backend.design.pattern.structural.facadePattern;

import com.backend.design.pattern.structural.facadePattern.SubSystem.BIOS;
import com.backend.design.pattern.structural.facadePattern.SubSystem.CoolingSystem;
import com.backend.design.pattern.structural.facadePattern.SubSystem.Cpu;
import com.backend.design.pattern.structural.facadePattern.SubSystem.Memory;
import com.backend.design.pattern.structural.facadePattern.SubSystem.OS;
import com.backend.design.pattern.structural.facadePattern.SubSystem.PowerSupply;

public class ComputerFacade {

    private final BIOS _bios;
    private CoolingSystem _coolingSystem;
    private Cpu _cpu;
    private Memory _memory;
    private OS _os;
    private PowerSupply _powerSupply;

    public ComputerFacade() {
        _powerSupply = new PowerSupply();
        _os = new OS();
        _memory = new Memory();
        _cpu = new Cpu();
        _coolingSystem = new CoolingSystem();
        _bios = new BIOS(_cpu, _memory);
    }

    public void startComputer() {
        _powerSupply.providePowerSupply();
        _coolingSystem.StartCoolingSystem();
        _cpu.initializeCPU();
        _memory.setUpMemory();
        _bios.startUpBIOS();
        _os.loadOS();
    }
}
