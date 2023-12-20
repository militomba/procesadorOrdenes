package ar.edu.um.programacion2.procesadorordenes.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Orden.
 */
@Entity
@Table(name = "orden")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Orden implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "operacion")
    private String operacion;

    @Column(name = "fecha_operacion")
    private ZonedDateTime fechaOperacion;

    @Column(name = "modo")
    private String modo;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "accion_id")
    private Integer accionId;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "cliente")
    private Integer cliente;

    @Column(name = "accion")
    private String accion;

    @Column(name = "operacion_exitosa")
    private Boolean operacionExitosa;

    @Column(name = "operacion_observaciones")
    private String operacionObservaciones;

    @Column(name = "reportada")
    private Boolean reportada;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orden id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperacion() {
        return this.operacion;
    }

    public Orden operacion(String operacion) {
        this.setOperacion(operacion);
        return this;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public ZonedDateTime getFechaOperacion() {
        return this.fechaOperacion;
    }

    public Orden fechaOperacion(ZonedDateTime fechaOperacion) {
        this.setFechaOperacion(fechaOperacion);
        return this;
    }

    public void setFechaOperacion(ZonedDateTime fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getModo() {
        return this.modo;
    }

    public Orden modo(String modo) {
        this.setModo(modo);
        return this;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public Orden precio(Double precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getAccionId() {
        return this.accionId;
    }

    public Orden accionId(Integer accionId) {
        this.setAccionId(accionId);
        return this;
    }

    public void setAccionId(Integer accionId) {
        this.accionId = accionId;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Orden cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCliente() {
        return this.cliente;
    }

    public Orden cliente(Integer cliente) {
        this.setCliente(cliente);
        return this;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public String getAccion() {
        return this.accion;
    }

    public Orden accion(String accion) {
        this.setAccion(accion);
        return this;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Boolean getOperacionExitosa() {
        return this.operacionExitosa;
    }

    public Orden operacionExitosa(Boolean operacionExitosa) {
        this.setOperacionExitosa(operacionExitosa);
        return this;
    }

    public void setOperacionExitosa(Boolean operacionExitosa) {
        this.operacionExitosa = operacionExitosa;
    }

    public String getOperacionObservaciones() {
        return this.operacionObservaciones;
    }

    public Orden operacionObservaciones(String operacionObservaciones) {
        this.setOperacionObservaciones(operacionObservaciones);
        return this;
    }

    public void setOperacionObservaciones(String operacionObservaciones) {
        this.operacionObservaciones = operacionObservaciones;
    }

    public Boolean getReportada() {
        return this.reportada;
    }

    public Orden reportada(Boolean reportada) {
        this.setReportada(reportada);
        return this;
    }

    public void setReportada(Boolean reportada) {
        this.reportada = reportada;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orden)) {
            return false;
        }
        return id != null && id.equals(((Orden) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orden{" +
            "id=" + getId() +
            ", operacion='" + getOperacion() + "'" +
            ", fechaOperacion='" + getFechaOperacion() + "'" +
            ", modo='" + getModo() + "'" +
            ", precio=" + getPrecio() +
            ", accionId=" + getAccionId() +
            ", cantidad=" + getCantidad() +
            ", cliente=" + getCliente() +
            ", accion='" + getAccion() + "'" +
            ", operacionExitosa='" + getOperacionExitosa() + "'" +
            ", operacionObservaciones='" + getOperacionObservaciones() + "'" +
            ", reportada='" + getReportada() + "'" +
            "}";
    }
}
