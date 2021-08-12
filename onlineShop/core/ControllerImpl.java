package onlineShop.core;

import onlineShop.common.constants.ExceptionMessages;
import onlineShop.common.constants.OutputMessages;
import onlineShop.core.interfaces.Controller;
import onlineShop.models.products.Product;
import onlineShop.models.products.components.*;
import onlineShop.models.products.computers.Computer;
import onlineShop.models.products.computers.DesktopComputer;
import onlineShop.models.products.computers.Laptop;
import onlineShop.models.products.peripherals.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ControllerImpl implements Controller {
        private List<Computer> computers;
        private List<Component> components ;
        private List<Peripheral> peripherals ;

    public ControllerImpl() {
        this.computers = new ArrayList<>();
        this.components = new ArrayList<>();
        this.peripherals = new ArrayList<>();
    }

    @Override
    public String addComputer(String computerType, int id, String manufacturer, String model, double price) {

        for (Computer computer : this.computers) {
            if(computer.getId() == id){
                throw new IllegalArgumentException(ExceptionMessages.EXISTING_COMPUTER_ID);
            }
        }
        Computer current = null;
        switch (computerType){
            case "DesktopComputer":
                current = new DesktopComputer(id, manufacturer, model, price);
                break;
            case "Laptop":
                current = new Laptop(id, manufacturer, model, price);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_COMPUTER_TYPE);
        }
        this.computers.add(current);
        return String.format(OutputMessages.ADDED_COMPUTER, id);
    }

    @Override
    public String addPeripheral(int computerId, int id, String peripheralType, String manufacturer, String model, double price, double overallPerformance, String connectionType) {
        for (Peripheral peripheral: this.peripherals) {
            if(peripheral.getId() == id){
                throw new IllegalArgumentException(ExceptionMessages.EXISTING_PERIPHERAL_ID);
            }
        }
        Computer currentComputer = this.computers.stream()
                .filter(c -> c.getId() == computerId)
                .findFirst()
                .orElse(null);
        if(currentComputer == null){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        Peripheral currentPeripheral = null;
        switch (peripheralType){
            case "Headset":
                currentPeripheral = new Headset(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Keyboard":
                currentPeripheral = new Keyboard(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Monitor":
                currentPeripheral = new Monitor(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Mouse":
                currentPeripheral = new Mouse(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_PERIPHERAL_TYPE);
        }
        currentComputer.addPeripheral(currentPeripheral);
        this.peripherals.add(currentPeripheral);
        return String.format(OutputMessages.ADDED_PERIPHERAL, peripheralType, id, computerId);
    }

    @Override
    public String removePeripheral(String peripheralType, int computerId) {
        Computer currentComputer = this.computers.stream()
                .filter(c -> c.getId() == computerId)
                .findFirst()
                .orElse(null);
        if(currentComputer == null){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }

        Peripheral currentPeripheral = currentComputer.getPeripherals().stream()
                .filter(p -> p.getClass().getSimpleName().equals(peripheralType))
                .findFirst()
                .orElse(null);
        if(currentPeripheral == null){
            throw new IllegalArgumentException(String.format(ExceptionMessages.NOT_EXISTING_PERIPHERAL,
                    peripheralType, currentComputer.getClass().getSimpleName(), currentComputer.getId()));
        }
        currentComputer.removePeripheral(peripheralType);
        this.peripherals.remove(currentPeripheral);
        return String.format(OutputMessages.REMOVED_PERIPHERAL, peripheralType, currentPeripheral.getId());
    }

    @Override
    public String addComponent(int computerId, int id, String componentType, String manufacturer, String model, double price, double overallPerformance, int generation) {
        for (Component component: this.components) {
            if(component.getId() == id){
                throw new IllegalArgumentException(ExceptionMessages.EXISTING_COMPONENT_ID);
            }
        }
        Computer currentComputer = this.computers.stream()
                .filter(c -> c.getId() == computerId)
                .findFirst()
                .orElse(null);
        if(currentComputer == null){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        Component currentComponent = null;
        switch (componentType){
            case "VideoCard":
                currentComponent = new VideoCard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "CentralProcessingUnit":
                currentComponent = new CentralProcessingUnit(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "Motherboard":
                currentComponent = new Motherboard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "PowerSupply":
                currentComponent = new PowerSupply(id, manufacturer, model, price, overallPerformance, generation);
               break;
            case "RandomAccessMemory":
                currentComponent = new RandomAccessMemory(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "SolidStateDrive":
                currentComponent = new SolidStateDrive(id, manufacturer, model, price, overallPerformance, generation);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_COMPONENT_TYPE);
        }
        currentComputer.addComponent(currentComponent);
        this.components.add(currentComponent);
        return String.format(OutputMessages.ADDED_COMPONENT, componentType, id, computerId);
    }

    @Override
    public String removeComponent(String componentType, int computerId) {
        Computer currentComputer = this.computers.stream()
                .filter(c -> c.getId() == computerId)
                .findFirst()
                .orElse(null);
        if(currentComputer == null){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        Component currentComponent = currentComputer.getComponents().stream()
                .filter(c -> c.getClass().getSimpleName().equals(componentType))
                .findFirst()
                .orElse(null);
        if(currentComponent == null){
            throw new IllegalArgumentException(String.format(ExceptionMessages.NOT_EXISTING_COMPONENT,
                    componentType, currentComputer.getClass().getSimpleName(), currentComputer.getId()));
        }
        currentComputer.removeComponent(componentType);
        this.components.remove(currentComponent);
        return String.format(OutputMessages.REMOVED_PERIPHERAL, componentType, currentComponent.getId());
    }

    @Override
    public String buyComputer(int id) {
        Computer currentComputer = this.computers.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        if(currentComputer == null){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        this.computers.remove(currentComputer);
        return currentComputer.toString();
    }

     @Override
    public String BuyBestComputer(double budget) {
        Computer computer = this.computers.stream().filter(c -> c.getPrice() <= budget)
                .max(Comparator.comparingDouble(Computer::getOverallPerformance))
                .stream().findFirst().orElse(null);

        if (computer == null){
            throw new IllegalArgumentException(String.format(ExceptionMessages.CAN_NOT_BUY_COMPUTER,budget));
        }
        this.computers.remove(computer);
        return computer.toString();
    }

    @Override
    public String getComputerData(int id) {
        Computer currentComputer = this.computers.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        if(currentComputer == null){
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }
        return currentComputer.toString();
    }
}
