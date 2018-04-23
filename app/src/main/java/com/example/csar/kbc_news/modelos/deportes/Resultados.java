package com.example.csar.kbc_news.modelos.deportes;

/**
 * Created by braya on 4/22/2018.
 */

public class Resultados {
    private Evento sport_event;
    private EventoEstatus sport_event_status;

    public Evento getSport_event() {
        return sport_event;
    }

    public void setSport_event(Evento sport_event) {
        this.sport_event = sport_event;
    }

    public EventoEstatus getSport_event_status() {
        return sport_event_status;
    }

    public void setSport_event_status(EventoEstatus sport_event_status) {
        this.sport_event_status = sport_event_status;
    }

    @Override
    public String toString() {
        return "Resultados{" +
                "sport_event=" + sport_event +
                ", sport_event_status=" + sport_event_status +
                '}';
    }
}
