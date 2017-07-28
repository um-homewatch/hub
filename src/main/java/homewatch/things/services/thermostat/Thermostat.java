package homewatch.things.services.thermostat;

import homewatch.things.Thing;

public class Thermostat implements Thing {
  //celsius
  private double targetTemperature;

  public Thermostat() {
    this.targetTemperature = 0;
  }

  public Thermostat(double targetTemperature) {
    this.targetTemperature = targetTemperature;
  }

  public double getTargetTemperature() {
    return targetTemperature;
  }

  public void setTargetTemperature(double targetTemperature) {
    this.targetTemperature = targetTemperature;
  }

  @Override
  public ThermostatServiceFactory getFactory() {
    return new ThermostatServiceFactory();
  }

  @Override
  public String getStringRepresentation() {
    return "thermostats";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Thermostat that = (Thermostat) o;

    return Double.compare(that.targetTemperature, targetTemperature) == 0;
  }

  @Override
  public int hashCode() {
    long temp = Double.doubleToLongBits(targetTemperature);
    return (int) (temp ^ (temp >>> 32));
  }

  @Override
  public String toString() {
    return "Thermostat{" +
            "targetTemperature=" + targetTemperature +
            '}';
  }
}
