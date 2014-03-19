package uk.ac.lancaster.wave.Networking.Jobs.Appointment.CancelAppointment;

import uk.ac.lancaster.wave.Data.Model.Appointment;

public class CancelAppointmentDone {
    public Appointment appointment;

    public CancelAppointmentDone(Appointment appointment) {
        this.appointment = appointment;
    }
}
