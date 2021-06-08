package qmp;

import java.util.ArrayList;
import java.util.List;
import observers.GeneradorDeSugerencias;
import observers.ObserverAlertas;



public class Usuario {
  private final List<ObserverAlertas> listaDeObservers;
  private final String mail;
  private ServicioMeteorologico servicioMeteorologico;
  private GeneradorDeSugerencias generador;
  private Atuendo atuendoActual;

  public Usuario(String mail) {
    this.listaDeObservers = new ArrayList<>();
    this.mail = mail;
  }

  public void setServicioMeteorologico(ServicioMeteorologico servicioMeteorologico) {
    this.servicioMeteorologico = servicioMeteorologico;
  }

  public void setGenerador(GeneradorDeSugerencias generador) {
    this.generador = generador;
  }

  public void obtenerSugerencia() {
    this.atuendoActual = this.generador.generarAtuendo();
  }

  public void obtenerSugerencia(Atuendo atuendo) {
    this.atuendoActual = atuendo;
  }

  public List<String> obtenerAlertasEnBsAsArgentina() {
    return this.servicioMeteorologico.getAlerts();
  }

  public Atuendo getAtuendoActual() {
    return atuendoActual;
  }

  public void agregarObserver(ObserverAlertas observerAlertas) {
    this.listaDeObservers.add(observerAlertas);
  }

  public void removerObserver(ObserverAlertas observerAlertas) {
    this.listaDeObservers.remove(observerAlertas);
  }

  public String getMail() {
    return mail;
  }

  public void ejecutarObservers(String alerta) {
    this.listaDeObservers.forEach(observer -> observer.recibirAlerta(this, alerta));
  }

  public List<ObserverAlertas> getListaDeObservers() {
    return listaDeObservers;
  }
}
