package sv.edu.udb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Productos")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_producto")
    private String nombre;
    
    @Column(name = "categoria")
    private String categoria;
    
    @Column(name = "stock_actual")
    private Integer cantidad;
    
    @Column(name = "stock_minimo")
    private Integer stockMinimo;
}

