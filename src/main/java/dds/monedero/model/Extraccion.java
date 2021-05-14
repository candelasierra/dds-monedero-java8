package dds.monedero.model;

import java.time.LocalDate;

public class Extraccion extends Movimiento{
  public Extraccion(LocalDate fecha, double monto){
    super(fecha, monto);
  }

  public double montoAAplicar() {
    return -getMonto();
  }

  @Override
  public boolean fueExtraido(LocalDate fecha) {
    return esDeLaFecha(fecha);
  }
}
