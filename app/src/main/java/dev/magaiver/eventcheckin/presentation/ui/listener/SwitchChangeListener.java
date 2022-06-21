package dev.magaiver.eventcheckin.presentation.ui.listener;


import dev.magaiver.eventcheckin.domain.model.Event;

public interface SwitchChangeListener {
    void switchChangeListener(Event event, boolean isChecked);
}
