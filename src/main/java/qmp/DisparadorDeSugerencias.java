package qmp;

import java.util.List;

public class DisparadorDeSugerencias {
  private final List<Usuario> listaDeUsuarios;

  public DisparadorDeSugerencias(List<Usuario> lista) {
    this.listaDeUsuarios = lista;
  }

  public void sugerirAtuendoATodosLosUsuarios() {
    this.listaDeUsuarios.forEach(usuario -> usuario.obtenerSugerencia());
  }

  public void recibirAlerta(String alerta) {
    this.listaDeUsuarios.forEach(usuario -> usuario.ejecutarObservers(alerta));
  }
}
