import observers.GeneradorDeSugerencias;
import observers.MailSenderAlertas;
import observers.NotificationService;
import qmp.Atuendo;
import qmp.DisparadorDeSugerencias;
import qmp.ServicioMeteorologico;
import qmp.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SugerenciasTests {

  Usuario usuarioA;
  Usuario usuarioB;
  Usuario usuarioC;
  ServicioMeteorologico servicio;

  DisparadorDeSugerencias sugeridor;

  GeneradorDeSugerencias generador;
  GeneradorDeSugerencias recalculador;
  MailSenderAlertas mailSender;
  NotificationService notificador;

  @BeforeEach
  void setUp() {
    this.usuarioA = new Usuario("usuarioAlvaro@gmail.com");
    this.usuarioB = new Usuario("usuarioBautista@gmail.com");
    this.usuarioC = new Usuario("usuarioCamila@gmail.com");

    this.generador = new GeneradorDeSugerencias();
    this.recalculador = mock(GeneradorDeSugerencias.class);
    this.mailSender = mock(MailSenderAlertas.class);
    this.notificador = mock(NotificationService.class);
    this.servicio = mock(ServicioMeteorologico.class);

    this.usuarioA.setGenerador(this.generador);
    this.usuarioB.setGenerador(this.generador);
    this.usuarioC.setGenerador(this.generador);
    this.usuarioA.setServicioMeteorologico(this.servicio);
    this.usuarioB.setServicioMeteorologico(this.servicio);
    this.usuarioC.setServicioMeteorologico(this.servicio);

    this.sugeridor = new DisparadorDeSugerencias(Arrays.asList(usuarioA, usuarioB, usuarioC));

  }

  @Test
  void usuarioPuedeObtenerSugerenciaActualizada() {
    assertNull(usuarioA.getAtuendoActual());
    usuarioA.obtenerSugerencia();
    assertNotNull(usuarioA.getAtuendoActual());
  }

  @Test
  void usuariosObtienenSugerenciasTrasDisparoDeSugerencias() {
    assertNull(usuarioA.getAtuendoActual());

    this.sugeridor.sugerirAtuendoATodosLosUsuarios();
    Atuendo unAtuendoDeA = usuarioA.getAtuendoActual();
    Atuendo unAtuendoDeB = usuarioB.getAtuendoActual();
    Atuendo unAtuendoDeC = usuarioC.getAtuendoActual();

    assertNotNull(unAtuendoDeA);
    assertNotNull(unAtuendoDeB);
    assertNotNull(unAtuendoDeC);
  }

  @Test
  void sePuedenObtenerLasAlertasMeteorologicas() {
    when(servicio.getAlerts()).thenReturn(Arrays.asList("Tormenta", "Granizo"));

    List<String> alertas = servicio.getAlerts();

    assertNotNull(alertas);
    assertEquals(Arrays.asList("Tormenta", "Granizo"), alertas);
  }

  @Test
  void sePuedenObtenerLasUltimasAlertasMeteorologicasActualizadas() {
    List<String> alertas = new ArrayList<>();

    doAnswer(invocation -> {
      alertas.addAll(Arrays.asList("Tormenta Electrica"));
      return null;
    }).when(servicio).actualizarAlertas(any());

    servicio.actualizarAlertas("Bs As");
    assertEquals(alertas, Arrays.asList("Tormenta Electrica"));
  }

  @Test
  void usuarioAAgregaLosTresObserversYRemueveUno() {

    usuarioA.agregarObserver(recalculador);
    usuarioA.agregarObserver(notificador);
    usuarioA.agregarObserver(mailSender);

    assertEquals(usuarioA.getListaDeObservers().size(), 3);

    usuarioA.removerObserver(recalculador);

    assertEquals(usuarioA.getListaDeObservers().size(), 2);
    assertTrue(usuarioA.getListaDeObservers().contains(notificador));
    assertTrue(usuarioA.getListaDeObservers().contains(mailSender));
  }

  @Test
  void unUsusarioPuedeSerSugeridoUnAtuendoLocoDeTormentas(){
    Atuendo atuendoLocoDeTormentas = new Atuendo();
    usuarioA.obtenerSugerencia(atuendoLocoDeTormentas);
    assertEquals(atuendoLocoDeTormentas, usuarioA.getAtuendoActual());
  }

  @Test
  void unUsuarioEsAlertadoYLeLlegaUnMail(){
    usuarioA.agregarObserver(mailSender);

    doAnswer(invocation -> {
      this.mailSender.send("usuarioAlvaro@gmail.com", "Alerta de: Tormenta");
      return null;
    }).when(mailSender).recibirAlerta(any(), any());

    doNothing().when(mailSender).send(any(), any());

    sugeridor.recibirAlerta("Tormenta");

    verify(mailSender).send(any(), any());
  }

  @Test
  void siSeDisparaUnaAlertaTodosLosObserversEjecutan() {
    Atuendo atuendoLocoDeTormentas = new Atuendo();

    //primero el usuario carga sus observers
    usuarioA.agregarObserver(recalculador);
    usuarioA.agregarObserver(notificador);
    usuarioA.agregarObserver(mailSender);

    //se mockea el comportamiento de recibir alertas
    doAnswer(invocation -> {
      Usuario usuario = invocation.getArgument(0);
      usuario.obtenerSugerencia(atuendoLocoDeTormentas);
      return null;
    }).when(recalculador).recibirAlerta(any(), any());

    doAnswer(invocation -> {
      this.mailSender.send("usuarioAlvaro@gmail.com", "Alerta de: Tormenta");
      return null;
    }).when(mailSender).recibirAlerta(any(), any());

    doAnswer(invocation -> {
      this.notificador.notify("Alerta de tormenta: se recomienda llevar paraguas");
      return null;
    }).when(notificador).recibirAlerta(any(), any());

    //ahora se mockea el comportamiento no implementado
    doNothing().when(notificador).notify("Tormenta");
    doNothing().when(mailSender).send(any(), any());

    //ahora el sugeridor dispara la alerta meteorologica
    sugeridor.recibirAlerta("Tormenta");

    //como usuarioA esta suscripto, tambien la recibe y ejecutan sus observers

    //ejecuta recalculador
    assertEquals(usuarioA.getAtuendoActual(), atuendoLocoDeTormentas);
    //ejecuta mailsender
    verify(mailSender).send(any(), any());
    //ejecuta notificador
    verify(notificador).notify(any());

  }
}
