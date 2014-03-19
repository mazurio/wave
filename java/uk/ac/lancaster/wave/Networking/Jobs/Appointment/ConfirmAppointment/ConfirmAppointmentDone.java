package uk.ac.lancaster.wave.Networking.Jobs.Appointment.ConfirmAppointment;

import uk.ac.lancaster.wave.Data.Model.Appointment;

public class ConfirmAppointmentDone {
    public Appointment appointment;

    public ConfirmAppointmentDone(Appointment appointment) {
        this.appointment = appointment;
    }
}