package things.thermostat;

public class Thermostat {
  //celsius
  private double targetTemperature;

  public Thermostat() {
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
}
