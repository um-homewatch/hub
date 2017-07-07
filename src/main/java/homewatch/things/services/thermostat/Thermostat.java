package homewatch.things.services.thermostat;

public class Thermostat {
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
}
