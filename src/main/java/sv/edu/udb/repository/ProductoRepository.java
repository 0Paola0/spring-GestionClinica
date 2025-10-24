package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.edu.udb.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}

