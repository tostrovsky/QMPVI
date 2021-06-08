package observers;

import qmp.Usuario;

public class MailSenderAlertas implements MailSender, ObserverAlertas {

  public MailSenderAlertas(Usuario usuarioAEnviarMail) {
  }

  public void recibirAlerta(Usuario usuarioAEnviarMail, String alerta) {

    send(usuarioAEnviarMail.getMail(), "Alerta de: " + alerta);
  }

  public void send(String adress, String message) {
    //TODO
  }

}
