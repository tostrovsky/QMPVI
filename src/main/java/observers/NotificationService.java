package observers;

import qmp.Usuario;

public class NotificationService implements ObserverAlertas {

  private Usuario usuarioANotificar;

  public NotificationService() {
  }

  public void recibirAlerta(Usuario usuarioANotificar, String alerta) {
    this.usuarioANotificar = usuarioANotificar;
    switch (alerta) {
      case "Tormenta":
        notify("Alerta de tormenta: se recomienda llevar paraguas");
        break;
      case "Granizo":
        notify("Alerta de granizo: evita salir con el auto");
        break;
      default:
        break;
    }
  }

  public void notify(String text) {
    usuarioANotificar.obtenerAlertasEnBsAsArgentina();
    //TODO notificar al usuario
  }
}
