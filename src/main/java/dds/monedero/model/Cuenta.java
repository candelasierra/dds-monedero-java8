package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();


  public Cuenta() {
    saldo = 0;
  }


  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {
    validarMontoNegativo(cuanto);
    validarMaximaCantidadDepositosDiarios(3);
    agregarMovimiento(new Movimiento(LocalDate.now(), cuanto, true));
  }

  private void validarMaximaCantidadDepositosDiarios (int cantidadMaxima) {
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= cantidadMaxima) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + cantidadMaxima + " depositos diarios");
    }
  }

  public void sacar(double cuanto) {
    validarMontoNegativo(cuanto);
    validarSaldoSuficiente(cuanto);
    validarMaximaExtraccionDiaria(cuanto, 1000);
    agregarMovimiento(new Movimiento(LocalDate.now(), cuanto, false));
  }

  private void validarMaximaExtraccionDiaria (double monto, double maximaExtraccion){
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = maximaExtraccion - montoExtraidoHoy;
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + maximaExtraccion
          + " diarios, límite: " + limite);
    }
  }

  private void validarSaldoSuficiente(double monto){
    if (getSaldo() - monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  private void validarMontoNegativo (double monto){
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void agregarMovimiento(Movimiento movimiento) {
    setSaldo(getSaldo() + movimiento.montoAAplicar());
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
