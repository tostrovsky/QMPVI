package observers;

import qmp.Usuario;

public interface ObserverAlertas {
  void recibirAlerta(Usuario usuario, String alerta);
}
