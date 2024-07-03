package edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.jms;

import edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.websocket.WebSocketPosluzitelj;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.json.bind.JsonbBuilder;

@MessageDriven(mappedName = "jms/nwtisQ", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class NadzornikKazni implements MessageListener {

  public NadzornikKazni() {}

  public void onMessage(Message message) {
    ObjectMessage msg = null;

    if (message instanceof ObjectMessage) {
      try {
        System.out.println("Stigla poruka: " + msg.getObject() + " " + message.getJMSMessageID()
        + " " + new java.util.Date(message.getJMSTimestamp()));
        // TODO ovo je samo za provjeru. Kasnije obrisati!
        WebSocketPosluzitelj.send(msg.getObject().toString());
        // TODO napraviti prema opisu zadaće.
      } catch (JMSException ex) {
        ex.printStackTrace();
      }
    }
  }
}
