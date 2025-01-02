package com.revature.globetrotters.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "travel_plan_collaborator")
public class Collaborator {
    @EmbeddedId
    private CollaboratorId id;

    public Collaborator() {
    }

    public Collaborator(CollaboratorId id) {
        this.id = id;
    }

    public Collaborator(Integer collaboratorId, Integer travelPlanId) {
        id = new CollaboratorId(collaboratorId, travelPlanId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collaborator that = (Collaborator) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Collaborator{" +
                "id=" + id +
                '}';
    }

    public CollaboratorId getId() {
        return id;
    }

    public void setId(CollaboratorId id) {
        this.id = id;
    }

    @Embeddable
    public static class CollaboratorId implements Serializable {
        @Column(name = "collaborator_id")
        private Integer collaboratorId;
        @Column(name = "travel_plan_id")
        private Integer travelPlanId;

        public CollaboratorId(Integer collaboratorId, Integer travelPlanId) {
            this.collaboratorId = collaboratorId;
            this.travelPlanId = travelPlanId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CollaboratorId that = (CollaboratorId) o;
            return Objects.equals(collaboratorId, that.collaboratorId) && Objects.equals(travelPlanId, that.travelPlanId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(collaboratorId, travelPlanId);
        }

        @Override
        public String toString() {
            return "CollaboratorId{" +
                    "collaboratorId=" + collaboratorId +
                    ", travelPlanId=" + travelPlanId +
                    '}';
        }

        public Integer getCollaboratorId() {
            return collaboratorId;
        }

        public void setCollaboratorId(Integer collaboratorId) {
            this.collaboratorId = collaboratorId;
        }

        public Integer getTravelPlanId() {
            return travelPlanId;
        }

        public void setTravelPlanId(Integer travelPlanId) {
            this.travelPlanId = travelPlanId;
        }
    }
}
