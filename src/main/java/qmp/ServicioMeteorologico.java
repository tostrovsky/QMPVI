package qmp;

import java.math.BigDecimal;
import java.util.List;

public interface ServicioMeteorologico {

  BigDecimal getTemperature(String string);

  void setTemperatura(BigDecimal temperatura);

  List<String> getAlerts();

  void actualizarAlertas(String string);
}
