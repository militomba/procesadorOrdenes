package ar.edu.um.programacion2.procesadorordenes.service.dto;

import ar.edu.um.programacion2.procesadorordenes.domain.Orden;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class OrdenDTO implements Serializable {

    private Long id;
    private Integer clienteId;
    private Integer accionId;
    private String codigoAccion;
    private String operacion;
    private Integer cantidad;
    private ZonedDateTime fechaOperacion;
    private String modo;
    private Double precio;

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getAccionId() {
        return accionId;
    }

    public void setAccionId(Integer accionId) {
        this.accionId = accionId;
    }

    public String getCodigoAccion() {
        return codigoAccion;
    }

    public void setCodigoAccion(String codigoAccion) {
        this.codigoAccion = codigoAccion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public ZonedDateTime getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(ZonedDateTime fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public OrdenDTO() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OrdenDTO)) {
            return false;
        }

        OrdenDTO ordenDTO = (OrdenDTO) obj;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ordenDTO.id);
    }

    @Override
    public String toString() {
        return (
            "OrdenDTO{" +
            "id=" +
            getId() +
            ", cliente=" +
            getClienteId() +
            ", accionId=" +
            getAccionId() +
            ", CodigoAccion='" +
            getCodigoAccion() +
            "'" +
            ", operacion='" +
            getOperacion() +
            "'" +
            ", precio=" +
            getPrecio() +
            ", cantidad=" +
            getCantidad() +
            ", fechaOperacion='" +
            getFechaOperacion() +
            "'" +
            ", modo='" +
            getModo() +
            "'"
        );
    }
}
