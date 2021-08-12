package onlineShop.models.products.computers;

import onlineShop.common.constants.ExceptionMessages;
import onlineShop.models.products.BaseProduct;
import onlineShop.models.products.Product;
import onlineShop.models.products.components.Component;
import onlineShop.models.products.peripherals.Peripheral;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseComputer extends BaseProduct implements Computer {

    private List<Component> components;
    private List<Peripheral> peripherals;
    private double averageOverallPerformance;

    protected BaseComputer(int id, String manufacturer, String model, double price, double overallPerformance) {
        super(id, manufacturer, model, price, overallPerformance);
        this.components = new ArrayList<>();
        this.peripherals = new ArrayList<>();
    }


    @Override
    public double getOverallPerformance() {
       if(this.components.isEmpty()){
           return this.overallPerformance;
       }

       return this.overallPerformance +
               (this.components.stream().mapToDouble(Product::getOverallPerformance).sum()
                       / this.components.size());
    }

    @Override
    public double getPrice(){
        return this.price + this.peripherals.stream()
                .mapToDouble(Product::getPrice).sum()
                + this.components.stream()
                .mapToDouble(Product::getPrice).sum();
    }

    @Override
    public List<Component> getComponents() {return this.components;}

    @Override
    public List<Peripheral> getPeripherals() {
        return this.peripherals;
    }

    @Override
    public void addComponent(Component component) {

        for (Component currentComponent : this.components) {
            if(currentComponent.getClass().getSimpleName().equals(component.getClass().getSimpleName()))
                throw new IllegalArgumentException(String.format(ExceptionMessages.EXISTING_COMPONENT,
                        component.getClass().getSimpleName(), this.getClass().getSimpleName(), this.getId()));
        }

        this.components.add(component);
    }

    @Override
    public Component removeComponent(String componentType) {
        for (Component component : this.components) {
            if(component.getClass().getSimpleName().equals(componentType)) {
                this.components.remove(component);
                return component;
            }
        }
       throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPONENT);
    }

    @Override
    public void addPeripheral(Peripheral peripheral) {
        for (Peripheral currentPeripheral : this.peripherals) {
            if(currentPeripheral.getClass().getSimpleName().equals(peripheral.getClass().getSimpleName()))
                throw new IllegalArgumentException(String.format(ExceptionMessages.EXISTING_PERIPHERAL,
                        peripheral.getClass().getSimpleName(), this.getClass().getSimpleName(), this.getId()));
        }

        this.peripherals.add(peripheral);
    }

    @Override
    public Peripheral removePeripheral(String peripheralType) {
        for (Peripheral peripheral : this.peripherals) {
            if(peripheral.getClass().getSimpleName().equals(peripheralType))
                this.peripherals.remove(peripheral);
            return peripheral;
        }
        throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_PERIPHERAL);
    }

    @Override
    public String toString() {
        StringBuilder fill = new StringBuilder();
        fill.append(super.toString())
                .append(System.lineSeparator())
                .append(String.format(" Components (%d):", this.components.size()))
                .append(System.lineSeparator());
        for (Component component : this.components) {
            fill.append(String.format("  %s", component.toString()))
                    .append(System.lineSeparator());
        }

        if(this.peripherals.isEmpty()){
            this.averageOverallPerformance = 0;
        }else {
         this.averageOverallPerformance = this.peripherals.stream()
                    .mapToDouble(Product::getOverallPerformance).sum() / this.peripherals.size();
        }

        fill.append(String.format(" Peripherals (%d); Average Overall Performance " +
                        "(%.2f):", this.peripherals.size(), averageOverallPerformance))
                .append(System.lineSeparator());
        for (Peripheral peripheral : this.peripherals) {
            fill.append(String.format("  %s", peripheral.toString()))
                    .append(System.lineSeparator());
        }
        return fill.toString().trim();
    }
}
