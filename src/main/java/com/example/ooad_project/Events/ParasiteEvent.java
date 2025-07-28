package com.example.ooad_project.Events;

import com.example.ooad_project.Parasite.Parasite;

public class ParasiteEvent {

    private final Parasite detectedInvader;

    public ParasiteEvent(Parasite invasiveOrganism) {
        this.detectedInvader = invasiveOrganism;
    }

    public Parasite getParasite() {
        return detectedInvader;
    }


}
