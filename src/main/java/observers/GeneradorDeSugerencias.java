package observers;


import qmp.Atuendo;
import qmp.Usuario;

public class GeneradorDeSugerencias implements ObserverAlertas {

  private String ultimaAlerta;

  public GeneradorDeSugerencias() {
  }

  public Atuendo generarAtuendo() {
    this.ultimaAlerta = this.ultimaAlerta + "g";
    //TODO usar this.ultimaAlerta para generar el atuendo
    return new Atuendo();
  }

  public void recibirAlerta(Usuario usuario, String alerta) {
    this.ultimaAlerta = alerta;
    usuario.obtenerSugerencia(this.generarAtuendo());
  }
}
