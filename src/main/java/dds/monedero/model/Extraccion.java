package dds.monedero.model;

import java.time.LocalDate;

public class Extraccion extends Movimiento {

  public Extraccion(LocalDate fecha, double monto, boolean esDeposito) {
    super(fecha, monto, esDeposito);
  }

  @Override
  public boolean isExtraccion() {
    return true;
  }

  @Override
  public double calcularValor(Cuenta cuenta) {
    return cuenta.getSaldo() - getMonto();
  }

}
